
package com.richstonedt.cmszbsszcst.smpcore.monitorweb.shutdownhook;


import com.richstonedt.cmszbsszcst.smpcore.monitorweb.server.JettyServer;

/**
 * <b><code>ShutdownMonitorWebJob</code></b>
 * <p>
 * class_comment
 * <p>
 * <b>Creation Time:</b> 2018年9月30日 下午3:07:50
 *
 * @author panjian
 * @version $Revision$ $Date$
 * @since smp-core-monitor-web 0.0.1
 */
public class ShutdownMonitorWebJob extends Thread {

    /**
     * @see java.lang.Thread#run()
     * @since smp-core-monitor-web 0.0.1
     */
    @Override
    public void run() {
        JettyServer.shutdown();
    }

}
