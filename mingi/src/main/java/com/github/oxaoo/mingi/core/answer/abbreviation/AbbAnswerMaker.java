package com.github.oxaoo.mingi.core.answer.abbreviation;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.mingi.parse.ConllGraphComparator;
import com.github.oxaoo.mingi.parse.ConllParseGraphBuilder;
import com.github.oxaoo.mingi.parse.ParseGraph;
import com.github.oxaoo.mingi.parse.ParseNode;
import com.github.oxaoo.mingi.core.answer.AnswerMakerTools;
import com.github.oxaoo.mingi.search.data.DataFragment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class AbbAnswerMaker extends AbbreviationAnswerMaker<String, Conll, DataFragment> {

    @Override
    public List<Callable<String>> toAnswer(List<Conll> tokens, List<DataFragment> data) {
        //find the potential abb
        Conll targetToken = AnswerMakerTools.findSecondAfterRoot(tokens, "Nc.*");

        Set<String> sentences = AnswerMakerTools.getSentences(data);
        return sentences.stream()
                .map(s -> (Callable<String>) () -> this.answer(s, targetToken))
                .collect(Collectors.toList());
    }

    private String answer(String sentence, Conll targetToken)
            throws FailedParsingException {
        List<Conll> conlls = this.parser.parseSentence(sentence, Conll.class);
        ParseGraph<Conll> graph = new ConllParseGraphBuilder().build(conlls);
        ParseNode<Conll> foundNode = graph.find(targetToken, new ConllGraphComparator());
        //skip the fragments which doesn't contain the necessary information
        if (foundNode == null) {
            return "";
        }
        foundNode = AnswerMakerTools.getFirstParent(foundNode, "V.*");
        List<ParseNode<Conll>> dependentNodes = foundNode.getAllChild();
        return AnswerMakerTools.prepareAnswer(new HashSet<>(dependentNodes));
    }
}
