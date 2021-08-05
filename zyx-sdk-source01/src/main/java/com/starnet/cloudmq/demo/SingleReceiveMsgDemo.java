package com.starnet.cloudmq.demo;

import com.starnet.cloudmq.rabbitmq.CloudMQClient;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;

public class SingleReceiveMsgDemo {
    /**
     * 接收对象类型的消息
     */
    public static void main(String[] args) {

        try {
            CloudMQClient cloudMQClient = new CloudMQClient("conn_adr", "app_key", "app_secret", "qid");

            int count = 1;
            for (; count < 4; count++) {
                Message message = cloudMQClient.receive();
                printMsg(message, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printMsg(Message message, Integer count) throws ParseException {
        if (message == null) {
            System.out.println("第" + count + "次接收消息,没有消息.");
            return;
        }
        TestMessage testMessage = TestMessage.parseTestMessage(message);
        assert testMessage != null;
        System.out.println("第" + count + "次接收消息.");
        System.out.println("id:" + testMessage.getId());
        System.out.println("content:" + testMessage.getContent());
    }
}
