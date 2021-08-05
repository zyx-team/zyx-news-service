/***********************************************************************
 * Module:  ManualCloudMqConsumer.java
 * Author:  Administrator
 * Purpose: Defines the Class ManualCloudMqConsumer
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.consumer.impl;

import org.json.simple.parser.ParseException;

import com.starnet.cloudmq.rabbitmq.consumer.CloudMqConsumerAbstract;


/**
 * 消费类
 * @author Administrator
 *
 */
public class CloudMqConsumer extends CloudMqConsumerAbstract {
   /** @param connAdr 
    * @param appKey 
    * @param appSecret 
    * @param qid
 * @throws ParseException 
    * */
   public  CloudMqConsumer(String connAdr, String appKey, String appSecret, String qid) throws ParseException {
	   cloudMqInit(connAdr, appKey, appSecret, qid);
   }

}