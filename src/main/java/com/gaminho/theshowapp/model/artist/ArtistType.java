package com.gaminho.theshowapp.model.artist;

public enum ArtistType {
    RAPPER("RAP"),
    BEATBOXER("BBX");

    private String symbol;

    ArtistType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
