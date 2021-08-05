package com.starnet.cloudmq.utils;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Assert;

import com.starnet.cloudmq.rabbitmq.CloudMQException;

public class ConfigUtil {

    private static ConcurrentHashMap<String, String> configMap = new ConcurrentHashMap<String, String>();

    private static String RABBIT_HOST = "rabbit.host";
    private static String RABBIT_PORT = "rabbit.port";
    private static String RABBIT_USER = "rabbit.user";
    private static String RABBIT_PWD = "rabbit.pwd";
    private static String RABBIT_VHOST = "rabbit.vhost";
    private static String RABBIT_QUEUE = "rabbit.queue";
    private static String RABBIT_EXCHANGE_NAME = "rabbit.exchange.name";
    private static String RABBIT_EXCHANGE_TYPE = "rabbit.exchange.type";
    private static String RABBIT_QUEUE_ROUTINGKEY = "rabbit.queue.routingkey";
    private static String RABBIT_OVERFLOW_HANDLE = "RABBIT.overflow_handle";

    private static String QUEUE_ISSEND_DATE = "queue.issend_date";
    private static String QUEUE_ISSEND_ERRCODE = "queue.issend_errcode";
    private static String QUEUE_ISSEND_ERRMSG = "queue.issend_errmsg";
    private static String QUEUE_ISSEND_CHECK_RESOURCE_INTERVAL = "queue.issend_check_resource_interval";
    private static String QUEUE_ISSEND_CLUSTER_REMOVE_QUERY_INTERVAL = "queue.issend_cluster_remove_query_interval";
    private static Boolean QUEUE_MSG_DURABLE = true;

	/**
     * 健康检查的线程池个数
     */
    private static int healthExamPoolSize=10;
    /**
     * 集群迁移检查的线程池个数
     */
    private static int clusterRemoveQueryPoolSize=10;
    
    private static void addConfigMap(String key, String value) {
        configMap.put(key, value);
    }
    public static Boolean getQUEUE_MSG_DURABLE() {
		return QUEUE_MSG_DURABLE;
	}

	public static void setQUEUE_MSG_DURABLE(Boolean qUEUE_MSG_DURABLE) {
		QUEUE_MSG_DURABLE = qUEUE_MSG_DURABLE;
	}
    public static void setRabbitHost(String app_key, String qid, String host) {
        addConfigMap(app_key + qid + RABBIT_HOST, host);
    }

    public static String getRabbitHost(String app_key, String qid) {
        return configMap.get(app_key + qid + RABBIT_HOST);
    }

    public static void setRabbitPort(String app_key, String qid, Integer port) {
        addConfigMap(app_key + qid + RABBIT_PORT, Util.notNullString(port));
    }

    public static Integer getRabbitPort(String app_key, String qid) {
        return Util.stringToInteger(configMap.get(app_key + qid + RABBIT_PORT));
    }

    public static void setRabbitUser(String app_key, String qid, String user) {
        addConfigMap(app_key + qid + RABBIT_USER, user);
    }

    public static String getRabbitUser(String app_key, String qid) {
        return configMap.get(app_key + qid + RABBIT_USER);
    }

    public static void setRabbitPwd(String app_key, String qid, String pwd) {
        addConfigMap(app_key + qid + RABBIT_PWD, pwd);
    }

    public static String getRabbitPwd(String app_key, String qid) {
        return configMap.get(app_key + qid + RABBIT_PWD);
    }

    public static void setRabbitVhost(String app_key, String qid, String vhost) {
        addConfigMap(app_key + qid + RABBIT_VHOST, vhost);
    }

    public static String getRabbitVhost(String app_key, String qid) {
        return configMap.get(app_key + qid + RABBIT_VHOST);
    }

    public static void setRabbitQueue(String app_key, String qid) {
        addConfigMap(app_key + qid + RABBIT_QUEUE, qid);
    }

    public static String getRabbitQueue(String app_key, String qid) {
        return configMap.get(app_key + qid + RABBIT_QUEUE);
    }

    public static void setRabbitExchangeName(String app_key, String qid, String exchange_name) {
        addConfigMap(app_key + qid + RABBIT_EXCHANGE_NAME, exchange_name);
    }

    public static String getRabbitExchangeName(String app_key, String qid) {
        return configMap.get(app_key + qid + RABBIT_EXCHANGE_NAME);
    }

    public static void setRabbitExchangeType(String app_key, String qid, String exchange_type) {
        addConfigMap(app_key + qid + RABBIT_EXCHANGE_TYPE, exchange_type);
    }

    public static String getRabbitExchangeType(String app_key, String qid) {
        return configMap.get(app_key + qid + RABBIT_EXCHANGE_TYPE);
    }
    
    public static String getRabbitQueueRoutingkey(String app_key, String qid) {
		return configMap.get(app_key + qid + RABBIT_QUEUE_ROUTINGKEY);
	}

	public static void setRabbitQueueRoutingkey(String app_key, String qid,String rabbitQueueRoutingkey) {
		addConfigMap(app_key + qid + RABBIT_QUEUE_ROUTINGKEY, rabbitQueueRoutingkey);
	}

	public static void setRabbitOverflowHandle(String app_key, String qid, Integer overflow_handle) {
        addConfigMap(app_key + qid + RABBIT_OVERFLOW_HANDLE, Util.notNullString(overflow_handle));
    }

    public static Integer getRabbitOverflowHandle(String app_key, String qid) {
        return Util.stringToInteger(configMap.get(app_key + qid + RABBIT_OVERFLOW_HANDLE));
    }

    public static void setQueueIssendDate(String app_key, String qid, Long date) {
        addConfigMap(app_key + qid + QUEUE_ISSEND_DATE, Util.notNullString(date));
    }

    public static Long getQueueIssendDate(String app_key, String qid) {
        if (Util.isEmpty(configMap.get(app_key + qid + QUEUE_ISSEND_DATE))) {
            return 0L;
        }
        return Util.stringToLong(configMap.get(app_key + qid + QUEUE_ISSEND_DATE));
    }

    public static void setQueueIssendErrcode(String app_key, String qid, String errcode) {
        addConfigMap(app_key + qid + QUEUE_ISSEND_ERRCODE, Util.notNullString(errcode));
    }

    public static String getQueueIssendErrcode(String app_key, String qid) {
        return configMap.get(app_key + qid + QUEUE_ISSEND_ERRCODE);
    }

    public static void setQueueIssendErrmsg(String app_key, String qid, String errmsg) {
        addConfigMap(app_key + qid + QUEUE_ISSEND_ERRMSG, Util.notNullString(errmsg));
    }

    public static String getQueueIssendErrmsg(String app_key, String qid) {
        return configMap.get(app_key + qid + QUEUE_ISSEND_ERRMSG);
    }
    public static void setQueueIssendCheckResourceInterval(String app_key, String qid, Long checkResourceInterval) {
        addConfigMap(app_key + qid + QUEUE_ISSEND_CHECK_RESOURCE_INTERVAL, Util.notNullString(checkResourceInterval));
    }

    public static Long getQueueIssendCheckResourceInterval(String app_key, String qid) {
        if (Util.isEmpty(configMap.get(app_key + qid + QUEUE_ISSEND_CHECK_RESOURCE_INTERVAL))) {
            return 0L;
        }
        return Util.stringToLong(configMap.get(app_key + qid + QUEUE_ISSEND_CHECK_RESOURCE_INTERVAL));
    }
    public static void setQueueIssendClusterRemoveQueryInterval(String app_key, String qid, Long clusterRemoveQueryInterval) {
        addConfigMap(app_key + qid + QUEUE_ISSEND_CLUSTER_REMOVE_QUERY_INTERVAL, Util.notNullString(clusterRemoveQueryInterval));
    }

    public static Long getQueueIssendClusterRemoveQueryInterval(String app_key, String qid) {
        if (Util.isEmpty(configMap.get(app_key + qid + QUEUE_ISSEND_CLUSTER_REMOVE_QUERY_INTERVAL))) {
            return 0L;
        }
        return Util.stringToLong(configMap.get(app_key + qid + QUEUE_ISSEND_CLUSTER_REMOVE_QUERY_INTERVAL));
    }
	public static int getHealthExamPoolSize() {
		return healthExamPoolSize;
	}
	/**
	 * 设置健康监测线程池的核心池大小(不设置，默认为10)
	 * @param healthExamPoolSize
	 */
	public static void setHealthExamPoolSize(int healthExamPoolSize) {
		Assert.isTrue(healthExamPoolSize >= 0, "'healthExamPoolSize' must be >= 0");
		ConfigUtil.healthExamPoolSize = healthExamPoolSize;
	}
	
	public static int getClusterRemoveQueryPoolSize() {
		return clusterRemoveQueryPoolSize;
	}
	/**
	 * 设置集群迁移检查线程池的核心池大小(不设置，默认为10)
	 * @param clusterRemoveQueryPoolSize
	 */
	public static void setClusterRemoveQueryPoolSize(int clusterRemoveQueryPoolSize) {
		Assert.isTrue(clusterRemoveQueryPoolSize >= 0, "'clusterRemoveQueryPoolSize' must be >= 0");
		ConfigUtil.clusterRemoveQueryPoolSize = clusterRemoveQueryPoolSize;
	}

}
