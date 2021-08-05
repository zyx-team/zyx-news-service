package com.starnet.cloudmq.demo;

import com.starnet.cloudmq.rabbitmq.CloudMQClient;
import com.starnet.cloudmq.rabbitmq.CloudMqFactory;
import com.starnet.cloudmq.rabbitmq.consumer.CloudMqConsumerService;

public class ListenStringMsgDemo {
    /**
     * 监听字符串类型的消息
     */
    public static void main(String[] args) {

        try {
            CloudMQClient cloudMQClient = new CloudMQClient("https://10.31.57.173:8200", "e085725c-d87a-4274-8ac1-3d3341eabe4d",
        			"6b30326d-d29b-4919-a5c5-b063ad77b372", "fanout1");
            cloudMQClient.setConcurrentConsumers(20);
            cloudMQClient.listen(new TestStringMessageListener());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
