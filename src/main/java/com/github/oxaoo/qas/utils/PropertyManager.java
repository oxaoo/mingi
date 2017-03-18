package com.github.oxaoo.qas.utils;

import com.github.oxaoo.qas.exceptions.ErrorId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private static PropertyManager propertyManager = new PropertyManager();
    private Properties properties;

    private PropertyManager() {
        this.properties = new Properties();
        try (InputStream in = new FileInputStream(PROPERTY_FILE_NAME)) {
            properties.load(in);
        } catch (IOException e) {
            LOG.error(ErrorId.FAILED_LOAD_APPLICATION_PROPERTIES.getDescription(e));
        }
    }

    public static Properties getProperties() {
        return propertyManager.properties;
    }

    public static String getProperty(String key) {
        return propertyManager.properties.getProperty(key);
    }
}
