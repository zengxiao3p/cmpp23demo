
package com.richstonedt.cmszbsszcst.smpcore.monitorweb.shutdownhook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.richstonedt.cmszbsszcst.smpcore.monitorweb.util.H2ConnectionPoolUtil;

/**
 * <b><code>CloseDbConnectionJob</code></b>
 * <p>
 * class_comment
 * <p>
 * <b>Creation Time:</b> 2018年9月29日 下午8:25:19
 *
 * @author panjian
 * @version $Revision$ $Date$
 * @since smp-core-monitor-web 0.0.1
 */
public class CloseDbConnectionJob extends Thread {

    private static final Logger logger = LoggerFactory
            .getLogger(CloseDbConnectionJob.class);

    /**
     * @see java.lang.Thread#run()
     * @since smp-core-monitor-web 0.0.1
     */
    @Override
    public void run() {
        logger.info("***************************************************************");
        logger.info("关闭monitor-web的H2连接开始");
        H2ConnectionPoolUtil.closeAllConnection();
        logger.info("关闭monitor-web的H2连接结束");
        logger.info("***************************************************************");
    }

}
