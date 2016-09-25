package com.github.oxaoo.qas.syn.train;

import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;

import static com.github.oxaoo.qas.syn.train.SyntaxPropertyKeys.*;

public class SyntaxAnalyzer {

    private final MaltParserService maltParserService;

    public SyntaxAnalyzer() throws MaltChainedException {
        maltParserService = new MaltParserService(OPTION_CONTAINER);

//        String trainingDataFile = "Z:\\java\\STUDY\\qas\\src\\examples\\data\\talbanken05_train.conll";
        // Trains the parser model model0.mco and uses the option container 0
//        new MaltParserService(0).runExperiment("-c model0 -i "+trainingDataFile+" -m learn -ne true -nr false");
    }

    public void learn() throws MaltChainedException {
        String command = CONFIG_NAME_FLAG + CONFIG_NAME_MODEL
                + INPUT_FORMAT_FLAG + INPUT_FORMAT_MALTTAB
                + INPUT_INFILE_FLAG + INPUT_INFILE_PATH
                + CONFIG_FLOWCHART_FLAG + CONFIG_FLOWCHART_LEARN
                + GUIDE_LEARNER_FLAG + GUIDE_LEARNER_LIBLINEAR;

        maltParserService.runExperiment(command.trim());

        //TODO: return path to model.mco
    }
}
