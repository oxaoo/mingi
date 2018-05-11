package com.github.oxaoo.mingi.business.logic.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 11.05.2018
 */
public class PropertyKeeper {
    public final static String PROXY_HOST_KEY = "proxy.host";
    public final static String PROXY_PORT_KEY = "proxy.port";
    public final static String GOOGLE_CSE_ID_KEY = "google.cse.id";
    public final static String GOOGLE_API_KEY_KEY = "google.api.key";

    private static final ConcurrentMap<String, String> properties = new ConcurrentHashMap<>();
    private static volatile boolean init = false;

    private PropertyKeeper() {
    }

    public static String get(final String key) {
        if (!init) {
            throw new IllegalStateException("PropertyKeeper isn't init");
        }
        return properties.get(key);
    }

    public static void put(final String key, final String value) {
        init = true;
        properties.put(key, value);
    }
}
