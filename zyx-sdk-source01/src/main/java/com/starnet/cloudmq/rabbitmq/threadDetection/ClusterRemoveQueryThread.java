/***********************************************************************
 * Module:  ClusterRemoveQueryThread.java
 * Author:  Administrator
 * Purpose: Defines the Class ClusterRemoveQueryThread
 ***********************************************************************/

package com.starnet.cloudmq.rabbitmq.threadDetection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

import com.starnet.cloudmq.utils.ConfigUtil;

/**
 * 集群迁移，sdk检测线程类
 * @author Administrator
 *
 */
public class ClusterRemoveQueryThread implements Runnable {
	private static final Log log = LogFactory.getLog(HealthExamThread.class);
	
	private static ScheduledExecutorService scheduledThreadPool = null;
   
	private static int poolSize = 10;
  
	private static ConcurrentHashMap<String, Future> clusterRemoveQueryMap = new ConcurrentHashMap<String, Future>();
   
	private QueryRabbitClusterInfo queryRabbitClusterInfo = null;
   
	private Long clusterRemoveQueryInterval = 1000L;
   
	private static ConcurrentHashMap<String, Set<CachingConnectionFactory>> cloudMqConMap = new ConcurrentHashMap<String, Set<CachingConnectionFactory>>();
   
   /**
    * 
    * @param queryRabbitClusterInfo
    * @param clusterRemoveQueryInterval
    * @param cachingConnectionFactory
    */
   public ClusterRemoveQueryThread(QueryRabbitClusterInfo queryRabbitClusterInfo, Long clusterRemoveQueryInterval,CachingConnectionFactory cachingConnectionFactory) {
      this.queryRabbitClusterInfo=queryRabbitClusterInfo;
      this.clusterRemoveQueryInterval=clusterRemoveQueryInterval;
      String intervalMapKey=queryRabbitClusterInfo.getAppKey()+"_"+queryRabbitClusterInfo.getQid();
      synchronized (cloudMqConMap) {
			if(cloudMqConMap.get(intervalMapKey)==null){
				cloudMqConMap.put(intervalMapKey, new HashSet<CachingConnectionFactory>());
			}
			Set<CachingConnectionFactory> cachingConnectionFactories=cloudMqConMap.get(intervalMapKey);
		    cachingConnectionFactories.add(cachingConnectionFactory);
      }
   }
   /**
    * implement重置CachingConnectionFactory的属性 
    */
   private void reSetConParame(String intervalMapKey) {
	   CachingConnectionFactory connectionFactory=null;
	   Set<CachingConnectionFactory> cachingConnectionFactories=cloudMqConMap.get(intervalMapKey);
	   synchronized (cachingConnectionFactories) {
		   for (CachingConnectionFactory cachingConnectionFactory : cachingConnectionFactories) {
			   connectionFactory=cachingConnectionFactory;
			   if(!connectionFactory.getHost().equals(ConfigUtil.getRabbitHost(queryRabbitClusterInfo.getAppKey(), queryRabbitClusterInfo.getQid()))){
				   connectionFactory.setHost(ConfigUtil.getRabbitHost(queryRabbitClusterInfo.getAppKey(), queryRabbitClusterInfo.getQid()));
			       connectionFactory.setPort(ConfigUtil.getRabbitPort(queryRabbitClusterInfo.getAppKey(), queryRabbitClusterInfo.getQid()));
			       connectionFactory.setUsername(ConfigUtil.getRabbitUser(queryRabbitClusterInfo.getAppKey(), queryRabbitClusterInfo.getQid()));
			       connectionFactory.setPassword(ConfigUtil.getRabbitPwd(queryRabbitClusterInfo.getAppKey(), queryRabbitClusterInfo.getQid()));
			       connectionFactory.setVirtualHost(ConfigUtil.getRabbitVhost(queryRabbitClusterInfo.getAppKey(), queryRabbitClusterInfo.getQid()));
			       connectionFactory.resetConnection();
			   }
			   
		   }
	   }
	   
   }
   /** 
    * @param poolSize
    */
   public static void setPoolSize(int poolSize) {
	   ClusterRemoveQueryThread.poolSize=poolSize;
   }
   
   /** @pdOid 8b9fe5a1-c4d7-4563-94f4-453cba69e999 */
   public static ScheduledExecutorService getScheduledExecutorService() {
      if(scheduledThreadPool==null){
    	  synchronized(ClusterRemoveQueryThread.class){
    		  if(scheduledThreadPool==null){
    			  scheduledThreadPool = Executors.newScheduledThreadPool(poolSize);
    		  }
    	  }
      }
      return scheduledThreadPool;
   }
   
   /** @pdOid 6f7cbd17-6a7b-421e-b135-8072727ba870 */
   public void run() {
       	//线程的业务方法，进行健康检测的业务处理
   		try {
   			String intervalMapKey=queryRabbitClusterInfo.getAppKey()+"_"+queryRabbitClusterInfo.getQid();
   			log.debug("集群迁移定时检测线程启动："+intervalMapKey+"_"+this.hashCode());
   			if(clusterRemoveQueryInterval==0){
   				//结束当前任务。
   				Future future = clusterRemoveQueryMap.remove(intervalMapKey+"_"+this.hashCode());
   				if(future!=null){
   					
   					log.debug("集群迁移定时检测线程正常停止："+intervalMapKey+"_"+this.hashCode());
   					future.cancel(true);
   				}
   			}
   			
   			queryRabbitClusterInfo.getRabbitSettings();
   			reSetConParame(intervalMapKey);
   		} catch (ParseException e) {
   			log.error(e.getMessage(), e);
   		}
   }
   
   /** @pdOid cf90a1a3-8eef-40f4-a75b-4333c725baa5 */
   public void initThread() {
      //检测当前针对此队列设置的检测时间间隔是否需要启动线程进行检测
   		String intervalMapKey=queryRabbitClusterInfo.getAppKey()+"_"+queryRabbitClusterInfo.getQid();
   		synchronized (ClusterRemoveQueryThread.class) {
			log.debug("集群迁移定时检测集群迁移线程加入："+intervalMapKey+"_"+this.hashCode());
			if(clusterRemoveQueryMap.get(intervalMapKey+"_"+this.hashCode())!=null){
				return;
			}
			//创建并执行一个在给定初始延迟后首次启用的定期操作，随后，在每一次执行终止和下一次执行开始之间都存在给定的延迟。如果任务的任一执行遇到异常，就会取消后续执行。否则，只能通过执行程序的取消或终止方法来终止该任务。
			Future future = getScheduledExecutorService().scheduleWithFixedDelay(this, 0, clusterRemoveQueryInterval, TimeUnit.MILLISECONDS);
			clusterRemoveQueryMap.put(intervalMapKey+"_"+this.hashCode(), future);
			
   		}
   }
	public long getClusterRemoveQueryInterval() {
		return clusterRemoveQueryInterval;
	}
	public void setClusterRemoveQueryInterval(long clusterRemoveQueryInterval) {
		this.clusterRemoveQueryInterval = clusterRemoveQueryInterval;
	}
   
}