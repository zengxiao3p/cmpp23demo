
package com.richstonedt.cmszbsszcst.smpcore.monitorweb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.richstonedt.cmszbsszcst.smpcore.monitorweb.domain.OverviewInfo;

/**
 * <b><code>DbUtil</code></b>
 * <p>
 * class_comment
 * <p>
 * <b>Creation Time:</b> 2018年9月29日 下午4:14:46
 *
 * @author panjian
 * @version $Revision$ $Date$
 * @since smp-core-monitor-web 0.0.1
 */
public class DBUtil {

    private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);

    /**
     * method_comment(use third person)
     *
     * @since smp-core-monitor-web 0.0.1
     */
    public static void executeSQLFile(String file) throws IOException {
        BufferedReader reader = null;
        Connection conn = null;
        Statement pst = null;
        try {
            conn = H2ConnectionPoolUtil.getConnection();
            pst = conn.createStatement();
            ClassLoader classLoader = DBUtil.class.getClassLoader();
            InputStream in = classLoader.getResourceAsStream(file);
            reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                pst.addBatch(line);
                if (i % 100 == 0) {
                    logger.info("       执行了：" + (i + 1) + "条SQL语句。");
                }
                i += 1;
            }
            // 执行批量更新
            pst.executeBatch();
        } catch (Exception e) {
            logger.error(
                    "DBUtil.executeSQLFile happened error. Error Message:", e);
        } finally {
            reader.close();
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                logger.error(
                        "DBUtil.executeSQLFile happened error. Error Message:",
                        e);
            }
        }
    }

    /**
     * 查询短信发送信息概览
     *
     * @return
     * @since smp-core-monitor-web 0.0.1
     */
    public static List<OverviewInfo> getOverviewInfos() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<OverviewInfo> overviewInfos = new ArrayList<OverviewInfo>();
        try {
            conn = H2ConnectionPoolUtil.getConnection();
            String sql = "select id, send_count, success_count, fail_count, success_rate, avrage_speed, min_speed, max_speed, create_time, update_time from smp_monitor_web_overview_infos";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                long sendCount = rs.getLong("send_count");
                long successCount = rs.getLong("success_count");
                long failCount = rs.getLong("fail_count");
                int successRate = rs.getInt("success_rate");
                int avarageSpeed = rs.getInt("avrage_speed");
                int minSpeed = rs.getInt("min_speed");
                int maxSpeed = rs.getInt("max_speed");
                Timestamp createTime = rs.getTimestamp("create_time");
                Timestamp updateTime = rs.getTimestamp("update_time");
                OverviewInfo overviewInfo = new OverviewInfo(id, sendCount,
                        successCount, failCount, successRate, avarageSpeed,
                        minSpeed, maxSpeed, createTime, updateTime);
                overviewInfos.add(overviewInfo);
            }
        } catch (Exception e) {
            logger.error(
                    "DBUtil.getOverviewInfos happened error. Error Message:", e);
        } finally {
            if (null != rs) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error(
                            "getOverviewInfos happened error. Error Message:",
                            e);
                }
            }
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error(
                            "getOverviewInfos happened error. Error Message:",
                            e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    logger.error(
                            "getOverviewInfos happened error. Error Message:",
                            e);
                }
            }
        }
        return overviewInfos;
    }

    /**
     * 为了避免单元测试之前影响，没执行完一个涉及数据库操作的单元测，清空下数据
     *
     * @since smp-core-monitor-web 0.0.1
     */
    public static void deleteAll() {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean rs = true;
        try {
            conn = H2ConnectionPoolUtil.getConnection();
            String sql = "delete from smp_monitor_web_overview_infos";
            ps = conn.prepareStatement(sql);
            rs = ps.execute();
        } catch (Exception e) {
            logger.error("DBUtil.deleteAll happened error. Error Message:", e);
        } finally {
            if (!rs) {
                logger.error("DBUtil.deleteAll affect 0 row.");
            }
            try {
                if (null != ps) {
                    ps.close();
                }
            } catch (SQLException e) {
                logger.error("getOverviewInfos happened error. Error Message:",
                        e);
            }

        }
    }


    /**
     * 插入监控数据
     *
     * @since smp-core-monitor-web 0.0.1
     */
    public static int insertOverviewInfos(OverviewInfo overviewInfo) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;

        try {
            conn = H2ConnectionPoolUtil.getConnection();
            long id = overviewInfo.getId();
            long sendCount = overviewInfo.getSendCount();
            long successCount = overviewInfo.getSuccessCount();
            long failCount = overviewInfo.getFailCount();
            double successRate = overviewInfo.getSuccessRate();
            int avrageSpeed = overviewInfo.getAvarageSpeed();
            double minSpeed = overviewInfo.getMinSpeed();
            double maxSpeed = overviewInfo.getMaxSpeed();
            Timestamp createDate = overviewInfo.getCreateTime();
            Timestamp updateDate = overviewInfo.getUpdateTime();
            String insertSql = "insert  into  smp_monitor_web_overview_infos(ID,SEND_COUNT,SUCCESS_COUNT,FAIL_COUNT,SUCCESS_RATE," +
                    "AVRAGE_SPEED,MIN_SPEED,MAX_SPEED,CREATE_TIME,UPDATE_TIME)" +
                    "values (" + id + "," + sendCount + "," + successCount + "," + failCount + "," + successRate + ","
                    + avrageSpeed + "," + minSpeed + "," + maxSpeed + ",\'" + createDate.toString()
                    + "\',\'" + updateDate.toString() + "\')";
            ps = conn.prepareStatement(insertSql);
            result = ps.executeUpdate();

        } catch (Exception e) {
            logger.error("insertOverviewInfos insert error", e);

        } finally {

            try {
                if (null != ps) {
                    ps.close();
                }
            } catch (SQLException e) {
                logger.error("getOverviewInfos happened error. Error Message:",
                        e);
            }
            try {
                conn.close();
            } catch (Exception e) {
                logger.error("getOverviewInfos happened error. Error Message:",
                        e);
            }
        }
        return result;
    }


}
