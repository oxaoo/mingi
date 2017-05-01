package com.github.oxaoo.qas.business.logic.core;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.mp4ru.syntax.tokenize.SimpleTokenizer;
import com.github.oxaoo.qas.business.logic.core.auxiliary.WebSearchEngineStub;
import com.github.oxaoo.qas.business.logic.core.auxiliary.WebSearchStubResultProvider;
import com.github.oxaoo.qas.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.business.logic.exceptions.InitQasEngineException;
import com.github.oxaoo.qas.business.logic.exceptions.ProvideParserException;
import com.github.oxaoo.qas.business.logic.parse.ParserManager;
import com.github.oxaoo.qas.business.logic.qa.answer.AnswerEngine;
import com.github.oxaoo.qas.business.logic.qa.question.QuestionClassifier;
import com.github.oxaoo.qas.business.logic.qa.question.QuestionDomain;
import com.github.oxaoo.qas.business.logic.search.data.DataFragment;
import com.github.oxaoo.qas.business.logic.search.data.RelevantInfo;
import com.github.oxaoo.qas.business.logic.search.engine.SearchEngine;
import com.github.oxaoo.qas.business.logic.search.engine.web.WebSearchUnit;
import com.github.oxaoo.qas.business.logic.common.JsonBuilder;
import com.google.api.services.customsearch.model.Result;
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
import java.util.Set;

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
    private SearchEngine searchEngine;
    @Mock
    private AnswerEngine answerEngine;
    @Mock
    private static RussianParser parser;

    @BeforeClass
    public static void setUp() throws ProvideParserException {
        parser = ParserManager.getParser();
    }

    @Test
    public void answerParseTest()
            throws FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        String question = "Что такое титан?";
        List<Conll> questionTokens = this.parseQuestion(question);
        LOG.info("*** QUESTION ***");
        LOG.info(JsonBuilder.toJson(questionTokens));

        String answer = "Титан это элемент четвёртого периода периодической системы химических элементов";
        List<Conll> answerTokens = this.parseQuestion(answer);
        LOG.info("*** ANSWER ***");
        LOG.info(JsonBuilder.toJson(answerTokens));
    }

    @Test
    public void listQuestions() throws FailedParsingException {
        String[] questions = {
                "Что такое ферменты печени?",
                "Что такое этология?",
                "Что такое титан?",
                "Что такое кальдера?",
                "Что такое точка росы?",
                "В чем смысл Иисуса?",
                "Что означает слово Хойя?",
                "Что такое транзистор?",
                "Что такое гомофобия?"
        };
        for (String question : questions) {
            List<Conll> questionTokens = this.parseQuestion(question);
            LOG.info("==> Question: {}", question);
            LOG.info(JsonBuilder.toJson(questionTokens));
        }
    }


    @Test
    public void answerTest() throws FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException {
        String question = "В каком году затонул Титаник?";
        List<Conll> questionTokens = parser.parse(question, Conll.class);
//        List<Conll> question = Collections.emptyList();
        QuestionDomain questionDomain = QuestionDomain.DATE;
        DataFragment dataFragment = this.prepareDataFragment();
        List<DataFragment> singleDataFragments = Collections.singletonList(dataFragment);
        Mockito.when(this.questionClassifier.classify(questionTokens)).thenReturn(questionDomain);
        Mockito.when(this.searchEngine.collectInfo(question)).thenReturn(singleDataFragments);

        this.qasEngine.answer(question, true);

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

//        List<DataFragment> dataFragments = this.searchEngine.collectInfo(question);
    }

    private List<Conll> parseQuestion(String question) throws FailedParsingException {
        return parser.parse(question, Conll.class);
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


    @Test
    public void test() throws InitQasEngineException, FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        QasEngine qasEngine = new QasEngine();
        String question = "Что такое титан?";
        Set<String> answers = qasEngine.answer(question, true);
        LOG.info("List of answers:");
        answers.forEach(LOG::info);
        qasEngine.shutdown();
    }

    @Test
    public void failedParse() throws FailedParsingException {
        String str = "   Туризм и активный отдых Туры из Волгограда Регионы Города Отчеты Поиск отелей ЖД билеты Авиабилеты \uFEFF Элиста ДостопримечательностиКак добратьсяКартаОтчеты о поездкахТуры и экскурсииРоссия Регионы и областиВолгоградскаяКалмыкияАдыгеяАстраханскаяГорода и селаВолгоградВолжскийЭлистаАстраханьГеленджикАнапаНовороссийскДостопримечательности Полезные сервисы Поиск достопримечательностейПоиск и бронирование отелей Поиск ЖД билетов Поиск авиабилетов \uFEFF Поиск по сайту Популярные туры Элиста из Волгограда Баскунчак из Волгограда Геленджик из Волгограда \uFEFFРоссия \\ Элиста \\ Достопримечательности Буддийский храм (Элиста), центральный хурул «Золотая обитель Будды Шакьямуни» Координаты: N46 18.558 E44 17.034. ";
        List<String> str2 = parser.parse(str);
        LOG.info("STR2:");
        str2.forEach(LOG::info);
//        String fstr = str.replaceAll("[—«»\"`‚„‘’“”%;:\\p{Z}]+", "");
//        LOG.info("Filtering string: {}", fstr);
    }

    @Test
    public void failedParse2() throws FailedParsingException {
        String str = "   Туризм и активный отдых Туры из Волгограда Регионы Города Отчеты Поиск отелей ЖД билеты Авиабилеты \uFEFF Элиста ДостопримечательностиКак добратьсяКартаОтчеты о поездкахТуры и экскурсииРоссия Регионы и областиВолгоградскаяКалмыкияАдыгеяАстраханскаяГорода и селаВолгоградВолжскийЭлистаАстраханьГеленджикАнапаНовороссийскДостопримечательности Полезные сервисы Поиск достопримечательностейПоиск и бронирование отелей Поиск ЖД билетов Поиск авиабилетов \uFEFF Поиск по сайту Популярные туры Элиста из Волгограда Баскунчак из Волгограда Геленджик из Волгограда \uFEFFРоссия \\ Элиста \\ Достопримечательности Буддийский храм (Элиста), центральный хурул «Золотая обитель Будды Шакьямуни» Координаты: N46 18.558 E44 17.034. ";

        SimpleTokenizer tokenizer = new SimpleTokenizer();
        List<String> tokens = tokenizer.tokenization(str);
        LOG.info("Tokens: {}", tokens);
    }


    @Test
    public void answerWithFinderStubTest() throws InitQasEngineException,
            FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException {
        SearchEngine<List<Result>, List<WebSearchUnit>> searchEngine = new SearchEngine<>(new WebSearchEngineStub());
        QasEngine qasEngine = new QasEngine();
        String question = "В каком году затонул Титаник?";
        this.provideResult();
        Set<String> answers = qasEngine.answer(question, searchEngine);
        answers.forEach(LOG::info);
    }

    private void provideResult() {
        String link = "https://ru.wikipedia.org/wiki/%D0%A2%D0%B8%D1%82%D0%B0%D0%BD%D0%B8%D0%BA";
        String snippet = "«Тита́ник» (англ. Titanic) — британский трансатлантический пароход, второй лайнер класса «Олимпик»."
                + " Строился в Белфасте на верфи «Харленд энд Вулф» с 1909 по 1912 год\n"
                + "В 2:20 15 апреля, разломившись на две части, «Титаник» затонул, унеся жизни 1496 человек."
                + " 712 спасшихся человек";

        Result result = new Result()
                .setLink(link)
                .setSnippet(snippet);
        List<Result> results = Collections.singletonList(result);
        WebSearchStubResultProvider.add(results);
    }
}
