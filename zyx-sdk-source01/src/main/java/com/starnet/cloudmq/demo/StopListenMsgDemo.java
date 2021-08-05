package com.starnet.cloudmq.demo;

import com.starnet.cloudmq.rabbitmq.CloudMQClient;

public class StopListenMsgDemo {
    /**
     * 停止监听消息
     */
    public static void main(String[] args) {

        try {
            CloudMQClient cloudMQClient = new CloudMQClient("conn_adr", "app_key", "app_secret", "qid");

            cloudMQClient.listen(new TestMessageListener());
            cloudMQClient.stopListen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
