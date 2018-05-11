package com.github.oxaoo.mingi.business.logic;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.github.oxaoo.mingi.business.logic.common.PropertyKeeper;
import com.github.oxaoo.mingi.business.logic.core.QasEngine;
import com.github.oxaoo.mingi.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.mingi.business.logic.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.mingi.business.logic.qa.answer.AnswerEngine;
import com.github.oxaoo.mingi.business.logic.qa.question.QuestionClassifier;
import com.github.oxaoo.mingi.business.logic.search.engine.SearchEngine;
import com.github.oxaoo.mingi.business.logic.search.engine.web.WebSearchEngine;
import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.exceptions.InitRussianParserException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.utils.RussianParserBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(com.github.oxaoo.mp4ru.Main.class);

    //todo fix description
    @Parameter(
            names = {"-cps", "--commonParserSource"},
            description = "Represents the parser common data source. "
                    + "It is possible to use instead of 'classifierModel', 'treeTagger' and 'parserConfig' "
                    + "if in this place there are all necessary resources")
    public String commonParserSource;

    @Parameter(
            names = {"-mh", "--mingiHome"},
            description = "The path to the Mingi home directory with data source")
    public String mingiHome;

    @Parameter(
            names = {"-p", "--proxy"},
            description = "The proxy configuration in format host:port")
    public String proxy;

    @Parameter(
            names = {"-ci", "--customSearchId"},
            description = "The Google custom search id")
    public String cseId;

    @Parameter(
            names = {"-ck", "--customSearchKey"},
            description = "The Google custom search API key")
    public String cseKey;

    @Parameter(
            names = {"-h", "--help"},
            description = "Information on use of the mp4ru",
            help = true)
    public boolean help = false;

    public static void main(String[] args) throws FailedParsingException, InitRussianParserException,
            FailedQuestionTokenMapException, FailedConllMapException, LoadQuestionClassifierModelException {
        final Main app = new Main();
        final JCommander jcmd = new JCommander(app, args);
        if (app.help) {
            jcmd.usage();
        } else {
            app.run();
        }
    }

    private void run() throws InitRussianParserException, LoadQuestionClassifierModelException,
            FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        if (this.commonParserSource == null || this.mingiHome == null
                || this.cseId == null || this.cseKey == null) {
            LOG.error("Missing required parameters.");
            return;
        }
        this.initProperties();

        final RussianParser parser = RussianParserBuilder.build(this.commonParserSource);
        final QuestionClassifier questionClassifier = new QuestionClassifier(parser, this.mingiHome);
        final AnswerEngine answerEngine = new AnswerEngine(parser);
        final QasEngine qasEngine = new QasEngine(parser, questionClassifier, answerEngine);

//        Set<String> answers = qasEngine.answer("В каком году затонул Титаник?", new SearchEngine<>(new WebSearchEngine())); //date
//        Set<String> answers = qasEngine.answer("Какая аббревиатура у тринитротолуол?", new SearchEngine<>(new WebSearchEngine())); //abbreviation
//        Set<String> answers = qasEngine.answer("Что значит слово лол?", new SearchEngine<>(new WebSearchEngine())); //abbreviation
//        Set<String> answers = qasEngine.answer("Кто был первым в космосе?", new SearchEngine<>(new WebSearchEngine())); //ind
        Set<String> answers = qasEngine.answer("В каком регионе России находится крупнейший буддистский храм?", new SearchEngine<>(new WebSearchEngine())); //state
//        Set<String> answers = qasEngine.answer("В каком году Медведев стал презедентом?");
//        Set<String> answers = qasEngine.answer("Какое событие произошло в Санкт-Петербурге третьего апреля?", new SearchEngine<>(new WebSearchEngine())); //event
        LOG.info("List of answers:");
        answers.forEach(LOG::info);
        qasEngine.shutdown();
    }

    private void initProperties() {
        PropertyKeeper.put(PropertyKeeper.GOOGLE_CSE_ID_KEY, this.cseId);
        PropertyKeeper.put(PropertyKeeper.GOOGLE_API_KEY_KEY, this.cseKey);
        if (this.proxy != null && this.proxy.contains(":")) {
            final String proxyInfo[] = this.proxy.split(":");
            PropertyKeeper.put(PropertyKeeper.PROXY_HOST_KEY, proxyInfo[0]);
            PropertyKeeper.put(PropertyKeeper.PROXY_PORT_KEY, proxyInfo[1]);
        }
    }
}
