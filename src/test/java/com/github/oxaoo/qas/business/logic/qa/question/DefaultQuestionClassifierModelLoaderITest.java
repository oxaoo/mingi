package com.github.oxaoo.qas.business.logic.qa.question;

import com.github.oxaoo.mp4ru.exceptions.ResourceResolverException;
import com.github.oxaoo.qas.business.logic.common.PropertyManager;
import com.github.oxaoo.qas.business.logic.exceptions.ProvideParserException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.oxaoo.qas.business.logic.qa.question.DefaultQuestionClassifierModelLoader.QAS_HOME_PROPERTY;
import static org.junit.Assert.*;

/**
 * The type Default question classifier model loader i test.
 *
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 07.05.2017
 */
public class DefaultQuestionClassifierModelLoaderITest {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultQuestionClassifierModelLoaderITest.class);

    /**
     * Apply 2 home test.
     *
     * @throws ProvideParserException    the provide parser exception
     * @throws ResourceResolverException the resource resolver exception
     */
    @Test
    public void apply2HomeTest() throws ProvideParserException, ResourceResolverException {
        String path2Qcm = "qas/qcm.model";
        DefaultQuestionClassifierModelLoader modelLoader = new DefaultQuestionClassifierModelLoader();
        String fullPath = modelLoader.apply2Home(path2Qcm);
        LOG.info("Full path: {}", fullPath);
        assertNotNull(fullPath);
        assertTrue(fullPath.endsWith(path2Qcm));
    }

    /**
     * Apply 2 home only file name test.
     *
     * @throws ProvideParserException    the provide parser exception
     * @throws ResourceResolverException the resource resolver exception
     */
    @Test
    public void apply2HomeOnlyFileNameTest() throws ProvideParserException, ResourceResolverException {
        String fileName = "qcm.model";
        DefaultQuestionClassifierModelLoader modelLoader = new DefaultQuestionClassifierModelLoader();
        String fullPath = modelLoader.apply2Home(fileName);
        LOG.info("Full path: {}", fullPath);
        assertNotNull(fullPath);
        assertTrue(fullPath.endsWith(fileName));
    }

    /**
     * Apply 2 home not exist file test.
     *
     * @throws ProvideParserException    the provide parser exception
     * @throws ResourceResolverException the resource resolver exception
     */
    @Test
    public void apply2HomeNotExistFileTest() throws ProvideParserException, ResourceResolverException {
        String fileName = "qcm_123_qwerty.model";
        DefaultQuestionClassifierModelLoader modelLoader = new DefaultQuestionClassifierModelLoader();
        String fullPath = modelLoader.apply2Home(fileName);
        LOG.info("Full path: {}", fullPath);
        assertNotNull(fullPath);
        assertTrue(fullPath.endsWith(fileName));
    }

    /**
     * Apply 2 home not exist home directory test.
     *
     * @throws ProvideParserException    the provide parser exception
     * @throws ResourceResolverException the resource resolver exception
     */
    @Test(expected = ResourceResolverException.class)
    public void apply2HomeNotExistHomeDirectoryTest() throws ProvideParserException, ResourceResolverException {
        String path2Qcm = "qas/qcm.model";
        PropertyManager.setProperty(QAS_HOME_PROPERTY, "qas_not_exist_dir/");
        DefaultQuestionClassifierModelLoader modelLoader = new DefaultQuestionClassifierModelLoader();
        modelLoader.apply2Home(path2Qcm);
    }

    //todo add itest for load() and build()
}
