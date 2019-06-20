
package com.richstonedt.cmszbsszcst.smpcore.monitorweb.server;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.List;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.richstonedt.cmszbsszcst.smpcore.monitorweb.shutdownhook.CloseDbConnectionJob;
import com.richstonedt.cmszbsszcst.smpcore.monitorweb.shutdownhook.ShutdownMonitorWebJob;
import com.richstonedt.cmszbsszcst.smpcore.monitorweb.util.DBUtil;
import com.richstonedt.cmszbsszcst.smpcore.monitorweb.util.ServerConfig;

/**
 * <b><code>JettyServer</code></b>
 * <p>
 * class_comment
 * <p>
 * <b>Creation Time:</b> 2018年9月25日 下午3:02:14
 *
 * @author panjian
 * @version $Revision$ $Date$
 * @since smp-core-monitor-web 0.0.1
 */
public class JettyServer {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(JettyServer.class);

    private static Server server;
    private static final String DEV = "dev";

    /**
     * 访问地址:http://ip:port/monitor-web/index.html
     *
     * @param args
     * @since smp-core-monitor-web 0.0.1
     */
    public static void main(String[] args) throws Exception {
        //启动jetty服务
        startup("jar");
    }

    /**
     * 启动jetty服务
     *
     * @since smp-core-monitor-web 0.0.1
     */
    public static void startup(String model) {
        // 指定端口号
        int port = ServerConfig.getJettyPort();

        server = new Server(port);
        //jar包形式启动
        if (DEV.equals(model)) {

            WebAppContext webAppContext = new WebAppContext("webapp",
                    "/monitor-web");
            String path = JettyServer.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            webAppContext.setDescriptor(path + "webapp/WEB-INF/web.xml");
            LOGGER.info("dev: descriptor" + path + "webapp/WEB-INF/web.xml");
            webAppContext.setResourceBase(path + "webapp");
            LOGGER.info("dev resourceBase:" + path + "webapp");
            webAppContext.setDisplayName("monitor-web");
            webAppContext.setClassLoader(Thread.currentThread().getContextClassLoader());
            webAppContext.setConfigurationDiscovered(true);
            webAppContext.setParentLoaderPriority(true);
            server.setHandler(webAppContext);
        } else {
            server.setStopAtShutdown(true);
            ProtectionDomain protectionDomain = JettyServer.class.getProtectionDomain();
            URL location = protectionDomain.getCodeSource().getLocation();
            String warFile = location.toExternalForm();
            System.out.println("war file path:" + warFile);

            WebAppContext context = new WebAppContext(warFile, "/monitor-web");
            context.setServer(server);
            context.setClassLoader(Thread.currentThread().getContextClassLoader());
            // 设置work dir,war包将解压到该目录，jsp编译后的文件也将放入其中。
            String currentDir = new File(location.getPath()).getParent();
            File workDir = new File(currentDir, "work");
            context.setTempDirectory(workDir);
            server.setHandler(context);
        }

        try {
            // 启动jetty
            server.start();
            server.join();
            //清理工作
            cleanup();
            // 初始化库表
            initDB();
        } catch (Exception e) {
            LOGGER.error("JettyServer start happend error.Error Message:"
                    + e.getMessage());
        }
        LOGGER.info("smp-core monitor web server is started, port:" + ServerConfig.getJettyPort()
                + "............");
    }

    /**
     * method_comment(use third person)
     *
     * @since smp-core-monitor-web 0.0.1
     */
    public static void shutdown() {
        if (null != server) {
            LOGGER.info("***************************************************************");
            LOGGER.info("停止MonitorWeb服务...");
            try {
                server.stop();
            } catch (Exception e) {
                LOGGER.error("{}", e);
            }
            LOGGER.info("***************************************************************");
        }
    }

    /**
     * 初始化库表到H2
     *
     * @since smp-core-monitor-web 0.0.1
     */
    public static void initDB() throws IOException {
        LOGGER.info("***************************************************************");
        LOGGER.info("初始化monitor-web模块库表开始");
        List<String> sqlFiles = ServerConfig.getInitSQLFiles();
        for (String sqlFile : sqlFiles) {
            DBUtil.executeSQLFile(sqlFile);
        }
        LOGGER.info("初始化monitor-web模块库表结束");
        LOGGER.info("初始化SQL文件合计：" + sqlFiles.size() + "个。");
        LOGGER.info("***************************************************************");
    }

    /**
     * method_comment(use third person)
     *
     * @since smp-core-monitor-web 0.0.1
     */
    private static void cleanup() {
        Runtime.getRuntime().addShutdownHook(new CloseDbConnectionJob());
        Runtime.getRuntime().addShutdownHook(new ShutdownMonitorWebJob());
    }


}
