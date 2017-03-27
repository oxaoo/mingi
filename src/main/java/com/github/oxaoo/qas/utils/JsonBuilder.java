package com.github.oxaoo.qas.utils;

import com.google.gson.GsonBuilder;
import org.slf4j.Logger;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 27.03.2017
 */
public class JsonBuilder {
    public static<T> String toJson(T t) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(t);
    }

    public static<T> void jsonPrettyLog(T t, Logger log) {
        String json = toJson(t);
        log.info("Pretty view: {}", json);
    }
}
