/***********************************************************************
 * Module:  CloudMqConsumerAbstract.java
 * Author:  Administrator
 * Purpose: Defines the Class CloudMqConsumerAbstract
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.consumer;

import java.util.*;

import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.util.Assert;

import com.starnet.cloudmq.rabbitmq.CloudMQClientConfiguration;
import com.starnet.cloudmq.rabbitmq.CloudMQException;
import com.starnet.cloudmq.rabbitmq.threadDetection.ClusterRemoveQueryThread;
import com.starnet.cloudmq.rabbitmq.threadDetection.QueryRabbitClusterInfo;
import com.starnet.cloudmq.utils.ConfigUtil;
import com.starnet.cloudmq.utils.DeclareRabbitUtils;
import com.starnet.cloudmq.utils.StringUtil;
import com.starnet.cloudmq.utils.Util;

/**
 * 消费抽象基类
 * 
 * @author Administrator
 *
 */
public abstract class CloudMqConsumerAbstract implements CloudMqConsumerService {
	private CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
	private RabbitAdmin rabbitAdmin;
	private RabbitTemplate rabbitTemplate;
	private SimpleMessageListenerContainer simpleMessageListenerContainer;
    private CloudMQClientConfiguration cloudMQClientConfiguration = new CloudMQClientConfiguration();

	private String QID = null;
	private String APP_KEY = null;
	private String APP_SECRET = null;
	private String CONN_ADR = null;
	private int rabbitSettingsCount=0;//记录调用getRabbitSettings获取云消息集群信息的次数。
    private int rabbitSettingsCountLimit=3;//记录调用getRabbitSettings获取云消息集群信息的次数的最大上限。
    private ClusterRemoveQueryThread clusterRemoveQueryThread=null;
    /** @pdOid 9568b85d-9c0b-4d51-bf56-b6190ae69453 */
	public Message receive() {
		checkRabbitTemplateIsExisits();
		return rabbitTemplate.receive(QID);
	}

	/** @pdOid 553e894e-df09-414d-9244-2155bfe732a0 */
	public String receiveAndConvert() {

		checkRabbitTemplateIsExisits();
		Message message = rabbitTemplate.receive(QID);
		if (message == null || message.getBody() == null) {
			return null;
		}
		//return StringUtil.byteToString(message.getBody());
        JsonMessageConverter jsonMessageConverter = new JsonMessageConverter();
        return jsonMessageConverter.fromMessage(message).toString();

	}

	/**
	 * @param messageListener
	 * @pdOid 12da9135-e32e-41ec-af6d-70650587b710
	 */
	public void listen(MessageListener messageListener) {
		checkRabbitTemplateIsExisits();
		simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        simpleMessageListenerContainer.setMessageListener(messageListener);
		declareListenContainer(simpleMessageListenerContainer);
	}

	/**
	 * @param channelAwareMessageListener
	 * @pdOid 8e096fa1-3da5-4a23-85b0-bdad6edd821b
	 */
	public void listenManualAck(
			ChannelAwareMessageListener channelAwareMessageListener) {
		checkRabbitTemplateIsExisits();
		simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        simpleMessageListenerContainer.setMessageListener(channelAwareMessageListener);
        simpleMessageListenerContainer
		.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        declareListenContainer(simpleMessageListenerContainer);

		
	}

	/** @pdOid dc148a7c-6d80-4655-a8e8-0e83c34f42b0 */
	public void stopListen() {
		checkRabbitTemplateIsExisits();
		if (simpleMessageListenerContainer == null) {
			throw new CloudMQException("14010004", "CloudMQClient_no_listen");
		}
		simpleMessageListenerContainer.stop();
	}

	private void checkRabbitTemplateIsExisits() {
		if (Util.isNull(rabbitTemplate)) {
			throw new CloudMQException("14010005", "CloudMQClient_is_null");
		}
	}
	private void declareListenContainer(final SimpleMessageListenerContainer simpleMessageListenerContainer) {
        
        simpleMessageListenerContainer.setQueueNames(QID);
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
	/**
	 * amqp连接初始化的公共方法
	 * @param connAdr
	 * @param appKey
	 * @param appSecret
	 * @param qid
	 * @param sessionCacheSize
	 * @param channelCheckoutTimeout
	 * @param publisherConfirms
	 * @throws ParseException
	 */
	protected final void cloudMqInit(String connAdr, String appKey, String appSecret, String qid) throws ParseException{
		CONN_ADR = connAdr;
        QID = qid;
        APP_KEY = appKey;
        APP_SECRET = appSecret;
        DeclareRabbitUtils.declareRabbit(this, new QueryRabbitClusterInfo(connAdr, appKey, appSecret, qid), connAdr, appKey, appSecret, qid, null, null, null);
	}

	public CachingConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(CachingConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public RabbitAdmin getRabbitAdmin() {
		return rabbitAdmin;
	}

	public void setRabbitAdmin(RabbitAdmin rabbitAdmin) {
		this.rabbitAdmin = rabbitAdmin;
	}

	public RabbitTemplate getRabbitTemplate() {
		return rabbitTemplate;
	}

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public SimpleMessageListenerContainer getSimpleMessageListenerContainer() {
		return simpleMessageListenerContainer;
	}

	public void setSimpleMessageListenerContainer(
			SimpleMessageListenerContainer simpleMessageListenerContainer) {
		this.simpleMessageListenerContainer = simpleMessageListenerContainer;
	}

	public String getQID() {
		return QID;
	}

	public void setQID(String qID) {
		QID = qID;
	}

	public String getAPP_KEY() {
		return APP_KEY;
	}

	public void setAPP_KEY(String aPP_KEY) {
		APP_KEY = aPP_KEY;
	}

	public String getAPP_SECRET() {
		return APP_SECRET;
	}

	public void setAPP_SECRET(String aPP_SECRET) {
		APP_SECRET = aPP_SECRET;
	}

	public String getCONN_ADR() {
		return CONN_ADR;
	}

	public void setCONN_ADR(String cONN_ADR) {
		CONN_ADR = cONN_ADR;
	}

	public int getRabbitSettingsCount() {
		return rabbitSettingsCount;
	}

	public void setRabbitSettingsCount(int rabbitSettingsCount) {
		this.rabbitSettingsCount = rabbitSettingsCount;
	}

	public int getRabbitSettingsCountLimit() {
		return rabbitSettingsCountLimit;
	}

	public void setRabbitSettingsCountLimit(int rabbitSettingsCountLimit) {
		this.rabbitSettingsCountLimit = rabbitSettingsCountLimit;
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
    /**
     * 设置集群迁移的检测时间间隔，并开启检测线程
     * @param clusterRemoveQueryInterval 单位ms
     */
     public void setClusterRemoveQueryInterval(long clusterRemoveQueryInterval){
     	Assert.isTrue(clusterRemoveQueryInterval > 0, "'clusterRemoveQueryInterval' must be > 0");
         //将客户端设置的健康检测时间设置到它的configMap集合中
         ConfigUtil.setQueueIssendClusterRemoveQueryInterval(APP_KEY, QID, clusterRemoveQueryInterval);
     	 //从线程类启动
         ClusterRemoveQueryThread.setPoolSize(ConfigUtil.getClusterRemoveQueryPoolSize());
         if(clusterRemoveQueryThread==null){
         	clusterRemoveQueryThread=new ClusterRemoveQueryThread(new QueryRabbitClusterInfo(CONN_ADR,APP_KEY,APP_SECRET,QID),ConfigUtil.getQueueIssendClusterRemoveQueryInterval(APP_KEY, QID),this.connectionFactory);
         }
         clusterRemoveQueryThread.initThread(); 
     }
     
     public void closeClusterRemoveQuery(){
     	clusterRemoveQueryThread.setClusterRemoveQueryInterval(0);
     }
}