package com.starnet.cloudmq.demo;

import com.starnet.cloudmq.rabbitmq.CloudMQClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

public class ConvertAndSendMsgByPublisherConfirmsDemo {

    /**
     * 发送字符串类型消息,发送确认机制
     */
    public static void main(String[] args) {

        try {
            Boolean publisherConfirms = true;
//            CloudMQClient cloudMQClient = new CloudMQClient(
//                    "conn_adr", "app_key", "app_secret", "qid",
//                    publisherConfirms);
            CloudMQClient cloudMQClient = new CloudMQClient(
            "https://10.18.11.53:8200",
                    "b4b24a85-c913-4586-8fba-626523abe912",
                    "ec2a9dc5-b039-49a5-b302-4c7efe0a1b09"
                    , "test01",10,3000,true);
            cloudMQClient.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
                @Override
                public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                    if (ack) {
                        //对消息发送成功,进行处理
                        System.out.println("消息发送成功,回调id:" + correlationData.getId());
                    } else {
                        //对消息发送失败,进行处理
                        System.out.println("消息发送失败,回调id:" + correlationData.getId() + ";原因:" + cause);
                    }
                }
            });

            int count = 1;
            for (; count < 10; count++) {

                cloudMQClient.convertAndSend("你好,hello" + count, String.valueOf(count));
                System.out.println("第" + count + "条消息已被发送.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
