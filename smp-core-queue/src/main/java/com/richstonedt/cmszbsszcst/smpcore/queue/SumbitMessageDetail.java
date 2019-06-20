

package com.richstonedt.cmszbsszcst.smpcore.queue;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

/**
 * <b><code>SumbitMessageDetail</code></b>
 * <p/>
 * Description 用于展示提交的短信任务细节
 * <p/>
 * <b>Creation Time:</b> 2018/11/6 17:49.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 */
public class SumbitMessageDetail {

    /**
     * id
     */
    private String id;

    /**
     * no
     */
    private String no;

    /**
     * 推送的电话号码
     */
    private BlockingQueue phoneNumbers;

    /**
     * 发送的短信息内容
     */
    private String contentMessage;

    /**
     * 任务创建的时间
     */
    private Date createDate;

    /**
     * 执行任务的耗时
     */
    private String useTime;

    /**
     * 任务执行的状态
     * 0 没完成
     * 1 已完成
     */
    private String status;


    /**
     * successRate
     * 成功速率
     */
    private String successRate;


    /**
     * maxSpeed
     * 最大速率
     */
    private String maxSpeed;


    /**
     * minSpeed
     * 最小速率
     */
    private String minSpeed;

    /**
     * averageSpeed
     * 平均速率
     */
    private String averageSpeed;

    /**
     * failCount
     * 失败数量
     */
    private String failCount;

    /**
     * sendCount
     * 发送数量
     */
    private String sendCount;


    /**
     * 成功数量
     */
    public String successCount;

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

    /**
     * 网关成功接收数
     */
    private String successGetCount;

    /**
     * 网关失败接收数
     */
    private String failGetCount;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public BlockingQueue getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(BlockingQueue phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public void setContentMessage(String contentMessage) {
        this.contentMessage = contentMessage;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(String minSpeed) {
        this.minSpeed = minSpeed;
    }

    public String getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(String averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public String getFailCount() {
        return failCount;
    }

    public void setFailCount(String failCount) {
        this.failCount = failCount;
    }

    public String getSendCount() {
        return sendCount;
    }

    public void setSendCount(String sendCount) {
        this.sendCount = sendCount;
    }

    public String getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(String successCount) {
        this.successCount = successCount;
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

    public String getSuccessGetCount() {
        return successGetCount;
    }

    public void setSuccessGetCount(String successGetCount) {
        this.successGetCount = successGetCount;
    }

    public String getFailGetCount() {
        return failGetCount;
    }

    public void setFailGetCount(String failGetCount) {
        this.failGetCount = failGetCount;
    }
}
