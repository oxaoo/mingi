package com.github.oxaoo.qas.core;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.qa.QuestionClassifier;
import com.github.oxaoo.qas.qa.QuestionDomain;
import com.github.oxaoo.qas.search.DataFragment;
import com.github.oxaoo.qas.search.PageExtractorTest;
import com.github.oxaoo.qas.search.RelevantInfo;
import com.github.oxaoo.qas.search.SearchFacade;
import com.github.oxaoo.qas.training.QuestionModel;
import com.github.oxaoo.qas.training.QuestionToken;
import com.github.oxaoo.qas.utils.JsonBuilder;
import com.github.oxaoo.qas.utils.PropertyManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 26.03.2017
 */
@RunWith(MockitoJUnitRunner.class)
public class QasEngineTest {
    private static final Logger LOG = LoggerFactory.getLogger(QasEngineTest.class);

    @InjectMocks
    private QasEngine qasEngine;
    @Mock
    private QuestionClassifier questionClassifier;
    @Mock
    private SearchFacade searchFacade;

    private static RussianParser parser;

    private final static String PARSER_CLASSIFIER_MODEL_PROPERTY = "parser.classifier.model.path";
    private final static String PARSER_CONFIG_PATH_PROPERTY = "parser.config.path";
    private final static String PARSER_TREE_TAGGER_HOME_PROPERTY = "parser.tree.tagger.path";

    @BeforeClass
    public static void setUp() {
        Properties properties = PropertyManager.getProperties();
        String classifierModelPath = properties.getProperty(PARSER_CLASSIFIER_MODEL_PROPERTY);
        String treeTaggerHome = properties.getProperty(PARSER_TREE_TAGGER_HOME_PROPERTY);
        String parserConfigPath = properties.getProperty(PARSER_CONFIG_PATH_PROPERTY);
        parser = new RussianParser(classifierModelPath, treeTaggerHome, parserConfigPath);

    }

    @Test
    public void answerTest() throws FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        String question = "В каком году затонул Титаник?";
        QuestionDomain questionDomain = QuestionDomain.DATE;
        DataFragment dataFragment = this.prepareDataFragment();
        List<DataFragment> singleDataFragments = Collections.singletonList(dataFragment);
        Mockito.when(this.questionClassifier.classify(question)).thenReturn(questionDomain);
        Mockito.when(this.searchFacade.collectInfo(question)).thenReturn(singleDataFragments);

        this.qasEngine.answer(question);

        List<Conll> parsedQuestion = this.parseQuestion(question);
        LOG.info("Pretty question view: {}", JsonBuilder.toJson(parsedQuestion));
        for (RelevantInfo ri : singleDataFragments.get(0).getRelevantInfoList()) {
            for (String sentence : ri.getRelevantSentences()) {
                List<String> tokens = parser.parse(sentence);
                List<Conll> sentencesTokens = new ArrayList<>();
                for (String token : tokens) {
                    Conll conll = Conll.map(token);
                    sentencesTokens.add(conll);
                }
                LOG.info("Question: {}", sentence);
                LOG.info("Question tokens: {}", JsonBuilder.toJson(sentencesTokens));
            }
            LOG.info("\n");
        }

//        List<DataFragment> dataFragments = this.searchFacade.collectInfo(question);
    }

    private List<Conll> parseQuestion(String question)
            throws FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        List<String> parsedTokens = parser.parse(question);
        List<Conll> sentencesTokens = new ArrayList<>();
        for (String token : parsedTokens) {
            Conll conll = Conll.map(token);
            sentencesTokens.add(conll);
        }
        return sentencesTokens;
    }

    private DataFragment prepareDataFragment() {
        DataFragment dataFragment = new DataFragment();
        dataFragment.setLink("https://ru.wikipedia.org/wiki/%D0%A2%D0%B8%D1%82%D0%B0%D0%BD%D0%B8%D0%BA");
        RelevantInfo relevantInfo = new RelevantInfo();
        relevantInfo.setSnippet("«Тита́ник» (англ. Titanic) — британский трансатлантический пароход, второй лайнер класса «Олимпик». Строился в Белфасте на верфи «Харленд энд Вулф» с 1909 по 1912 год");
        List<String> relevantInfoSentences = new ArrayList<>();
        relevantInfoSentences.add("Состояние отпатрулирована Перейти к: навигация, поиск Титаник Titanic «Титаник» выходит из Саутгемптона в первый и последний рейс 10 апреля 1912 года Флаг Великобритания Великобритания Назван в честь титаны Класс и тип судна Пассажирское судно класса «Олимпик» Порт приписки Ливерпуль Позывной MGY Организация «International Mercantile Marine Company» Оператор «Уайт Стар Лайн» Изготовитель «Харленд энд Вулф» Заказан к постройке 17 сентября 1908 Строительство начато 31 марта 1909 Спущен на воду 31 мая 1911 Введён в эксплуатацию 2 апреля 1912[комм. 1] Статус затонул Основные характеристики Водоизмещение 52 310 т Длина 269,1 м[комм. 2][1] Ширина 28,19 м[2] Высота 18,5 м (от ватерлинии до шлюпочной палубы[комм. 3]) Осадка 10,54 м[2] Двигатели две четырёхцилиндровые паровые машины тройного расширения и паровая турбина Мощность 55 тыс. л. с. ");
        relevantInfoSentences.add("Строился в Белфасте на верфи «Харленд энд Вулф» с 1909 по 1912 год по заказу судоходной компании «Уайт Стар Лайн». ");
        relevantInfoSentences.add("Titanic) — британский трансатлантический пароход, второй лайнер класса «Олимпик». ");
        relevantInfo.setRelevantSentences(relevantInfoSentences);

        RelevantInfo relevantInfo1 = new RelevantInfo();
        relevantInfo1.setSnippet("В 2:20 15 апреля, разломившись на две части, «Титаник» затонул, унеся жизни 1496 человек. 712 спасшихся человек");
        List<String> relevantInfoSentences1 = new ArrayList<>();
        relevantInfoSentences1.add("В 2:20 15 апреля, разломившись на две части, «Титаник» затонул, унеся жизни 1496 человек[10]. 712 спасшихся человек подобрал пароход «Карпатия»[11]. ");
        relevantInfoSentences1.add("Состояние отпатрулирована Перейти к: навигация, поиск Титаник Titanic «Титаник» выходит из Саутгемптона в первый и последний рейс 10 апреля 1912 года Флаг Великобритания Великобритания Назван в честь титаны Класс и тип судна Пассажирское судно класса «Олимпик» Порт приписки Ливерпуль Позывной MGY Организация «International Mercantile Marine Company» Оператор «Уайт Стар Лайн» Изготовитель «Харленд энд Вулф» Заказан к постройке 17 сентября 1908 Строительство начато 31 марта 1909 Спущен на воду 31 мая 1911 Введён в эксплуатацию 2 апреля 1912[комм. 1] Статус затонул Основные характеристики Водоизмещение 52 310 т Длина 269,1 м[комм. 2][1] Ширина 28,19 м[2] Высота 18,5 м (от ватерлинии до шлюпочной палубы[комм. 3]) Осадка 10,54 м[2] Двигатели две четырёхцилиндровые паровые машины тройного расширения и паровая турбина Мощность 55 тыс. л. с. ");
        relevantInfoSentences1.add("В соответствии с устаревшими правилами «Титаник» был оснащён 20 спасательными шлюпками, суммарной вместимостью 1178 человек, что составляло лишь треть от максимальной загрузки парохода[5]. ");
        relevantInfo1.setRelevantSentences(relevantInfoSentences1);

        List<RelevantInfo> relevantInfos = new ArrayList<>();
        relevantInfos.add(relevantInfo);
        relevantInfos.add(relevantInfo1);
        dataFragment.setRelevantInfoList(relevantInfos);
        return dataFragment;
    }
}
