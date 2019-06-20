

package com.richstonedt.cmszbsszcst.smpcore.queue;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.BlockingQueue;

/**
 * <b><code>ExcuteQueue</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/11/6 17:34.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 */
public interface ExcuteQueue {

    /**
     * 监听器
     *
     * @param future
     * @return nothing
     */
    void addListener(ListenableFuture future);

    /**
     * 执行excute
     * @param messageContent 短信内容
     * @param blockingQueue  电话号码
     * @return 生成短信唯一任务Id
     */

    String excute(String messageContent, BlockingQueue<?> blockingQueue);

    /**
     * 短信内容字符检查
     *
     * @param messageContent
     * @return
     */
    boolean check(String messageContent);

    /**
     * 展示所有正在执行和尚未执行的任务
     *
     * @return 展示所有正在队列的任务
     */
    String showAllTask();

    /**
     * 中断所有尚未执行的任务
     *
     * @param flag 允许中断正在运行的任务以及之后的任务
     * @return 中断任务成功还是失败
     */
    boolean cancelNowTask(boolean flag);
}
