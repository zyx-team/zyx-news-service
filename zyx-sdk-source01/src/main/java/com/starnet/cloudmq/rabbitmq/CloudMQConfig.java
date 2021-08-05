package com.starnet.cloudmq.rabbitmq;

public interface CloudMQConfig {

    String API_V1_RPC_CLUSTER_QUEUE_GET = "/api/v1/rpc/cluster/queue/get";
    String API_V1_RPC_QUEUE_RESOURCE_GET = "/api/v1/rpc/queue/resource/get";
    String API_V1_RPC_QUEUE_STATUS_GET = "/api/v1/rpc/queue/status/get";

    String HOST = "host";
    String PORT = "port";
    String USER = "user";
    String PWD = "pwd";
    String VHOST = "vhost";
    String QID = "qid";
    String MSG_SIZE = "msg_size";
    String EXCHANGE = "exchange";
    String EXCHANGE_TYPE = "exchange_type";
    String ROUTING_KEY="routing_key";
    String OVERFLOW_HANDLE = "overflow_handle";
    String ALARM_THRESHOLD = "alarm_threshold";
    String APP_KEY = "app_key";
    String APP_SECRET = "app_secret";
    String CONTENT = "content";
    String MSG_DURABLE = "msg_durable";

    String ERRCODE = "errcode";
    String ERRMSG = "errmsg";
    String MAX_LENGTH_LEFT = "max_length_left";
    String MAX_LENGTH = "max_length";
    String MAX_LENGTH_BYTES_LEFT = "max_length_bytes_left";
    String MAX_LENGTH_BYTES = "max_length_bytes";
}
