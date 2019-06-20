
package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.factory;

/**
 * <b><code>ClientName</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/10/18 17:28.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */
public enum ClientName {
    /**
     * 声明连接cmppClientId
     */
    INT;
    private static final String CLINETNAME = "CMPPPCLIENTID";


    public String getCLINETNAME() {
        return CLINETNAME;
    }
}
