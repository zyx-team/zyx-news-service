/***********************************************************************
 * Module:  CloudMqSendAbstract.java
 * Author:  Administrator
 * Purpose: Defines the Class CloudMqSendAbstract
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.publish;

import java.io.UnsupportedEncodingException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.util.Assert;

import com.starnet.cloudmq.rabbitmq.CloudMQClient;
import com.starnet.cloudmq.rabbitmq.CloudMQConfig;
import com.starnet.cloudmq.rabbitmq.CloudMQException;
import com.starnet.cloudmq.rabbitmq.CloudMQQueueResource;
import com.starnet.cloudmq.rabbitmq.threadDetection.CheckQueueIsLimit;
import com.starnet.cloudmq.rabbitmq.threadDetection.ClusterRemoveQueryThread;
import com.starnet.cloudmq.rabbitmq.threadDetection.HealthExamThread;
import com.starnet.cloudmq.rabbitmq.threadDetection.QueryRabbitClusterInfo;
import com.starnet.cloudmq.utils.ConfigUtil;
import com.starnet.cloudmq.utils.DeclareRabbitUtils;
import com.starnet.cloudmq.utils.HttpUtil;
import com.starnet.cloudmq.utils.JsonUtil;
import com.starnet.cloudmq.utils.Util;

/**
 * 发送消息抽象类
 */
public abstract class CloudMqSendAbstract implements CloudMqSendService,
		ConfirmCloudMqSendService {
	private CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    private JsonMessageConverter jsonMessageConverter = new JsonMessageConverter();
    private RabbitAdmin rabbitAdmin;
	private RabbitTemplate rabbitTemplate;
	private ClusterRemoveQueryThread clusterRemoveQueryThread=null;
	/**
	 * 单条消息内容允许的最大长度,单位byte(字节)
	 */
	private int msgContentLengthLimit = 0;

	private String QID = null;
	private String APP_KEY = null;
	private String APP_SECRET = null;
	private String CONN_ADR = null;
	private int rabbitSettingsCount=0;//记录调用getRabbitSettings获取云消息集群信息的次数。
    private int rabbitSettingsCountLimit=3;//记录调用getRabbitSettings获取云消息集群信息的次数的最大上限。
	/**
	 * 发送消息（对象类型）
	 */
	public void send(Message message) throws CloudMQException, ParseException{
		send(message, null);
	}

	/**
	 * @param message
	 * @param id
	 *            （消息唯一标示，确认机制时使用） 发送消息
	 */
	public void send(Message message, String id) throws CloudMQException, ParseException{
		send(rabbitTemplate.getExchange(),QID,message,id);
	}

	/**
	 * @param message
	 *            发送消息（字符串类型）
	 */
	public void convertAndSend(String message) throws CloudMQException, ParseException{

		convertAndSend(message, null);
	}

	/**
	 * @param message
	 * @param id
	 *            （消息唯一标示，确认机制时使用） 发送消息（字符串类型）
	 */
	public void convertAndSend(String message, String id) throws CloudMQException, ParseException{
		convertAndSend(rabbitTemplate.getExchange(),QID,message,id);
	}

	/**
	 * @param confirmCallback
	 *            确认机制时使用，用于接收确认的回执信息
	 */
	public void setConfirmCallback(
			RabbitTemplate.ConfirmCallback confirmCallback) {
		checkRabbitTemplateIsExisits();
		if (!rabbitTemplate.isConfirmListener()) {
			rabbitTemplate.setConfirmCallback(confirmCallback);
		}
	}

	private void checkRabbitTemplateIsExisits() {
		if (Util.isNull(rabbitTemplate)) {
			throw new CloudMQException("14010005", "CloudMQClient_is_null");
		}
	}

	/**
	 * 单条消息内容允许的最大长度进行检测，超过限制抛出异常
	 * 
	 * @param message
	 */
	private void checkMsgContentLengthLimit(Message message) {
		if (msgContentLengthLimit == 0) {
			// 不控制消息体大小
			return;
		}
		int msg_size = message == null ? 0 : message.getBody().length;
		if (msg_size > this.msgContentLengthLimit) {
			throw new CloudMQException("errcode:14010012,errmsg:消息内容超过上限，上限值为"
					+ this.msgContentLengthLimit + "字节,消息体大小为" + msg_size
					+ "字节");
		}
	}

	/**
	 * 单条消息内容允许的最大长度进行检测，超过限制抛出异常
	 * 
	 * @param message
	 */
	private void checkMsgContentLengthLimit(String message) {
		if (msgContentLengthLimit == 0) {
			// 不控制消息体大小
			return;
		}
		int msg_size = 0;
		try {
			msg_size = message == null ? 0 : message.getBytes("utf-8").length;
			if (msg_size > this.msgContentLengthLimit) {
				throw new CloudMQException(
						"errcode:14010012,errmsg:消息内容超过上限，上限值为"
								+ this.msgContentLengthLimit + "字节,消息体大小为"
								+ msg_size + "字节");
			}
		} catch (UnsupportedEncodingException e) {
			throw new CloudMQException("errcode:14010013,errmsg:消息内容大小检测转换异常，"
					+ e.getMessage(), e);
		}
	}

	/**
	 * @param exchange
	 * @param routingKey
	 * @param message
	 * @param id
	 * */
	protected void send(String exchange, String routingKey, Message message,
			String id) {
		checkMsgContentLengthLimit(message);
		checkRabbitTemplateIsExisits();
		if(!ConfigUtil.getQUEUE_MSG_DURABLE()){
			message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
		}
		if (ConfigUtil.getRabbitOverflowHandle(APP_KEY, QID) == 1) {

			if (ConfigUtil.getQueueIssendErrcode(APP_KEY, QID) == null
					|| ConfigUtil.getQueueIssendErrcode(APP_KEY, QID).equals(
							"0")) {
				assert message != null;
				rabbitTemplate.send(
						exchange,
						routingKey,
						new Message(message.getBody(), message
								.getMessageProperties()), new CorrelationData(
								id));
				return;
			} else {
				throw new CloudMQException(ConfigUtil.getQueueIssendErrcode(
						APP_KEY, QID), ConfigUtil.getQueueIssendErrmsg(APP_KEY,
						QID));
			}
		}
		rabbitTemplate.send(exchange, routingKey, new Message(
				message.getBody(), message.getMessageProperties()),
				new CorrelationData(id));
	}

	/**
	 * @param exchange
	 * @param routingKey
	 * @param message
	 * @param id
	 **/
	protected void convertAndSend(String exchange, String routingKey,
			String message, String id) {
		checkMsgContentLengthLimit(message);
		Message convertMessage = jsonMessageConverter.toMessage("", null);
		if(!ConfigUtil.getQUEUE_MSG_DURABLE()){
			convertMessage.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
		}
		try {
			convertMessage = new Message(message.getBytes("utf-8"), convertMessage.getMessageProperties());
		}catch (Exception e){
			e.printStackTrace();
		}
		// 检查是否存在RabbitTempalte对象
		checkRabbitTemplateIsExisits();
		if (ConfigUtil.getRabbitOverflowHandle(APP_KEY, QID) == 1) {
			if (ConfigUtil.getQueueIssendErrcode(APP_KEY, QID) == null
					|| ConfigUtil.getQueueIssendErrcode(APP_KEY, QID).equals(
							"0")) {
				rabbitTemplate.convertAndSend(exchange, routingKey,
						(Object) convertMessage, new CorrelationData(id));
				return;
			} else {
				throw new CloudMQException(ConfigUtil.getQueueIssendErrcode(
						APP_KEY, QID), ConfigUtil.getQueueIssendErrmsg(APP_KEY,
						QID));
			}
		}
		rabbitTemplate.convertAndSend(exchange, routingKey, (Object) convertMessage,
				new CorrelationData(id));
	}

	/**
	 * 单条消息内容允许的最大长度,单位byte(字节)
	 * 
	 * @param msgContentLengthLimit
	 */
	public final void setMsgContentLengthLimit(int msgContentLengthLimit) {
		this.msgContentLengthLimit = msgContentLengthLimit;
	}
	public final void setCheckResourceInterval(long checkResourceInterval) throws CloudMQException {
        Assert.isTrue(checkResourceInterval >= 0, "'checkResourceInterval' must be >= 0");
        //将客户端设置的健康检测时间设置到它的configMap集合中
        ConfigUtil.setQueueIssendCheckResourceInterval(APP_KEY, QID, checkResourceInterval);        
        if (ConfigUtil.getRabbitOverflowHandle(APP_KEY, QID) == 1) {
        	 //从线程类启动
            HealthExamThread.setPoolSize(ConfigUtil.getHealthExamPoolSize());
            HealthExamThread healthExamThread=new HealthExamThread(new CheckQueueIsLimit(APP_KEY,QID,CONN_ADR),ConfigUtil.getQueueIssendCheckResourceInterval(APP_KEY, QID));
            healthExamThread.initThread();
        }
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
    /**
     * 获取队列的状态
     */
    public final CloudMQQueueResource getQueueResource() throws CloudMQException, ParseException {
        JSONObject json = new JSONObject();
        json.put(CloudMQConfig.APP_KEY, APP_KEY);
        json.put(CloudMQConfig.APP_SECRET, APP_SECRET);
        json.put(CloudMQConfig.QID, QID);
        String retObj;
        try {
            String connUrl = CONN_ADR + CloudMQConfig.API_V1_RPC_QUEUE_RESOURCE_GET;
            retObj = HttpUtil.postJson(connUrl, json);
        } catch (Exception e) {
            throw new CloudMQException("errcode:14010000,errmsg:" + "ex", e);
        }
        JSONObject retJsonObj = JsonUtil.toJson(retObj);
        JSONObject content = JsonUtil.toJson(retJsonObj.get(CloudMQConfig.CONTENT));
        if (Long.valueOf(retJsonObj.get(CloudMQConfig.ERRCODE).toString()) != 0L) {
            throw new CloudMQException(retJsonObj.get(CloudMQConfig.ERRCODE).toString(), retJsonObj.get(CloudMQConfig.ERRMSG).toString());
        }
        if (content == null) {
            throw new CloudMQException("14010006", "wrong_param");
        }
        CloudMQQueueResource cloudMQQueueResource = new CloudMQQueueResource();
        if (content.containsKey(CloudMQConfig.MAX_LENGTH)) {
            cloudMQQueueResource.setMax_length(Integer.valueOf(content.get(CloudMQConfig.MAX_LENGTH).toString()));
        }
        if (content.containsKey(CloudMQConfig.MAX_LENGTH_LEFT)) {
            cloudMQQueueResource.setMax_length_left(Integer.valueOf(content.get(CloudMQConfig.MAX_LENGTH_LEFT).toString()));
        }
        if (content.containsKey(CloudMQConfig.MAX_LENGTH_BYTES)) {
            cloudMQQueueResource.setMax_length_bytes(Integer.valueOf(content.get(CloudMQConfig.MAX_LENGTH_BYTES).toString()));
        }
        if (content.containsKey(CloudMQConfig.MAX_LENGTH_BYTES_LEFT)) {
            cloudMQQueueResource.setMax_length_bytes_left(Integer.valueOf(content.get(CloudMQConfig.MAX_LENGTH_BYTES_LEFT).toString()));
        }
        if (content.containsKey(CloudMQConfig.ALARM_THRESHOLD)) {
            cloudMQQueueResource.setAlarm_threshold(Double.valueOf(content.get(CloudMQConfig.ALARM_THRESHOLD).toString()));
        }
        if (content.containsKey(CloudMQConfig.OVERFLOW_HANDLE)) {
            cloudMQQueueResource.setOverflow_handle(Integer.valueOf(content.get(CloudMQConfig.OVERFLOW_HANDLE).toString()));
        }
        if (content.containsKey(CloudMQConfig.MSG_DURABLE)) {
            cloudMQQueueResource.setMsg_durable(Boolean.valueOf(content.get(CloudMQConfig.MSG_DURABLE).toString()));
        }
        return cloudMQQueueResource;
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

	public RabbitTemplate getRabbitTemplate() {
		return rabbitTemplate;
	}

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public String getQID() {
		return QID;
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
	protected final void cloudMqInit(String connAdr, String appKey, String appSecret, String qid, Integer sessionCacheSize, Long channelCheckoutTimeout, Boolean publisherConfirms) throws ParseException{
		CONN_ADR = connAdr;
        QID = qid;
        APP_KEY = appKey;
        APP_SECRET = appSecret;
        DeclareRabbitUtils.declareRabbit(this, new QueryRabbitClusterInfo(connAdr, appKey, appSecret, qid), connAdr, appKey, appSecret, qid, sessionCacheSize, channelCheckoutTimeout, publisherConfirms);
	}
}
	