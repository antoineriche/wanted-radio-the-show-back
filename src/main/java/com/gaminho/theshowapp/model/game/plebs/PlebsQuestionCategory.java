package com.gaminho.theshowapp.model.game.plebs;

/**
 * Different categories for {@link PlebsQuestion}
 */
public enum PlebsQuestionCategory {
    CRAZY_GIRL("tchoin.png"),
    CHILD("child.png"),
    ANGRY_HUSTLER("hustler.png"),
    INTERESTING("geek.png");

    private final String imgUrl;

    PlebsQuestionCategory(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
