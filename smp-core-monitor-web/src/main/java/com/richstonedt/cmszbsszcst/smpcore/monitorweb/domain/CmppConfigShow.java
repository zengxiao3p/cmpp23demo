

package com.richstonedt.cmszbsszcst.smpcore.monitorweb.domain;


import java.io.Serializable;

/**
 * <b><code>CmppConfigShow</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/11/12 18:01.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 */
public class CmppConfigShow implements Serializable {
    private String ip;
    private int port;
    private String userName;
    private String password;
    private String groupName;
    private String serviceId;
    private String srcId;
    private String linkId;
    private String msgSrc;
    private String cmppVersion;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getMsgSrc() {
        return msgSrc;
    }

    public void setMsgSrc(String msgSrc) {
        this.msgSrc = msgSrc;
    }

    public String getCmppVersion() {
        return cmppVersion;
    }

    public void setCmppVersion(String cmppVersion) {
        this.cmppVersion = cmppVersion;
    }
}
