package com.starnet.cloudmq.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;

public class TestStringMessageManualListener implements ChannelAwareMessageListener{

	@Override
	public void onMessage(Message message, Channel arg1) throws Exception {
		// TODO: 对监听到的字符串消息做处理
        String messageStr;
        try {
            messageStr = byteToString(message.getBody());
            //确认已处理
            arg1.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            e.printStackTrace();
            //处理失败，重入队列
            arg1.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
            return;
        }
        System.out.println(messageStr);
	}
	private String byteToString(byte[] obj) throws Exception {
        try {
            return new String(obj, "UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new Exception(e);
        }
    }
}
