/***********************************************************************
 * Module:  CloudMqFactory.java
 * Author:  Administrator
 * Purpose: Defines the Class CloudMqFactory
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq;

import org.json.simple.parser.ParseException;

import com.starnet.cloudmq.rabbitmq.consumer.CloudMqConsumerService;
import com.starnet.cloudmq.rabbitmq.consumer.impl.CloudMqConsumer;
import com.starnet.cloudmq.rabbitmq.publish.CloudMqSendService;
import com.starnet.cloudmq.rabbitmq.publish.ConfirmCloudMqSendService;
import com.starnet.cloudmq.rabbitmq.publish.ConfirmRouteCloudMqSendService;
import com.starnet.cloudmq.rabbitmq.publish.RouteCloudMqSendService;
import com.starnet.cloudmq.rabbitmq.publish.impl.ConfirmRouteCloudMqSend;
import com.starnet.cloudmq.rabbitmq.publish.impl.RouteCloudMqSend;
import com.starnet.cloudmq.rabbitmq.publish.impl.TransactionRouteSendDecorator;
import com.starnet.cloudmq.rabbitmq.publish.impl.TransactionSendDecorator;


/**
 * mq连接工厂类
 * @author Administrator
 *
 */
public class CloudMqFactory {
  /**
   * 路由模式消费发送
   * @param connAdr
   * @param appKey
   * @param appSecret
   * @param qid
   * @param sessionCacheSize
   * @param channelCheckoutTimeout
   * @return
   * @throws ParseException
   */
   public static RouteCloudMqSendService newRouteCloudMqSend(String connAdr, String appKey, String appSecret, String qid, int sessionCacheSize, long channelCheckoutTimeout) throws ParseException {
      
      return new RouteCloudMqSend(connAdr, appKey, appSecret, qid, sessionCacheSize, channelCheckoutTimeout);
   }
   
   /**
    * 路由模式消息发送（确认机制）
    * @param connAdr
    * @param appKey
    * @param appSecret
    * @param qid
    * @param sessionCacheSize
    * @param channelCheckoutTimeout
    * @param publisherConfirms
    * @return
    * @throws ParseException
    */
   public static ConfirmRouteCloudMqSendService newRouteCloudMqSendConfirm(String connAdr, String appKey, String appSecret, String qid, int sessionCacheSize, long channelCheckoutTimeout, Boolean publisherConfirms) throws ParseException {
      
      return new ConfirmRouteCloudMqSend(connAdr, appKey, appSecret, qid, sessionCacheSize, channelCheckoutTimeout, publisherConfirms);
   }
   
   /**
    * 路由消息发送（事务机制）
    * @param connAdr
    * @param appKey
    * @param appSecret
    * @param qid
    * @param sessionCacheSize
    * @param channelCheckoutTimeout
    * @return
    * @throws ParseException
    */
   public static TransactionRouteSendDecorator newRouteCloudMqSendTransaction(String connAdr, String appKey, String appSecret, String qid, int sessionCacheSize, long channelCheckoutTimeout) throws ParseException {
	  RouteCloudMqSend routeCloudMqSend=  new RouteCloudMqSend(connAdr, appKey, appSecret, qid, sessionCacheSize, channelCheckoutTimeout);
      return new TransactionRouteSendDecorator(routeCloudMqSend, routeCloudMqSend.getConnectionFactory());
   }
   
   /**
    * direct,fanout消息发送
    * @param connAdr
    * @param appKey
    * @param appSecret
    * @param qid
    * @param sessionCacheSize
    * @param channelCheckoutTimeout
    * @return
    * @throws CloudMQException
    * @throws ParseException
    */
   public static CloudMqSendService newCommonCloudMqSend(String connAdr, String appKey, String appSecret, String qid, int sessionCacheSize, long channelCheckoutTimeout) throws CloudMQException, ParseException {
      
      return (CloudMqSendService) new CloudMQClient(connAdr, appKey,appSecret,qid,sessionCacheSize,channelCheckoutTimeout);
   }
   
   /**
    * direct,fanout消息发送(确认机制)
    * @param connAdr
    * @param appKey
    * @param appSecret
    * @param qid
    * @param sessionCacheSize
    * @param channelCheckoutTimeout
    * @param publisherConfirms
    * @return
    * @throws CloudMQException
 * @throws ParseException 
    */
   public static ConfirmCloudMqSendService newCommonCloudMqSendConfirm(String connAdr, String appKey, String appSecret, String qid, int sessionCacheSize, long channelCheckoutTimeout, Boolean publisherConfirms) throws CloudMQException, ParseException  {
      return (ConfirmCloudMqSendService) new CloudMQClient(connAdr, appKey,appSecret,qid,sessionCacheSize,channelCheckoutTimeout,publisherConfirms);
   }
   
   /**
    * direct,fanout消息发送(事务机制)
    * @param connAdr
    * @param appKey
    * @param appSecret
    * @param qid
    * @param sessionCacheSize
    * @param channelCheckoutTimeout
    * @return
 * @throws ParseException 
 * @throws CloudMQException 
    */
   public static TransactionSendDecorator newCommonCloudMqSendTransaction(String connAdr, String appKey, String appSecret, String qid, int sessionCacheSize, long channelCheckoutTimeout) throws CloudMQException, ParseException {
	   CloudMQClient cloudMQClient= new CloudMQClient(connAdr, appKey,appSecret,qid,sessionCacheSize,channelCheckoutTimeout);
       return new TransactionSendDecorator((CloudMqSendService)cloudMQClient,cloudMQClient.getConnectionFactory());
   }
   
   /**
    * 消费消息
    * @param connAdr
    * @param appKey
    * @param appSecret
    * @param qid
    * @return
    * @throws ParseException
    */
   public static CloudMqConsumerService newCloudMqConsumer(String connAdr, String appKey, String appSecret, String qid) throws ParseException {
      
      return new CloudMqConsumer(connAdr, appKey, appSecret, qid);
   }

}