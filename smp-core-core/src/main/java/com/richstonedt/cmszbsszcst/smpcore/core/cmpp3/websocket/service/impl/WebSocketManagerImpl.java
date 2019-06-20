

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service.impl;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.common.WebSocketConfig;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.entity.WebSocketEntity;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.factory.EventLoopGroupWebSocketFactory;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service.WebClient;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service.WebServer;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service.WebSocketManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.*;


/**
 * <b><code>WebSocketManagerImpl</code></b>
 * <p/>
 * Description  单例实现的提供的WebSocket服务
 * <p/>
 * <b>Creation Time:</b> 2018/9/30 14:59.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */

public class WebSocketManagerImpl implements WebSocketManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketManagerImpl.class);

    private WebSocketEntity webSocketEntity;
    boolean flag = ISOPEN;
    private WebClient webClient = null;
    private WebServer webServer = null;

    private static WebSocketManagerImpl webSocketManagerImpl = null;

    private WebSocketManagerImpl() {
    }


    private WebSocketManagerImpl(WebSocketEntity webSocketEntity) {
        this.webSocketEntity = webSocketEntity;
    }


    public static final synchronized WebSocketManagerImpl getInstance() throws URISyntaxException {

        if (webSocketManagerImpl == null) {
            WebSocketEntity webSocketEntity = new WebSocketEntity();
            URI uri = new URI(WebSocketConfig.getUri());
            webSocketEntity.setUri(uri);
            webSocketEntity.setPort(Integer.valueOf(WebSocketConfig.getPort()));
            webSocketEntity.setHost(WebSocketConfig.getIp());
            webSocketManagerImpl = new WebSocketManagerImpl(webSocketEntity);
        }
        return webSocketManagerImpl;
    }


    @Override
    public WebSocketEntity getWebSocketEntity() {
        return webSocketEntity;
    }

    @Override
    public void init() {
        if (flag) {
            webServer = new WebServer(webSocketEntity);
        }
    }

    @Override
    public void open() throws InterruptedException, URISyntaxException {
        this.openWebSocketClient();
    }

    /**
     * 启动server
     */
    @Override
    public void openWebSocketServer() throws InterruptedException {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("webSocketServer-singlePool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1),
                namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    new WebServer(getWebSocketEntity()).webServerStart();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        singleThreadPool.shutdown();
    }

    @Override
    public void openWebSocketClient() throws InterruptedException, URISyntaxException {
        WebClient webClient;
        webClient = new WebClient(webSocketEntity);
        this.webClient = webClient;
        webClient.startClient();
    }

    @Override
    public boolean close() throws InterruptedException {
        EventLoopGroupWebSocketFactory.INS.shutdown();
        return true;
    }

    /**
     * 调用的接口
     */
    @Override
    public void sendMessage(String message) throws Exception {
        //off
        try {
            if (EventLoopGroupWebSocketFactory.STATUS.intValue() == 0) {
                EventLoopGroupWebSocketFactory.INS.shutdown();
                this.restartService();
            }
            webClient.sendMessage(message);
        } catch (Exception e) {
            EventLoopGroupWebSocketFactory.INS.shutdown();
        }
    }


    @Override
    public boolean isActive() {
        return false;
    }


    /**
     * 启动webSocketServer服务
     */
    public synchronized void startService() throws Exception {
        try {
            if (EventLoopGroupWebSocketFactory.STATUS.intValue() == 0) {
                this.openWebSocketServer();
                Thread.sleep(5000);
                this.openWebSocketClient();
                EventLoopGroupWebSocketFactory.INS.turnOn();
            }
        } catch (Exception e) {
            EventLoopGroupWebSocketFactory.INS.shutdown();
            LOGGER.error("error cause by {}", e.getMessage());
        }
    }

    /**
     * 重启webSocket服务
     */
    private void restartService() throws Exception {
        startService();
    }


}
