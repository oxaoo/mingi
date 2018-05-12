package com.github.oxaoo.mingi.search.common;

import com.github.oxaoo.mingi.common.PropertyKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * The type Proxy manager.
 *
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 10.04.2017
 */
public class ProxyManager {
    private static final Logger LOG = LoggerFactory.getLogger(ProxyManager.class);

    /**
     * Inspect exist the proxy.
     *
     * @return <tt>true</tt> if proxy is exist
     */
    public static boolean inspect() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            //connection is exist
            return false;
        } catch (MalformedURLException e) {
            LOG.warn("Failed to inspect exist proxy. Cause: {}", e.getMessage());
            return true;
        } catch (IOException e) {
            return true;
        }
    }

    public static Proxy getProxyIf() {
        boolean isExistProxy = inspect();
        if (isExistProxy) {
            String proxyHost = PropertyKeeper.get(PropertyKeeper.PROXY_HOST_KEY);
            String proxyPortStr = PropertyKeeper.get(PropertyKeeper.PROXY_PORT_KEY);
            if (proxyHost == null || proxyPortStr == null) {
                LOG.debug("Proxy private properties are not set.");
                return null;
            }
            int proxyPort = Integer.valueOf(proxyPortStr);
            return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        } else return null;
    }
}
