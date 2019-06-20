
package com.richstonedt.cmszbsszcst.smpcore.monitorweb.util;

import java.sql.Timestamp;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.richstonedt.cmszbsszcst.smpcore.monitorweb.domain.OverviewInfo;

/**
 * <b><code>DBUtilTest</code></b>
 * <p>
 * class_comment
 * <p>
 * <b>Creation Time:</b> 2018年9月30日 上午11:16:05
 * 
 * @author panjian
 * @version $Revision$ $Date$
 * @since smp-core-monitor-web 0.0.1
 */
public class DBUtilTest {

    private static final Logger logger = LoggerFactory
            .getLogger(DBUtilTest.class);

    /**
     * 
     * method_comment(use third person)
     * @throws Exception
     * @since smp-core-monitor-web 0.0.1
     */
    @Before
    public void setUp() throws Exception {
        logger.info("***************************************************************");
        logger.info("初始化monitor-web模块库表开始（单元测试）");
        List<String> sqlFiles = ServerConfig.getInitSQLFiles();
        for (String sqlFile : sqlFiles) {
            DBUtil.executeSQLFile(sqlFile);
        }
        logger.info("初始化monitor-web模块库表结束（单元测试）");
        logger.info("初始化SQL文件合计：" + sqlFiles.size() + "个。");
        logger.info("***************************************************************");
    }

    /**
     * 
     * method_comment(use third person)
     * @throws Exception
     * @since smp-core-monitor-web 0.0.1
     */
    @After
    public void tearDown() throws Exception {
        DBUtil.deleteAll();
        H2ConnectionPoolUtil.closeAllConnection();
    }

    /**
     * 
     * method_comment(use third person)
     * @since smp-core-monitor-web 0.0.1
     */
    @Test
    public void testGetOverviewInfos() {
        List<OverviewInfo> overviewInfos = DBUtil.getOverviewInfos();
        Assert.assertTrue(overviewInfos.size() == 1);
    }



    @Test
    public void testInsertOverviewInfos() throws Exception {

        OverviewInfo overviewInfo= new OverviewInfo(2, 1, 1,1,
        1.0, 1, 1,
        1,  Timestamp.valueOf("2018-09-29 16:53:32"),  Timestamp.valueOf("2018-09-29 16:53:32"));
        int success =  DBUtil.insertOverviewInfos(overviewInfo);
        List<OverviewInfo> overviewInfos = DBUtil.getOverviewInfos();
        Assert.assertTrue(success==1);
    }


}
