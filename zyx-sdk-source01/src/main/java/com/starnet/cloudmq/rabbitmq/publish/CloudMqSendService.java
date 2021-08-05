/***********************************************************************
 * Module:  CloudMqSendService.java
 * Author:  Administrator
 * Purpose: Defines the Interface CloudMqSendService
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.publish;

import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;

import com.starnet.cloudmq.rabbitmq.CloudMQException;

/** 
 * 发送direct,fanout模式消息的接口类
 */
public interface CloudMqSendService extends SendService {
	/** 
    * 发送消息（对象类型）
    */
   void send(Message message)  throws CloudMQException, ParseException;
   /** @param message
    * 发送消息（字符串类型）
    */
   void convertAndSend(String message) throws CloudMQException, ParseException;

}