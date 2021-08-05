package com.starnet.cloudmq.demo;

import com.starnet.cloudmq.rabbitmq.CloudMQClient;

public class SetListenMsgParamsDemo {
    /**
     * 设置监听消息的参数
     */
    public static void main(String[] args) {

        try {
            CloudMQClient cloudMQClient = new CloudMQClient("conn_adr", "app_key", "app_secret", "qid");

            //监听者参数可以监听设置,也可以在监听后设置
            cloudMQClient.setRecoveryInterval(5000);
            cloudMQClient.setConcurrentConsumers(1);
            cloudMQClient.setTxSize(1);
            cloudMQClient.setConsecutiveActiveTrigger(10);
            cloudMQClient.setConsecutiveIdleTrigger(10);
            cloudMQClient.setDeclarationRetries(3);
            cloudMQClient.setFailedDeclarationRetryInterval(5000);
            cloudMQClient.setMaxConcurrentConsumers(1);
            cloudMQClient.setPrefetchCount(1);
            cloudMQClient.setReceiveTimeout(1000);

            cloudMQClient.listen(new TestMessageListener());

            cloudMQClient.setRetryDeclarationInterval(60000);
            cloudMQClient.setShutdownTimeout(5);
            cloudMQClient.setStartConsumerMinInterval(100000);
            cloudMQClient.setStopConsumerMinInterval(60000);
            cloudMQClient.setTxSize(1);
            cloudMQClient.setConsecutiveIdleTrigger(10);

            System.out.println("ActiveConsumerCount:" + cloudMQClient.getActiveConsumerCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
