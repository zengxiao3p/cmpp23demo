

package com.richstonedt.cmszbsszcst.smpcore.queue;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * <b><code>PauseQueueConfig</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/11/5 20:33.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 */
public enum PauseQueueConfig {
    /**
     * 提供暂停线程的配置
     * */
    INI;
    private static final BlockingQueue<Runnable> QUEUE = new LinkedBlockingQueue<Runnable>(1024);
    /**
     * 统计线程名称
     */
    private static final ThreadFactory NAMED_THREAD_FACTORY = new ThreadFactoryBuilder()
            .setNameFormat("queuqTask-pool-%d").build();
    private static final PausableThreadPoolExecutor PAUSABLE_THREAD_POOL_EXECUTOR = new PausableThreadPoolExecutor(
            1, 1, 0L,
            TimeUnit.SECONDS, QUEUE, NAMED_THREAD_FACTORY);

    public static PausableThreadPoolExecutor getPausableThreadPoolExecutor() {
        return PAUSABLE_THREAD_POOL_EXECUTOR;
    }

}
