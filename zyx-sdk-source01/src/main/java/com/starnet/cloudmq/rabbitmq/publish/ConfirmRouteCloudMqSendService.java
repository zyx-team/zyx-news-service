/***********************************************************************
 * Module:  ConfirmRouteCloudMqSendService.java
 * Author:  Administrator
 * Purpose: Defines the Interface ConfirmRouteCloudMqSendService
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.publish;

import org.springframework.amqp.core.Message;

/** 
 * 发送路由模式消息的接口类(确认机制接口类)
 */
public interface ConfirmRouteCloudMqSendService extends ConfirmSendService {
   /** @param routingKey 
    * @param message 
    * @param id
    * 发送消息（对象类型）
    */
   void sendRouting(String routingKey, Message message, String id);
   /** @param routingKey 
    * @param message 
    * @param id
    * 发送消息（字符串类型）
    */
   void convertAndSendRouting(String routingKey, String message, String id);

}