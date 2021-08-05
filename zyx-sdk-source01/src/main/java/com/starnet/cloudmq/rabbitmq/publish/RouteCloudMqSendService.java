/***********************************************************************
 * Module:  RouteCloudMqSendService.java
 * Author:  Administrator
 * Purpose: Defines the Interface RouteCloudMqSendService
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.publish;

import org.springframework.amqp.core.Message;

/** 
 * 发送路由模式消息的接口类
 */
public interface RouteCloudMqSendService extends SendService {
   /** @param routingKey 
    * @param message
    * 发送消息（对象类型）
    */
   void sendRouting(String routingKey, Message message);
   /** @param routingKey 
    * @param message
    *发送消息（字符串类型） 
    */
   void convertAndSendRouting(String routingKey, String message);

}