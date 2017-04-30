package com.github.oxaoo.qas.search.enterprise;

import com.github.oxaoo.qas.search.DataFragment;
import com.github.oxaoo.qas.search.SearchFinder;
import com.github.oxaoo.qas.search.SearchLoader;
import com.github.oxaoo.qas.search.SearchRetriever;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class EnterpriseSearchRetriever implements SearchRetriever<File, List<String>> {

    @Override
    public List<DataFragment> retrieve(List<String> textFragments) {
        //todo implement
        return Collections.emptyList();
    }

    @Override
    public List<DataFragment> retrieve(SearchFinder<File> searchFinder,
                                       SearchLoader<File, List<String>> searchLoader,
                                       String question) {
        //todo implement
        File fileResult = searchFinder.find();
        List<String> textFragments = searchLoader.load(fileResult);
        return this.retrieve(textFragments);
    }
}
