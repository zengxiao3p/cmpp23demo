
package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <b><code>BaseHttpHandler</code></b>
 * <p/>
 * Description  websock基础处理器，
 * <p/>
 * <b>Creation Time:</b> 2018/9/30 11:46.
 *
 * @author zengweijie
 * @since smp-core-core 1.0.0
 */
public class BaseHttpHandler extends SimpleChannelInboundHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseHttpHandler.class);
    public static final ChannelGroup CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        CHANNELS.add(incoming);
        CHANNELS.writeAndFlush(new TextWebSocketFrame(" Client " + ctx.channel() + " connect..."));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        CHANNELS.writeAndFlush(new TextWebSocketFrame(" Client " + ctx.channel() +
                " disconnect..."));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        CHANNELS.writeAndFlush(new TextWebSocketFrame(" Client " + ctx.channel() + " active..."));
        LOGGER.info("Client:" + incoming.remoteAddress() + "active");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        CHANNELS.writeAndFlush(new TextWebSocketFrame(" Client " + ctx.channel() + " inactive..."));
        LOGGER.info("Client:" + incoming.remoteAddress() + "inactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        Channel incoming = ctx.channel();
        CHANNELS.writeAndFlush(new TextWebSocketFrame(" Client " + ctx.channel() + " error..."));
        LOGGER.error("Client:" + incoming.remoteAddress() + "error");
        cause.printStackTrace();
        ctx.close();
    }

    protected void handleReaderIdle(ChannelHandlerContext ctx) {
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
    }
}
