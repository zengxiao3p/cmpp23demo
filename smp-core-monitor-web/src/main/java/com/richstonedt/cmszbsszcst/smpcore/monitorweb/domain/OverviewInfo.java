
package com.richstonedt.cmszbsszcst.smpcore.monitorweb.domain;

import java.sql.Timestamp;

/**
 * <b><code>OverviewInfo</code></b>
 * <p>
 * class_comment
 * <p>
 * <b>Creation Time:</b> 2018年9月29日 下午3:30:30
 *
 * @author panjian
 * @version $Revision$ $Date$
 * @since smp-core-monitor-web 0.0.1
 */
public class OverviewInfo {

    public OverviewInfo() {
    }

    /**
     * Constructs a <code>OverviewInfo</code>
     *
     * @param sendCount
     * @param successCount
     * @param failCount
     * @param successRate
     * @param avarageSpeed
     * @param minSpeed
     * @param maxSpeed
     * @param createTime
     * @param updateTime
     * @since smp-core-monitor-web 0.0.1
     */
    public OverviewInfo(long id, long sendCount, long successCount, long failCount,
                        double successRate, int avarageSpeed, int minSpeed,
                        int maxSpeed, Timestamp createTime, Timestamp updateTime) {
        super();
        this.id = id;
        this.sendCount = sendCount;
        this.successCount = successCount;
        this.failCount = failCount;
        this.successRate = successRate;
        this.avarageSpeed = avarageSpeed;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    private long id;

    private long sendCount;

    private long successCount;

    private long failCount;

    private double successRate;

    private int avarageSpeed;

    private int minSpeed;

    private int maxSpeed;

    private Timestamp createTime;

    private Timestamp updateTime;

    /**
     * Returns the id
     *
     * @return the id
     * @since smp-core-monitor-web 0.0.1
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id
     *
     * @param id the id to set
     * @since smp-core-monitor-web 0.0.1
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the sendCount
     *
     * @return the sendCount
     * @since smp-core-monitor-web 0.0.1
     */
    public long getSendCount() {
        return sendCount;
    }

    /**
     * Sets the sendCount
     *
     * @param sendCount the sendCount to set
     * @since smp-core-monitor-web 0.0.1
     */
    public void setSendCount(long sendCount) {
        this.sendCount = sendCount;
    }

    /**
     * Returns the successCount
     *
     * @return the successCount
     * @since smp-core-monitor-web 0.0.1
     */
    public long getSuccessCount() {
        return successCount;
    }

    /**
     * Sets the successCount
     *
     * @param successCount the successCount to set
     * @since smp-core-monitor-web 0.0.1
     */
    public void setSuccessCount(long successCount) {
        this.successCount = successCount;
    }

    /**
     * Returns the failCount
     *
     * @return the failCount
     * @since smp-core-monitor-web 0.0.1
     */
    public long getFailCount() {
        return failCount;
    }

    /**
     * Sets the failCount
     *
     * @param failCount the failCount to set
     * @since smp-core-monitor-web 0.0.1
     */
    public void setFailCount(long failCount) {
        this.failCount = failCount;
    }

    /**
     * Returns the successRate
     *
     * @return the successRate
     * @since smp-core-monitor-web 0.0.1
     */
    public double getSuccessRate() {
        return successRate;
    }

    /**
     * Sets the successRate
     *
     * @param successRate the successRate to set
     * @since smp-core-monitor-web 0.0.1
     */
    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    /**
     * Returns the avarageSpeed
     *
     * @return the avarageSpeed
     * @since smp-core-monitor-web 0.0.1
     */
    public int getAvarageSpeed() {
        return avarageSpeed;
    }

    /**
     * Sets the avarageSpeed
     *
     * @param avarageSpeed the avarageSpeed to set
     * @since smp-core-monitor-web 0.0.1
     */
    public void setAvarageSpeed(int avarageSpeed) {
        this.avarageSpeed = avarageSpeed;
    }

    /**
     * Returns the minSpeed
     *
     * @return the minSpeed
     * @since smp-core-monitor-web 0.0.1
     */
    public double getMinSpeed() {
        return minSpeed;
    }

    /**
     * Sets the minSpeed
     *
     * @param minSpeed the minSpeed to set
     * @since smp-core-monitor-web 0.0.1
     */
    public void setMinSpeed(int minSpeed) {
        this.minSpeed = minSpeed;
    }

    /**
     * Returns the maxSpeed
     *
     * @return the maxSpeed
     * @since smp-core-monitor-web 0.0.1
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Sets the maxSpeed
     *
     * @param maxSpeed the maxSpeed to set
     * @since smp-core-monitor-web 0.0.1
     */
    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Returns the createTime
     *
     * @return the createTime
     * @since smp-core-monitor-web 0.0.1
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    /**
     * Sets the createTime
     *
     * @param createTime the createTime to set
     * @since smp-core-monitor-web 0.0.1
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    /**
     * Returns the updateTime
     *
     * @return the updateTime
     * @since smp-core-monitor-web 0.0.1
     */
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    /**
     * Sets the updateTime
     *
     * @param updateTime the updateTime to set
     * @since smp-core-monitor-web 0.0.1
     */
    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

}
