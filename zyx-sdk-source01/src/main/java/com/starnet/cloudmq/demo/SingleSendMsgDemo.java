package com.starnet.cloudmq.demo;

import com.starnet.cloudmq.rabbitmq.CloudMQClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.JsonMessageConverter;

import java.util.UUID;

public class SingleSendMsgDemo {
    /**
     * 发送对象类型的消息
     */
    public static void main(String[] args) {

        try {
            JsonMessageConverter jsonMessageConverter = new JsonMessageConverter();

            CloudMQClient cloudMQClient = new CloudMQClient("conn_adr", "app_key", "app_secret", "qid");

            int count = 1;
            cloudMQClient.setCheckResourceInterval(1);//设置判断是否发送消息的缓存时间,单位毫秒
            for (; count < 3; count++) {
                TestMessage testMessage = new TestMessage();
                testMessage.setId(UUID.randomUUID().toString());
                testMessage.setContent("hello world:" + count);
                Message message = jsonMessageConverter.toMessage(testMessage, null);

                cloudMQClient.send(message);
                System.out.println("第" + count + "条消息已被发送.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
