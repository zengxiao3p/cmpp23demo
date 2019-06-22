/**
 * <b><code>MyCmppClientEndpointConnector</code></b>
 * <p/>
 * Description:
 * <p/>
 * <b>Creation Time:</b> 2019/6/22 15:15
 *
 * @author zengweijie
 * @since
 */
package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service.impl;

import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.handler.CmppClientIdleStateHandler;
import com.zx.sms.codec.cmpp.msg.CmppSubmitRequestMessage;
import com.zx.sms.common.GlobalConstance;
import com.zx.sms.connect.manager.EndpointEntity;
import com.zx.sms.connect.manager.cmpp.CMPPClientEndpointConnector;
import com.zx.sms.connect.manager.cmpp.CMPPClientEndpointEntity;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 首先创建自己扩展的Connector,在其中增加自己的Handler
 */
public class MyCmppClientEndpointConnector extends CMPPClientEndpointConnector {

    public static final Logger LOGGER = LoggerFactory.getLogger(MyCmppClientEndpointConnector.class);
    public MyCmppClientEndpointConnector(CMPPClientEndpointEntity e) {
        super(e);
    }

    @Override
    protected void doBindHandler(ChannelPipeline pipe, EndpointEntity cmppentity) {
        pipe.addFirst(new IdleStateHandler(0, 20,
                0, TimeUnit.SECONDS));
        pipe.addFirst(new CmppClientIdleStateHandler());
        //这个handler加在协议解析的后边
        pipe.addAfter(GlobalConstance.codecName, "Myhandler", new ChannelDuplexHandler() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
                ctx.write(msg, promise);
                if (msg instanceof CmppSubmitRequestMessage) {
                    if (!StringUtil.isNullOrEmpty(((CmppSubmitRequestMessage) msg).getMsgContent())) {
                        LOGGER.debug("CmppSubmitRequestMessage 发送的短信内容为:{},到达手机号为:{}", ((CmppSubmitRequestMessage) msg)
                                .getMsgContent(), ((CmppSubmitRequestMessage) msg).getDestterminalId());
                    }
                }
            }
        });
        super.doBindHandler(pipe,cmppentity);
    }
}