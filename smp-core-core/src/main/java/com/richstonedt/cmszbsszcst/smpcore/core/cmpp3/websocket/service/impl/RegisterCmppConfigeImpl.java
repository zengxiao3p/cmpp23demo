/**
 * <b><code>RegisterCmppConfigeImpl</code></b>
 * <p/>
 * Description:
 * <p/>
 * <b>Creation Time:</b> 2019/6/22 15:02
 *
 * @author zengweijie
 * @since
 */
package com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.service.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.sumbithandler.SessionConnectedHandler;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.cmppsubmit.sumbithandler.WebSocketMessageHandler;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.common.CmppConfig;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.entity.MyCmppClientEndpointEntity;
import com.richstonedt.cmszbsszcst.smpcore.core.cmpp3.websocket.factory.ClientName;
import com.zx.sms.connect.manager.EndpointManager;
import com.zx.sms.handler.api.BusinessHandlerInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

public class RegisterCmppConfigeImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterCmppConfigeImpl.class);

    private RegisterCmppConfigeImpl() {
    }

    private static RegisterCmppConfigeImpl registerCmppConfige = null;

    public static final synchronized RegisterCmppConfigeImpl getInstance() {
        if (registerCmppConfige == null) {
            registerCmppConfige = new RegisterCmppConfigeImpl();
        }
        return registerCmppConfige;
    }


    private void init() throws Exception {
        final EndpointManager manager = EndpointManager.INS;
        List<MyCmppClientEndpointEntity> clientList = null;
        //main thread
        List<BusinessHandlerInterface> clienthandlers = new ArrayList<>();
        //add wirte handler
        clienthandlers.add(new SessionConnectedHandler());
        //add websocket handler
        clienthandlers.add(new WebSocketMessageHandler());
        clientList = initClientList();

        for (MyCmppClientEndpointEntity client : clientList) {
            client.setBusinessHandlerSet(clienthandlers);
            manager.addEndpointEntity(client);
            manager.openEndpoint(client);
        }
        manager.startConnectionCheckTask();
        //park this thread
        LOGGER.debug("-------park start-------");
        LockSupport.park();
        LOGGER.debug("-------end park-------");

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
     * cmpp配置服务启动
     */
    public void start() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("RegisterCmppConfigeImpl-singlePool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1),
                namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(() -> {
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        singleThreadPool.shutdown();
    }
}