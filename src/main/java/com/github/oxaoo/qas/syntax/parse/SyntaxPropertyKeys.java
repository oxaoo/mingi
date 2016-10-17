package com.github.oxaoo.qas.syntax.parse;

/**
 * Property keys for syntax analyzer.
 *
 * @author oxaoo
 * @since 25/09/16
 *
 * @link http://www.maltparser.org/userguide.html#config
 */
public final class SyntaxPropertyKeys {

    /*example: -c my_guid_test_ll_by_malttab
               -if appdata/dataformat/malttab.xml
               -i examples/data/databank.malttab
               -m learn
               -l liblinear
     */
    //TODO: to replace the full path to a relative path
    public static final String CONFIG_WORKINGDIR_PATH = " -w src/main/resources/model/";
    public static final String CONFIG_NAME_MODEL = " -c russian";
    //input conll format by default.
    public static final String INPUT_FORMAT_MALTTAB = " -if Z:/java/STUDY/qas/src/main/resources/dateformat/malttab.xml";
//    public static final String INPUT_INFILE_PATH = " -i Z:/java/STUDY/qas/src/main/resources/syn_training_texts/malttab/train_bank.malttab";
    public static final String INPUT_INFILE_PATH = " -i Z:/java/STUDY/qas/src/main/resources/input/text.conll";
    public static final String OUTPUT_OUTFILE_PATH = " -o Z:/java/STUDY/qas/src/main/resources/output/text.parse";
    public static final String CONFIG_FLOWCHART_LEARN = " -m learn";
    public static final String CONFIG_FLOWCHART_PARSE = " -m parse";
    //liblinear learner by default.
    public static final String GUIDE_LEARNER_LIBLINEAR = " -l liblinear";

    public static final int OPTION_CONTAINER = 0;
}
