
package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service;

import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.AbstractServerConnector;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.entity.WebSocketEntity;

/**
 * <b><code>WebServer</code></b>
 * <p/>
 * Description webSocketServer
 * <p/>
 * <b>Creation Time:</b> 2018/9/30 14:59.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */
public class WebServer extends AbstractServerConnector {

    private static WebServer webServer;

    public WebServer(WebSocketEntity webSocketEntity) {
        super(webSocketEntity);
    }

    public  static final synchronized   WebServer getInstance(WebSocketEntity webSocketEntity) {
        if (webServer == null) {
            webServer = new WebServer(webSocketEntity);
        }
        return webServer;
    }

    /**
     * 启动webServer
     */
    public boolean webServerStart() throws InterruptedException {
        boolean flag;
        try {
            this.open();
            flag = true;
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
}
