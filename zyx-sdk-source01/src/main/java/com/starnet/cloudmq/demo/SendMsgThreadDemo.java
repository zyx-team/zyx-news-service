package com.starnet.cloudmq.demo;

import com.starnet.cloudmq.rabbitmq.CloudMQClient;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.support.converter.JsonMessageConverter;

import java.util.UUID;

public class SendMsgThreadDemo {

    public static void main(String[] args) {

        final JsonMessageConverter jsonMessageConverter = new JsonMessageConverter();

        final TestMessage testMessage = new TestMessage();
        testMessage.setId(UUID.randomUUID().toString());
        testMessage.setContent("hello world:");
        final Message message = jsonMessageConverter.toMessage(testMessage, null);

        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
        System.out.println(message.getMessageProperties());
//        final CloudMQClient cloudMQClient;
//        final CloudMQClient cloudMQClient2;
//        try {
//            cloudMQClient = new CloudMQClient("conn_adr", "app_key", "app_secret", "qid");
//            cloudMQClient2 = new CloudMQClient("conn_adr", "app_key2", "app_secret2", "qid2");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//
//        Thread t1 = new Thread(new Runnable() {
//            public void run() {
//                try {
//                    cloudMQClient.send(message);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("线程t1消息已被发送.");
//            }
//        });
//
//        Thread t2 = new Thread(new Runnable() {
//            public void run() {
//                try {
//                    cloudMQClient2.send(message);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("线程t2消息已被发送.");
//            }
//        });
//        t1.start();
//        t2.start();
    }
}
