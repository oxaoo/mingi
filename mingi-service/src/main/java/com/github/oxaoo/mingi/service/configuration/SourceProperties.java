package com.github.oxaoo.mingi.service.configuration;

import com.github.oxaoo.mingi.service.model.MingiProperties;
import com.github.oxaoo.mingi.service.model.ParserProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("sources")
public class SourceProperties {
    private ParserProperties parser;
    private MingiProperties mingi;
}
