package com.starnet.cloudmq.demo;

import com.starnet.cloudmq.rabbitmq.CloudMQClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.JsonMessageConverter;

import java.util.UUID;

public class SingleSendMsgByPublisherConfirmsDemo {

    /**
     * 发送对象类型的消息,发送确认机制
     */
    public static void main(String[] args) {

        try {
            JsonMessageConverter jsonMessageConverter = new JsonMessageConverter();

            Boolean publisherConfirms = true;
            int sessionCacheSize = 25;
            long channelCheckoutTimeout = 0;
            CloudMQClient cloudMQClient = new CloudMQClient(
                    "conn_adr", "app_key", "app_secret", "qid",
                    sessionCacheSize,
                    channelCheckoutTimeout,
                    publisherConfirms);

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

                TestMessage testMessage = new TestMessage();
                testMessage.setId(UUID.randomUUID().toString());
                testMessage.setContent("hello world:" + count);
                Message message = jsonMessageConverter.toMessage(testMessage, null);

                cloudMQClient.send(message, String.valueOf(count));
                System.out.println("第" + count + "条消息已被发送.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
