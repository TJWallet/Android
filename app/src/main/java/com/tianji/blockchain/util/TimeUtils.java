package com.tianji.blockchain.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {

    /**
     * 得到UTC时间，类型为字符串，格式自定义，例如"yyyy-MM-dd HH:mm"<br />
     * 如果获取失败，返回null
     *
     * @return 返回String类型时间
     */
    public static String getUTCTimeStr(DateFormat df) {
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        return df.format(new Date());
    }
}