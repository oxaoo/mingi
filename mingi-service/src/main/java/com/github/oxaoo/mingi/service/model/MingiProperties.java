package com.github.oxaoo.mingi.service.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 10.05.2018
 */
@Getter
@Setter
public class MingiProperties {
    private String questionClassifierModelPath;
}
