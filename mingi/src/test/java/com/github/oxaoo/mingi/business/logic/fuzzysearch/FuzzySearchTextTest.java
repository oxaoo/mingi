package com.github.oxaoo.mingi.business.logic.fuzzysearch;

import com.github.oxaoo.mp4ru.syntax.tokenize.SentenceTokenizer;
import com.github.oxaoo.mp4ru.syntax.tokenize.Tokenizer;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.algorithms.WeightedRatio;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author <a href="mailto:aleksandr.kuleshov@t-systems.ru">Alexander Kuleshov</a>
 */
public class FuzzySearchTextTest {
    private static final Logger LOG = LoggerFactory.getLogger(FuzzySearchTextTest.class);
    private Tokenizer tokenizer = new SentenceTokenizer();

    @Test
    public void test() {
        final List<String> sentences = this.tokenizer.tokenization(text);
//        final List<ExtractedResult> results = FuzzySearch.extractAll(query2, sentences);
//        final List<ExtractedResult> results = FuzzySearch.extractSorted(query1, sentences, 3);
        final List<ExtractedResult> results = FuzzySearch.extractTop(query1, sentences, new WeightedRatio(), 3);
        LOG.info("List of result {}:", results.size());
        results.forEach(r -> LOG.info(r.toString()));
//        LOG.info("Results: {}", results.toString());
    }

    private String query1 = "В России буддизм традиционно исповедуют жители Бурятии, Калмыкии, Тувы. Численность этнических буддистов данных регионов составляет";
    private String query2 = "Буддизм в России — одна из распространенных на территории страны религиозных ... Действовало 44 дацана, 144 малых храма, в которых насчитывалось";

    private String text = "Буддизм в России — одна из распространенных на территории страны религиозных традиций. Традиционными районами, где исповедуется буддизм, являются Бурятия, Тыва, Калмыкия и Забайкальский край. Также буддийские общины существуют в Санкт-Петербурге, Москве и других городах. Большая часть приверженцев буддизма в России относится к школе Гэлуг, также значительное количество российских буддистов принадлежат к школе Карма Кагью." +
            "В России буддизм традиционно исповедуют жители Бурятии, Калмыкии, Тувы. Численность этнических буддистов данных регионов составляет около 900 тысяч человек[8]. В последние годы буддийские общины возникли в Москве, Санкт-Петербурге, Самаре и некоторых других наиболее крупных российских городах, не связанных с традиционными регионами буддизма. Число буддистов в данных городах, согласно опросам, составляет около 1 % их жителей. Такой же процент составляет число буддистов в масштабе всей страны[9]. Численность российских буддистов, выполняющих практику, составляет не более 500 тысяч человек" +
            "В Бурятии, Калмыкии, Туве, Санкт-Петербурге восстанавливаются уцелевшие буддийские храмы и открываются новые, при монастырях создаются учебные заведения, приглашаются тибетские учителя.\n" +
            "В России буддизм обретает также популярность среди русских и других народов.\n" +
            "В настоящее время в России представлены многие буддийские школы: тхеравада, несколько направлений махаяны, в том числе японский дзэн, корейский сон и практически все школы тибетского буддизма.";

}
