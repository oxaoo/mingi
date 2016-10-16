package com.github.oxaoo.qas.syntax.parse;

import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The syntax analyzer.
 *
 * @author oxaoo
 * @since 25/09/16
 */
public class SyntaxAnalyzer {
    private static final Logger LOG = LoggerFactory.getLogger(SyntaxAnalyzer.class);

    private final MaltParserService maltParserService;

    public SyntaxAnalyzer() throws MaltChainedException {
        maltParserService = new MaltParserService(SyntaxPropertyKeys.OPTION_CONTAINER);
    }

    /**
     * Syntax analyze by means of MaltParser.
     *
     * @return <tt>true</tt> if analyze is successful
     */
    public boolean analyze() {

        /**
         * TODO: implement formation LEMMA;
         * TODO: implement formation CPOSTAG;
         * TODO: implement formation POSTAG;
         * TODO: implement formation FEATS.
         */
        final String command = SyntaxPropertyKeys.CONFIG_WORKINGDIR_PATH
                + SyntaxPropertyKeys.CONFIG_NAME_MODEL
                + SyntaxPropertyKeys.INPUT_INFILE_PATH
                + SyntaxPropertyKeys.OUTPUT_OUTFILE_PATH
                + SyntaxPropertyKeys.CONFIG_FLOWCHART_PARSE;

        try {
            maltParserService.runExperiment(command.trim());
        } catch (final MaltChainedException | OutOfMemoryError e) {
            LOG.error("Failed to syntax analyze: [{}]", e.toString());
            return false;
        }
        return true;
    }
}
