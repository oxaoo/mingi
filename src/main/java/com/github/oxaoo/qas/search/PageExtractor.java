package com.github.oxaoo.qas.search;

import com.google.api.services.customsearch.model.Result;
import org.apache.http.protocol.HTTP;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 22.03.2017
 */
public class PageExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(PageExtractor.class);

    public PageExtractor() {
        try {
            enableSSLSocket();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            LOG.error("Failed to enable ssl socket. Cause: {}", e);
        }
    }

    public void extract(List<Result> snippets) {
        for (Result snippet : snippets) {
            //todo workaround
            String link = snippet.getLink();
            String encodedLink;
/*            try {
                String queryPath = link.substring(link.lastIndexOf('/') + 1, link.length());
                String lq = "эльбрус";
//                String elq = URLEncoder.encode(lq,  HTTP.UTF_8);
                encodedLink = String.format(link, URLEncoder.encode(queryPath.toLowerCase(),  HTTP.UTF_8));
                encodedLink = URLEncoder.encode(link, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOG.error("Failed to encoding url. Cause: {}", e);
                continue;
            }*/
            try {
                Document doc = Jsoup.connect("https://ru.wikipedia.org/wiki/эльбрус")
                        .proxy("proxy.t-systems.ru", 3128)
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                        .timeout(10000)
//                        .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
//                        .method(Connection.Method.POST)
                        .get();
                String text = doc.body().text();
                LOG.info("Link: {}, \nText: {}", link, text);
            } catch (IOException e) {
                LOG.error("Failed to get page {}. Cause: {}", link, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }
}
