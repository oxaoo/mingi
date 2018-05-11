package com.github.oxaoo.mingi.business.logic.training;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.mingi.business.logic.exceptions.FailedQuestionTokenMapException;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 14.03.2017
 */
public class QuestionToken {
    private final int tokenId;
    private final PosType pos;
    private final int head;

    public QuestionToken(int tokenId, PosType pos, int head) {
        this.tokenId = tokenId;
        this.pos = pos;
        this.head = head;
    }

    //todo check valueOf
    public QuestionToken(int tokenId, String posValue, int head) {
        this.tokenId = tokenId;
        this.pos = PosType.valueOf(posValue);
        this.head = head;
    }

    public QuestionToken(int tokenId, int posId, int head) {
        this.tokenId = tokenId;
        this.pos = PosType.values[posId - 1];
        this.head = head;
    }

    public static QuestionToken map(Conll conll) throws FailedQuestionTokenMapException {
        if (conll == null) {
            throw new FailedQuestionTokenMapException("Null conll instance.");
        }

        int id = conll.getId();
        char posChar = conll.getPosTag();
        int head = conll.getHead();
        PosType pos;
        //feature
        if (posChar == '-') {
            pos = PosType.UND;
        } else {
            try {
                pos = PosType.valueOf(String.valueOf(posChar).toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new FailedQuestionTokenMapException("Invalid POS tag.", e);
            }
        }

        return new QuestionToken(id, pos, head);
    }

    public int getTokenId() {
        return tokenId;
    }

    public PosType getPos() {
        return pos;
    }

    public int getHead() {
        return head;
    }

    @Override
    public String toString() {
        return "QuestionToken{" +
                "tokenId=" + tokenId +
                ", pos=" + pos +
                ", head=" + head +
                '}';
    }
}
