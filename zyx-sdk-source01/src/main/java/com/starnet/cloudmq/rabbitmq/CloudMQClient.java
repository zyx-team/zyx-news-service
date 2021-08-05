package com.starnet.cloudmq.rabbitmq;

import java.io.UnsupportedEncodingException;

import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.util.Assert;

import com.starnet.cloudmq.rabbitmq.publish.CloudMqSendAbstract;
import com.starnet.cloudmq.utils.Util;

public class CloudMQClient extends CloudMqSendAbstract{
    
    private SimpleMessageListenerContainer simpleMessageListenerContainer;
   
    private CloudMQClientConfiguration cloudMQClientConfiguration = new CloudMQClientConfiguration();
    private long checkResourceIntervall=1000L; 
    private static int poolSize=10;
    
    /**
     * 单条消息内容允许的最大长度,单位byte(字节)
     */
    private int msgContentLengthLimit=0;

	public CloudMQClient(String conn_adr, String app_key, String app_secret, String qid, int sessionCacheSize, long channelCheckoutTimeout) throws CloudMQException, ParseException {
         cloudMqInit(conn_adr, app_key, app_secret, qid, sessionCacheSize, channelCheckoutTimeout, null);
	}

    public CloudMQClient(String conn_adr, String app_key, String app_secret, String qid, int sessionCacheSize, long channelCheckoutTimeout, Boolean publisherConfirms) throws CloudMQException, ParseException {
        
        cloudMqInit(conn_adr, app_key, app_secret, qid, sessionCacheSize, channelCheckoutTimeout, publisherConfirms);

    }

    public CloudMQClient(String conn_adr, String app_key, String app_secret, String qid, Boolean publisherConfirms) throws CloudMQException, ParseException {
        
        cloudMqInit(conn_adr, app_key, app_secret, qid, null, null, publisherConfirms);

    }


    public CloudMQClient(String conn_adr, String app_key, String app_secret, String qid) throws CloudMQException, ParseException {
        cloudMqInit(conn_adr, app_key, app_secret, qid, null, null, null);
    }
    
    /**
     * 设置健康监测线程池的核心池大小(不设置，默认为10)
     * @param poolSize
     * @throws CloudMQException
     */
    public  final static void setPoolSize(int poolSize) throws CloudMQException {
        Assert.isTrue(poolSize >= 0, "'poolSize' must be >= 0");
        CloudMQClient.poolSize=poolSize;
    }
    

   

    public Message receive() throws CloudMQException {
        checkRabbitTemplateIsExisits();
        return this.getRabbitTemplate().receive(this.getQID());
    }

    public String receiveAndConvert() throws CloudMQException {
        checkRabbitTemplateIsExisits();
        Message message = this.getRabbitTemplate().receive(this.getQID());
        if (message == null || message.getBody() == null) {
            return null;
        }
        //return byteToString(message.getBody());
        JsonMessageConverter jsonMessageConverter = new JsonMessageConverter();
        return jsonMessageConverter.fromMessage(message).toString();
    }

    public void listen(MessageListener messageListener) throws CloudMQException {
        checkRabbitTemplateIsExisits();
        declareListenContainer(messageListener);
    }

    public void stopListen() throws CloudMQException {
        checkRabbitTemplateIsExisits();
        if (simpleMessageListenerContainer == null) {
            throw new CloudMQException("14010004", "CloudMQClient_no_listen");
        }
        simpleMessageListenerContainer.stop();
    }

    public void setRecoveryInterval(long recoveryInterval) throws CloudMQException {
        checkRabbitTemplateIsExisits();
        cloudMQClientConfiguration.setRecoveryInterval(recoveryInterval);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setRecoveryInterval(recoveryInterval);
        }

    }

    public void setConcurrentConsumers(int concurrentConsumers) throws CloudMQException {
        checkRabbitTemplateIsExisits();
        cloudMQClientConfiguration.setConcurrentConsumers(concurrentConsumers);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setConcurrentConsumers(concurrentConsumers);
        }
    }

    public void setMaxConcurrentConsumers(int maxConcurrentConsumers) throws CloudMQException {
        checkRabbitTemplateIsExisits();
        cloudMQClientConfiguration.setMaxConcurrentConsumers(maxConcurrentConsumers);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setMaxConcurrentConsumers(maxConcurrentConsumers);
        }
    }

    public void setStartConsumerMinInterval(int startConsumerMinInterval) throws CloudMQException {
        checkRabbitTemplateIsExisits();
        cloudMQClientConfiguration.setStartConsumerMinInterval(startConsumerMinInterval);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setStartConsumerMinInterval(startConsumerMinInterval);
        }
    }

    public void setStopConsumerMinInterval(int stopConsumerMinInterval) throws CloudMQException {
        checkRabbitTemplateIsExisits();
        cloudMQClientConfiguration.setStopConsumerMinInterval(stopConsumerMinInterval);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setStopConsumerMinInterval(stopConsumerMinInterval);
        }
    }

    public void setConsecutiveActiveTrigger(int consecutiveActiveTrigger) throws CloudMQException {
        checkRabbitTemplateIsExisits();
        cloudMQClientConfiguration.setConsecutiveActiveTrigger(consecutiveActiveTrigger);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setConsecutiveActiveTrigger(consecutiveActiveTrigger);
        }
    }

    public void setConsecutiveIdleTrigger(int consecutiveIdleTrigger) throws CloudMQException {
        checkRabbitTemplateIsExisits();
        cloudMQClientConfiguration.setConsecutiveIdleTrigger(consecutiveIdleTrigger);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setConsecutiveIdleTrigger(consecutiveIdleTrigger);
        }
    }

    public void setReceiveTimeout(long receiveTimeout) throws CloudMQException {
        checkRabbitTemplateIsExisits();
        cloudMQClientConfiguration.setReceiveTimeout(receiveTimeout);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setReceiveTimeout(receiveTimeout);
        }
    }

    public void setShutdownTimeout(long shutdownTimeout) throws CloudMQException {
        checkRabbitTemplateIsExisits();
        cloudMQClientConfiguration.setShutdownTimeout(shutdownTimeout);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setShutdownTimeout(shutdownTimeout);
        }
    }

    public void setPrefetchCount(int prefetchCount) throws CloudMQException {
        checkRabbitTemplateIsExisits();
        cloudMQClientConfiguration.setPrefetchCount(prefetchCount);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setPrefetchCount(prefetchCount);
        }
    }

    public void setTxSize(int txSize) throws CloudMQException {
        checkRabbitTemplateIsExisits();
        cloudMQClientConfiguration.setTxSize(txSize);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setTxSize(txSize);
        }
    }

    public void setDeclarationRetries(int declarationRetries) throws CloudMQException {
        checkRabbitTemplateIsExisits();
        cloudMQClientConfiguration.setDeclarationRetries(declarationRetries);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setDeclarationRetries(declarationRetries);
        }
    }

    public void setFailedDeclarationRetryInterval(long failedDeclarationRetryInterval) throws CloudMQException {
        checkRabbitTemplateIsExisits();
        cloudMQClientConfiguration.setFailedDeclarationRetryInterval(failedDeclarationRetryInterval);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setFailedDeclarationRetryInterval(failedDeclarationRetryInterval);
        }
    }

    public void setRetryDeclarationInterval(long retryDeclarationInterval) throws CloudMQException {
        checkRabbitTemplateIsExisits();
        cloudMQClientConfiguration.setRetryDeclarationInterval(retryDeclarationInterval);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setRetryDeclarationInterval(retryDeclarationInterval);
        }
    }

    public int getActiveConsumerCount() throws CloudMQException {
        checkRabbitTemplateIsExisits();
        if (simpleMessageListenerContainer == null) {
            throw new CloudMQException("14010004", "CloudMQClient_no_listen");
        }
        return simpleMessageListenerContainer.getActiveConsumerCount();
    }

    private String byteToString(byte[] obj) throws CloudMQException {
        try {
            return new String(obj, "UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new CloudMQException("errcode:14010000,errmsg:" + "ex", e);
        }
    }

    private void checkRabbitTemplateIsExisits() {
        if (Util.isNull(this.getRabbitTemplate())) {
            throw new CloudMQException("14010005", "CloudMQClient_is_null");
        }
    }


    
    private void declareListenContainer(final MessageListener messageListener) {
        simpleMessageListenerContainer = new SimpleMessageListenerContainer(this.getConnectionFactory());
        simpleMessageListenerContainer.setMessageListener(messageListener);
        simpleMessageListenerContainer.setQueueNames(this.getQID());
        if (cloudMQClientConfiguration.getRecoveryInterval() != null) {
            simpleMessageListenerContainer.setRecoveryInterval(cloudMQClientConfiguration.getRecoveryInterval());
        }
        if (cloudMQClientConfiguration.getRetryDeclarationInterval() != null) {
            simpleMessageListenerContainer.setRetryDeclarationInterval(cloudMQClientConfiguration.getRetryDeclarationInterval());
        }
        if (cloudMQClientConfiguration.getFailedDeclarationRetryInterval() != null) {
            simpleMessageListenerContainer.setFailedDeclarationRetryInterval(cloudMQClientConfiguration.getFailedDeclarationRetryInterval());
        }
        if (cloudMQClientConfiguration.getStartConsumerMinInterval() != null) {
            simpleMessageListenerContainer.setStartConsumerMinInterval(cloudMQClientConfiguration.getStartConsumerMinInterval());
        }
        if (cloudMQClientConfiguration.getStopConsumerMinInterval() != null) {
            simpleMessageListenerContainer.setStopConsumerMinInterval(cloudMQClientConfiguration.getStopConsumerMinInterval());
        }
        if (cloudMQClientConfiguration.getConcurrentConsumers() != null) {
            simpleMessageListenerContainer.setConcurrentConsumers(cloudMQClientConfiguration.getConcurrentConsumers());
        }
        if (cloudMQClientConfiguration.getMaxConcurrentConsumers() != null) {
            simpleMessageListenerContainer.setMaxConcurrentConsumers(cloudMQClientConfiguration.getMaxConcurrentConsumers());
        }
        if (cloudMQClientConfiguration.getConsecutiveActiveTrigger() != null) {
            simpleMessageListenerContainer.setConsecutiveActiveTrigger(cloudMQClientConfiguration.getConsecutiveActiveTrigger());
        }
        if (cloudMQClientConfiguration.getConsecutiveIdleTrigger() != null) {
            simpleMessageListenerContainer.setConsecutiveIdleTrigger(cloudMQClientConfiguration.getConsecutiveIdleTrigger());
        }
        if (cloudMQClientConfiguration.getDeclarationRetries() != null) {
            simpleMessageListenerContainer.setDeclarationRetries(cloudMQClientConfiguration.getDeclarationRetries());
        }
        if (cloudMQClientConfiguration.getReceiveTimeout() != null) {
            simpleMessageListenerContainer.setReceiveTimeout(cloudMQClientConfiguration.getReceiveTimeout());
        }
        if (cloudMQClientConfiguration.getShutdownTimeout() != null) {
            simpleMessageListenerContainer.setShutdownTimeout(cloudMQClientConfiguration.getShutdownTimeout());
        }
        if (cloudMQClientConfiguration.getTxSize() != null) {
            simpleMessageListenerContainer.setTxSize(cloudMQClientConfiguration.getTxSize());
        }
        if (cloudMQClientConfiguration.getPrefetchCount() != null) {
            simpleMessageListenerContainer.setPrefetchCount(cloudMQClientConfiguration.getPrefetchCount());
        }
        simpleMessageListenerContainer.start();

    }

    

	
	
}
