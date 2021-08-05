/***********************************************************************
 * Module:  TrasactionSendDecorator.java
 * Author:  Administrator
 * Purpose: Defines the Class TrasactionSendDecorator
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.publish.impl;

import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.starnet.cloudmq.rabbitmq.CloudMQException;
import com.starnet.cloudmq.rabbitmq.publish.AbstractSendDecorator;
import com.starnet.cloudmq.rabbitmq.publish.CloudMqSendService;

/**
 * 发送direct,fanout模式消息的事务装饰类
 * @author Administrator
 *
 */
public class TransactionSendDecorator extends AbstractSendDecorator {
   
   private TransactionTemplate transactionTemplate = null;;
   
   /** @param connectionFactory 
    * @param cloudMqSendService
    * 发送direct,fanout模式消息的事务装饰类构造 */
   public TransactionSendDecorator(CloudMqSendService cloudMqSendService,CachingConnectionFactory connectionFactory) {
	   this.cloudMqSendService=cloudMqSendService;
	   getRabbitTemplate().setChannelTransacted(true);
	   RabbitTransactionManager transactionManager = new RabbitTransactionManager(connectionFactory);
       transactionTemplate = new TransactionTemplate(transactionManager);
   }
   
   /** @param messages
    * 发送对象类型消息（事务机制） */
   public void send(final List<Message> messages) {
      transactionTemplate.execute(new TransactionCallback<String>() {
           @Override
           public String doInTransaction(TransactionStatus status) {
               for (Message message : messages) {
	   		        try {
						cloudMqSendService.send(message);
					} catch (CloudMQException e) {
						throw e;
					} catch (ParseException e) {
						throw new CloudMQException("14010013","json解析错误", e);
					}
	   	       }
               return status.toString();
           }
       });
   }
   
   /** @param messages
    * 发送字符串类型消息（事务机制） */
   public void convertAndSend(final List<String> messages) {
      transactionTemplate.execute(new TransactionCallback<String>() {
           @Override
           public String doInTransaction(TransactionStatus status) {
               
               for (String message : messages) {
            	   try{
            		   cloudMqSendService.convertAndSend(message);
            	   } catch (CloudMQException e) {
						throw e;
            	   } catch (ParseException e) {
						throw new CloudMQException("14010013","json解析错误", e);
            	   }
   		         
   	         }
               return status.toString();
           }
       });
   }
   
   /** @param message
    *发送对象类型消息（事务机制） */
   public void send(final Message message) {
      transactionTemplate.execute(new TransactionCallback<String>() {
           @Override
           public String doInTransaction(TransactionStatus status) {
               
               try{
            	   cloudMqSendService.send(message);
        	   } catch (CloudMQException e) {
					throw e;
        	   } catch (ParseException e) {
					throw new CloudMQException("14010013","json解析错误", e);
        	   }
               return status.toString();
           }
       });
   }
   
   /** @param message
    * 发送字符串类型消息（事务机制） */
   public void convertAndSend(final String message) {
      transactionTemplate.execute(new TransactionCallback<String>() {
           @Override
           public String doInTransaction(TransactionStatus status) {
        	   try{
        		   cloudMqSendService.convertAndSend(message);
        	   } catch (CloudMQException e) {
					throw e;
        	   } catch (ParseException e) {
					throw new CloudMQException("14010013","json解析错误", e);
        	   }
               return status.toString();
           }
       });
   }

	
	public void setMsgContentLengthLimit(int msgContentLengthLimit) {
		cloudMqSendService.setMsgContentLengthLimit(msgContentLengthLimit);
		
	}
	
}