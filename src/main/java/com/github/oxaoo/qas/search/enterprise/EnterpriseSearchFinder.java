package com.github.oxaoo.qas.search.enterprise;

import com.github.oxaoo.qas.search.SearchFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class EnterpriseSearchFinder implements SearchFinder<File> {
    private static final Logger LOG = LoggerFactory.getLogger(EnterpriseSearchFinder.class);

    public EnterpriseSearchFinder() {
        this.init();
    }

    private void init() {
        //todo implement
    }

    @Override
    public File find() {
        //todo implement
        return null;
    }

    @Override
    public File find(String s) {
        throw new UnsupportedOperationException("Method is not supported by the Enterprise Engine Finder.");
    }
}
