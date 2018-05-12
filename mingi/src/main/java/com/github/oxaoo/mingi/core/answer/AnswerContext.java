package com.github.oxaoo.mingi.core.answer;

import com.github.oxaoo.mp4ru.syntax.RussianParser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerContext<R, T, D> {
    private RussianParser parser;
    private AnswerMaker<R, T, D> answerMaker;

    public List<Callable<R>> runAnswerMaker(List<T> questionTokens, List<D> dataFragments) {
        this.answerMaker.setParser(this.parser);
        return this.answerMaker.toAnswer(questionTokens, dataFragments);
    }
}
