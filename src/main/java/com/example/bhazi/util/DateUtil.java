package com.example.bhazi.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

import com.example.bhazi.core.exception.GlobalException;

public class DateUtil {
    
    private static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static DateFormat zoneFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm:ss")
            .withZone(ZoneId.of("Asia/Kolkata"));
    
    // read a date string and parse/convert to a date
    public static Date parseDate(String dateStr) {
        Date theDate;
        try {
            theDate = formatter.parse(dateStr);
        } catch (ParseException e) {
            throw new GlobalException("Timestamp not in format: dd/MM/yyyy HH:mm:ss");
        }
        return theDate;        
    }

    public static Instant getTimestamp() {
        return Instant.now();
    }

    public static LocalDate getDate() {
        return LocalDate.now(ZoneId.of("Asia/Kolkata"));
    }
    
    public static LocalTime getCurrentTime() {
    	return LocalTime.now(ZoneId.of("Asia/Kolkata"));
    }

    public static String getTimestampString(Instant timestamp) {
        return dateTimeFormatter.format(timestamp);
    }
    
    // read a date and format/convert to a string
    public static String formatDate(Date date) {
        String result = null;
        
        zoneFormatter.setTimeZone(TimeZone.getTimeZone("IST"));
        if (date != null) {
            result = zoneFormatter.format(date);
        }
        
        return result;
    }

    public static int differenceBetweenDays(Instant first, Instant second) {
        return first
            .truncatedTo(ChronoUnit.DAYS)
            .compareTo(second.truncatedTo(ChronoUnit.DAYS));
    }
}
