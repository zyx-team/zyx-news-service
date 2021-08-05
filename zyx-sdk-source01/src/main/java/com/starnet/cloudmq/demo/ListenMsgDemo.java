package com.starnet.cloudmq.demo;

import com.starnet.cloudmq.rabbitmq.CloudMQClient;

public class ListenMsgDemo {
    /**
     * 监听对象类型的消息
     */
    public static void main(String[] args) {

        try {
            CloudMQClient cloudMQClient = new CloudMQClient("conn_adr", "app_key", "app_secret", "qid");

            cloudMQClient.listen(new TestMessageListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
