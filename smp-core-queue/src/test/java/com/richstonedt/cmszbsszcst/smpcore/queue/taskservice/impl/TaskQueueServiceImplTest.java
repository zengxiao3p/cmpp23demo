

package com.richstonedt.cmszbsszcst.smpcore.queue.taskservice.impl;

import com.richstonedt.cmszbsszcst.smpcore.queue.SumbitMessageDetail;
import com.richstonedt.cmszbsszcst.smpcore.queue.taskservice.TaskQueueService;
import com.richstonedt.cmszbsszcst.smpcore.queue.taskservice.TaskQueueServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.LockSupport;

import static org.junit.Assert.*;

/**
 * <b><code>TaskQueueServiceImplTest</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/1/8 12:24.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 */
public class TaskQueueServiceImplTest extends TaskQueueService {


    BlockingQueue arrayListblockingQueue;

    @Before
    public void befor() {
        int num = new Random().nextInt(1000);
        arrayListblockingQueue = new ArrayBlockingQueue(num);
        long original = 15920359543L;
        for (long i = 0L; i < num; i++) {
            String temp = String.valueOf(++original);
            arrayListblockingQueue.add(temp);
        }
    }

    /**
     * 短息发送接口
     */
    @Override
    public String send(String messageContent, BlockingQueue<?> blockingQueue) {
        return excute(messageContent, blockingQueue);
    }


    /**
     * 初始化推送任务
     */
    @Override
    public SumbitMessageDetail excuteTaskInit() {
        SumbitMessageDetail sumbitMessageDetail = new SumbitMessageDetail();
        sumbitMessageDetail.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        sumbitMessageDetail.setCreateDate(new Date());
        sumbitMessageDetail.setStatus("0");
        return sumbitMessageDetail;
    }


    /**
     * 任务执行前执行
     */
    @Override
    public void beforeTaskExcute(SumbitMessageDetail detail) throws Exception {
    }


    @Test
    public void testSendQueue() {
            BlockingQueue queue = arrayListblockingQueue;
            new TaskQueueServiceImpl().excute("7654321", queue);
        LockSupport.park();
    }

}