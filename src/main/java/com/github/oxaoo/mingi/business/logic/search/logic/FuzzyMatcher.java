package com.github.oxaoo.mingi.business.logic.search.logic;

import com.github.oxaoo.mingi.business.logic.search.data.RelevantInfo;
import com.github.oxaoo.mp4ru.syntax.tokenize.SentenceTokenizer;
import com.github.oxaoo.mp4ru.syntax.tokenize.Tokenizer;
import lombok.AllArgsConstructor;
import lombok.Setter;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 0.1.0
 * @since 18.08.2017
 */
@AllArgsConstructor
@Setter
public class FuzzyMatcher implements TextMatcher {
    private Tokenizer tokenizer;

    public FuzzyMatcher() {
        super();
        this.tokenizer = new SentenceTokenizer();
    }

    @Override
    public List<RelevantInfo> matching(String snippetsFragment, String text) {
        List<RelevantInfo> relevantInfoList = new ArrayList<>();
        List<String> snippets = this.snippetSplitter(snippetsFragment);
        List<String> sentences = this.tokenizer.tokenization(text);
        for (String snippet : snippets) {
            List<ExtractedResult> results = FuzzySearch.extractSorted(snippet, sentences, 3);
            List<String> relevantSentences = results.stream().map(ExtractedResult::getString).collect(Collectors.toList());
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
