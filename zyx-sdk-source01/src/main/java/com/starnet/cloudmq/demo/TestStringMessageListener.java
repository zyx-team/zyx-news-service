package com.starnet.cloudmq.demo;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * 字符串类型消息的监听处理
 */
@Component
public class TestStringMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // TODO: 16/9/22  对监听到的字符串消息做处理
        String messageStr;
        try {
            messageStr = byteToString(message.getBody());
        } catch (Exception e) {
            e.printStackTrace();
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
