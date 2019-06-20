

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.sumbithandler;


import java.util.concurrent.atomic.AtomicLong;

/**
 * <b><code>Count</code></b>
 * <p/>
 * Description  用于统计数据，每个任务开始前都要进行清零。
 * <p/>
 * <b>Creation Time:</b> 2018/10/31 19:28.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 */
public enum Count implements Cloneable {
    /**
     * 用作统计
     */
    INI;
    /**
     * 推送成功
     */
    private static final AtomicLong PUSH_SUCCESS = new AtomicLong(0);

    /**
     * 记录上次推送成功
     */
    private static final AtomicLong LAST_PUSH_SUCCESS = new AtomicLong(0);

    /**
     * 记录上次网关获取成功数
     * */
    private static final  AtomicLong LAST_GET_SUCCESS = new AtomicLong(0);

    /**
     * 推送失败
     */
    private static final AtomicLong PUSH_FAIL = new AtomicLong(0);


    /**
     * 网关接收成功
     */
    private static final AtomicLong GET_SUCCESS = new AtomicLong(0);


    /**
     * 网关接收失败
     */
    private static final AtomicLong GET_FAIL = new AtomicLong(0);


    public AtomicLong getGetSuccess() {
        return GET_SUCCESS;
    }

    public AtomicLong getGetFail() {
        return GET_FAIL;
    }

    public AtomicLong getLastPushSuccess() {
        return LAST_PUSH_SUCCESS;
    }

    public AtomicLong getPushSuccess() {
        return PUSH_SUCCESS;
    }

    /**
     * 清零
     */
    public void clearAll() {
        PUSH_SUCCESS.set(0);
        PUSH_FAIL.set(0);
        LAST_GET_SUCCESS.set(0);
        LAST_PUSH_SUCCESS.set(0);
        GET_FAIL.set(0);
        GET_SUCCESS.set(0);
    }

    public void setPush(int i) {
        PUSH_SUCCESS.set(i);
    }

    public void setLastPushSuccess(int i) {
        LAST_PUSH_SUCCESS.set(i);
    }

    public AtomicLong getPushFail() {
        return PUSH_FAIL;
    }

    public void setPushFail(int i) {
        PUSH_FAIL.set(i);
    }

    public  AtomicLong getLastGetSuccess() {
        return LAST_GET_SUCCESS;
    }
}
