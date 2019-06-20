

package com.richstonedt.cmszbsszcst.smpcore.monitorweb.servlet;

import com.alibaba.fastjson.JSON;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.common.CmppConfig;
import com.richstonedt.cmszbsszcst.smpcore.monitorweb.domain.CmppConfigShow;
import com.richstonedt.cmszbsszcst.smpcore.monitorweb.util.Constants;
import io.netty.util.internal.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * <b><code>TaskqueueServlet</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/11/12 16:57.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 */
public class TaskqueueServlet extends HttpServlet {


    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        HashMap<String, Object> returnMap = new HashMap<>(16);
        String method = req.getParameter("method");
        //0 fail
        String result = "";
        switch (method) {
            case "cmppConfigshow": {
                try {
                    result = cmppConfigshow();
                } catch (Exception e) {
                    result = "0";
                }
                break;
            }

            case "start2Send": {
                try {
                    String messageContent = req.getParameter("messageContent");
                    String phonesNum = req.getParameter("phonesNum");
                    if (!StringUtil.isNullOrEmpty(messageContent) && !StringUtil.isNullOrEmpty(phonesNum)) {
                        result = start2Send(messageContent, phonesNum);
                    } else {
                        result = "0";
                    }
                } catch (Exception e) {
                    result = "0";
                }

                break;
            }

            case "showQueueTask": {
                try {
                    result = showQueueTask();
                } catch (Exception e) {
                    result = "0";
                }
                break;
            }

            case "pauseTask": {
                try {
                    result = pauseTask();
                } catch (Exception e) {
                    result = "0";
                }
                break;
            }
            case "taskContinue": {
                try {
                    result = taskContinue();
                } catch (Exception e) {
                    result = "0";
                }

                break;
            }
            case "cancelNowTask": {
                try {
                    String flag = req.getParameter("flag");
                    boolean success = cancelNowTask(Boolean.valueOf(flag));
                    if (success) {
                        result = "1";
                    } else {
                        result = "0";
                    }
                } catch (Exception e) {
                    result = "0";
                }
                break;
            }
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
     * 推送任务执行完成后执行
     * 数据入库
     */
    TaskQueueServiceImpl taskExcute = new TaskQueueServiceImpl();


    /**
     * 查看监控配置接口
     */
    public String cmppConfigshow() {
        CmppConfigShow cmppConfigShow = new CmppConfigShow();
        cmppConfigShow.setIp(CmppConfig.getIsmgIp());
        cmppConfigShow.setPort(CmppConfig.getIsmgPort());
        cmppConfigShow.setUserName(CmppConfig.getSpId());
        cmppConfigShow.setPassword(CmppConfig.getSpSharedSecret());
        cmppConfigShow.setGroupName(CmppConfig.getGroupName());
        cmppConfigShow.setServiceId(CmppConfig.getServiceId());
        cmppConfigShow.setSrcId(CmppConfig.getSrcId());
        cmppConfigShow.setLinkId(CmppConfig.getLinkID());
        cmppConfigShow.setMsgSrc(CmppConfig.getMsgSrc());
        cmppConfigShow.setCmppVersion(CmppConfig.getCmppVersion());
        return JSON.toJSONString(cmppConfigShow);
    }


    /**
     * 任务开始
     */
    public String start2Send(String messageContent, String phoneNumbers) {
        try {
            BlockingQueue blockingQueue = new LinkedBlockingQueue();
            String[] phones = phoneNumbers.split(",");

            for (String phone : phones) {
                blockingQueue.add(phone);
            }
            String taskId = taskExcute.send(messageContent, blockingQueue);
            return taskId;
        } catch (Exception e) {
            return "0";
        }
    }


    /**
     * 展示当前在还没执行的队列中的任务。
     */
    public String showQueueTask() {
        return taskExcute.showTasking();
    }


    /**
     * 暂停队列中的任务
     */
    public String pauseTask() {
        try {
            TaskQueueServiceImpl.pause();
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 继续队列中还没执行的任务
     */
    public String taskContinue() {
        try {
            TaskQueueServiceImpl.goon();
            return "1";
        } catch (Exception e) {
            return "0";
        }

    }


    /**
     * 中断执行任务 (nouse)
     */
    boolean cancelNowTask(boolean flag) {
        return taskExcute.cancelNowTask(flag);
    }



}
