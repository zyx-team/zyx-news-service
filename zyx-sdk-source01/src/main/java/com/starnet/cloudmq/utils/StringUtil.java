package com.starnet.cloudmq.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.starnet.cloudmq.rabbitmq.CloudMQException;

public class StringUtil {

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static String toString(InputStream is) {
        return Inputstr2Str_ByteArrayOutputStream(is, "utf-8");
    }

    public static String Inputstr2Str_ByteArrayOutputStream(InputStream in, String encode) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = 0;
        try {
            if (encode == null || encode.equals("")) {
                // 默认以utf-8形式
                encode = "utf-8";
            }
            while ((len = in.read(b)) > 0) {
                out.write(b, 0, len);
            }
            return out.toString(encode);
        } catch (IOException e) {
            return null;
        }
    }
    public static String byteToString(byte[] obj) throws CloudMQException {
        try {
            return new String(obj, "UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new CloudMQException("errcode:14010000,errmsg:" + "ex", e);
        }
    }
}
