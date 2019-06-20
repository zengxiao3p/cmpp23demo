

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.entity;

import java.net.URI;

/**
 * <b><code>WebSocketEntity</code></b>
 * <p/>
 * Description webSocket实体配置类
 * <p/>
 * <b>Creation Time:</b> 2018/9/30 11:46.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */
public class WebSocketEntity {
    private  int port ;
    private  URI uri;
    private  String host;
    private  String sendMessage;
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }
}
