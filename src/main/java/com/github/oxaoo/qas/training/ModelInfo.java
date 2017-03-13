package com.github.oxaoo.qas.training;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 14.03.2017
 */
public class ModelInfo {
    private final int tokenId;
    private final POS pos;
    private final int head;

    public ModelInfo(int tokenId, POS pos, int head) {
        this.tokenId = tokenId;
        this.pos = pos;
        this.head = head;
    }

    //todo check valueOf
    public ModelInfo(int tokenId, String posValue, int head) {
        this.tokenId = tokenId;
        this.pos = POS.valueOf(posValue);
        this.head = head;
    }

    public int getTokenId() {
        return tokenId;
    }

    public POS getPos() {
        return pos;
    }

    public int getHead() {
        return head;
    }

    @Override
    public String toString() {
        return "ModelInfo{" +
                "tokenId=" + tokenId +
                ", pos=" + pos +
                ", head=" + head +
                '}';
    }
}
