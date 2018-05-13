package com.github.oxaoo.mingi.search.engine.web;

import com.github.oxaoo.mingi.search.data.DataFragment;
import com.github.oxaoo.mingi.search.data.RelevantInfo;
import com.github.oxaoo.mingi.search.engine.SearchFinder;
import com.github.oxaoo.mingi.search.engine.SearchLoader;
import com.github.oxaoo.mingi.search.engine.SearchRetriever;
import com.github.oxaoo.mingi.search.logic.FuzzyMatcher;
import com.github.oxaoo.mingi.search.logic.TextMatcher;
import com.google.api.services.customsearch.model.Result;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
@AllArgsConstructor
@Setter
public class WebSearchRetriever implements SearchRetriever<List<Result>, List<WebSearchUnit>> {
    private TextMatcher matcher;

    public WebSearchRetriever() {
        super();
        this.matcher = new FuzzyMatcher();
    }

    @Override
    public List<DataFragment> retrieve(List<WebSearchUnit> units) {
        List<DataFragment> dataFragments = new ArrayList<>();
        for (WebSearchUnit unit : units) {
            List<RelevantInfo> relevantInfo = this.matcher.matching(unit.getResult().getSnippet(), unit.getText());
            dataFragments.add(new DataFragment(unit.getResult().getLink(), relevantInfo));
        }
        return dataFragments;
    }

    @Override
    public List<DataFragment> retrieve(SearchFinder<List<Result>> searchFinder,
                                       SearchLoader<List<Result>, List<WebSearchUnit>> searchLoader,
                                       String question) {
        List<Result> results = searchFinder.find(question);
        List<WebSearchUnit> units = searchLoader.load(results);
        return this.retrieve(units);
    }
}
