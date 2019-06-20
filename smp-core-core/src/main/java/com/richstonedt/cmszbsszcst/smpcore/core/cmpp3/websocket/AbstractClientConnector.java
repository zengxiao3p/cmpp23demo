

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket;


import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.handler.HeartBeatHandler;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.entity.WebSocketEntity;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.factory.EventLoopGroupWebSocketFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * <b><code>AbstractClientConnector</code></b>
 * <p/>
 * Description webSocket连接器
 * <p/>
 * <b>Creation Time:</b> 2018/9/30 14:59.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */
public abstract class AbstractClientConnector extends AbstractConnector {
    public AbstractClientConnector(WebSocketEntity webSocketEntity) {
        super(webSocketEntity);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractClientConnector.class);
    final Bootstrap boot = new Bootstrap();

    ChannelFuture channelFuture;

    @Override
    public void init() {
        boot.option(ChannelOption.SO_KEEPALIVE, true)
                .group(EventLoopGroupWebSocketFactory.INS.getWebSocketClient())
                .handler(new LoggingHandler(LogLevel.INFO))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline p = socketChannel.pipeline();
                        p.addLast(new IdleStateHandler(0, 300,
                                0, TimeUnit.SECONDS));
                        p.addLast(new HeartBeatHandler());
                        // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                        p.addLast(new HttpResponseDecoder());
                        // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                        p.addLast(new HttpRequestEncoder());
                    }
                });
    }

    @Override
    public void open() throws URISyntaxException, InterruptedException {
        init();
        connect();
    }

    @Override
    public void close() throws InterruptedException {
        if (channelFuture.channel().isActive()) {
            channelFuture.channel().close().sync();
        }
        //netty 优雅关闭
        EventLoopGroupWebSocketFactory.INS.getWebSocketClient().shutdownGracefully();
    }

    /**
     * <p>获取到webSocket推送流用作推送消息</p>
     * 获取到webSocket推送流用作推送消息
     *
     * @param channelPipeline
     * @throws UnsupportedEncodingException
     * @throws InterruptedException
     */
    protected abstract void dealWithChannelPipeline(ChannelPipeline channelPipeline)
            throws UnsupportedEncodingException, InterruptedException;

    /**
     * webSocketClient连接
     */
    @Override
    public void connect() {
        WebSocketEntity webSocketEntity = getWebSocketEntity();
        try {
            channelFuture = boot.connect(webSocketEntity.getHost(),
                    webSocketEntity.getPort()).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        LOGGER.info("webClient connect success ! client address is:"+channelFuture.channel().remoteAddress());
                        dealWithChannelPipeline(channelFuture.channel().pipeline());
                    }
                }
            });
        } catch (Exception e) {
            LOGGER.error("AbstractClientConnector connect error cause by {}", e.getMessage());
        }
    }
}
