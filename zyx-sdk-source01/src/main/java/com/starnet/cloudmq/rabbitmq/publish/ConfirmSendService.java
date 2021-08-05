/***********************************************************************
 * Module:  ConfirmSendService.java
 * Author:  Administrator
 * Purpose: Defines the Interface ConfirmSendService
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.publish;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

/** 
 * 发送direct,fanout模式消息的接口类(确认机制基类)
 */
public interface ConfirmSendService extends SendService {
	/** @param confirmCallback
    * 确认机制时使用，用于接收确认的回执信息
    */
    void setConfirmCallback(RabbitTemplate.ConfirmCallback confirmCallback);

}