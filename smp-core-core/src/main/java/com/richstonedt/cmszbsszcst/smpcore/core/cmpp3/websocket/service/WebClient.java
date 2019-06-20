
package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service;

import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.AbstractClientConnector;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.entity.WebSocketEntity;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * <b><code>WebClient</code></b>
 * <p/>
 * Description  webSocketClient
 * <p/>
 * <b>Creation Time:</b> 2018/9/30 14:59.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */
public class WebClient extends AbstractClientConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebClient.class);
    private WebSocketEntity webSocketEntity;
    private ChannelPipeline channelPipeline;


    @Override
    public WebSocketEntity getWebSocketEntity() {
        return webSocketEntity;
    }

    public void setWebSocketEntity(WebSocketEntity webSocketEntity) {
        this.webSocketEntity = webSocketEntity;
    }

    public ChannelPipeline getChannelPipeline() {
        return channelPipeline;
    }

    public void setChannelPipeline(ChannelPipeline channelPipeline) {
        this.channelPipeline = channelPipeline;
    }

    public WebClient(WebSocketEntity webSocketEntity)
            throws InterruptedException, URISyntaxException {
        super(webSocketEntity);
        this.webSocketEntity = webSocketEntity;
    }

    @Override
    protected void dealWithChannelPipeline(ChannelPipeline channelPipeline) {
        this.channelPipeline = channelPipeline;
    }

    public boolean startClient() {
        boolean flag = false;
        try {
            this.open();
            flag = true;
        } catch (Exception e) {
            LOGGER.error("webClient start fail causeBy {}", e.getMessage());
        }
        return flag;
    }


    /**
     * 该方法默认使用具有完整的http get方法体将信息传输到client的pipeline管道中并传到webSocketServer中，用作广播
     */
    public void sendMessage(String msg) throws UnsupportedEncodingException {
        if (this.channelPipeline.channel().isActive()) {
            URI uri = webSocketEntity.getUri();
            DefaultFullHttpRequest httpRequest = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET,
                    uri.toASCIIString(), Unpooled.wrappedBuffer(
                    msg.getBytes("UTF-8")));
            httpRequest.headers().set(HttpHeaderNames.HOST, uri.getHost());
            httpRequest.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            httpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpRequest.content().readableBytes());
            this.channelPipeline.channel().writeAndFlush(httpRequest);
        }
    }

}
