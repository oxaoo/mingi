package com.github.oxaoo.qas.utils;

import com.github.oxaoo.mp4ru.common.ResourceResolver;
import com.github.oxaoo.mp4ru.exceptions.ResourceResolverException;
import com.github.oxaoo.qas.search.PageExtractorTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.net.URL;

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
//        InputStreamReader r = ResourceResolver.getResourceAsStreamReader("qas/qcm.model");
        URL url = ResourceResolver.getUrl("qas/qcm.model");
        LOG.info("Absolute path: {}", absPath);
    }

    @Test
    public void test() {
        String path = "path/to/file.txt";
        String home = "path";
        String res = path.replace(home + "/", "");
        LOG.info("Res: {}", res);
    }

    @Test
    public void test2() {
        String str = "hello [world] hello (my) name is [alex], and how - your \"name\"?";
//        String str = "hello (world) hello (my) name is (alex)";
//        String str2 = str.replaceAll("[—«»\"`‚„‘’“”%;:.?!]*[\\[.*\\]]*", "");
//        String str2 = str.replaceAll("\\([^)]*\\)", "");
//        String str2 = str.replaceAll("\\[[^]]*\\]", "");
        String str2 = str.replaceAll("(\\[[^]]*\\])?(\\([^)]*\\))?([—«»\"`‚„‘’“”%;:,.?!]*[\\[.*\\]]*)?", "");
        LOG.info("STR2: {}", str2);
    }
}
