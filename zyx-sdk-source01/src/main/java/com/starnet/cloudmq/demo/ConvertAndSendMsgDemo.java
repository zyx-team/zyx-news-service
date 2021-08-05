package com.starnet.cloudmq.demo;

import com.starnet.cloudmq.rabbitmq.CloudMQClient;
import com.starnet.cloudmq.rabbitmq.CloudMqFactory;
import com.starnet.cloudmq.rabbitmq.publish.CloudMqSendService;
import com.starnet.cloudmq.rabbitmq.publish.RouteCloudMqSendService;

public class ConvertAndSendMsgDemo {
    /**
     * 发送字符串类型消息
     */
    public static void main(String[] args) {

        try {
//            CloudMQClient cloudMQClient = new CloudMQClient("conn_adr", "app_key", "app_secret", "qid");
//
//            cloudMQClient.setCheckResourceInterval(1);//设置判断是否发送消息的缓存时间,单位毫秒
//            int count = 1;
//            for (; count < 3; count++) {
//                cloudMQClient.convertAndSend("你好,hello" + count);
//                System.out.println("第" + count + "条消息已被发送.");
//            }
        	CloudMqSendService routeCloudMqSendService=  CloudMqFactory.newCommonCloudMqSend("https://10.31.57.173:8200", "e085725c-d87a-4274-8ac1-3d3341eabe4d",
        			"6b30326d-d29b-4919-a5c5-b063ad77b372", "fanout1", 100, 100);
        	//routeCloudMqSendService.setClusterRemoveQueryInterval(1000);
        	routeCloudMqSendService.setCheckResourceInterval(100);
           int count = 1;
         for (; count < 20; count++) {
        	 Thread.sleep(20000);
        	 routeCloudMqSendService.convertAndSend(count+"ss");
             System.out.println("第" + count + "条消息已被发送.");
         }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
