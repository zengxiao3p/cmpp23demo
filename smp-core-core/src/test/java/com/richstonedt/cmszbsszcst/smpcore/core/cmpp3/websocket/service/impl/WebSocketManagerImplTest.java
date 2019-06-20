

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

import static org.junit.Assert.*;

/**
 * <b><code>WebSocketManagerImplTest</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/1/8 12:04.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 */
public class WebSocketManagerImplTest {
    WebSocketManagerImpl webSocketManager;

    @Before
    public void setUp() throws Exception {
        webSocketManager = WebSocketManagerImpl.getInstance();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getInstance() throws Exception {
        webSocketManager.startService();
        LockSupport.parkUntil(400);
    }
}