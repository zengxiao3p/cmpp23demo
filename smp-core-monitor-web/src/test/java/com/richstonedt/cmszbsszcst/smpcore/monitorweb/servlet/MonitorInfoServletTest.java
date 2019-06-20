
package com.richstonedt.cmszbsszcst.smpcore.monitorweb.servlet;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.richstonedt.cmszbsszcst.smpcore.monitorweb.server.JettyServer;
import com.richstonedt.cmszbsszcst.smpcore.monitorweb.util.Constants;
import com.richstonedt.cmszbsszcst.smpcore.monitorweb.util.DBUtil;
import com.richstonedt.cmszbsszcst.smpcore.monitorweb.util.ServerConfig;

/**
 * <b><code>MonitorInfoServletTest</code></b>
 * <p>
 * class_comment
 * <p>
 * <b>Creation Time:</b> 2018年9月30日 下午2:56:09
 * @author panjian
 * @version $Revision$ $Date$
 * @since smp-core-monitor-web 0.0.1
 */
public class MonitorInfoServletTest {
    
    private static final Logger logger = LoggerFactory
            .getLogger(MonitorInfoServletTest.class);
    

    private CloseableHttpClient client;
    
    @Before
    public void setup() {
        JettyServer server = new JettyServer();
        try {
            server.startup("dev");
            server.initDB();
        } catch (Exception e) {
            logger.error("MonitorInfoServletTest.setup happened error.Error Message:" + e.getMessage());
        }
        client = HttpClients.createDefault();
    }
    
    @After
    public void tearDown() {
        DBUtil.deleteAll();
        JettyServer.shutdown();
    }
    
    /**
     * 
     * method_comment(use third person)
     * @since smp-core-monitor-web 0.0.1
     */
    @Test
    public void testGetOverviewInfos() {
        int port = ServerConfig.getJettyPort();
        String url = "http://localhost:" + port + "/monitor-web/servlet/monitorinfoservlet?method=getOverviewInfos";
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet);
        } catch (Exception e) {
            logger.error("MonitorInfoServletTest.testGetOverviewInfos happened error.Error Message:" + e.getMessage());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("MonitorInfoServletTest.testGetOverviewInfos happened error.Error Message:" + e.getMessage());
                }
            }
        }
        Assert.assertTrue(Constants.HTTP_CODE_SUCCEE == response.getStatusLine().getStatusCode());
    }
}
