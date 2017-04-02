package com.github.oxaoo.qas.utils;

import com.github.oxaoo.mp4ru.common.ResourceResolver;
import com.github.oxaoo.mp4ru.exceptions.ResourceResolverException;
import com.github.oxaoo.qas.search.PageExtractorTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 02.04.2017
 */
public class UtilsTest {
    private static final Logger LOG = LoggerFactory.getLogger(UtilsTest.class);

    @Test
    public void getAbsolutePathTest() throws ResourceResolverException {
        String absPath = ResourceResolver.getAbsolutePath("qas/qcm.model");
        LOG.info("Absolute path: {}", absPath);
    }

    @Test
    public void test() {
        String path = "path/to/file.txt";
        String home = "path";
        String res = path.replace(home + "/", "");
        LOG.info("Res: {}", res);
    }
}
