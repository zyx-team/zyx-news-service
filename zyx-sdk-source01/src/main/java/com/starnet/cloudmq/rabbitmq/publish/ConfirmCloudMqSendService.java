/***********************************************************************
 * Module:  ConfirmCloudMqSendService.java
 * Author:  Administrator
 * Purpose: Defines the Interface ConfirmCloudMqSendService
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.publish;

import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;

import com.starnet.cloudmq.rabbitmq.CloudMQException;

/** 
 * 发送direct,fanout模式消息的接口类(确认机制接口类)
 */
public interface ConfirmCloudMqSendService extends ConfirmSendService {
	/** @param message 
    * @param id（消息唯一标示，确认机制时使用）
    * 发送消息
    */
   void send(Message message, String id) throws CloudMQException, ParseException;
   /** @param message 
    * @param id（消息唯一标示，确认机制时使用）
    * 发送消息（字符串类型）
    */
   void convertAndSend(String message, String id) throws CloudMQException, ParseException;

}