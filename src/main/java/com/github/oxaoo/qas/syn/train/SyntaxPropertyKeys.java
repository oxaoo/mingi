package com.github.oxaoo.qas.syn.train;

/**
 * @author oxaoo
 * @since 25/09/16
 *
 * see http://www.maltparser.org/userguide.html#config
 */
public final class SyntaxPropertyKeys {

    /*example: -c my_guid_test_ll_by_malttab
               -if appdata/dataformat/malttab.xml
               -i examples/data/databank.malttab
               -m learn
               -l liblinear
     */

    public static final String CONFIG_NAME_FLAG = " -c ";
    public static final String CONFIG_NAME_MODEL = "qas_model";

    //TODO: replace the full path to a relative path
    public static final String INPUT_FORMAT_FLAG = " -if ";
//    public static final String INPUT_FORMAT_MALTTAB = "\\appdata\\dataformat\\malttab.xml";
    public static final String INPUT_FORMAT_MALTTAB = "Z:\\java\\STUDY\\qas\\src\\main\\resources\\dateformat\\malttab.xml";

    public static final String INPUT_INFILE_FLAG = " -i ";
//    public static final String INPUT_INFILE_PATH = "syn_training_texts/malttab/train_bank.malttab";
    public static final String INPUT_INFILE_PATH = "Z:\\java\\STUDY\\qas\\src\\main\\resources\\syn_training_texts\\malttab\\train_bank.malttab";

    public static final String CONFIG_FLOWCHART_FLAG = " -m ";
    public static final String CONFIG_FLOWCHART_LEARN = "learn";

    public static final String GUIDE_LEARNER_FLAG = " -l ";
    public static final String GUIDE_LEARNER_LIBLINEAR = "liblinear";


    public static final int OPTION_CONTAINER = 0;

}
