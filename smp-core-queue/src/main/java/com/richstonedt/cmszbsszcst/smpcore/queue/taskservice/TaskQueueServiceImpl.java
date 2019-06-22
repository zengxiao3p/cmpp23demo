
package com.richstonedt.cmszbsszcst.smpcore.queue.taskservice;

import com.richstonedt.cmszbsszcst.smpcore.queue.SumbitMessageDetail;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * <b><code>TaskQueueServiceImpl</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2018/11/14 19:50.
 *
 * @author zengweijie
 * @since smp-core ${PROJECT_VERSION}
 */
public class TaskQueueServiceImpl extends TaskQueueService {
    @Override
    public SumbitMessageDetail excuteTaskInit() {
        SumbitMessageDetail sumbitMessageDetail = new SumbitMessageDetail();
        sumbitMessageDetail.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        sumbitMessageDetail.setCreateDate(new Date());
        sumbitMessageDetail.setStatus("0");
        return sumbitMessageDetail;
    }

    @Override
    public void afterTaskFinish(SumbitMessageDetail sumbitMessageDetail) throws Exception {

    }


    /**
     * 开始模拟构建待发送的电话号码
     */
    private static BlockingQueue<String> phoneNum() {
        int num = 5000;
        BlockingQueue arrayListblockingQueue = new ArrayBlockingQueue(num);
        long original = 15920359543L;
        for (long i = 0L; i < num; i++) {
            String temp = String.valueOf(++original);
            arrayListblockingQueue.add(temp);
        }
        return arrayListblockingQueue;
    }


    @Override
    public void beforeTaskExcute(SumbitMessageDetail detail) throws Exception {

    }

     public static void main(String[] args) {
          for (int i = 0; i < 100; i++) {
              BlockingQueue queue = phoneNum();
              new TaskQueueServiceImpl().excute("7654321", queue);
          }
      }


  /*  public static void main(String[] args) throws Exception {
        String str = "";
        if (args.length > 0) {
            str = args[0].toString();
        }

        if (str.equals("0") || str.equals("")) {
            singleMessageTest();
        }

        if (str.equals("1")) {
            manyPhoneMessageTest();
        }

        if (str.equals("2")) {
            repeatSigleTest();
        }

        if (str.equals("3")) {
            longMessageSendTest();
        }

    }

    private static void singleMessageTest() {
        int num = 1;
        BlockingQueue blockingQueue = new LinkedBlockingQueue(num);
        long original = 15976826239L;
        blockingQueue.add(String.valueOf(original));
        try {
            new TaskQueueServiceImpl()
                    .excute("尊敬的用户你好哇，我是一条有灵魂的测试信息，惊喜不?~(*,w,*)！========单条短短信:",
                            blockingQueue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void manyPhoneMessageTest() {
        int num = 5;
        long me = 13612266558L;
        long chen = 15976826239L;
        long hao = 13750006849L;
        long pan = 13710701787L;
        long cang = 13924290569L;
        BlockingQueue blockingQueue = new LinkedBlockingQueue(num);
        blockingQueue.add(String.valueOf(me));
        blockingQueue.add(String.valueOf(chen));
        blockingQueue.add(String.valueOf(hao));
        blockingQueue.add(String.valueOf(pan));
        blockingQueue.add(String.valueOf(cang));
        new TaskQueueServiceImpl()
                .excute("尊敬的用户你好哇，我是一条有灵魂的测试信息，惊喜不?~(*,w,*)！========单条短短信:", blockingQueue);

    }

    private static void repeatSigleTest() {
        for(int i = 0; i < 10; ++i) {
            singleMessageTest();
        }
    }

    private static void longMessageSendTest() {
        int num = 1;
        BlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(num);
        long original = 15976826239L;
        linkedBlockingQueue.add(String.valueOf(original));
        new TaskQueueServiceImpl()
                .excute("尊敬的用户你好哇，我是一条有灵魂的测试信息，惊喜不?~(*,w,*)！一条来自瀚信的深切呼唤?尊敬的用户你好哇，我是一条有灵魂的测试信息，惊喜不?~(*,w,*！一条来自瀚信的深切呼唤========superLongMessage:", linkedBlockingQueue);

    }
*/
}
