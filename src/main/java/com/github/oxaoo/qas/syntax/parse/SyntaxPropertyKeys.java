package com.github.oxaoo.qas.syntax.parse;

import com.github.oxaoo.qas.GlobalPropertyKeys;

import static com.github.oxaoo.qas.GlobalPropertyKeys.CONLL_TEXT_FILE;

/**
 * Property keys for syntax analyzer.
 *
 * @author Alexander Kuleshov
 * @version 0.1
 * @link http://www.maltparser.org/userguide.html#config
 * @since 25.09.2016
 */
final class SyntaxPropertyKeys {
    static final String CONFIG_WORKINGDIR_PATH = " -w src/main/resources/model/";
    static final String CONFIG_NAME_MODEL = " -c russian";
    static final String INPUT_INFILE_PATH = " -i " + CONLL_TEXT_FILE;
    static final String OUTPUT_OUTFILE_PATH = " -o src/main/resources/output/text.parse";
    static final String CONFIG_FLOWCHART_PARSE = " -m parse";

    static final int OPTION_CONTAINER = 0;
}
