/**
 * <b><code>MyCmppClientEndpointEntity</code></b>
 * <p/>
 * Description:
 * <p/>
 * <b>Creation Time:</b> 2019/6/22 15:32
 *
 * @author zengweijie
 * @since
 */
package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.entity;

import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service.impl.MyCmppClientEndpointConnector;
import com.zx.sms.connect.manager.cmpp.CMPPClientEndpointConnector;
import com.zx.sms.connect.manager.cmpp.CMPPClientEndpointEntity;

/**
 * 创建自己扩展的Entity
 */
public  class MyCmppClientEndpointEntity extends CMPPClientEndpointEntity {
    @Override
    public CMPPClientEndpointConnector buildConnector() {
        return new MyCmppClientEndpointConnector(this);
    }
}