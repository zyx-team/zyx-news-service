package com.starnet.cloudmq.demo;

import com.starnet.cloudmq.rabbitmq.CloudMQClient;

public class ReceiveAndConvertMsgDemo {
    /**
     * 接收字符串类型的消息
     */
    public static void main(String[] args) {

        try {
            CloudMQClient cloudMQClient = new CloudMQClient("conn_adr", "app_key", "app_secret", "qid");

            int count = 1;
            for (; count < 3; count++) {
                String message = cloudMQClient.receiveAndConvert();
                printMsg(message, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printMsg(String message, Integer count) {
        if (message == null) {
            System.out.println("第" + count + "次接收消息,没有消息.");
            return;
        }
        System.out.println("第" + count + "次接收消息, 消息:" + message);
    }
}
