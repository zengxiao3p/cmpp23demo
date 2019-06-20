
package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.pipeline;

import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.websockethandler.SocketHandel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * <b><code>WebsocketServerInitializer</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/9/29 18:19.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */

public class WebsocketServerInitializer extends
        ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        // Http消息编码解码
        pipeline.addLast(new HttpServerCodec());
        //对ByteBuf数据流进行处理，转换成http的对象
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        pipeline.addLast(new ChunkedWriteHandler());
        //自定义处理类
        pipeline.addLast(new SocketHandel());
    }
}

