package com.starnet.cloudmq.demo;

import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;

public class TestMessageManualListener implements ChannelAwareMessageListener{

	@Override
	public void onMessage(Message message, Channel arg1) throws Exception {
		// 对监听到的消息做处理
        try {
            printMsg(message);
            arg1.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (ParseException e) {
            e.printStackTrace();
            arg1.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
	}
	
	private static void printMsg(Message message) throws ParseException {
        if (message == null) {
            System.out.println("message为空");
            return;
        }
        TestMessage testMessage = TestMessage.parseTestMessage(message);
        assert testMessage != null;
        System.out.println("id:" + testMessage.getId());
        System.out.println("content:" + testMessage.getContent());
    }
}
