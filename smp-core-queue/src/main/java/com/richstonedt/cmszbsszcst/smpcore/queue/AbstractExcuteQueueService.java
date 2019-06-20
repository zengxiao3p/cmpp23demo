

package com.richstonedt.cmszbsszcst.smpcore.queue;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.*;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.SumbitMessage;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.report.ReportEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b><code>AbstractExcuteQueueService</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/11/6 17:38.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 */
public abstract class AbstractExcuteQueueService implements ExcuteQueue {

   private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExcuteQueueService.class);

    /**
     * 用于存储执行的任务,用作展示
     */
    private static final Map<String, Object> TASKMAP = new ConcurrentHashMap<String, Object>();


    /**
     * 包装成Guava的ListeningExecutorService,具有监听功能的线程池
     */
    private static final ListeningExecutorService EXECUTOR_SERVICE = MoreExecutors
            .listeningDecorator(PauseQueueConfig.getPausableThreadPoolExecutor());


    @Override
    public void addListener(final ListenableFuture future) {
        future.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    //监听器执行
                    LOGGER.info("监听器触发");
                    LOGGER.info("result:" + JSON.toJSONString(future.get()));
                } catch (Exception e) {
                    LOGGER.error("Exception cause by:{}", e.getMessage());
                    e.printStackTrace();
                }
            }
        }, MoreExecutors.directExecutor());
        Futures.addCallback(future, new FutureCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                //removeTask from taskMap
                removeTaskRecord(result);
                try {
                    //task successed excute
                    afterTaskFinish((SumbitMessageDetail) result);
                } catch (Exception e) {
                    LOGGER.error("after task success beacuse :{}",e.getMessage());
                    e.printStackTrace();
                }
                LOGGER.info("finish a task and remove task from  queue!");
            }

            // Futures工具类提供了工具方法用于任务正常或异常情况的处理。
            @Override
            public void onFailure(Throwable t) {
                LOGGER.error("{}", t.getMessage());
            }
        }, MoreExecutors.directExecutor());

    }

    @Override
    public String excute(final String messageContent, final BlockingQueue<?> blockingQueue) {
        final SumbitMessageDetail sumbitMessageDetail = excuteTaskInit();
        TASKMAP.put(sumbitMessageDetail.getId(), sumbitMessageDetail);
        ListenableFuture<?> futures = EXECUTOR_SERVICE.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                String contenMessage = messageContent;
                BlockingQueue queue = blockingQueue;
                //the result of the sumbitMessage
                beforeTaskExcute(sumbitMessageDetail);
                ReportEntity reportEntity = (ReportEntity) new SumbitMessage(queue, contenMessage).sendMessage(sumbitMessageDetail.getId());
                SumbitMessageDetail sumbitMessageDetail1 = reportMessageDeliver2SumbitMessageDetail(sumbitMessageDetail, reportEntity);
                return sumbitMessageDetail1;
            }
        });
        addListener(futures);
        return sumbitMessageDetail.getId();
    }


    @Override
    public boolean check(String messageContent) {
        return messageContent.length() <= 70 ? true : false;
    }


    @Override
    public String showAllTask() {
        LOGGER.info("queue size is " + TASKMAP.size());
        LOGGER.info(JSON.toJSONString(TASKMAP));
        return JSON.toJSONString(TASKMAP);
    }


    /**
     * 执行任务 初始化
     * <p>执行短信发送任务初始化</p>
     *
     * @return SumbitMessageDetail
     * @version 0.0.1
     */
    public abstract SumbitMessageDetail excuteTaskInit();


    /**
     * 任务开始 可以用来持久化任务对象
     * <p>任务结束前执行</p>
     *
     * @param detail
     * @return 没有返回值
     * @throws Exception
     */
    public abstract void beforeTaskExcute(SumbitMessageDetail detail) throws Exception;


    /**
     * 任务结束
     * <p>任务结束后执行</p>
     *
     * @param result
     * @return 没有返回值
     * @throws Exception
     */
    public abstract void afterTaskFinish(SumbitMessageDetail result) throws Exception;

    /**
     * 成功时执行的动作
     */
    public void removeTaskRecord(Object result) {
        SumbitMessageDetail successedMessage = (SumbitMessageDetail) result;
        synchronized (TASKMAP) {
            TASKMAP.remove(successedMessage.getId());
        }
    }


    /**
     * 构造新的报告实体
     *
     * @param old          SumbitMessageDetail
     * @param reportEntity ReportEntity
     */
    public SumbitMessageDetail reportMessageDeliver2SumbitMessageDetail(SumbitMessageDetail old, ReportEntity reportEntity) {
        SumbitMessageDetail newSumbitMessageDetail;
        newSumbitMessageDetail = old;
        newSumbitMessageDetail.setAverageSpeed(reportEntity.getAverageRate());
        newSumbitMessageDetail.setMaxSpeed(reportEntity.getMaxRate());
        newSumbitMessageDetail.setMinSpeed(reportEntity.getMinRate());
        newSumbitMessageDetail.setSendCount(reportEntity.getToBeSendMessageNum());
        newSumbitMessageDetail.setFailCount(reportEntity.getIsmgPushFailNum());
        newSumbitMessageDetail.setSuccessCount(reportEntity.getIsmgPushSuccessNum());
        newSumbitMessageDetail.setSuccessRate(reportEntity.getSuccessRate());
        newSumbitMessageDetail.setMaxGetRate(reportEntity.getMaxGetRate());
        newSumbitMessageDetail.setMinGetRate(reportEntity.getMinGetRate());
        newSumbitMessageDetail.setAverageGetRate(reportEntity.getAverageGetRate());
        newSumbitMessageDetail.setSuccessGetCount(reportEntity.getIsmgGetSuccessNum());
        newSumbitMessageDetail.setFailGetCount(reportEntity.getIsmgGetFailNum());
        return newSumbitMessageDetail;
    }


}
