

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket;


import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.entity.WebSocketEntity;

/**
 * <b><code>AbstractConnector</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/9/30 14:59.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */
public abstract class AbstractConnector implements Connector<WebSocketEntity> {
    private WebSocketEntity webSocketEntity;

    public AbstractConnector(WebSocketEntity webSocketEntity) {
        this.webSocketEntity = webSocketEntity;
    }

    @Override
    public WebSocketEntity getWebSocketEntity() {
        return webSocketEntity;
    }
}
