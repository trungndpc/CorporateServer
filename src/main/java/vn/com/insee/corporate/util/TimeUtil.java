package vn.com.insee.corporate.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class TimeUtil {

    public static int getTime(String str) {
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date parse = formatter.parse(str);
            long l = parse.getTime() / 1000 ;
            return (int) l;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
