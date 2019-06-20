
package com.richstonedt.cmszbsszcst.smpcore.monitorweb.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.richstonedt.cmszbsszcst.smpcore.monitorweb.domain.OverviewInfo;
import com.richstonedt.cmszbsszcst.smpcore.monitorweb.util.Constants;
import com.richstonedt.cmszbsszcst.smpcore.monitorweb.util.DBUtil;

/**
 * <b><code>MonitorInfoServlet</code></b>
 * <p>
 * class_comment
 * <p>
 * <b>Creation Time:</b> 2018年9月29日 下午4:16:07
 *
 * @author panjian
 * @version $Revision$ $Date$
 * @since smp-core-monitor-web 0.0.1
 */
public class MonitorInfoServlet extends HttpServlet {

    /**
     * 访问方式：http://ip:port/monitor-web/servlet/monitorinfoservlet?method=method1
     * */


    /**
     * field_comment
     *
     * @since smp-core-monitor-web 0.0.1
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     * @since smp-core-monitor-web 0.0.1
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HashMap<String, Object> returnMap = new HashMap<>(16);
        resp.setHeader("Access-Control-Allow-Origin", "*");
        String method = req.getParameter("method");
        List result = new ArrayList();
        switch (method) {
            case "goOverViewInfos":
                goOverViewInfos(req, resp);
                break;
            case "getOverviewInfos":
                result = getOverviewInfos();
                break;
            default:
                break;
        }
        returnMap.put("code", Constants.HTTP_CODE_SUCCEE);
        returnMap.put("message", Constants.RETURN_MESSAGE_SUCCESS);
        returnMap.put("body", result);
        String returnJson = JSON.toJSONString(returnMap);
        resp.getWriter().write(returnJson);
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     * @since smp-core-monitor-web 0.0.1
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doPost(req, resp);
    }

    /**
     * 查询监控信息概览
     *
     * @return
     * @since smp-core-monitor-web 0.0.1
     */
    private List<OverviewInfo> getOverviewInfos() {
        List<OverviewInfo> overviewInfos = new ArrayList<OverviewInfo>();
        overviewInfos = DBUtil.getOverviewInfos();
        return overviewInfos;
    }

    private void addOverviewInfos() {

    }

    /**
     *
     * */
    private void goOverViewInfos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/index.html").forward(req, resp);
    }

}
