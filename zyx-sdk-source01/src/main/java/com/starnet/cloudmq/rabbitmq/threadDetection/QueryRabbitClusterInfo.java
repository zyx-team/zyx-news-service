/***********************************************************************
 * Module:  QueryRabbitClusterInfo.java
 * Author:  Administrator
 * Purpose: Defines the Class QueryRabbitClusterInfo
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.threadDetection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.starnet.cloudmq.rabbitmq.CloudMQConfig;
import com.starnet.cloudmq.rabbitmq.CloudMQException;
import com.starnet.cloudmq.utils.ConfigUtil;
import com.starnet.cloudmq.utils.HttpUtil;
import com.starnet.cloudmq.utils.JsonUtil;

/**
 * 查询rabbitmq集群的真实地址信息
 */
public class QueryRabbitClusterInfo {

	private String connAdr = null;
	private String appSecret = null;
	private String appKey = null;
	private String qid = null;

	/**
	 * @param connAdr
	 * @param appSecret
	 * @param appKey
	 * @param qid
	 * */
	public QueryRabbitClusterInfo(String connAdr, String appKey,
			String appSecret, String qid) {
		this.connAdr = connAdr;
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.qid = qid;
	}

	public void getRabbitSettings() throws ParseException {
		JSONObject json = new JSONObject();
		json.put(CloudMQConfig.APP_KEY, this.appKey);
		json.put(CloudMQConfig.APP_SECRET, this.appSecret);
		json.put(CloudMQConfig.QID, qid);
		String retObj;
		try {
			String connUrl = this.connAdr
					+ CloudMQConfig.API_V1_RPC_CLUSTER_QUEUE_GET;
			retObj = HttpUtil.postJson(connUrl, json);
			System.out.println(retObj);
		} catch (Exception e) {
			throw new CloudMQException("errcode:14010000,errmsg:" + "ex", e);
		}
		JSONObject retJsonObj = JsonUtil.toJson(retObj);
		JSONObject content = JsonUtil.toJson(retJsonObj
				.get(CloudMQConfig.CONTENT));
		if (Long.valueOf(retJsonObj.get(CloudMQConfig.ERRCODE).toString()) != 0L) {
			throw new CloudMQException(retJsonObj.get(CloudMQConfig.ERRCODE)
					.toString(), retJsonObj.get(CloudMQConfig.ERRMSG)
					.toString());
		}
		if (content == null) {
			throw new CloudMQException("14010006", "wrong_param");
		}
		ConfigUtil.setRabbitHost(this.appKey, qid,
				content.get(CloudMQConfig.HOST).toString());
		ConfigUtil.setRabbitPort(this.appKey, qid,
				Integer.valueOf(content.get(CloudMQConfig.PORT).toString()));
		ConfigUtil.setRabbitVhost(this.appKey, qid,
				content.get(CloudMQConfig.VHOST).toString());
		ConfigUtil.setRabbitUser(this.appKey, qid, this.appKey);
		ConfigUtil.setRabbitPwd(this.appKey, qid, this.appSecret);
		ConfigUtil.setRabbitQueue(this.appKey, qid);
		ConfigUtil.setRabbitExchangeName(this.appKey, qid,
				content.get(CloudMQConfig.EXCHANGE).toString());
		ConfigUtil.setRabbitExchangeType(this.appKey, qid,
				content.get(CloudMQConfig.EXCHANGE_TYPE).toString());
		ConfigUtil.setQUEUE_MSG_DURABLE(Boolean.valueOf(content.get(CloudMQConfig.MSG_DURABLE).toString()));;
		if(content.get(CloudMQConfig.ROUTING_KEY)!=null){
			JSONArray jsonArray= (JSONArray) content.get(CloudMQConfig.ROUTING_KEY);
			if(jsonArray!=null&&(!jsonArray.toString().equals(""))){
				StringBuilder stringBuilder=new StringBuilder();
				for (Object routingKey : jsonArray) {
					stringBuilder.append(routingKey.toString());
					stringBuilder.append(";");
				}
				ConfigUtil.setRabbitQueueRoutingkey(this.appKey, qid,
						stringBuilder.toString().substring(0, stringBuilder.toString().length()-1));
			}else{
				ConfigUtil.setRabbitQueueRoutingkey(this.appKey, qid,null);
			}
		}
		ConfigUtil
				.setRabbitOverflowHandle(
						this.appKey,
						qid,
						Integer.valueOf(content.get(
								CloudMQConfig.OVERFLOW_HANDLE).toString()));
	}

	public String getConnAdr() {
		return connAdr;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public String getAppKey() {
		return appKey;
	}

	public String getQid() {
		return qid;
	}

}