

package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit;

import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.report.ReportEntity;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.sumbithandler.MyScheduledExecutor;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.sumbithandler.SessionConnectedHandler;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.sumbithandler.WebSocketMessageHandler;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.common.CmppConfig;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.handler.CmppClientIdleStateHandler;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.factory.ClientName;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service.impl.WebSocketManagerImpl;
import com.sun.org.apache.regexp.internal.RE;
import com.zx.sms.codec.cmpp.msg.CmppSubmitRequestMessage;
import com.zx.sms.common.GlobalConstance;
import com.zx.sms.connect.manager.*;
import com.zx.sms.connect.manager.cmpp.CMPPClientEndpointConnector;
import com.zx.sms.connect.manager.cmpp.CMPPClientEndpointEntity;
import com.zx.sms.connect.manager.cmpp.CMPPEndpointEntity;
import com.zx.sms.handler.api.BusinessHandlerInterface;
import com.zx.sms.handler.cmpp.ReWriteSubmitMsgSrcHandler;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

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

    //初始化webSocketManager
    static {
        try {
            webSocketManager = WebSocketManagerImpl.getInstance();
        } catch (URISyntaxException e) {
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

    /**
     * 用于线程锁，0表示通过，1表示锁住
     */
    private AtomicInteger lock;

    private static Thread stopTask;


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
        // 1 means park until unpark
        lock = new AtomicInteger(1);
        webSocketManager.startService();
    }


    /**
     * 首先创建自己扩展的Connector,在其中增加自己的Handler
     */
    private class MyCmppClientEndpointConnector extends CMPPClientEndpointConnector {

        MyCmppClientEndpointConnector(CMPPClientEndpointEntity e) {
            super(e);
        }

        @Override
        protected void doBindHandler(ChannelPipeline pipe, EndpointEntity cmppentity) {
            pipe.addFirst(new IdleStateHandler(0, 20,
                    0, TimeUnit.SECONDS));
            pipe.addFirst(new CmppClientIdleStateHandler());
            //这个handler加在协议解析的后边
            pipe.addAfter(GlobalConstance.codecName, "Myhandler", new ChannelDuplexHandler() {
                @Override
                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
                    ctx.write(msg, promise);
                    if (msg instanceof CmppSubmitRequestMessage) {
                        if (!StringUtil.isNullOrEmpty(((CmppSubmitRequestMessage) msg).getMsgContent())) {
                            LOGGER.debug("CmppSubmitRequestMessage 发送的短信内容为:{},到达手机号为:{}", ((CmppSubmitRequestMessage) msg)
                                    .getMsgContent(), ((CmppSubmitRequestMessage) msg).getDestterminalId());
                        }
                    }
                }
            });

            CMPPEndpointEntity entity = (CMPPEndpointEntity) cmppentity;
            if (entity instanceof ClientEndpoint) {
                pipe.addLast("reWriteSubmitMsgSrcHandler", new ReWriteSubmitMsgSrcHandler(entity));
            }
            pipe.addLast("CmppTerminateRequestMessageHandler", GlobalConstance.terminateHandler);
            pipe.addLast("CmppTerminateResponseMessageHandler", GlobalConstance.terminateRespHandler);
        }
    }

    /**
     * 创建自己扩展的Entity
     */
    private class MyCmppClientEndpointEntity extends CMPPClientEndpointEntity {
        @Override
        public CMPPClientEndpointConnector buildConnector() {
            return new MyCmppClientEndpointConnector(this);
        }
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
        List<MyCmppClientEndpointEntity> clientList = null;
        try {
            final EndpointManager manager = EndpointManager.INS;
            //main thread
            stopTask = Thread.currentThread();
            List<BusinessHandlerInterface> clienthandlers = new ArrayList<BusinessHandlerInterface>();
            //add wirte handler
            clienthandlers.add(new SessionConnectedHandler(queue.size(), queue, stopTask, contentMessage, lock));
            //add websocket handler
            clienthandlers.add(new WebSocketMessageHandler());
            clientList = initClientList();

            //统计开始
            MyScheduledExecutor.startCount(queue.size(), taskId);

            for (MyCmppClientEndpointEntity client : clientList) {
                client.setBusinessHandlerSet(clienthandlers);
                manager.addEndpointEntity(client);
                manager.openEndpoint(client);
            }
            LOGGER.info("sumbitMessage start ! taskId is {}", taskId);
            //根据maxChannel数量开启channel
            manager.startConnectionCheckTask();
            //park this thread
            LOGGER.debug("-------park start-------thread name:" + stopTask.getName());
            LockSupport.park(stopTask);
            LOGGER.debug("-------end park-------thread name:" + stopTask.getName());
            //关闭netty推送connenctor
            manager.stopConnectionCheckTask();

        } catch (Exception e) {
            LOGGER.error("SumbitMessage error cause by:{}", e.getMessage());
            e.printStackTrace();
        } finally {
            //clear
            pauseAll(clientList.get(0), taskId);
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
     * 初始化client
     * 根据提供的管道的速率制定连接的clientList，计算每个client的发送的数量。
     * 其中每个client的速率最好低于200/s 。
     * 例如ISMG网关提供的管道是500/s,只能用450/s
     */
    private List initClientList() {
        int speed = Integer.parseInt(CmppConfig.getIsmgSpeed());
        int clientCount = (int) (speed * 0.9) / 200 + 1;
        int clientSpeed = (int) (speed * 0.9 / clientCount);
        List<MyCmppClientEndpointEntity> myCMPPClientEndpointEntityArrayList = new ArrayList<MyCmppClientEndpointEntity>();
        MyCmppClientEndpointEntity client = new MyCmppClientEndpointEntity();
        client.setId(ClientName.INT.getCLINETNAME());
        client.setHost(CmppConfig.getIsmgIp());
        client.setPort(CmppConfig.getIsmgPort());
        client.setChartset(Charset.forName("utf-8"));
        client.setGroupName(CmppConfig.getGroupName());
        client.setUserName(CmppConfig.getSpId());
        client.setPassword(CmppConfig.getSpSharedSecret());
        client.setMaxChannels((short) clientCount);
        client.setVersion(Short.valueOf(CmppConfig.getCmppVersion().substring(2), 16));
        client.setRetryWaitTimeSec((short) 30);
        client.setUseSSL(false);
        client.setReSendFailMsg(false);
        client.setWriteLimit(clientSpeed);
        myCMPPClientEndpointEntityArrayList.add(client);
        return myCMPPClientEndpointEntityArrayList;
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
}
