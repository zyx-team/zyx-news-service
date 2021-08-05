package com.starnet.cloudmq.rabbitmq;

public class CloudMQException extends RuntimeException {
    public CloudMQException(String message) {
        super(message);
    }

    public CloudMQException(Throwable cause) {
        super(cause);
    }

    public CloudMQException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloudMQException(String errCode, String message) {
        super("errcode:" + errCode + ",errmsg:" + message);
    }

    public CloudMQException(String errCode, String message, Throwable cause) {
        super("errcode:" + errCode + ",errmsg:" + message, cause);
    }
}