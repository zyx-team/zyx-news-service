package com.starnet.cloudmq.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class Util {
    private static final Log log = LogFactory.getLog(Util.class);

    public static long now() {
        return new Date().getTime();
    }


    public static String notNullString(Object field) {
        return field == null ? "" : field.toString();
    }

    public static Long stringToLong(String field) {
        try {
            return StringUtil.isEmpty(field) ? null : Long.parseLong(field);
        } catch (Exception e) {
            log.error(" string to Long error : " + field);
            return null;
        }
    }

    public static Integer stringToInteger(String field) {
        try {
            return StringUtil.isEmpty(field) ? null : Integer.parseInt(field);
        } catch (Exception e) {
            log.error(" string to Integer error : " + field);
            return null;
        }
    }

    @SuppressWarnings("all")
    public static void setNull(Object object) {
        object = null;
    }

    public static Boolean isNull(Object object) {
        return object == null;
    }

    public static Boolean isEmpty(Object object) {
        return object == null || object == "";
    }
}
