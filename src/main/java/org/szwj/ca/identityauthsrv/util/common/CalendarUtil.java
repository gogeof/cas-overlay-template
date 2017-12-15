package org.szwj.ca.identityauthsrv.util.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarUtil {

    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Timestamp GetUTCTimestamp() {
        Calendar calendar = getUTCCalendar();
        String currentTime = convertCalendarToStr(calendar);
        if (null == currentTime) {
            return null;
        }
        return Timestamp.valueOf(currentTime);
    }

    public static Timestamp GetExpiredUTCTimestamp(int expiredMinute) {
        Calendar calendar = getUTCCalendar();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + expiredMinute);
        String expiredTime = convertCalendarToStr(calendar);
        if (null == expiredTime) {
            return null;
        }
        return Timestamp.valueOf(expiredTime);
    }

    public static int GetYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int GetMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int GetDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    private static String convertCalendarToStr(Calendar calendar) {
        StringBuffer UTCTimeBuffer = new StringBuffer();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        UTCTimeBuffer.append(year).append("-").append(month).append("-").append(day);
        UTCTimeBuffer.append(" ").append(hour).append(":").append(minute).append(":")
            .append(second);
        try {
            format.parse(UTCTimeBuffer.toString());
            return UTCTimeBuffer.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Calendar getUTCCalendar() {
        // 1、取得本地时间;
        Calendar calendar = Calendar.getInstance();
        // 2、取得时间偏移量;
        int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
        // 3、取得夏令时差;
        int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间;
        calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return calendar;
    }
}
