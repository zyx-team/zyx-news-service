/***********************************************************************
 * Module:  AbstractSendDecorator.java
 * Author:  Administrator
 * Purpose: Defines the Class AbstractSendDecorator
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.publish;

import org.json.simple.parser.ParseException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.starnet.cloudmq.rabbitmq.CloudMQException;
import com.starnet.cloudmq.rabbitmq.CloudMQQueueResource;

/**
 * 发送direct,fanout模式消息的装饰基类
 * 
 * @author Administrator
 *
 */
public abstract class AbstractSendDecorator implements CloudMqSendService {
	/** 
    * 
    *  */
	protected CloudMqSendService cloudMqSendService = null;

	public void setMsgContentLengthLimit(int msgContentLengthLimit) {
		cloudMqSendService.setMsgContentLengthLimit(msgContentLengthLimit);
	}

	@Override
	public void setCheckResourceInterval(long checkResourceInterval) {
		cloudMqSendService.setCheckResourceInterval(checkResourceInterval);

	}
	/**
    * 设置集群迁移的检测时间间隔，并开启检测线程
    * @param clusterRemoveQueryInterval 单位ms
    */
    public void setClusterRemoveQueryInterval(long clusterRemoveQueryInterval){
    	cloudMqSendService.setClusterRemoveQueryInterval(clusterRemoveQueryInterval);
    }
    /**
     * 关闭集群迁移的检测线程
     */
    public void closeClusterRemoveQuery(){
    	cloudMqSendService.closeClusterRemoveQuery();
    }
    /**
     * 获取队列的状态
     */
    @Override
    public CloudMQQueueResource getQueueResource() throws CloudMQException,
    		ParseException {
    	return cloudMqSendService.getQueueResource();
    }
    @Override
    public RabbitTemplate getRabbitTemplate() {
    	return cloudMqSendService.getRabbitTemplate();
    }
}