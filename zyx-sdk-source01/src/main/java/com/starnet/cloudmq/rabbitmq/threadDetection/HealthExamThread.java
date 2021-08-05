package com.starnet.cloudmq.rabbitmq.threadDetection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.parser.ParseException;

public class HealthExamThread implements Runnable{
	private static final Log log = LogFactory.getLog(HealthExamThread.class);
	


	//创建一个线程池，它可安排在给定延迟后运行命令或者定期地执行。
    private static ScheduledExecutorService scheduledThreadPool = null;
    
    private static int poolSize=10;

	//key为appkey_qid ,value为对应此队列的阈值检测时间间隔
    private static ConcurrentHashMap<String, Long> checkResourceIntervalMap=new ConcurrentHashMap<String, Long>();
	//当前存活的检测阈值的任务：key为appkey_qid_checkResourceInterval ,value为调度线程返回的future
    private static ConcurrentHashMap<String, Future> checkResourceMap=new ConcurrentHashMap<String, Future>();
    //当前应该停止的检测阈值的任务：key为appkey_qid_checkResourceInterval ,value为调度线程返回的future
	private static ConcurrentHashMap<String, Future> checkResourceExpireMap=new ConcurrentHashMap<String, Future>();
    private CheckQueueIsLimit checkQueueIsLimit = null;
    private Long checkResourceInterval=null;
	
	
	public HealthExamThread(CheckQueueIsLimit checkQueueIsLimit,long checkResourceInterval){
		this.checkQueueIsLimit = checkQueueIsLimit;
		this.checkResourceInterval=checkResourceInterval;
	}
	
	

	
	public static void setPoolSize(int poolSize){
		HealthExamThread.poolSize=poolSize;
	      
    }
	public static ScheduledExecutorService getScheduledExecutorService(){
		if(scheduledThreadPool==null){
	    	  synchronized(HealthExamThread.class){
	    		  if(scheduledThreadPool==null){
	    			  scheduledThreadPool = Executors.newScheduledThreadPool(poolSize);
	    		  }
	    	  }
	    	  
	      }
	      return scheduledThreadPool;
	}
	
	
	@Override
	public void run() {
		//线程的业务方法，进行健康检测的业务处理
		try {
			String intervalMapKey=checkQueueIsLimit.getAppKey()+"_"+checkQueueIsLimit.getQid()+"_"+checkResourceInterval;
			log.debug("定时检测线程启动："+intervalMapKey);
			Future future = checkResourceExpireMap.remove(intervalMapKey);
			if(future!=null){
				//当前任务已进入应停止集合，结束当前任务。
				log.debug("定时检测线程正常停止："+intervalMapKey);
				future.cancel(true);
			}
			checkQueueIsLimit.handleCacheQueueIssend(0);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void initThread(){
		
		//检测当前针对此队列设置的检测时间间隔是否需要启动线程进行检测
		String intervalMapKey=checkQueueIsLimit.getAppKey()+"_"+checkQueueIsLimit.getQid();
		String checkResourceMapKey=intervalMapKey+"_"+checkResourceInterval;
		String checkResourceExpireKey="";
		synchronized (HealthExamThread.class) {
			if(checkResourceIntervalMap.get(intervalMapKey)!=null){
				if(checkResourceIntervalMap.get(intervalMapKey)==this.checkResourceInterval){
					//当前队列的时间间隔时间未变，无需重新启动检测线程
					return;
				}
				//旧检测线程该停止
				log.debug("定时检测线程正常加入应停止集合："+checkResourceExpireKey);
				checkResourceExpireKey=intervalMapKey+"_"+checkResourceIntervalMap.get(intervalMapKey);
				checkResourceExpireMap.put(checkResourceExpireKey, checkResourceMap.remove(checkResourceExpireKey));
			}
			//不设置间隔，不启动检测线程，仍会每发一条，检测一次
			if(this.checkResourceInterval>0){
				log.debug("定时检测线程加入："+checkResourceMapKey);
				checkResourceIntervalMap.put(intervalMapKey, checkResourceInterval);
				//创建并执行一个在给定初始延迟后首次启用的定期操作，随后，在每一次执行终止和下一次执行开始之间都存在给定的延迟。如果任务的任一执行遇到异常，就会取消后续执行。否则，只能通过执行程序的取消或终止方法来终止该任务。
				Future future = getScheduledExecutorService().scheduleWithFixedDelay(this, 0, checkResourceInterval, TimeUnit.MILLISECONDS);
			    checkResourceMap.put(checkResourceMapKey, future);
			}
		}
	}
}
