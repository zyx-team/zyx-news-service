/***********************************************************************
 * Module:  TrasactionRouteSendDecorator.java
 * Author:  Administrator
 * Purpose: Defines the Class TrasactionRouteSendDecorator
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.publish.impl;

import java.util.*;

import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.starnet.cloudmq.rabbitmq.CloudMQException;
import com.starnet.cloudmq.rabbitmq.CloudMQQueueResource;
import com.starnet.cloudmq.rabbitmq.publish.AbstractRouteSendDecorator;
import com.starnet.cloudmq.rabbitmq.publish.RouteCloudMqSendService;

/**
 * 发送路由模式消息的事务装饰类
 * @author Administrator
 *
 */
public class TransactionRouteSendDecorator extends AbstractRouteSendDecorator {
   private TransactionTemplate transactionTemplate = null;
   
   /** @param connectionFactory 
    * @param cloudMqSendService
    * 发送路由模式消息的事务装饰类构造 */
   public  TransactionRouteSendDecorator(RouteCloudMqSendService routeCloudMqSendService,CachingConnectionFactory connectionFactory) {
       this.routeCloudMqSendService=routeCloudMqSendService;
       getRabbitTemplate().setChannelTransacted(true);
       RabbitTransactionManager transactionManager = new RabbitTransactionManager(connectionFactory);
       transactionTemplate = new TransactionTemplate(transactionManager);
   }
   
   /** @param routingKey 
    * @param messages
    *发送对象类型消息（事务机制）*/
   public void sendRouting(final String routingKey, final List<Message> messages) {
      transactionTemplate.execute(new TransactionCallback<String>() {
           @Override
           public String doInTransaction(TransactionStatus status) {
               
               for (Message message : messages) {
    		        routeCloudMqSendService.sendRouting(routingKey, message);
    	       }
               return status.toString();
           }
       });
   }
   
   /** @param routingKey 
    * @param messages
    * 发送字符串类型消息（事务机制）*/
   public void convertAndSendRouting(final String routingKey, final List<String> messages) {
      transactionTemplate.execute(new TransactionCallback<String>() {
           @Override
           public String doInTransaction(TransactionStatus status) {
               
               for (String message : messages) {
   		         	routeCloudMqSendService.convertAndSendRouting(routingKey, message);
   	           }
               return status.toString();
           }
       });
   }
   
  
   /** @param routingKey 
    * @param message
    *发送对象类型消息（事务机制）*/
   @Override
   public void sendRouting(final String routingKey, final Message message) {
      transactionTemplate.execute(new TransactionCallback<String>() {
           @Override
           public String doInTransaction(TransactionStatus status) {
               routeCloudMqSendService.sendRouting(routingKey, message);
               return status.toString();
           }
       });
   }
   
   /** @param routingKey 
    * @param message
    * 发送字符串类型消息（事务机制） */
   public void convertAndSendRouting(final String routingKey, final String message) {
      transactionTemplate.execute(new TransactionCallback<String>() {
           @Override
           public String doInTransaction(TransactionStatus status) {
               routeCloudMqSendService.convertAndSendRouting(routingKey, message);
               return status.toString();
           }
       });
   }



}