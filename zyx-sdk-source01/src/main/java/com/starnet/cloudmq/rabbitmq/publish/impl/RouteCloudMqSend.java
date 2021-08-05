/***********************************************************************
 * Module:  RouteCloudMqSend.java
 * Author:  Administrator
 * Purpose: Defines the Class RouteCloudMqSend
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.publish.impl;

import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;

import com.starnet.cloudmq.rabbitmq.publish.CloudMqSendAbstract;
import com.starnet.cloudmq.rabbitmq.publish.RouteCloudMqSendService;

/**
 * 发送路由模式消息实现类
 * @author Administrator
 *
 */
public class RouteCloudMqSend extends CloudMqSendAbstract implements RouteCloudMqSendService {
   
	public RouteCloudMqSend(){
		
	}
	
	/**
    * 发送路由模式消息类初始化
    * @param connAdr 
    * @param appKey 
    * @param appSecret 
    * @param qid 
    * @param sessionCacheSize 
    * @param channelCheckoutTimeout
	* @throws ParseException 
	    *  */
	public  RouteCloudMqSend(String connAdr, String appKey, String appSecret, String qid, int sessionCacheSize, long channelCheckoutTimeout) throws ParseException {
	    cloudMqInit(connAdr, appKey, appSecret, qid, sessionCacheSize, channelCheckoutTimeout, false);
	}
	/**
	 * 发送路由消息
	 */
	@Override
	public void sendRouting(String routingKey, Message message) {
		send(getRabbitTemplate().getExchange(), routingKey, message, null);
		
	}
	/**
	 * 发送路由消息
	 */
	@Override
	public void convertAndSendRouting(String routingKey, String message) {
		convertAndSend(getRabbitTemplate().getExchange(), routingKey, message, null);
		
	}

}