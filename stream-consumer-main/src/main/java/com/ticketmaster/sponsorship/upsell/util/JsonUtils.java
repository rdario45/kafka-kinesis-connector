package com.ticketmaster.sponsorship.upsell.util;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonUtils {

    private JsonUtils() {
    }

    /**
     * throws JsonPathException
     */
    public static String read(String json, String path) {
        return JsonPath.read(json, path);
    }

    public static String readSilently(String json, String path) {
        try {
            return read(json, path);
        } catch (Exception e) {
            return null;
        }
    }

}
