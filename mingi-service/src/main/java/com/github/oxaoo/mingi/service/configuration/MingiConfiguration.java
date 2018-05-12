package com.github.oxaoo.mingi.service.configuration;

import com.github.oxaoo.mingi.common.PropertyKeeper;
import com.github.oxaoo.mingi.core.QasEngine;
import com.github.oxaoo.mingi.core.answer.AnswerEngine;
import com.github.oxaoo.mingi.core.question.QuestionClassifier;
import com.github.oxaoo.mingi.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.mingi.search.engine.SearchEngine;
import com.github.oxaoo.mingi.search.engine.web.WebSearchEngine;
import com.github.oxaoo.mp4ru.exceptions.InitRussianParserException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.utils.RussianParserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 10.05.2018
 */
@Configuration
public class MingiConfiguration {

    private ServiceProperties properties;

    @PostConstruct
    public void initQasEngine() {
        PropertyKeeper.put(PropertyKeeper.GOOGLE_CSE_ID_KEY, this.properties.getSearch().getId());
        PropertyKeeper.put(PropertyKeeper.GOOGLE_API_KEY_KEY, this.properties.getSearch().getKey());
        if (this.properties.getProxy().isEnable()) {
            PropertyKeeper.put(PropertyKeeper.PROXY_HOST_KEY, this.properties.getProxy().getHost());
            PropertyKeeper.put(PropertyKeeper.PROXY_PORT_KEY, this.properties.getProxy().getPort());
        }
    }

    @Bean
    public QasEngine qasEngineProvider() throws InitRussianParserException, LoadQuestionClassifierModelException {
        final RussianParser parser = RussianParserBuilder.build(this.properties.getHome().getParser());
        final QuestionClassifier questionClassifier = new QuestionClassifier(parser, this.properties.getHome().getMingi());
        final AnswerEngine answerEngine = new AnswerEngine(parser);
        return new QasEngine(parser, questionClassifier, answerEngine);
    }

    @Bean
    public SearchEngine searchEngineProvider() {
        return new SearchEngine<>(new WebSearchEngine());
    }

    @Autowired
    public void setProperties(final ServiceProperties properties) {
        this.properties = properties;
    }
}
