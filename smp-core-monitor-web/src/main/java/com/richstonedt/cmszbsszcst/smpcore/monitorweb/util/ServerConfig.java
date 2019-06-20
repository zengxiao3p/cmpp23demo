
package com.richstonedt.cmszbsszcst.smpcore.monitorweb.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b><code>ServerConfig</code></b>
 * <p>
 * class_comment
 * <p>
 * <b>Creation Time:</b> 2018年9月25日 下午4:57:31
 * @author panjian
 * @version $Revision$ $Date$
 * @since smp-core-monitor-web 0.0.1
 */
public class ServerConfig {
    
    private static final Logger logger = LoggerFactory
            .getLogger(ServerConfig.class);
    
    private static final ResourceBundle RESOURCE_BUNDLE =ResourceBundle
            .getBundle("serverconfig");
    
    public static String get(String key) {
        return RESOURCE_BUNDLE.getString(key);
    }
    
    /**
     * 
     * 获取jetty服务器端口
     * @return
     * @since smp-core-monitor-web 0.0.1
     */
    public static Integer getJettyPort() {
        int port = 8989;
        String portStr = ServerConfig.get("jetty.port");
        if (StringUtils.isNotEmpty(portStr)) {
            port = new Integer(portStr);
        }
        return port;
    }
    
    /**
     * 
     * method_comment(use third person)
     * @return
     * @since smp-core-monitor-web 0.0.1
     */
    public static String getMonitorWebDBDriver() {
        String driverClass = ServerConfig.get("monitor-web.db.driverClass");
        return driverClass;
    }
    
    /**
     * 
     * method_comment(use third person)
     * @return
     * @since smp-core-monitor-web 0.0.1
     */
    public static String getMonitorWebDBJDBC() {
        String jdbc = ServerConfig.get("monitor-web.db.jdbc");
        return jdbc;
    }
    
    /**
     * 
     * method_comment(use third person)
     * @return
     * @since smp-core-monitor-web 0.0.1
     */
    public static String getMonitorWebDBUsername() {
        String userName = ServerConfig.get("monitor-web.db.username");
        return userName;
    }
    
    /**
     * 
     * method_comment(use third person)
     * @return
     * @since smp-core-monitor-web 0.0.1
     */
    public static String getMonitorWebDBPassword() {
        String password = ServerConfig.get("monitor-web.db.password");
        return password;
    }
    
    /**
     * 
     * method_comment(use third person)
     * @return
     * @since smp-core-monitor-web 0.0.1
     */
    public static List<String> getInitSQLFiles() {
        String sqlFiles = ServerConfig.get("monitor-web.db.initSQLFiles");
        List<String> sqlFileList = new ArrayList<String>();
        if (StringUtils.isNotEmpty(sqlFiles)) {
            sqlFileList = Arrays.asList(sqlFiles.split(","));
        }
        return sqlFileList;
    }
    
}
