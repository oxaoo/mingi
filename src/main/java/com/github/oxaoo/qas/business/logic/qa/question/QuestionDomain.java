package com.github.oxaoo.qas.business.logic.qa.question;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @see <a href="http://cogcomp.cs.illinois.edu/Data/QA/QC/definition.html">Definition of Question Classes</a>
 * @since 14.03.2017
 */
public enum QuestionDomain {
    //ABBREVIATION
    ABB,
    EXP,
    //ENTITY
    ANIMAL,
    BODY,
    COLOR,
    CREATIVE,
    CURRENCY,
    DIS_MED,
    EVENT,
    FOOD,
    INSTRUMENT,
    LANG,
    LETTER,
    OTHER_ENTITY,
    PLANT,
    PRODUCT,
    RELIGION,
    SPORT,
    SUBSTANCE,
    SYMBOL,
    TECHNIQUE,
    TERM,
    VEHICLE,
    WORD,
    //DESCRIPTION
    DEFINITION,
    DESCRIPTION,
    MANNER,
    REASON,
    //HUMAN
    GROUP,
    IND,
    TITLE,
    DESCRIPTION_HUMAN,
    //LOCATION
    CITY,
    COUNTRY,
    MOUNTAIN,
    OTHER_NUMERIC,
    STATE,
    //NUMERIC
    CODE,
    COUNT,
    DATE,
    DISTANCE,
    MONEY,
    ORDER,
    OTHER,
    PERIOD,
    PERCENT,
    SPEED,
    TEMP,
    SIZE,
    WEIGHT;

    public static final QuestionDomain values[] = values();
}
