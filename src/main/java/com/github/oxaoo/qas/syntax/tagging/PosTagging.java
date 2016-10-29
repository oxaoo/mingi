package com.github.oxaoo.qas.syntax.tagging;

import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;

import java.io.IOException;

/**
 * The class represent the Part-of-Speech tagging
 */
public class PosTagging {

    // Point TT4J to the TreeTagger installation directory. The executable is expected
    // in the "bin" subdirectory - in this example at "/opt/treetagger/bin/tree-tagger"
    public void tagging() throws IOException, TreeTaggerException {
//        System.setProperty("treetagger.home", "E:/Study/dev/TreeTagger");
        System.setProperty("treetagger.home", "src/main/resources/TreeTagger");
        TreeTaggerWrapper<String> tt = new TreeTaggerWrapper<String>();
        try {
//            tt.setModel("E:/Study/dev/TreeTagger/lib/english-utf8.par:iso8859-1");
            tt.setModel("src/main/resources/TreeTagger/lib/russian-utf8.par");
            tt.setHandler(new TokenHandler<String>()
            {
                public void token(String token, String pos, String lemma)
                {
                    System.out.println(token + "\t" + pos + "\t" + lemma);
                }
            });
//            tt.process(new String[] { "This", "is", "a", "test", "." });
//            tt.process(new String[] { "Это", "простой", "текстик", "." });
            tt.process(new String[] { "Данное", "программное", "обеспечение", "работает", "корректно", "." });
        }
        finally {
            tt.destroy();
        }
    }
}
