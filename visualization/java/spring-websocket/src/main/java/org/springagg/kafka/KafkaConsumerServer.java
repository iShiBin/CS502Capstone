package org.springagg.kafka;

/**
 * Created by dcui013 on 1/24/2018.
 */


import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springagg.websocket.TextMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.web.socket.TextMessage;

public class KafkaConsumerServer implements MessageListener<String, String> {
    /**
     * 监听器自动执行该方法
     *     消费消息
     *     自动提交offset
     *     执行业务代码
     *     （high level api 不提供offset管理，不能指定offset进行消费）
     */

    public TextMessageHandler textMessageHandler=new TextMessageHandler();
    public void onMessage(ConsumerRecord<String, String> record) {

        String topic = record.topic();
        String key = record.key();
        String value = record.value();
        long offset = record.offset();
        int partition = record.partition();
        TextMessage message = new TextMessage(value);

        textMessageHandler.sendMessageToUser("username", message);

    }

}