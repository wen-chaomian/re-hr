package com.hrmessage.consumer;

import com.hrmessage.entity.Oplog;
import com.hrmessage.service.OplogService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@RocketMQMessageListener(
        topic = "employee_operate_topic",
        consumerGroup = "hr-message-consumer-group"
)
public class OplogConsumer implements RocketMQListener<Map> {

    @Autowired
    private OplogService oplogService;

    @Override
    public void onMessage(Map message) {
        System.out.println("【hr‑message收到消息】：" + message);

        if(message.get("hrid") == null
                || message.get("operateDesc") == null
                || message.get("msgId") == null){
            return;
        }

        Oplog oplog = new Oplog();
        oplog.setHrid(Integer.valueOf(message.get("hrid").toString()));
        oplog.setOperate((String) message.get("operateDesc"));
        oplog.setAddDate(new Date());
        oplog.setMsgId((String) message.get("msgId"));

        try {
            oplogService.save(oplog);
        } catch (Exception e) {
            System.out.println("检测到重复消息，跳过消费 msgId = " + message.get("msgId"));
        }
    }
}
