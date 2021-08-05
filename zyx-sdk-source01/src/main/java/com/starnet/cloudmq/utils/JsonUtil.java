package com.starnet.cloudmq.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JsonUtil {
    private static final Log log = LogFactory.getLog(JsonUtil.class);

    public static JSONObject toJson(String jsonString) throws ParseException {
        JSONObject result;
        JSONParser parser = new JSONParser();
        try {
            result = (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            log.error("Invalid jsonString : \n" + jsonString);
            throw e;
        }
        return result;
    }

    public static JSONObject toJson(Object object) throws ParseException {
        if (object == null) {
            return null;
        }
        JSONObject result;
        JSONParser parser = new JSONParser();
        try {
            result = (JSONObject) parser.parse(object.toString());
        } catch (ParseException e) {
            log.error("Invalid jsonString : \n" + object.toString());
            throw e;
        }
        return result;
    }


}
