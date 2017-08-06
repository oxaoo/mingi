package com.github.oxaoo.mingi.business.logic.qa.answer;

import com.github.oxaoo.mp4ru.syntax.RussianParser;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 29.04.2017
 */
@Setter
@Getter
public abstract class AnswerMaker<R, T, D> {
    protected RussianParser parser;

    public abstract List<Callable<R>> toAnswer(List<T> tokens, List<D> data);
}
