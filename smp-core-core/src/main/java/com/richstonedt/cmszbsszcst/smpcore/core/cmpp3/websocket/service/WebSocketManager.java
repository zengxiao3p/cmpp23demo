

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service;


import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.entity.WebSocketEntity;

import java.net.URISyntaxException;

/**
 * <b><code>WebSocketManager</code></b>
 * <p/>
 * Description 管理webSocket接口类
 * <p/>
 * <b>Creation Time:</b> 2018/9/30 14:59.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */
public interface WebSocketManager<T extends WebSocketEntity> {
    boolean ISOPEN = true;

    /**
     * 获取websocketEntity
     *
     * @return WebSocketEntity
     */
    T getWebSocketEntity();


    /**
     * 初始化
     *
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    void init() throws InterruptedException, URISyntaxException;

    /**
     * 开启webSocketServer或者webClient
     *
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    void open() throws InterruptedException, URISyntaxException;

    /**
     * 开启单独webSocketServer
     *
     * @throws Exception
     */
    void openWebSocketServer() throws Exception;

    /**
     * 开启webClient
     *
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    void openWebSocketClient() throws InterruptedException, URISyntaxException;

    /**
     * 是否正在启动
     *
     * @return
     */
    boolean isActive();

    /**
     * 关闭webServer
     *
     * @return 是否关闭成功
     * @throws InterruptedException
     */
    boolean close() throws InterruptedException;

    /**
     * 发送短信
     *
     * @param message
     * @throws Exception
     */
    void sendMessage(String message) throws Exception;
}
