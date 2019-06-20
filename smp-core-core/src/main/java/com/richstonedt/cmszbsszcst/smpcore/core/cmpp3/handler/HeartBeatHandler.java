

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.handler;

import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.factory.EventLoopGroupWebSocketFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <b><code>HeartBeatHandler</code></b>
 * <p/>
 * Description  webSocket处理器，心跳机制。
 * <p/>
 * <b>Creation Time:</b> 2018/9/30 11:46.
 *
 * @author zengweijie
 * @since smp-core-core 1.0.0
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartBeatHandler.class);
    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(
            Unpooled.copiedBuffer("Heartbeat",
                    CharsetUtil.UTF_8));

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                LOGGER.info("webSocketClient do not wirte anything ！ " +
                        "closing webSocketServer and webSocketClient ... ");
                ctx.close().sync();
                EventLoopGroupWebSocketFactory.INS.shutdown();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server channelRead..");
        System.out.println(ctx.channel().remoteAddress() + "->Server :" + msg.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        EventLoopGroupWebSocketFactory.INS.shutdown();
    }
}
