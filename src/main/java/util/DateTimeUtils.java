package util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtils {

    public static String getCurrentDateString() {
        LocalDateTime nowTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm:ss");
        return nowTime.format(format);
    }

    public static Date getCurrentDate() {
        LocalDateTime nowTime = LocalDateTime.now();
        return Date.from(nowTime.toInstant(OffsetDateTime.now().getOffset()));
    }
}
