package self.ed.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils {
    public static void log(String message) {
        System.out.println(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS").format(new Date()) + " - " + message);
    }
}
