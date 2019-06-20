
package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket;

import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.entity.WebSocketEntity;

import java.net.URISyntaxException;

/**
 * <b><code>Connector</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/9/30 14:59.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */
public interface Connector<T extends WebSocketEntity> {
    /**
     * <p>获取webSocketentity</p>
     *
     * @return 没有任何需要返回
     * @throws null
     */
    T getWebSocketEntity();

    /**
     * 初始化
     * <p>初始化</p>
     *
     * @return 没有任何需要返回
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    void init() throws InterruptedException, URISyntaxException;

    /**
     * 启动webServer或者webClient
     *
     * @return 没有任何需要返回
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    void open() throws URISyntaxException, InterruptedException;

    /**
     * 关闭webServer或者webClient
     *
     * @return 没有任何需要返回
     * @throws InterruptedException
     */
    void close() throws InterruptedException;

    /**
     * 连接到
     *
     * @return 没有任何需要返回
     * @throws InterruptedException
     */
    void connect() throws InterruptedException;
}
