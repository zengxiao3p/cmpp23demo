

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit;

import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.report.ReportEntity;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.sumbithandler.MyScheduledExecutor;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.sumbithandler.SessionConnectedHandler;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.common.CmppConfig;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.factory.ClientName;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service.impl.RegisterCmppConfigeImpl;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service.impl.WebSocketManagerImpl;
import com.zx.sms.BaseMessage;
import com.zx.sms.codec.cmpp.msg.CmppSubmitRequestMessage;
import com.zx.sms.common.util.ChannelUtil;
import com.zx.sms.common.util.MsgId;
import com.zx.sms.connect.manager.*;
import com.zx.sms.connect.manager.cmpp.CMPPClientEndpointEntity;
import io.netty.channel.*;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.*;

/**
 * <b><code>SumbitMessage</code></b>
 * <p/>
 * Description CMPP协议，短信发送核心类，短信发送入口类，提供接口短信任务进行发送。
 * <p/>
 * <b>Creation Time:</b> 2018/10/8 16:33.
 *
 * @author zengweijie
 * @since smp-core 1.0.0
 */
public class SumbitMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(SumbitMessage.class);

    static WebSocketManagerImpl webSocketManager = null;

    static RegisterCmppConfigeImpl registerCmppConfige = null;

    //初始化webSocketManager
    static {
        try {
            webSocketManager = WebSocketManagerImpl.getInstance();
            registerCmppConfige = RegisterCmppConfigeImpl.getInstance();
            registerCmppConfige.start();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 待发送的号码包
     */
    private BlockingQueue queue;

    /**
     * 短信内容
     */
    private String contentMessage;

    private SumbitMessage() throws Exception {
    }

    private SumbitMessage(BlockingQueue queue) throws Exception {
        this.queue = queue;
    }

    /**
     * 核心构造方法
     *
     * @param queue          号码队列
     * @param contentMessage 短信内容
     * @throws Exception 遇到异常时抛出
     */
    public SumbitMessage(BlockingQueue queue, String contentMessage) throws Exception {
        this.queue = queue;
        this.contentMessage = contentMessage;
        webSocketManager.startService();
    }


    /**
     * 使用自己扩展的Entity,cmpp短信发送的核心方法。
     *
     * @param taskId 任务Id
     * @return ReportEntity  任务执行完毕后用于返回任务结果。
     * @throws Exception
     */
    public ReportEntity sendMessage(String taskId) throws Exception {
        //执行结果
        ReportEntity report = null;
        try {
            //统计开始
            MyScheduledExecutor.startCount(queue.size(), taskId);
            long start = System.currentTimeMillis();
            queue.stream().forEach(phone -> {
                List<Promise<BaseMessage>> futures = null;
                try {
                    futures = ChannelUtil.syncWriteLongMsgToEntity(ClientName.INT.getCLINETNAME(),
                            createMessage(contentMessage, (String) phone));
                    for (Promise future : futures) {
                            future.await(60, TimeUnit.SECONDS);
                        if (!future.isSuccess()) {
                            LOGGER.error("syncWriteLongMsgToEntity error cause by:{}",
                                    SessionConnectedHandler.class, future.cause());
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("SumbitMessage error cause by:{}", e.getMessage());
                    e.printStackTrace();
                }
                long end = System.currentTimeMillis();
                LOGGER.info("sumbitMessage one task finished spent time: {} millis", end - start);
            });

        } catch (Exception e) {
            LOGGER.error("SumbitMessage error cause by:{}", e.getMessage());
            e.printStackTrace();
        } finally {
            //获取执行结果
            report = MyScheduledExecutor.countEnd(taskId);
        }
        return report;
    }


    /**
     * @param queue          待发送号码包
     * @param contentMessage 发送的内容 区分长短信和短短信
     */
    public void startSend(BlockingQueue queue, String contentMessage, String taskId) throws Exception {
        new SumbitMessage(queue, contentMessage).sendMessage(taskId);
    }

    /**
     * 异常时执行突破
     */
    public static String breakout() {
        EndpointManager.INS.remove(ClientName.INT.getCLINETNAME());
        return "success";
    }


    /**
     * 关闭client channel
     *
     * @param cmppClientEndpointEntity
     */
    private void pauseAll(CMPPClientEndpointEntity cmppClientEndpointEntity, String taskId) throws URISyntaxException,
            InterruptedException {
        Thread.sleep(4000);
        EndpointConnector e = EndpointManager.INS.getEndpointConnector(cmppClientEndpointEntity);
        Channel[] channels = e.getallChannel();
        for (Channel ch : channels) {
            //调用close方法。
            ch.close();
        }
        //关闭client端发送
        EndpointManager.INS.close();
        breakout();
        LOGGER.info("sumbitMessage stop ! taskId is {}", taskId);
    }

    /**
     * 私有的短信创建方法
     */
    private BaseMessage createMessage(String content, String destterminalId) {
        CmppSubmitRequestMessage msg = new CmppSubmitRequestMessage();
        msg.setDestterminalId(destterminalId);
        msg.setLinkID(CmppConfig.getLinkID());
        msg.setMsgContent(content);
        msg.setRegisteredDelivery((short) 1);
        msg.setMsgid(new MsgId());
        msg.setServiceId(CmppConfig.getServiceId());
        msg.setSrcId(CmppConfig.getSrcId());
        msg.setMsgsrc(CmppConfig.getMsgSrc());
        return msg;
    }
}
