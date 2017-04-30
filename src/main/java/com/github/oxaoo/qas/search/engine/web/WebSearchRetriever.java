package com.github.oxaoo.qas.search.engine.web;

import com.github.oxaoo.qas.search.data.DataFragment;
import com.github.oxaoo.qas.search.data.RelevantInfo;
import com.github.oxaoo.qas.search.engine.SearchFinder;
import com.github.oxaoo.qas.search.engine.SearchLoader;
import com.github.oxaoo.qas.search.engine.SearchRetriever;
import com.github.oxaoo.qas.search.logic.NaiveMatcher;
import com.google.api.services.customsearch.model.Result;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
@NoArgsConstructor
@Setter
public class WebSearchRetriever implements SearchRetriever<List<Result>, List<WebSearchUnit>> {

    @Override
    public List<DataFragment> retrieve(List<WebSearchUnit> units) {
        List<DataFragment> dataFragments = new ArrayList<>();
        for (WebSearchUnit unit : units) {
            List<RelevantInfo> relevantInfo = new NaiveMatcher()
                    .matching(unit.getResult().getSnippet(), unit.getText());
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
