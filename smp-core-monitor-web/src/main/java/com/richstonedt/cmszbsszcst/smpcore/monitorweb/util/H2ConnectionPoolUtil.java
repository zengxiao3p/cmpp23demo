
package com.richstonedt.cmszbsszcst.smpcore.monitorweb.util;

/**
 * <b><code>JDBCConnectionPool</code></b>
 * <p>
 * class_comment
 * <p>
 * <b>Creation Time:</b> 2018年9月29日 下午7:34:59
 * @author panjian
 * @version $Revision$ $Date$
 * @since smp-core-monitor-web 0.0.1
 */
import java.sql.Connection;

import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2ConnectionPoolUtil {

    private static final Logger logger = LoggerFactory
            .getLogger(H2ConnectionPoolUtil.class);

    /**
     * H2数据库自带的连接池
     */
    private static JdbcConnectionPool connectionPool = null;

    static {
        try {
            connectionPool = JdbcConnectionPool.create(ServerConfig.getMonitorWebDBJDBC(),
                    ServerConfig.getMonitorWebDBUsername(),
                    ServerConfig.getMonitorWebDBPassword());
        } catch (Exception e) {
            logger.error("H2ConnectionPoolUtil 连接池初始化异常。Error Message:"
                    + e.getMessage());
        }
    }

    /**
     * method_comment(use third person)
     * 
     * @return
     * @throws Exception
     * @since smp-core-monitor-web 0.0.1
     */
    public static Connection getConnection() throws Exception {
        return connectionPool.getConnection();
    }

    /**
     * method_comment(use third person)
     * 
     * @return
     * @since smp-core-monitor-web 0.0.1
     */
    public static JdbcConnectionPool getCp() {
        return connectionPool;
    }
    
    /**
     * 
     * method_comment(use third person)
     * @since smp-core-monitor-web 0.0.1
     */
    public static void closeAllConnection() {
        connectionPool.dispose();
    }
}
