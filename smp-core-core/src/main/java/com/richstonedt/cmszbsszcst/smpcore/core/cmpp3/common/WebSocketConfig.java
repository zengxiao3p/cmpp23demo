

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.common;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <b><code>WebSocketConfig</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/12/18 15:42.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */
public class WebSocketConfig {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("webSocketConfig",
            new Locale("zh", "CN"));

    public static String get(String key) {
        return RESOURCE_BUNDLE.getString(key);
    }

    public static String getUri() {
        return WebSocketConfig.get("uri");
    }

    public static String getIp() {
        return WebSocketConfig.get("ip");
    }

    public static String getPort() {
        return WebSocketConfig.get("port");
    }
}
