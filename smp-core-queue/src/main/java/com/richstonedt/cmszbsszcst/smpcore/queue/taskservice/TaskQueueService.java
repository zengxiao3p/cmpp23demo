

package com.richstonedt.cmszbsszcst.smpcore.queue.taskservice;

import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.SumbitMessage;
import com.richstonedt.cmszbsszcst.smpcore.queue.AbstractExcuteQueueService;
import com.richstonedt.cmszbsszcst.smpcore.queue.PauseQueueConfig;
import com.richstonedt.cmszbsszcst.smpcore.queue.SumbitMessageDetail;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <b><code>TaskQueueService</code></b>
 * <p/>
 * Description   cmpp推送队列服务。通过FIFO阻塞队列用于推送CMPP消息。
 * <p/>
 * <b>Creation Time:</b> 2018/11/6 20:11.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 */
public class TaskQueueService extends AbstractExcuteQueueService {


    /**
     * 短息发送接口
     */
    public String send(String messageContent, BlockingQueue<?> blockingQueue) {
        return excute(messageContent, blockingQueue);
    }


    /**
     * 初始化推送任务
     */
    @Override
    public SumbitMessageDetail excuteTaskInit() {
        SumbitMessageDetail sumbitMessageDetail = new SumbitMessageDetail();
        sumbitMessageDetail.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        sumbitMessageDetail.setCreateDate(new Date());
        sumbitMessageDetail.setStatus("0");
        return sumbitMessageDetail;
    }


    /**
     * 任务执行前执行
     */
    @Override
    public void beforeTaskExcute(SumbitMessageDetail detail) throws Exception {

    }


    /**
     * 推送任务结束后执行
     */
    @Override
    public void afterTaskFinish(SumbitMessageDetail sumbitMessageDetail) throws Exception {

    }


    /**
     * 暂停任务，在完成当前的任务的前提下暂停后续任务的进行，并阻塞主线程
     */
    public void pause() {
        PauseQueueConfig.getPausableThreadPoolExecutor().pause();
    }

    /**
     * 继续任务，任务队列中的任务继续进行
     */
    public void goon() {
        PauseQueueConfig.getPausableThreadPoolExecutor().resume();
    }

    /**
     * 中断任务
     */
    public void breakout() throws InterruptedException {
        SumbitMessage.breakout();
        PauseQueueConfig.getPausableThreadPoolExecutor().shutdownNow();
        PauseQueueConfig.getPausableThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);
    }

    /**
     * 查看任务队列的情况
     */
    public String showTasking() {
        return super.showAllTask();
    }

    /**
     * 取消尙没执行的任务
     */
    @Override
    public boolean cancelNowTask(boolean flag) {
        return false;
    }


}
