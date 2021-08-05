/***********************************************************************
 * Module:  SendService.java
 * Author:  Administrator
 * Purpose: Defines the Interface SendService
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.publish;

import org.json.simple.parser.ParseException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.starnet.cloudmq.rabbitmq.CloudMQException;
import com.starnet.cloudmq.rabbitmq.CloudMQQueueResource;


/** 
 * 发送消息的接口基类
 */
public interface SendService {
	/**
	 * 单条消息内容允许的最大长度,单位byte(字节)
	 * @param msgContentLengthLimit
	 */
   void setMsgContentLengthLimit(int msgContentLengthLimit);
   /**
    * 设置健康检查的时间间隔
    * @param checkResourceInterval 单位ms
    */
   public void setCheckResourceInterval(long checkResourceInterval);
   
   /**
    * 设置集群迁移的检测时间间隔，并开启检测线程
    * @param clusterRemoveQueryInterval 单位ms
    */
   public void setClusterRemoveQueryInterval(long clusterRemoveQueryInterval);
   /**
    * 关闭集群迁移的检测线程
    */
   public void closeClusterRemoveQuery();
   /**
    * 获取队列的状态
    * @return
    * @throws ParseException 
    */
   public CloudMQQueueResource getQueueResource() throws CloudMQException, ParseException;
   public RabbitTemplate getRabbitTemplate();
}