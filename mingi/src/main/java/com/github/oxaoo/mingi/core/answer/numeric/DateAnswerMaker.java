package com.github.oxaoo.mingi.core.answer.numeric;

import com.github.oxaoo.mingi.core.answer.AnswerMakerTools;
import com.github.oxaoo.mingi.parse.ConllGraphComparator;
import com.github.oxaoo.mingi.parse.ParseNode;
import com.github.oxaoo.mingi.search.data.DataFragment;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.mingi.parse.ConllParseGraphBuilder;
import com.github.oxaoo.mingi.parse.ParseGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class DateAnswerMaker extends NumericAnswerMaker<String, Conll, DataFragment> {
    private static final Logger LOG = LoggerFactory.getLogger(DateAnswerMaker.class);

    @Override
    public List<Callable<String>> toAnswer(List<Conll> tokens, List<DataFragment> data) {
        final Conll targetToken;
        //find the verb
        Conll gotRoot = AnswerMakerTools.getRoot(tokens, "V.*");
        if (gotRoot != null) {
            targetToken = gotRoot;
        } else {
            targetToken = AnswerMakerTools.getRoot(tokens);
            LOG.warn("The DateAnswerMaker doesn't find the root token which the feat begin with 'V'");
        }

        Set<String> sentences = AnswerMakerTools.getSentences(data);
        return sentences.stream()
                .map(s -> (Callable<String>) () -> this.answer(s, targetToken))
                .collect(Collectors.toList());
    }

    private String answer(String sentence, Conll headQuestionToken) throws FailedParsingException {
        List<Conll> conlls = this.parser.parseSentence(sentence, Conll.class);
        ParseGraph<Conll> graph = new ConllParseGraphBuilder().build(conlls);
        ParseNode<Conll> foundHeadNode = graph.find(headQuestionToken, new ConllGraphComparator());
        //skip the fragments which doesn't contain the necessary information
        if (foundHeadNode == null) {
            return "";
        }
        Set<ParseNode<Conll>> dependentNodes = AnswerMakerTools.buildChain2Feats(foundHeadNode, "M.*");
        return AnswerMakerTools.prepareAnswer(dependentNodes);
    }
}
