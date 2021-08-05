/***********************************************************************
 * Module:  ConfirmRouteCloudMqSend.java
 * Author:  Administrator
 * Purpose: Defines the Class ConfirmRouteCloudMqSend
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.publish.impl;

import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;

import com.starnet.cloudmq.rabbitmq.publish.ConfirmRouteCloudMqSendService;
/** 
 * 发送路由模式消息的类(确认机制类)
 */
public class ConfirmRouteCloudMqSend extends RouteCloudMqSend implements ConfirmRouteCloudMqSendService {
   /** @param connAdr 
    * @param appKey 
    * @param appSecret 
    * @param qid 
    * @param sessionCacheSize 
    * @param channelCheckoutTimeout 
    * @param publisherConfirms
    * 发送路由模式消息实例化
 * @throws ParseException */
   public  ConfirmRouteCloudMqSend(String connAdr, String appKey, String appSecret, String qid, int sessionCacheSize, long channelCheckoutTimeout, Boolean publisherConfirms) throws ParseException {
	   cloudMqInit(connAdr, appKey, appSecret, qid, sessionCacheSize, channelCheckoutTimeout, publisherConfirms);
   }

	@Override
	public void sendRouting(String routingKey, Message message, String id) {
		send(getRabbitTemplate().getExchange(), routingKey, message, id);
		
	}
	
	@Override
	public void convertAndSendRouting(String routingKey, String message, String id) {
		convertAndSend(getRabbitTemplate().getExchange(), routingKey, message, id);
		
	}

}