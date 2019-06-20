

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.factory;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b><code>EventLoopGroupWebSocketFactory</code></b>
 * <p/>
 * Description 基于netty的EventLoopGroup统一管理线程池
 * <p/>
 * <b>Creation Time:</b> 2018/9/30 14:59.
 *
 * @author zengweijie
 * @since smp-core  1.0.0
 */
public enum EventLoopGroupWebSocketFactory {
    /**
     * EventLoopGroupWebSocketFactory
     */
    INS;
    private static volatile EventLoopGroup bossGroup = new NioEventLoopGroup(
            1, newThreadFactory("bossGroup"));
    private static volatile EventLoopGroup workgroup = new NioEventLoopGroup(
            1, newThreadFactory("workGroup"));
    private static volatile EventLoopGroup webSocketClientGroup = new NioEventLoopGroup(
            1, newThreadFactory("webSocketClientGroup"));
    /**
     * 0 off ,1 on
     */
    public static AtomicInteger STATUS = new AtomicInteger(0);

    public synchronized EventLoopGroup getBoss() {
        if (bossGroup.isShutdown() || bossGroup.isTerminated()) {
            bossGroup = new NioEventLoopGroup(1, newThreadFactory("bossGroup"));
        }
        return bossGroup;
    }

    public synchronized EventLoopGroup getWorker() {
        if (workgroup.isShutdown() || workgroup.isTerminated()) {
            workgroup = new NioEventLoopGroup(1, newThreadFactory("workGroup"));
        }
        return workgroup;
    }

    public synchronized EventLoopGroup getWebSocketClient() {
        if (webSocketClientGroup.isShutdown() || webSocketClientGroup.isTerminated()) {
            webSocketClientGroup = new NioEventLoopGroup(
                    1, newThreadFactory("webSocketClientGroup"));
        }
        return webSocketClientGroup;
    }


    private static ThreadFactory newThreadFactory(final String name) {

        return new ThreadFactory() {

            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, name + threadNumber.getAndIncrement());
                t.setDaemon(true);
                if (t.getPriority() != Thread.NORM_PRIORITY) {
                    t.setPriority(Thread.NORM_PRIORITY);
                }
                return t;
            }
        };

    }

    public void turnOn() {
        STATUS.set(1);
    }

    public void turnOff() {
        STATUS.set(0);
    }

    public static AtomicInteger getSTATUS() {
        return STATUS;
    }

    /**
     * 线程池关闭。若果长时间不进行webSocket的传输的，需要将资源释放。
     */
    public synchronized void shutdown() throws InterruptedException {
        bossGroup.shutdownGracefully();
        workgroup.shutdownGracefully();
        webSocketClientGroup.shutdownGracefully();
        turnOff();
    }

}
