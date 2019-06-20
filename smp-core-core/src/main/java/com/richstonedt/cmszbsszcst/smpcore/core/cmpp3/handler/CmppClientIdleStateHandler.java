

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.handler;

import com.zx.sms.handler.cmpp.CmppServerIdleStateHandler;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b><code>CmppClientIdleStateHandler</code></b>
 * <p/>
 * Description  cmppClient心跳处理。
 * <p/>
 * <b>Creation Time:</b> 2018/11/15 21:19.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */
public class CmppClientIdleStateHandler extends ChannelDuplexHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(CmppServerIdleStateHandler.class);

    public CmppClientIdleStateHandler() {
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                LOGGER.info("CMPPCLIENT WIRTE TIMEOUT CLOSEING CMPPCLIENT NOW!");
                ctx.channel().close().sync().addListener(
                        new GenericFutureListener<Future<? super Void>>() {
                            @Override
                            public void operationComplete(Future<? super Void> future)
                                    throws Exception {
                                LOGGER.info("CMPPCLIENT ALL CLOSE!");
                            }
                        });
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        LOGGER.error("{CmppClientIdleStateHandler error cause by:}", cause.getCause().getMessage());
        cause.printStackTrace();
    }

}
