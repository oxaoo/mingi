package com.github.oxaoo.qas.business.logic.utils;

import com.github.oxaoo.qas.business.logic.exceptions.ErrorId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 19.03.2017
 */
public class PropertyManager {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyManager.class);

    private static final String PROPERTY_FILE_NAME = "qas.properties";
    private static final String PRIVATE_PROPERTY_FILE_NAME = "qas_private.properties";
    private static PropertyManager propertyManager = new PropertyManager();
    private Properties properties;
    private Properties privateProperties;

    private PropertyManager() {
        this.properties = this.loadProperties(PROPERTY_FILE_NAME);
        this.privateProperties = this.loadProperties(PRIVATE_PROPERTY_FILE_NAME);

    }

    private Properties loadProperties(String propertyFileName) {
        Properties prop = new Properties();
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFileName)) {
            prop.load(in);
        } catch (IOException e) {
            LOG.error(ErrorId.FAILED_LOAD_APPLICATION_PROPERTIES.getDescription(e));
        }
        return prop;
    }

    public static Properties getProperties() {
        return propertyManager.properties;
    }

    public static String getProperty(String key) {
        return propertyManager.properties.getProperty(key);
    }

    public static String getPrivateProperty(String key) {
        return propertyManager.privateProperties.getProperty(key);
    }
}
