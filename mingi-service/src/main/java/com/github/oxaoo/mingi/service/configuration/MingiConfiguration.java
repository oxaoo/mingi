package com.github.oxaoo.mingi.service.configuration;

import com.github.oxaoo.mingi.business.logic.core.QasEngine;
import com.github.oxaoo.mingi.business.logic.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.mingi.business.logic.qa.answer.AnswerEngine;
import com.github.oxaoo.mingi.business.logic.qa.question.QuestionClassifier;
import com.github.oxaoo.mingi.business.logic.search.engine.SearchEngine;
import com.github.oxaoo.mingi.business.logic.search.engine.web.WebSearchEngine;
import com.github.oxaoo.mp4ru.exceptions.InitRussianParserException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.utils.RussianParserBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 10.05.2018
 */
@Configuration
public class MingiConfiguration {

    private SourceProperties sourceProperties;

    @Bean
    public QasEngine qasEngineProvider() throws InitRussianParserException, LoadQuestionClassifierModelException {
        final RussianParser parser;
        if (StringUtils.isNotBlank(this.sourceProperties.getParser().getCommonSource())) {
            parser = RussianParserBuilder.build(this.sourceProperties.getParser().getCommonSource());
        } else {
            parser = RussianParserBuilder.build(
                    this.sourceProperties.getParser().getClassifierModelPath(),
                    this.sourceProperties.getParser().getTreeTaggerPath(),
                    this.sourceProperties.getParser().getParserConfigurationPath());
        }
        final QuestionClassifier questionClassifier = new QuestionClassifier(parser, this.sourceProperties.getMingi().getQuestionClassifierModelPath());
        final AnswerEngine answerEngine = new AnswerEngine(parser);
        return new QasEngine(parser, questionClassifier, answerEngine);
    }

    @Bean
    public SearchEngine searchEngineProvider() {
        return new SearchEngine<>(new WebSearchEngine());
    }

    @Autowired
    public void setSourceProperties(final SourceProperties sourceProperties) {
        this.sourceProperties = sourceProperties;
    }
}
