package com.github.oxaoo.mingi.service.controller;

import com.github.oxaoo.mingi.common.PropertyKeeper;
import com.github.oxaoo.mingi.service.configuration.ServiceProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 15.05.2018
 */
@RestController
@RequestMapping(value = "/configuration")
public class ConfigurationManagementController {

    @RequestMapping(value = "/proxy", method = RequestMethod.POST)
    public ResponseEntity<Void> setupProxy(@RequestBody ServiceProperties.Proxy proxy) {
        if (proxy.isEnable()) {
            PropertyKeeper.put(PropertyKeeper.PROXY_HOST_KEY, proxy.getHost());
            PropertyKeeper.put(PropertyKeeper.PROXY_PORT_KEY, proxy.getPort());
        } else {
            PropertyKeeper.remove(PropertyKeeper.PROXY_HOST_KEY);
            PropertyKeeper.remove(PropertyKeeper.PROXY_PORT_KEY);
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/search/google", method = RequestMethod.POST)
    public ResponseEntity<Void> setupGoogleSearch(@RequestBody ServiceProperties.Search search) {
        PropertyKeeper.put(PropertyKeeper.GOOGLE_CSE_ID_KEY, search.getId());
        PropertyKeeper.put(PropertyKeeper.GOOGLE_API_KEY_KEY, search.getKey());
        return ResponseEntity.ok().build();
    }
}
