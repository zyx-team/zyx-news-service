/***********************************************************************
 * Module:  CloudMqConsumerService.java
 * Author:  Administrator
 * Purpose: Defines the Interface CloudMqConsumerService
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
/**
 * 消费接口类
 * @author Administrator
 *
 */
public interface CloudMqConsumerService {
   /**
    *消费一条消息
    * @return
    */
   Message receive();
   /**
    * 消费一条消息
    * @return
    */
   String receiveAndConvert();
   /**
    * 自动ack 监听消费
    * @param messageListener
    */
   void listen(MessageListener messageListener);
   /**
    * 手动ack 监听消费
    * @param channelAwareMessageListener
    */
   void listenManualAck(ChannelAwareMessageListener channelAwareMessageListener);
   /**
    * 停止监听
    */
   void stopListen();
   
   public void setRecoveryInterval(long recoveryInterval);

   public void setConcurrentConsumers(int concurrentConsumers);

   public void setMaxConcurrentConsumers(int maxConcurrentConsumers);

   public void setStartConsumerMinInterval(int startConsumerMinInterval);

   public void setStopConsumerMinInterval(int stopConsumerMinInterval);

   public void setConsecutiveActiveTrigger(int consecutiveActiveTrigger);

   public void setConsecutiveIdleTrigger(int consecutiveIdleTrigger);

   public void setReceiveTimeout(long receiveTimeout);

   public void setShutdownTimeout(long shutdownTimeout);

   public void setPrefetchCount(int prefetchCount);

   public void setTxSize(int txSize);

   public void setDeclarationRetries(int declarationRetries);

   public void setFailedDeclarationRetryInterval(long failedDeclarationRetryInterval);

   public void setRetryDeclarationInterval(long retryDeclarationInterval);

   public int getActiveConsumerCount();
   /**
    * 设置集群迁移的检测时间间隔，并开启检测线程
    * @param clusterRemoveQueryInterval 单位ms
    */
   public void setClusterRemoveQueryInterval(long clusterRemoveQueryInterval);
   /**
    * 关闭集群迁移的检测线程
    */
   public void closeClusterRemoveQuery();

}