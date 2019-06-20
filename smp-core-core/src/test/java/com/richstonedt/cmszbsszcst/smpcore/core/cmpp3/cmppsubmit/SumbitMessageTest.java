
package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit;

import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.report.ReportEntity;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.sumbithandler.MyScheduledExecutor;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.sumbithandler.SessionConnectedHandler;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.sumbithandler.WebSocketMessageHandler;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.common.CmppConfig;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.handler.CmppClientIdleStateHandler;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.factory.ClientName;
import com.zx.sms.codec.cmpp.msg.CmppSubmitRequestMessage;
import com.zx.sms.common.GlobalConstance;
import com.zx.sms.connect.manager.ClientEndpoint;
import com.zx.sms.connect.manager.EndpointEntity;
import com.zx.sms.connect.manager.EndpointManager;
import com.zx.sms.connect.manager.cmpp.CMPPClientEndpointConnector;
import com.zx.sms.connect.manager.cmpp.CMPPClientEndpointEntity;
import com.zx.sms.connect.manager.cmpp.CMPPEndpointEntity;
import com.zx.sms.handler.api.BusinessHandlerInterface;
import com.zx.sms.handler.cmpp.ReWriteSubmitMsgSrcHandler;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.StringUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

import static org.junit.Assert.*;

/**
 * <b><code>SumbitMessageTest</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/1/8 11:25.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 */
public class SumbitMessageTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SumbitMessageTest.class);

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


    public SumbitMessageTest(){


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
            return new SumbitMessageTest.MyCmppClientEndpointConnector(this);
        }
    }


    @Before
    public void setUp() throws Exception {
        int num = new Random().nextInt(1000);
        BlockingQueue blockingQueue = new LinkedBlockingQueue(num);
        long original = 15920359543L;
        for (long i = 0L; i < num; i++) {
            String temp = String.valueOf(++original);
            blockingQueue.add(temp);
        }
        this.queue=blockingQueue;
        this.contentMessage="test";
        this.lock=new AtomicInteger(1);
    }


    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void sendMessage() {
        String taskId ="testId";
        //执行结果
        ReportEntity report = null;
        List<SumbitMessageTest.MyCmppClientEndpointEntity> clientList = null;
        try {
            final EndpointManager manager = EndpointManager.INS;
            //main thread
            Thread stopTask = Thread.currentThread();
            List<BusinessHandlerInterface> clienthandlers = new ArrayList<BusinessHandlerInterface>();
            //add wirte handler
            clienthandlers.add(new SessionConnectedHandler(queue.size(), queue, stopTask, contentMessage, lock));
            //add websocket handler
            clienthandlers.add(new WebSocketMessageHandler());
            clientList = initClientList();


            for (SumbitMessageTest.MyCmppClientEndpointEntity client : clientList) {
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
        }

    }


    private List initClientList() {
        int speed = Integer.parseInt(CmppConfig.getIsmgSpeed());
        int clientCount = (int) (speed * 0.9) / 200 + 1;
        int clientSpeed = (int) (speed * 0.9 / clientCount);
        List<SumbitMessageTest.MyCmppClientEndpointEntity> myCMPPClientEndpointEntityArrayList = new ArrayList<>();
        SumbitMessageTest.MyCmppClientEndpointEntity client = new SumbitMessageTest.MyCmppClientEndpointEntity();
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
}