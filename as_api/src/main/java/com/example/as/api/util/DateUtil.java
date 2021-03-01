package com.example.as.api.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 返回当前时间
     *
     * @return
     */
    public static String currentDate() {
        return sdf.format(new Date());
    }
}
