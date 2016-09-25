package com.github.oxaoo.qas.syn.train;

import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;

import java.io.File;

import static com.github.oxaoo.qas.syn.train.SyntaxPropertyKeys.*;

/**
 * @author oxaoo
 * @since 25/09/16
 */
public class SyntaxAnalyzer {

    private final MaltParserService maltParserService;

    public SyntaxAnalyzer() throws MaltChainedException {
        maltParserService = new MaltParserService(OPTION_CONTAINER);
    }

    public String learn() throws MaltChainedException {
        String command = CONFIG_WORKINGDIR_FLAG + CONFIG_WORKINGDIR_PATH
                + CONFIG_NAME_FLAG + CONFIG_NAME_MODEL
                + INPUT_FORMAT_FLAG + INPUT_FORMAT_MALTTAB
                + INPUT_INFILE_FLAG + INPUT_INFILE_PATH
                + CONFIG_FLOWCHART_FLAG + CONFIG_FLOWCHART_LEARN
                + GUIDE_LEARNER_FLAG + GUIDE_LEARNER_LIBLINEAR;

        maltParserService.runExperiment(command.trim());

        return CONFIG_WORKINGDIR_PATH + File.separator + CONFIG_NAME_MODEL;
    }
}
