package com.github.oxaoo.mingi.search.logic;

import com.github.oxaoo.mingi.search.data.RelevantInfo;

import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 25.03.2017
 */
public interface TextMatcher {
    List<RelevantInfo> matching(String fragment, String text);
}
