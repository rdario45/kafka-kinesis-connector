package com.ticketmaster.sponsorship.upsell.util;

import io.hawt.util.Strings;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.regex.Pattern;

@Slf4j
public class Utils {
    private static final int DEFAULT_LENGTH = 150;

    private Utils() {
    }

    public static String limitData(String data) {
        return limitDataInternal(data, DEFAULT_LENGTH);
    }

    public static String limitData(String data, int length) {
        return limitDataInternal(data, length);
    }

    private static String limitDataInternal(String data, int length) {
        if (data != null)
            return data.length() > length ? data.substring(0, length) : data;

        return null;
    }

    /**
     * Format date in ISO_LOCAL_DATE to epoch
     * @param date
     * @return null in case of incorrect date
     */
    public static Long dateToEpoch(String date) {
        if (Strings.isNotBlank(date)) {
            try {
                LocalDate localDate = LocalDate.parse(date);
                return localDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
            } catch (Exception ex) {}
        }
        return null;
    }

    /**
     * Timestamp of current date start
     * @return
     */
    public static long currentDateTs() {
        return LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC);
    }

    public static String generateKey(String eventId) {
        return String.format("%s.json", eventId);
    }

    public static boolean isValidS3Key(String s) {
        return Pattern.matches("[\\w!-.*'()]*", s);
    }
}
