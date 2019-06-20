

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.sumbithandler;

import com.alibaba.fastjson.JSON;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.report.ReportEntity;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service.impl.WebSocketManagerImpl;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

/**
 * <b><code>MyScheduledExecutorTest</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/11/30 15:29.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 */
public class MyScheduledExecutorTest implements Runnable {

    public MyScheduledExecutorTest() throws URISyntaxException {
    }




    private static final Logger LOGGER = LoggerFactory.getLogger(MyScheduledExecutor.class);

    /**
     * 记录推送成功
     */
    final AtomicLong pushSuccess = Count.INI.getPushSuccess();

    /**
     * 记录上次成功数
     */

    final AtomicLong lastPushSuccess = Count.INI.getLastPushSuccess();

    /**
     * 记录失败数
     */
    final AtomicLong pushFail = Count.INI.getPushFail();


    /**
     * 获取统计线程池服务
     */
    private final static EventLoopGroup TIMELOOP = new NioEventLoopGroup(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    });

    /**
     * 最大速度
     */
    private static long maxSpeed = 0;

    /**
     * 待推送总数
     */
    private static long total = 0;


    /**
     * websocket 服务，推送统计消息到前端页面
     */
    WebSocketManagerImpl webSocketManager = WebSocketManagerImpl.getInstance();

    /**
     * 速率比(表示每几秒统计一次)，例如填2表示每2秒统计一次
     */
    private final static int rate = 1;

    /**
     * 最小速率
     */
    private static long minSpeed = Long.MAX_VALUE;


    /**
     * 计数器(用于计算平均速度)
     */
    private static long countdown = 0L;


    /**
     *结果处理
     * */
    private static Future future = null;

    private static ReportEntity reportEntityResult = new ReportEntity();

    @Override
    public void run() {
        countdown++;
        long success = pushSuccess.get();
        long lps = lastPushSuccess.get();
        long fail = pushFail.get();
        long speedNow = (success - lps) / rate;
        //max speed
        if (speedNow > maxSpeed) {
            maxSpeed = speedNow;
        }
        //min speed
        if (speedNow < minSpeed && speedNow != 0L) {
            minSpeed = speedNow;
        }
        LOGGER.debug("{}speed now is {}/s", this.getClass(), speedNow);
        try {
            //average平均速率
            long averageSpeed = success / (countdown * rate);
            ReportEntity reportEntity = new ReportEntity() {
                @Override
                public void cloneObject() throws CloneNotSupportedException {
                    reportEntityResult = (ReportEntity) this.clone();
                }
            };
            reportEntity.setToBeSendMessageNum(String.valueOf(total));
            reportEntity.setIsmgPushSuccessNum(String.valueOf(success));
            reportEntity.setIsmgPushFailNum(String.valueOf(fail));
            reportEntity.setSuccessRate(String.valueOf(speedNow));
            reportEntity.setMaxRate(String.valueOf(maxSpeed));
            reportEntity.setMinRate(String.valueOf(minSpeed));
            reportEntity.setAverageRate(String.valueOf(averageSpeed));
            reportEntity.setType("reportEntity");
            reportEntity.cloneObject();
            webSocketManager.sendMessage(JSON.toJSONString(reportEntity));
        }  catch (Exception e) {
            e.printStackTrace();
        }
        lastPushSuccess.set(success);
    }



    /**
     * 清除计数功能
     */
    @Test
    public  void clearAll() {
        maxSpeed = 0;
        minSpeed = 0;
        total = 0;
        countdown = 0;
        Count.INI.clearAll();
        Assert.assertEquals(maxSpeed,0);
        Assert.assertEquals(minSpeed,0);
        Assert.assertEquals(total,0);
        Assert.assertEquals(countdown,0);
        AtomicLong success  = new  AtomicLong(0);
        AtomicLong fail  = new  AtomicLong(0);
        Assert.assertEquals(Count.INI.getPushSuccess().get(),success.get());
        Assert.assertEquals(Count.INI.getPushFail().get(),fail.get());

    }



    @Test
    public void startCount() {
        total = 100L;
        long initialDelay = 1;
        long period = 1;
        try {
            LOGGER.info("开始计数");
            future = TIMELOOP.scheduleAtFixedRate(new MyScheduledExecutorTest(), initialDelay, period
                    , TimeUnit.SECONDS);
            pushSuccess.incrementAndGet();
            Thread.sleep(1000);
            assertEquals(pushSuccess.get(),1);
        }catch (Exception e){
            LOGGER.error("class MyScheduledExecutor :startCount error cause by:{}",e);
        }
    }

    @Test
    public void countEnd() {
        try {
            Future future = TIMELOOP.scheduleAtFixedRate(new MyScheduledExecutorTest(), 1, 1, TimeUnit.SECONDS);
            future.cancel(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}