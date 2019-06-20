

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.sumbithandler;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.report.ReportEntity;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service.impl.WebSocketManagerImpl;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <b><code>MyScheduledExecutor</code></b>
 * <p/>
 * Description 用于统计短信发送情况线程池
 * <p/>
 * <b>Creation Time:</b> 2018/10/31 18:16.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 * <p>
 * 后面要改！
 */
public class MyScheduledExecutor implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyScheduledExecutor.class);

    /**
     * 记录短信网关接收成功数
     */
    final AtomicLong getSuccess = Count.INI.getGetSuccess();

    /**
     * 记录网关接收失败数
     */
    final AtomicLong getFail = Count.INI.getGetFail();

    /**
     * 记录上次网关接收成功数
     */
    final AtomicLong lastGetSuccess = Count.INI.getLastGetSuccess();

    /**
     * 记录推送成功
     */
    final AtomicLong pushSuccess = Count.INI.getPushSuccess();

    /**
     * 记录上次网关失败推送数
     */
    final AtomicLong pushFail = Count.INI.getPushFail();

    /**
     * 记录上次网关成功推送数
     */
    final AtomicLong lastPushSuccess = Count.INI.getLastPushSuccess();

    /**
     * 当前任务id
     */
    private String taskId;

    /**
     * 统计线程名称
     */
    private static final ThreadFactory NAMED_THREAD_FACTORY = new ThreadFactoryBuilder()
            .setNameFormat("statistics-pool-%d").build();

    /**
     * 获取统计线程池服务
     */
    private static final EventLoopGroup TIMELOOP = new NioEventLoopGroup(1, NAMED_THREAD_FACTORY);

    /**
     * 最大推送速度
     */
    private static long maxSpeed = 0;

    /**
     * 最小推送速率
     */
    private static long minSpeed = Integer.MAX_VALUE;

    /**
     * 最大网关获取速度
     */
    private static long getMaxSpeed = 0;

    /**
     * 最小网关获取速度
     */
    private static long getMinSpeed = Integer.MAX_VALUE;

    /**
     * 待推送总数
     */
    private static long total = 0;

    /**
     * 计数器:用于计算网关速率
     */
    private static long countdown = 0L;

    /**
     * websocket 服务，推送统计消息到前端页面
     */
    WebSocketManagerImpl webSocketManager = WebSocketManagerImpl.getInstance();

    /**
     * 速率比(表示每几秒统计一次)，例如填2表示每2秒统计一次
     */
    private static final int RATE = 1;

    /**
     * 外部不能实例化
     */
    private MyScheduledExecutor() throws URISyntaxException {
    }

    private MyScheduledExecutor(String taskId) throws URISyntaxException {
        this.taskId = taskId;
    }

    /**
     * 结果处理
     */
    private static Future future = null;

    private static ReportEntity reportEntityResult = new ReportEntity();

    @Override
    public void run() {
        countdown++;
        long success = pushSuccess.get();
        long lps = lastPushSuccess.get();
        long fail = pushFail.get();
        long speedNow = (success - lps) / RATE;
        //max push speed
        if (speedNow > maxSpeed) {
            maxSpeed = speedNow;
        }
        //min push speed
        if (speedNow < minSpeed && speedNow != 0L) {
            minSpeed = speedNow;
        }

        long gsucces = getSuccess.get();
        long lgs = lastGetSuccess.get();
        long gFail = getFail.get();
        long gSpeedNow = (gsucces - lgs) / RATE;
        //max get speed
        if (gSpeedNow > getMaxSpeed) {
            getMaxSpeed = gSpeedNow;
        }
        //min get speed
        if (gSpeedNow < getMinSpeed && gSpeedNow != 0L) {
            getMinSpeed = gSpeedNow;
        }
        LOGGER.info("{}getSpeed now is {}/s", this.getClass(), gSpeedNow);
        LOGGER.info("{}pushSpeed now is {}/s", this.getClass(), speedNow);
        try {
            //average平均推送速率
            long averageSpeed = success / (countdown * RATE);
            //average平均接收速率
            long averageGetSpeed = gsucces / (countdown * RATE);
            ReportEntity reportEntity = new ReportEntity() {
                @Override
                public void cloneObject() throws CloneNotSupportedException {
                    reportEntityResult = (ReportEntity) this.clone();
                }
            };
            reportEntity.setToBeSendMessageNum(String.valueOf(total));
            reportEntity.setIsmgPushSuccessNum(String.valueOf(success));
            reportEntity.setIsmgPushFailNum(String.valueOf(fail));
            reportEntity.setIsmgGetSuccessNum(String.valueOf(gsucces));
            reportEntity.setIsmgGetFailNum(String.valueOf(gFail));
            reportEntity.setSuccessRate(String.valueOf(speedNow));
            reportEntity.setSuccessGetRate(String.valueOf(gSpeedNow));
            reportEntity.setMaxGetRate(String.valueOf(getMaxSpeed));
            reportEntity.setMinGetRate(String.valueOf(getMinSpeed));
            reportEntity.setAverageGetRate(String.valueOf(averageGetSpeed));
            reportEntity.setMaxRate(String.valueOf(maxSpeed));
            reportEntity.setMinRate(String.valueOf(minSpeed));
            reportEntity.setAverageRate(String.valueOf(averageSpeed));
            reportEntity.setType("reportEntity");
            reportEntity.setTaskId(this.taskId);
            reportEntity.cloneObject();
            webSocketManager.sendMessage(JSON.toJSONString(reportEntity));
        } catch (Exception e) {
            e.printStackTrace();
        }
        lastPushSuccess.set(success);
        lastGetSuccess.set(gsucces);
    }

    /**
     * @param totalNum 待发送的短信的总数目
     * @param taskId   短信任务
     */
    public static void startCount(long totalNum, String taskId) {
        clearAll();
        total = totalNum;
        long initialDelay = 1;
        long period = 1;
        // 从现在开始1秒钟之后，每隔1秒钟执行一次job
        try {
            future = TIMELOOP.scheduleAtFixedRate(new MyScheduledExecutor(taskId), initialDelay, period,
                    TimeUnit.SECONDS);
            LOGGER.info("task start ! taskId id is: {}", taskId);
        } catch (Exception e) {
            clearAll();
            LOGGER.error("class MyScheduledExecutor :startCount error cause by:{}", e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 清零
     */
    private static void clearAll() {
        maxSpeed = 0;
        minSpeed = Integer.MAX_VALUE;
        getMaxSpeed = 0;
        getMinSpeed = Integer.MAX_VALUE;
        total = 0;
        countdown = 0;
        Count.INI.clearAll();
    }

    /**
     * 本次任务结束，并返回统计结果
     * 暂停统计线程,待下个任务再重新开启。
     */
    public static ReportEntity countEnd(String taskId) {
        try {
            Thread.sleep(1 * 1500);
            future.cancel(false);
            WebSocketManagerImpl.getInstance()
                    .sendMessage("{\"cmppSumbitTask\":\"finish\",\"type\":\"taskFinish\",\"taskId\":" + taskId + "}");
            LOGGER.info("task finish ! taskId id is: {}", taskId);
        } catch (Exception e) {
            LOGGER.error("MyScheduledExecutor close error cause by:{}", e.getMessage());
            e.printStackTrace();
        } finally {
            clearAll();
        }
        return reportEntityResult;
    }
}
