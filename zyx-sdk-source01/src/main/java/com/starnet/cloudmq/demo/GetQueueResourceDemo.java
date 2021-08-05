package com.starnet.cloudmq.demo;

import com.starnet.cloudmq.rabbitmq.CloudMQClient;
import com.starnet.cloudmq.rabbitmq.CloudMQQueueResource;

public class GetQueueResourceDemo {
    /**
     * 获取队列资源
     */
    public static void main(String[] args) {

        try {
            CloudMQClient cloudMQClient = new CloudMQClient("https://127.0.0.1:8200", "6acfeaeb-c2eb-4100-b8c1-28c2764f5e72", "29d01b10-41ac-4bc7-81c3-30ea47388f7b", "zxcvvb");

            CloudMQQueueResource queueResource = cloudMQClient.getQueueResource();
            System.out.println("Max_length:" + queueResource.getMax_length());
            System.out.println("Max_length_left:" + queueResource.getMax_length_left());
            System.out.println("Max_length_bytes:" + queueResource.getMax_length_bytes());
            System.out.println("Max_length_bytes_left:" + queueResource.getMax_length_bytes_left());
            System.out.println("Alarm_threshold:" + queueResource.getAlarm_threshold());
            System.out.println("Overflow_handle:" + queueResource.getOverflow_handle());
            System.out.println("msg_durable:" + queueResource.getMsg_durable());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
