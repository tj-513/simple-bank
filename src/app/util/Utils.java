package app.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static String getFormattedDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return date.format(formatter);
    }
}
