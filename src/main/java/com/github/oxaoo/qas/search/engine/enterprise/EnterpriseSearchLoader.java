package com.github.oxaoo.qas.search.engine.enterprise;

import com.github.oxaoo.qas.search.engine.SearchLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class EnterpriseSearchLoader implements SearchLoader<File, List<String>> {
    private static final Logger LOG = LoggerFactory.getLogger(EnterpriseSearchLoader.class);

    @Override
    public List<String> load(File results) {
        //todo implement
        return Collections.emptyList();
    }
}
