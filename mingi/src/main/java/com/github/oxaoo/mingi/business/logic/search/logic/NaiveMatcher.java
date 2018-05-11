package com.github.oxaoo.mingi.business.logic.search.logic;

import com.github.oxaoo.mingi.business.logic.search.data.RelevantInfo;
import com.github.oxaoo.mp4ru.syntax.tokenize.SentenceTokenizer;
import com.github.oxaoo.mp4ru.syntax.tokenize.Tokenizer;
import com.github.oxaoo.mp4ru.syntax.tokenize.WordTokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 25.03.2017
 */
@Deprecated
public class NaiveMatcher implements TextMatcher {

    @Override
    public List<RelevantInfo> matching(String snippetsFragment, String text) {
        List<RelevantInfo> relevantInfoList = new ArrayList<>();
        List<String> snippets = this.snippetSplitter(snippetsFragment);
        List<String> sentences = new SentenceTokenizer().tokenization(text);
        Tokenizer tokenizer = new WordTokenizer();
        for (String snippet : snippets) {
            SentenceScoreHandler scoreHandler = new SentenceScoreHandler(sentences.size());
            List<String> snippetTokens = tokenizer.tokenization(snippet);
            for (int i = 0; i < sentences.size(); i++) {
                for (String snippetToken : snippetTokens) {
                    if (sentences.get(i).contains(snippetToken)) scoreHandler.inc(i);
                }
            }
            List<SentenceScore> scores = scoreHandler.top(3);
            List<String> relevantSentences = scores.stream()
                    .map(s -> sentences.get(s.getIdSentence()))
                    .collect(Collectors.toList());
            relevantInfoList.add(new RelevantInfo(snippet, relevantSentences));
        }
        return relevantInfoList;
    }

    private List<String> snippetSplitter(String snippetsFragment) {
        snippetsFragment = snippetsFragment.replaceAll("[^\\S ]+", "");
        String separateToken = "\\.\\.\\.";
        return Arrays.stream(snippetsFragment.split(separateToken))
                .map(String::trim)
                .map(s -> s.replace(String.valueOf((char) 160), "")) // skip the '&nbsp;'
                .filter(s -> !(s.isEmpty()))
                .collect(Collectors.toList());
    }
}
