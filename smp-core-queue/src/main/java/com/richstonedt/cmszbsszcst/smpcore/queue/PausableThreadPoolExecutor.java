

package com.richstonedt.cmszbsszcst.smpcore.queue;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <b><code>PausableThreadPoolExecutor</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/11/5 20:20.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 */
public class PausableThreadPoolExecutor extends ThreadPoolExecutor {

    public PausableThreadPoolExecutor(int corePoolSize,
                                      int maximumPoolSize,
                                      long keepAliveTime,
                                      TimeUnit unit,
                                      BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    private boolean isPaused;
    private ReentrantLock pauseLock = new ReentrantLock();
    private Condition unpaused = pauseLock.newCondition();

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        try {
            // 如果线程池被暂停，则任务等待不暂停的条件(等待resume方法的调用)
            while (isPaused) {
                unpaused.await();
            }

        } catch (InterruptedException ie) {
            t.interrupt();
        } finally {
            pauseLock.unlock();
        }
    }

    public void pause() {
        pauseLock.lock();
        try {
            // 暂停
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    public void resume() {
        pauseLock.lock();
        try {
            // 恢复
            isPaused = false;
            // 激活所有等待的线程
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }


}
