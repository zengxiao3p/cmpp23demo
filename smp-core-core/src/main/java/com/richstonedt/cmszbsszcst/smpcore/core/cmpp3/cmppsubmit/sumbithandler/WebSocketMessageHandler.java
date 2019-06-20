

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.sumbithandler;

import com.zx.sms.codec.cmpp.msg.*;
import com.zx.sms.handler.api.AbstractBusinessHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <b><code>WebSocketMessageHandler</code></b>
 * <p/>
 * Description 处理ISMG网关请求消息
 * <p/>
 * <b>Creation Time:</b> 2018/10/8 16:33.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 */

@ChannelHandler.Sharable
public class WebSocketMessageHandler extends AbstractBusinessHandler implements Cloneable {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketMessageHandler.class);

    private static final String DELIVRD = "DELIVRD";


    /**
     * 成功推送数
     */
    private final AtomicLong pushSuccess = Count.INI.getPushSuccess();

    /**
     * 失败推送数
     */
    private final AtomicLong pushFail = Count.INI.getPushFail();

    /**
     * 网关接收成功数
     */
    private final AtomicLong getSuccess = Count.INI.getGetSuccess();

    /**
     * 网关接收失败数
     */
    private final AtomicLong getFail = Count.INI.getGetFail();


    public WebSocketMessageHandler() throws Exception {
    }


    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof CmppDeliverRequestMessage) {
            //回执消息
            CmppDeliverRequestMessage e = (CmppDeliverRequestMessage) msg;
            //success
            if (DELIVRD.equals(e.getReportRequestMessage().getStat())) {
                pushSuccess.incrementAndGet();
            } else {
                //fail
               pushFail.incrementAndGet();
                LOGGER.debug("ISMG DELIVRD 推送失败!失败数量{}", pushFail.get());
            }
        } else if (msg instanceof CmppSubmitResponseMessage) {
            CmppSubmitResponseMessage responseMessage = (CmppSubmitResponseMessage) msg;
            if (responseMessage.getResult() == 0) {
                getSuccess.incrementAndGet();
            } else {
                getFail.incrementAndGet();
                LOGGER.debug("ISMG  网关获取失败!失败数量{}", getFail.get());
            }
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public String name() {
        return "WebSocketMessageHandler";
    }

    @Override
    public WebSocketMessageHandler clone() throws CloneNotSupportedException {
        WebSocketMessageHandler ret = (WebSocketMessageHandler) super.clone();
        return ret;
    }
}
