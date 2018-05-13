package com.github.oxaoo.mingi.core;

import com.github.oxaoo.mingi.core.question.QuestionClassifier;
import com.github.oxaoo.mingi.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.mingi.core.answer.AnswerEngine;
import com.github.oxaoo.mingi.core.question.QuestionDomain;
import com.github.oxaoo.mingi.search.data.DataFragment;
import com.github.oxaoo.mingi.search.engine.SearchEngine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 25.03.2017
 */
@Setter
@Getter
@AllArgsConstructor
public class QasEngine {
    private static final Logger LOG = LoggerFactory.getLogger(QasEngine.class);

    private final RussianParser parser;
    private final QuestionClassifier questionClassifier;
    private final AnswerEngine answerEngine;

    public <T, K> Set<String> answer(String question, SearchEngine<T, K> searchEngine) throws FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException {
        List<Conll> questionTokens = this.parser.parse(question, Conll.class);
        QuestionDomain questionDomain = this.questionClassifier.classify(questionTokens);
        List<DataFragment> dataFragments = searchEngine.collectInfo(question);
        return this.makeAnswer(questionTokens, questionDomain, dataFragments);
    }

    private Set<String> makeAnswer(List<Conll> questionTokens,
                                   QuestionDomain questionDomain,
                                   List<DataFragment> dataFragments) {
        LOG.info("### Stage of make answer ###");
        LOG.info("Question: {}", questionTokens.toString());
        LOG.info("Question domain: {}", questionDomain.name());
        LOG.debug("Data fragments: {}", dataFragments.toString());

        return this.answerEngine.make(questionTokens, questionDomain, dataFragments);
    }

    public void shutdown() {
        this.answerEngine.shutdownExecutor();
    }
}
