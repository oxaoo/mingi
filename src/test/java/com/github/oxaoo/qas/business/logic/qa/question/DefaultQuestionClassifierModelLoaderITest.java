package com.github.oxaoo.qas.business.logic.qa.question;

import com.github.oxaoo.mp4ru.exceptions.ResourceResolverException;
import com.github.oxaoo.qas.business.logic.common.PropertyManager;
import com.github.oxaoo.qas.business.logic.exceptions.BuildModelException;
import com.github.oxaoo.qas.business.logic.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.qas.business.logic.exceptions.ProvideParserException;
import libsvm.svm_model;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static com.github.oxaoo.qas.business.logic.qa.question.DefaultQuestionClassifierModelLoader.QAS_HOME_PROPERTY;
import static com.github.oxaoo.qas.business.logic.qa.question.DefaultQuestionClassifierModelLoader.QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        String backupPropertyHome = PropertyManager.setProperty(QAS_HOME_PROPERTY, "qas_nonexistent_dir/");
        try {
            DefaultQuestionClassifierModelLoader modelLoader = new DefaultQuestionClassifierModelLoader();
            modelLoader.apply2Home(path2Qcm);
        } finally {
            //rollback in finally case, because the apply2Home method throw exception
            PropertyManager.setProperty(QAS_HOME_PROPERTY, backupPropertyHome);
        }
    }

    //todo add itest for load() and build()
    @Test
    public void loadTest() throws ProvideParserException, LoadQuestionClassifierModelException {
        QuestionClassifierModelLoader modelLoader = new DefaultQuestionClassifierModelLoader();
        svm_model model = modelLoader.load();
        assertNotNull(model);
    }

    @Test
    public void loadNotExistModelTest() throws ProvideParserException, LoadQuestionClassifierModelException {
        String modelPath = "qas/" + UUID.randomUUID().toString() + ".model";
        //intentionally changing the path to the model file
        String backupPropertyModel = PropertyManager.setProperty(QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY, modelPath);
        QuestionClassifierModelLoader modelLoader = new DefaultQuestionClassifierModelLoader();
        svm_model model = modelLoader.load();
        assertNotNull(model);
        //rollback
        PropertyManager.setProperty(QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY, backupPropertyModel);
    }

    @Test(expected = LoadQuestionClassifierModelException.class)
    public void loadNotExistModelAndHomeTest() throws ProvideParserException, LoadQuestionClassifierModelException {
        String modelPath = "qas/" + UUID.randomUUID().toString() + ".model";
        //intentionally changing the paths
        String backupPropertyModel = PropertyManager.setProperty(QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY, modelPath);
        String backupPropertyHome = PropertyManager.setProperty(QAS_HOME_PROPERTY, "qas_nonexistent_dir/");
        try {
            QuestionClassifierModelLoader modelLoader = new DefaultQuestionClassifierModelLoader();
            modelLoader.load();
        } finally {
            //rollback in finally case, because the load method throw exception
            PropertyManager.setProperty(QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY, backupPropertyModel);
            PropertyManager.setProperty(QAS_HOME_PROPERTY, backupPropertyHome);
        }
    }

    @Test
    public void buildTest() throws ProvideParserException, BuildModelException {
        QuestionClassifierModelLoader modelLoader = new DefaultQuestionClassifierModelLoader();
        svm_model model = modelLoader.build();
        assertNotNull(model);
    }
}
