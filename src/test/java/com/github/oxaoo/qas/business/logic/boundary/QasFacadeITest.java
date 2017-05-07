package com.github.oxaoo.qas.business.logic.boundary;

import com.github.oxaoo.qas.business.logic.exceptions.InitQasEngineException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 07.05.2017
 */
public class QasFacadeITest {

    /**
     * Init test.
     * Check that all facade components are initialized.
     *
     * @throws InitQasEngineException the init qas engine exception
     */
    @Test
    public void initTest() throws InitQasEngineException {
        QasFacade facade = new QasFacade();
        Assert.assertNotNull(facade.getQasEngine());
        Assert.assertNotNull(facade.getFileManager());
    }
}
