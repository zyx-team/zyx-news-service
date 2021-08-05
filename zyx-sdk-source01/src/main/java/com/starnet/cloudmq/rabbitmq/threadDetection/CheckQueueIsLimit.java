/***********************************************************************
 * Module:  CheckQueueIsLimit.java
 * Author:  Administrator
 * Purpose: Defines the Class CheckQueueIsLimit
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.threadDetection;

import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.starnet.cloudmq.rabbitmq.CloudMQConfig;
import com.starnet.cloudmq.rabbitmq.CloudMQException;
import com.starnet.cloudmq.utils.ConfigUtil;
import com.starnet.cloudmq.utils.HttpUtil;
import com.starnet.cloudmq.utils.JsonUtil;
import com.starnet.cloudmq.utils.Util;

/**
 * 检测队列是否达到上限
 * 
 * @author Administrator
 *
 */
public class CheckQueueIsLimit {

	private String appKey;

	private String qid;

	private String connAdr;

	/**
	 * @param appKey
	 * @param qid
	 * @param connAdr
	 * */
	public  CheckQueueIsLimit(String appKey, String qid, String connAdr) {
		this.appKey = appKey;
		this.qid = qid;
		this.connAdr = connAdr;
	}

	/**
	 * @param msgSize
	 * @throws ParseException
	 * */
	public void handleCacheQueueIssend(int msgSize) throws ParseException {
		JSONObject json = new JSONObject();
		json.put(CloudMQConfig.APP_KEY, appKey);
		json.put(CloudMQConfig.QID, qid);
		json.put(CloudMQConfig.MSG_SIZE, msgSize);
		String retObj;
		try {
			// 加入时间检测log
//			double startTime = new Date().getTime();
			String connUrl = connAdr
					+ CloudMQConfig.API_V1_RPC_QUEUE_STATUS_GET;

			retObj = HttpUtil.postJson(connUrl, json);

			// 加入时间检测log
//			double endTime = new Date().getTime();
//			double runningTime = (endTime - startTime) / 1000;
//			System.out.println(runningTime);
		} catch (Exception e) {
			throw new CloudMQException("errcode:14010000,errmsg:" + "ex", e);
		}
		JSONObject retJsonObj = JsonUtil.toJson(retObj);

		ConfigUtil.setQueueIssendDate(appKey, qid, Util.now());
		// 往ConfigUtil设置是否超过阈值的标识
		ConfigUtil.setQueueIssendErrcode(appKey, qid,
				retJsonObj.get(CloudMQConfig.ERRCODE).toString());
		ConfigUtil.setQueueIssendErrmsg(appKey, qid,
				retJsonObj.get(CloudMQConfig.ERRMSG).toString());

	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	public String getConnAdr() {
		return connAdr;
	}

	public void setConnAdr(String connAdr) {
		this.connAdr = connAdr;
	}

}