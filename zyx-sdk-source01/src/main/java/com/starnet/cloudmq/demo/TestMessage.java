package com.starnet.cloudmq.demo;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;

import java.nio.charset.Charset;


/**
 * 对象类型消息的定义
 */
public class TestMessage {

    private String id;

    private Object content;

    public TestMessage() {
    }

    public TestMessage(String id, Object content) {
        this.id = id;
        this.content = content;
    }

    public static TestMessage parseTestMessage(Message message) throws ParseException {
        String messageStr = new String(message.getBody(), Charset.forName("UTF-8"));
        JSONParser parser = new JSONParser();
        JSONObject testMessageJson = (JSONObject) parser.parse(messageStr);
        TestMessage testMessage = new TestMessage(testMessageJson.get("id").toString(), testMessageJson.get("content"));
        if (testMessage == null || isEmpty(testMessage.getId())) {
            return null;
        }
        return testMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}