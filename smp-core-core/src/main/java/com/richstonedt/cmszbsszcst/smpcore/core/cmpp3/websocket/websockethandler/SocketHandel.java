
package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.websockethandler;

import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.handler.BaseHttpHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * <b><code>SocketHandel</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/9/30 11:46.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */

public class SocketHandel extends BaseHttpHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketHandel.class);
    private WebSocketServerHandshaker handshaker;
    private final String wsUri = "/ws";
    private static final int SUCCESSCODE = 200;

    /**
     * channelAction
     * <p>
     * channel 通道 action 活跃的
     * <p>
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info(ctx.channel().localAddress().toString() + " 通道已激活！");
    }

    /**
     * channelInactive
     * <p>
     * channel 通道 Inactive 不活跃的
     * <p>
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info(ctx.channel().localAddress().toString() + " 通道不活跃！");
        // 关闭流
        ctx.close();
    }

    private String getMessage(ByteBuf buf) {
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            return new String(con, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 功能：读取服务器发送过来的信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 如果是HTTP请求，进行HTTP操作
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
            // 如果是Websocket请求，则进行websocket操作
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    /**
     * 处理HTTP的代码
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws UnsupportedEncodingException {
        // 如果HTTP解码失败，返回HHTP异常
        if (req instanceof HttpRequest) {
            HttpMethod method = req.method();
            // 如果是websocket请求就握手升级
            if (wsUri.equalsIgnoreCase(req.uri())) {
                LOGGER.info("websocket 请求接入"+req.uri());
                WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                        "", null, false);
                handshaker = wsFactory.newHandshaker(req);
                if (handshaker == null) {
                    WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
                } else {
                    handshaker.handshake(ctx.channel(), req);
                }
            }
            if (HttpMethod.POST == method) {
                // 是POST请求
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req);
                decoder.offer(req);
                LOGGER.info("post request");
                LOGGER.info(decoder.getBodyHttpDatas().toString());
            }
            if (HttpMethod.GET == method) {
                // 是GET请求
                // 编码解码
                ByteBuf in = (ByteBuf) req.content();
                byte[] byt = new byte[in.readableBytes()];
                in.readBytes(byt);
                String body = new String(byt, "UTF-8");
                QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
                // 将数据写入通道
                CHANNELS.writeAndFlush(new TextWebSocketFrame(body + "\t"));
                in.release();
            }
        }
    }

    /**
     * 握手请求不成功时返回的应答
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != SUCCESSCODE) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
    }

    /**
     * 处理Websocket的代码
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否是关闭链路的指令
        LOGGER.info("websocket get");
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 文本消息，不支持二进制消息
        if (frame instanceof TextWebSocketFrame) {
            // 返回应答消息
            String request = ((TextWebSocketFrame) frame).text();
            ctx.channel().writeAndFlush(new TextWebSocketFrame(
                    request + " , 欢迎使用Netty WebSocket服务，现在时刻：" + new java.util.Date().toString()));
        }
    }

    /**
     * 功能：服务端发生异常的操作
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        LOGGER.error("异常信息：\r\n" + cause.getMessage());
    }
}
