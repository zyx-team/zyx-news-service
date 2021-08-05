/***********************************************************************
 * Module:  DeclareRabbitUtils.java
 * Author:  Administrator
 * Purpose: Defines the Class DeclareRabbitUtils
 ***********************************************************************/

package com.starnet.cloudmq.utils;

import java.awt.datatransfer.StringSelection;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.starnet.cloudmq.rabbitmq.CloudMQException;
import com.starnet.cloudmq.rabbitmq.consumer.CloudMqConsumerAbstract;
import com.starnet.cloudmq.rabbitmq.publish.CloudMqSendAbstract;
import com.starnet.cloudmq.rabbitmq.threadDetection.QueryRabbitClusterInfo;


/**
 * 初始化rabbitmq的amqp连接的工具类
 * @author Administrator
 *
 */
public class DeclareRabbitUtils {
   /** @param appKey 
    * @param qid 
    * @param sessionCacheSize 
    * @param channelCheckoutTimeout 
    * @param publisherConfirms
 * @throws ParseException 
    * */
   private static void declareRabbitSettings(CloudMqSendAbstract cloudMqSendAbstract, QueryRabbitClusterInfo queryRabbitClusterInfo,String connAdr, String appKey, String appSecret, String qid, Integer sessionCacheSize, Long channelCheckoutTimeout, Boolean publisherConfirms) throws ParseException {
      try{
    	   CachingConnectionFactory connectionFactory=cloudMqSendAbstract.getConnectionFactory();
    	   connectionFactory.setHost(ConfigUtil.getRabbitHost(appKey, qid));
           connectionFactory.setPort(ConfigUtil.getRabbitPort(appKey, qid));
           connectionFactory.setUsername(ConfigUtil.getRabbitUser(appKey, qid));
           connectionFactory.setPassword(ConfigUtil.getRabbitPwd(appKey, qid));
           connectionFactory.setVirtualHost(ConfigUtil.getRabbitVhost(appKey, qid));
           if(sessionCacheSize!=null){
           	connectionFactory.setChannelCacheSize(sessionCacheSize);
           }
           if(channelCheckoutTimeout!=null){
           	connectionFactory.setChannelCheckoutTimeout(channelCheckoutTimeout);
           }
           if(publisherConfirms!=null){
           	connectionFactory.setPublisherConfirms(publisherConfirms);
           }
           cloudMqSendAbstract.setRabbitAdmin(new RabbitAdmin(connectionFactory));
           cloudMqSendAbstract.setRabbitTemplate(declareRabbitExchange(cloudMqSendAbstract.getRabbitAdmin(),appKey, qid,connectionFactory));
           
           declareQueue(cloudMqSendAbstract.getRabbitAdmin(),appKey, qid);
       	}catch(Exception ex){
       		if(cloudMqSendAbstract.getRabbitSettingsCount()<cloudMqSendAbstract.getRabbitSettingsCountLimit()){
       	        cloudMqSendAbstract.setRabbitSettingsCount(cloudMqSendAbstract.getRabbitSettingsCount()+1);
       	        queryRabbitClusterInfo.getRabbitSettings();
       			declareRabbitSettings(cloudMqSendAbstract,queryRabbitClusterInfo,connAdr, appKey, appSecret, qid, sessionCacheSize, channelCheckoutTimeout, publisherConfirms);
       		}else{
       			throw new CloudMQException("errcode:14010011,errmsg:" + ex.getMessage(), ex);
       		}
       	}
   }
   /**
    * 
    * @param cloudMqConsumerAbstract
    * @param queryRabbitClusterInfo
    * @param connAdr
    * @param appKey
    * @param appSecret
    * @param qid
    * @param sessionCacheSize
    * @param channelCheckoutTimeout
    * @param publisherConfirms
    * @throws ParseException
    */
   private static void declareRabbitSettingsConsumer(CloudMqConsumerAbstract cloudMqConsumerAbstract, QueryRabbitClusterInfo queryRabbitClusterInfo,String connAdr, String appKey, String appSecret, String qid, Integer sessionCacheSize, Long channelCheckoutTimeout, Boolean publisherConfirms) throws ParseException {
      try{
    	   CachingConnectionFactory connectionFactory=cloudMqConsumerAbstract.getConnectionFactory();
    	   connectionFactory.setHost(ConfigUtil.getRabbitHost(appKey, qid));
           connectionFactory.setPort(ConfigUtil.getRabbitPort(appKey, qid));
           connectionFactory.setUsername(ConfigUtil.getRabbitUser(appKey, qid));
           connectionFactory.setPassword(ConfigUtil.getRabbitPwd(appKey, qid));
           connectionFactory.setVirtualHost(ConfigUtil.getRabbitVhost(appKey, qid));
           if(sessionCacheSize!=null){
           	connectionFactory.setChannelCacheSize(sessionCacheSize);
           }
           if(channelCheckoutTimeout!=null){
           	connectionFactory.setChannelCheckoutTimeout(channelCheckoutTimeout);
           }
           if(publisherConfirms!=null){
           	connectionFactory.setPublisherConfirms(publisherConfirms);
           }
           cloudMqConsumerAbstract.setRabbitAdmin(new RabbitAdmin(connectionFactory));
           cloudMqConsumerAbstract.setRabbitTemplate(declareRabbitExchange(cloudMqConsumerAbstract.getRabbitAdmin(),appKey, qid,connectionFactory));
           
           declareQueue(cloudMqConsumerAbstract.getRabbitAdmin(),appKey, qid);
       	}catch(Exception ex){
       		if(cloudMqConsumerAbstract.getRabbitSettingsCount()<cloudMqConsumerAbstract.getRabbitSettingsCountLimit()){
       			cloudMqConsumerAbstract.setRabbitSettingsCount(cloudMqConsumerAbstract.getRabbitSettingsCount()+1);
       	        queryRabbitClusterInfo.getRabbitSettings();
       			declareRabbitSettingsConsumer(cloudMqConsumerAbstract,queryRabbitClusterInfo,connAdr, appKey, appSecret, qid, sessionCacheSize, channelCheckoutTimeout, publisherConfirms);
       		}else{
       			throw new CloudMQException("errcode:14010011,errmsg:" + ex.getMessage(), ex);
       		}
       	}
   }
   /** @param appKey 
    * @param qid
    *  */
   private static RabbitTemplate declareRabbitExchange(RabbitAdmin rabbitAdmin,String appKey, String qid,CachingConnectionFactory cachingConnectionFactory) {
	   Exchange exchange;
	   RabbitTemplate rabbitTemplate=null;
       if (ConfigUtil.getRabbitExchangeType(appKey, qid).equals(ExchangeTypes.DIRECT)) {
           exchange = new DirectExchange(ConfigUtil.getRabbitExchangeName(appKey, qid));
       } else if (ConfigUtil.getRabbitExchangeType(appKey, qid).equals(ExchangeTypes.FANOUT)) {
           exchange = new FanoutExchange(ConfigUtil.getRabbitExchangeName(appKey, qid));
       }else if (ConfigUtil.getRabbitExchangeType(appKey, qid).equals(ExchangeTypes.TOPIC)) {
           exchange = new TopicExchange(ConfigUtil.getRabbitExchangeName(appKey, qid));
       } else {
           return null;
       }
       rabbitAdmin.declareExchange(exchange);
       rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
       
       rabbitTemplate.setExchange(exchange.getName());
       if (rabbitTemplate.isConfirmListener()) {
           rabbitTemplate.setMandatory(true);
       }
       return rabbitTemplate;
   }
   
   /** @param appKey  
    * @param qid
    *  */
   private static void declareQueue(RabbitAdmin rabbitAdmin,String appKey, String qid) {
	   if (ConfigUtil.getRabbitExchangeType(appKey, qid).equals(ExchangeTypes.DIRECT)) {
           declareDirectQueue(rabbitAdmin,appKey, qid);
       }else if (ConfigUtil.getRabbitExchangeType(appKey, qid).equals(ExchangeTypes.FANOUT)) {
           declareFanoutQueue(rabbitAdmin,appKey, qid);
       }else if (ConfigUtil.getRabbitExchangeType(appKey, qid).equals(ExchangeTypes.TOPIC)) {
           declareRoutingQueue(rabbitAdmin,appKey, qid);
       }
   }
   
   /** @param appKey 
    * @param qid
    * */
   private static void declareDirectQueue(RabbitAdmin rabbitAdmin,String appKey, String qid) {
	   Queue queue = new Queue(qid, true, false, false);
       DirectExchange exchange = new DirectExchange(ConfigUtil.getRabbitExchangeName(appKey, qid));
       rabbitAdmin.declareQueue(queue);
       rabbitAdmin.declareBinding(
               BindingBuilder.bind(queue).to(exchange).with(qid));
   }
   
   /** @param appKey 
    * @param qid
    *  */
   private static void declareFanoutQueue(RabbitAdmin rabbitAdmin,String appKey, String qid) {
	   Queue queue = new Queue(qid, true, false, false);
       FanoutExchange exchange = new FanoutExchange(ConfigUtil.getRabbitExchangeName(appKey, qid));
       rabbitAdmin.declareQueue(queue);
       rabbitAdmin.declareBinding(
               BindingBuilder.bind(queue).to(exchange));
   }
   /** @param appKey 
    * @param qid
    *  */
   private static void declareRoutingQueue(RabbitAdmin rabbitAdmin,String appKey, String qid) {
	   Queue queue = new Queue(qid, true, false, false);
       TopicExchange exchange = new TopicExchange(ConfigUtil.getRabbitExchangeName(appKey, qid));
       rabbitAdmin.declareQueue(queue);
       String routing= ConfigUtil.getRabbitQueueRoutingkey(appKey, qid);
       if(routing!=null&&(!routing.equals(""))){
    	   String[] routings=routing.split(";");
    	   for (String routingKey : routings) {
    		   rabbitAdmin.declareBinding(
    	               BindingBuilder.bind(queue).to(exchange).with(routingKey));
    	   }
       }
       
   }
   /** @param cloudMqSendAbstract 
    * @param queryRabbitClusterInfo 
    * @param appKey 
    * @param qid 
    * @param sessionCacheSize 
    * @param channelCheckoutTimeout 
    * @param publisherConfirms
    * 初始化rabbitmq的amqp连接 
 * @throws ParseException 
    */
   public static void declareRabbit(CloudMqSendAbstract cloudMqSendAbstract, QueryRabbitClusterInfo queryRabbitClusterInfo,String connAdr, String appKey, String appSecret, String qid, Integer sessionCacheSize, Long channelCheckoutTimeout, Boolean publisherConfirms) throws ParseException {
      if (Util.isEmpty(connAdr)) {
           throw new CloudMQException("14010001", "conn_adr_is_nullOrEmpty");
       }
       
       if (Util.isEmpty(appKey)) {
           throw new CloudMQException("14010002", "app_key_is_nullOrEmpty");
       }
       if (Util.isEmpty(appSecret)) {
           throw new CloudMQException("14010010", "app_secret_is_nullOrEmpty");
       }
       if (Util.isEmpty(qid)) {
           throw new CloudMQException("14010003", "qid_is_nullOrEmpty");
       }
       cloudMqSendAbstract.setRabbitSettingsCount(cloudMqSendAbstract.getRabbitSettingsCount()+1);
       queryRabbitClusterInfo.getRabbitSettings();
       declareRabbitSettings(cloudMqSendAbstract,queryRabbitClusterInfo,connAdr, appKey, appSecret, qid, sessionCacheSize, channelCheckoutTimeout, publisherConfirms);
   }
   /** @param cloudMqSendAbstract 
    * @param queryRabbitClusterInfo 
    * @param appKey 
    * @param qid 
    * @param sessionCacheSize 
    * @param channelCheckoutTimeout 
    * @param publisherConfirms
    * 初始化rabbitmq的amqp连接 
 * @throws ParseException 
    */
   public static void declareRabbit(CloudMqConsumerAbstract cloudMqConsumerAbstract, QueryRabbitClusterInfo queryRabbitClusterInfo,String connAdr, String appKey, String appSecret, String qid, Integer sessionCacheSize, Long channelCheckoutTimeout, Boolean publisherConfirms) throws ParseException {
      if (Util.isEmpty(connAdr)) {
           throw new CloudMQException("14010001", "conn_adr_is_nullOrEmpty");
       }
       
       if (Util.isEmpty(appKey)) {
           throw new CloudMQException("14010002", "app_key_is_nullOrEmpty");
       }
       if (Util.isEmpty(appSecret)) {
           throw new CloudMQException("14010010", "app_secret_is_nullOrEmpty");
       }
       if (Util.isEmpty(qid)) {
           throw new CloudMQException("14010003", "qid_is_nullOrEmpty");
       }
       cloudMqConsumerAbstract.setRabbitSettingsCount(cloudMqConsumerAbstract.getRabbitSettingsCount()+1);
       queryRabbitClusterInfo.getRabbitSettings();
       declareRabbitSettingsConsumer(cloudMqConsumerAbstract,queryRabbitClusterInfo,connAdr, appKey, appSecret, qid, sessionCacheSize, channelCheckoutTimeout, publisherConfirms);
   }
}