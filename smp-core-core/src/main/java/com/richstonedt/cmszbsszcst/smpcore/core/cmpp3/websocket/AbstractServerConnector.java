
package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket;

import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.entity.WebSocketEntity;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.factory.EventLoopGroupWebSocketFactory;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.pipeline.WebsocketServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * <b><code>AbstractServerConnector</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/9/30 14:59.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */
public abstract class AbstractServerConnector extends AbstractConnector {

    public AbstractServerConnector(WebSocketEntity webSocketEntity) {
        super(webSocketEntity);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServerConnector.class);
    ServerBootstrap bootstrap = new ServerBootstrap();
    ChannelFuture channelFuture = null;

    @Override
    public void init() {
        bootstrap.group(
                EventLoopGroupWebSocketFactory.INS.getBoss(),
                EventLoopGroupWebSocketFactory.INS.getWorker())
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_BACKLOG, 1024 * 1024 * 10)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new WebsocketServerInitializer());
    }

    @Override
    public void open() throws InterruptedException {
        this.init();
        this.connect();
    }

    @Override
    public void close() throws InterruptedException {
        if (channelFuture.channel().isActive()) {
            channelFuture.channel().close().sync();
        }
        //netty优雅关闭
        EventLoopGroupWebSocketFactory.INS.getBoss().shutdownGracefully();
        EventLoopGroupWebSocketFactory.INS.getWorker().shutdownGracefully();
    }

    @Override
    public void connect() throws InterruptedException {
        WebSocketEntity webSocketEntity = getWebSocketEntity();
        try {
            channelFuture = bootstrap.bind(new InetSocketAddress(webSocketEntity.getPort())).sync();
            channelFuture.channel().closeFuture().sync();
            LOGGER.info("webSocketServer close safely !");
        } catch (Exception e) {
            LOGGER.error("webSocketServer start error cause by :{}", e);
        } finally {
            //netty优雅关闭
            EventLoopGroupWebSocketFactory.INS.shutdown();
        }
    }

}
