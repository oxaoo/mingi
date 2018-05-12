package com.github.oxaoo.mingi.service.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("config")
public class ServiceProperties {
    private Home home;
    private Search search;
    private Proxy proxy;


    @Getter
    @Setter
    public static class Home {
        private String parser;
        private String mingi;
    }

    @Getter
    @Setter
    public static class Search {
        private String type;
        private String id;
        private String key;
    }

    @Getter
    @Setter
    public static class Proxy {
        private String host;
        private String port;
        private boolean enable;
    }
}
