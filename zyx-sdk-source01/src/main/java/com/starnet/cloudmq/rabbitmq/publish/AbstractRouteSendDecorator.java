/***********************************************************************
 * Module:  AbstractRouteSendDecorator.java
 * Author:  Administrator
 * Purpose: Defines the Class AbstractRouteSendDecorator
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.publish;

import org.json.simple.parser.ParseException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.Assert;

import com.starnet.cloudmq.rabbitmq.CloudMQException;
import com.starnet.cloudmq.rabbitmq.CloudMQQueueResource;
import com.starnet.cloudmq.rabbitmq.threadDetection.ClusterRemoveQueryThread;
import com.starnet.cloudmq.rabbitmq.threadDetection.QueryRabbitClusterInfo;
import com.starnet.cloudmq.utils.ConfigUtil;

/**
 * 发送路由模式消息的装饰基类
 * 
 * @author Administrator
 *
 */
public abstract class AbstractRouteSendDecorator implements
		RouteCloudMqSendService {
	protected RouteCloudMqSendService routeCloudMqSendService = null;

	public void setMsgContentLengthLimit(int msgContentLengthLimit) {
		routeCloudMqSendService.setMsgContentLengthLimit(msgContentLengthLimit);
	}

	@Override
	public void setCheckResourceInterval(long checkResourceInterval) {
		routeCloudMqSendService.setCheckResourceInterval(checkResourceInterval);

	}
	/**
    * 设置集群迁移的检测时间间隔，并开启检测线程
    * @param clusterRemoveQueryInterval 单位ms
    */
    public void setClusterRemoveQueryInterval(long clusterRemoveQueryInterval){
    	routeCloudMqSendService.setClusterRemoveQueryInterval(clusterRemoveQueryInterval);
    }
    /**
     * 关闭集群迁移的检测线程
     */
    public void closeClusterRemoveQuery(){
    	routeCloudMqSendService.closeClusterRemoveQuery();
    }
    /**
     * 获取队列的状态
     */
    @Override
    public CloudMQQueueResource getQueueResource() throws CloudMQException,
    		ParseException {
    	return routeCloudMqSendService.getQueueResource();
    }
    @Override
    public RabbitTemplate getRabbitTemplate() {
    	return routeCloudMqSendService.getRabbitTemplate();
    }
}