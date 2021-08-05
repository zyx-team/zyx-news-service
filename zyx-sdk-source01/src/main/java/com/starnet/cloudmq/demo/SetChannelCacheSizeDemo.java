package com.starnet.cloudmq.demo;

import com.starnet.cloudmq.rabbitmq.CloudMQClient;
import org.json.simple.parser.ParseException;

public class SetChannelCacheSizeDemo {
    /**
     * 设置单个connection最大Channel数,需channelCheckoutTimeout>0
     */
    public static void main(String[] args) {

        try {
            int sessionCacheSize = 25;//channel数
            /**
             * Sets the channel checkout timeout. When greater than 0, enables channel limiting
             * in that the channelCacheSize becomes the total number of available channels per
             * connection rather than a simple cache size. Note that changing the channelCacheSize
             * does not affect the limit on existing connection(s), invoke destroy() to cause a
             * new connection to be created with the new limit.
             * <p>
             * channelCheckoutTimeout the timeout in milliseconds; default 0 (channel limiting not enabled).
             */
            long channelCheckoutTimeout = 0;
            final CloudMQClient cloudMQClient = new CloudMQClient("conn_adr", "app_key", "app_secret", "qid", sessionCacheSize, channelCheckoutTimeout);

            cloudMQClient.listen(new TestStringMessageListener());

            int count = 1;
            for (; count < 30000; count++) {
                try {
                    cloudMQClient.convertAndSend("你好,hello");
                    System.out.println("消息已被发送.");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
