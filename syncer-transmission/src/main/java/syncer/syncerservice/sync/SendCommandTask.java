package syncer.syncerservice.sync;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import syncer.syncerplusredis.cmd.impl.DefaultCommand;
import syncer.syncerplusredis.replicator.Replicator;
import syncer.syncerplusredis.util.TaskDataManagerUtils;
import syncer.syncerservice.compensator.ISyncerCompensator;
import syncer.syncerservice.filter.KeyValueRunFilterChain;
import syncer.syncerservice.po.KeyValueEventEntity;
import syncer.syncerservice.util.queue.SyncerQueue;

import java.io.IOException;

@Slf4j
@Builder
public class SendCommandTask implements Runnable{
    private Replicator r;
    private KeyValueRunFilterChain filterChain;
    private SyncerQueue<KeyValueEventEntity> queue;
    private String taskId;
    private boolean status = true;
    private ISyncerCompensator syncerCompensator;

    public SendCommandTask(Replicator r, KeyValueRunFilterChain filterChain, SyncerQueue<KeyValueEventEntity> queue, String taskId, boolean status, ISyncerCompensator syncerCompensator) {
        this.r = r;
        this.filterChain = filterChain;
        this.queue = queue;
        this.taskId = taskId;
        this.status = status;
        this.syncerCompensator = syncerCompensator;

        new Thread(new SendCommandTask.AliveMonitorThread()).start();
    }

    @Override
    public void run() {

        while (true){
            try {

                KeyValueEventEntity keyValueEventEntity=null;
                keyValueEventEntity=queue.take();
                keyValueEventEntity.setISyncerCompensator(syncerCompensator);
//                System.out.println(JSON.toJSONString(queue.take()));
                try {


                    if(null!=keyValueEventEntity){
                        filterChain.run(r,keyValueEventEntity);
                    }

                }catch (Exception e){
                    log.warn("[{}]抛弃key:{}:原因[{}]",taskId,JSON.toJSONString(keyValueEventEntity.getEvent()),e.getMessage());
                }

            }catch (Exception e){
                try {
                    log.warn("[{}]key从队列拿出失败:{}",taskId,e.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }


    }

    class AliveMonitorThread implements Runnable{

        @Override
        public void run() {
            while (true){
                if (TaskDataManagerUtils.isTaskClose(taskId)) {
                    int i=3;
                    if(i>=0&&queue.isEmpty()){
                        i--;
                        return;
                    }
                    if(i<0&&queue.isEmpty()){
                        //判断任务是否关闭
                        try {
                            r.close();
                            if (status) {
                                Thread.currentThread().interrupt();
                                status = false;
                                System.out.println(" 线程正准备关闭..." + Thread.currentThread().getName());
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }


    public static void main(String[] args) {
        DefaultCommand command=   JSON.parseObject("{\"args\":[],\"command\":\"Zmx1c2hhbGw=\",\"context\":{\"offsets\":{\"v1\":0,\"v2\":18}}}", DefaultCommand.class);
        System.out.println(new String(command.getCommand()));
    }
}


