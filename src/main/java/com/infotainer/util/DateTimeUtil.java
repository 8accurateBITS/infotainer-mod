package com.infotainer.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.infotainer.config.ModConfig;

public class DateTimeUtil {
    public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        ModConfig.ConfigData config = ModConfig.get();

        String timeFormat = config.use12HourFormat ? "hh:mm:ss a" : "HH:mm:ss";
        String pattern = "yyyy-MM-dd " + timeFormat;

        return now.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        ModConfig.ConfigData config = ModConfig.get();
        String timeFormat = config.use12HourFormat ? "hh:mm:ss a" : "HH:mm:ss";
        return now.format(DateTimeFormatter.ofPattern(timeFormat));
    }
}
