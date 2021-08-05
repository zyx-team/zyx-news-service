package com.starnet.cloudmq.demo;

import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

/**
 * 对象类型消息的监听处理
 */
@Component
public class TestMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // TODO: 16/9/22  对监听到的消息做处理
        try {
            printMsg(message);
        } catch (ParseException e) {
            e.printStackTrace();
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
