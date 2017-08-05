package com.github.oxaoo.qas.business.logic.qa.answer.human;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.business.logic.parse.ConllGraphComparator;
import com.github.oxaoo.qas.business.logic.parse.ConllParseGraphBuilder;
import com.github.oxaoo.qas.business.logic.parse.ParseGraph;
import com.github.oxaoo.qas.business.logic.parse.ParseNode;
import com.github.oxaoo.qas.business.logic.qa.answer.AnswerMakerTools;
import com.github.oxaoo.qas.business.logic.search.data.DataFragment;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class IndAnswerMaker extends HumanAnswerMaker<String, Conll, DataFragment> {

    @Override
    public List<Callable<String>> toAnswer(List<Conll> tokens, List<DataFragment> data) {
        Conll headQuestionToken = AnswerMakerTools.getRoot(tokens);
        List<String> sentences = AnswerMakerTools.getSentences(data);
        return sentences.stream()
                .map(s -> (Callable<String>) () -> this.answer(s, headQuestionToken))
                .collect(Collectors.toList());
    }

    private String answer(String sentence, Conll headQuestionToken)
            throws FailedParsingException {
        List<Conll> conlls = this.parser.parseSentence(sentence, Conll.class);
        ParseGraph<Conll> graph = new ConllParseGraphBuilder().build(conlls);
        ParseNode<Conll> foundNode = graph.find(headQuestionToken, new ConllGraphComparator());
        //skip the fragments which doesn't contain the necessary information
        if (foundNode == null) {
            return "";
        }
        Set<ParseNode<Conll>> dependentNodes = AnswerMakerTools.buildChain2Feats(foundNode, "Np.*");
        return AnswerMakerTools.prepareAnswer(dependentNodes);
    }
}
