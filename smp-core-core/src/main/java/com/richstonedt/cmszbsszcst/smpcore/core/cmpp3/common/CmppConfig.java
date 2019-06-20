
package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.common;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <b><code>CmppConfig</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/11/14 19:50.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */
public class CmppConfig {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("cmppConfig",
            new Locale("zh", "CN"));

    public static String get(String key) {
        return RESOURCE_BUNDLE.getString(key);
    }

    /**
     * 获取互联网短信网关IP
     *
     * @return
     */
    public static String getIsmgIp() {
        return CmppConfig.get("ismgIp");
    }

    /**
     * 获取互联网短信网关端口号
     *
     * @return
     */
    public static int getIsmgPort() {
        return Integer.parseInt(CmppConfig.get("ismgPort"));
    }

    /**
     * 获取sp企业代码
     *
     * @return
     */
    public static String getSpId() {
        return CmppConfig.get("spId");
    }

    /**
     * 获取sp下发短信号码
     *
     * @return
     */
    public static String getSrcId() {
        return CmppConfig.get("srcId");
    }

    /**
     * 获取sp sharedSecret
     * 访问网关需要的密码
     *
     * @return
     */
    public static String getSpSharedSecret() {
        return CmppConfig.get("sharedSecret");
    }

    /**
     * 获取企业代码serviceId
     *
     * @return
     */
    public static String getServiceId() {
        return CmppConfig.get("serviceId");
    }

    /**
     * 获取groupName组名
     */

    public static String getGroupName() {
        return CmppConfig.get("groupName");
    }

    /**
     * 获取linkId
     */
    public static String getLinkID() {
        return CmppConfig.get("linkId");
    }

    /**
     * 获取msgSrc企业代码
     */
    public static String getMsgSrc() {
        return CmppConfig.get("msgSrc");
    }

    /**
     * cmpp使用版本号
     */
    public static String getCmppVersion() {
        return CmppConfig.get("cmppVersion");
    }

    /**
     * ISMG网关速度
     */
    public static String getIsmgSpeed() {
        return CmppConfig.get("ismgSpeed");
    }

}