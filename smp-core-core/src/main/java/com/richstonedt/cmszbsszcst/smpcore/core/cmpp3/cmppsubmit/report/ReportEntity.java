
package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.report;

import java.io.Serializable;

/**
 * <b><code>ReportEntity</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/10/11 16:52.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 * 用作展示报告内容的实体类
 */
public class ReportEntity implements Serializable, Cloneable {

    private String clientId;
    private String ip;
    private String port;
    private String groupName;

    /**
     * 0 success other fail
     */
    private String connectStatus;

    private String sendedMessage;

    private String ismgGetSuccessNum;
    private String ismgGetFailNum;

    private String ismgPushSuccessNum;
    private String ismgPushFailNum;

    /**
     * 短信呼叫用户
     */
    private String phone;

    /**
     * 消息的messageId，备用
     */
    private String messageId;

    /**
     * 消息种类
     */
    private String type;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 待发送的短息的数量
     */
    private String toBeSendMessageNum;

    /**
     * 发送消息的条数
     */
    private String sendCount;

    /**
     * 发送消息的速率
     */
    private String successRate;

    /**
     * 网关消息成功接收速率
     */
    private String successGetRate;

    /**
     * 发送消息的平均速率
     */
    private String averageRate;

    /**
     * 发送消息的最小速率
     */
    private String minRate;

    /**
     * 发送消息的最大速率
     */
    private String maxRate;


    private String taskId;

    /**
     * 网关接收最大速率
     */
    private String maxGetRate;

    /**
     * 网关接收最小速率
     */
    private String minGetRate;

    /**
     * 网关平均接收速率
     */
    private String averageGetRate;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getToBeSendMessageNum() {
        return toBeSendMessageNum;
    }

    public void setToBeSendMessageNum(String toBeSendMessageNum) {
        this.toBeSendMessageNum = toBeSendMessageNum;
    }

    public String getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(String connectStatus) {
        this.connectStatus = connectStatus;
    }

    public String getSendedMessage() {
        return sendedMessage;
    }

    public void setSendedMessage(String sendedMessage) {
        this.sendedMessage = sendedMessage;
    }


    public String getIsmgGetSuccessNum() {
        return ismgGetSuccessNum;
    }

    public void setIsmgGetSuccessNum(String ismgGetSuccessNum) {
        this.ismgGetSuccessNum = ismgGetSuccessNum;
    }

    public String getIsmgGetFailNum() {
        return ismgGetFailNum;
    }

    public void setIsmgGetFailNum(String ismgGetFailNum) {
        this.ismgGetFailNum = ismgGetFailNum;
    }

    public String getIsmgPushSuccessNum() {
        return ismgPushSuccessNum;
    }

    public void setIsmgPushSuccessNum(String ismgPushSuccessNum) {
        this.ismgPushSuccessNum = ismgPushSuccessNum;
    }

    public String getIsmgPushFailNum() {
        return ismgPushFailNum;
    }

    public void setIsmgPushFailNum(String ismgPushFailNum) {
        this.ismgPushFailNum = ismgPushFailNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getSendCount() {
        return sendCount;
    }

    public void setSendCount(String sendCount) {
        this.sendCount = sendCount;
    }

    public String getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }

    public String getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(String averageRate) {
        this.averageRate = averageRate;
    }

    public String getMinRate() {
        return minRate;
    }

    public void setMinRate(String minRate) {
        this.minRate = minRate;
    }

    public String getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(String maxRate) {
        this.maxRate = maxRate;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getMaxGetRate() {
        return maxGetRate;
    }

    public void setMaxGetRate(String maxGetRate) {
        this.maxGetRate = maxGetRate;
    }

    public String getMinGetRate() {
        return minGetRate;
    }

    public void setMinGetRate(String minGetRate) {
        this.minGetRate = minGetRate;
    }

    public String getAverageGetRate() {
        return averageGetRate;
    }

    public void setAverageGetRate(String averageGetRate) {
        this.averageGetRate = averageGetRate;
    }

    public String getSuccessGetRate() {
        return successGetRate;
    }

    public void setSuccessGetRate(String successGetRate) {
        this.successGetRate = successGetRate;
    }

    public void cloneObject() throws CloneNotSupportedException {

    }
}
