package com.gaminho.theshowapp.web.model;

public class PlebsQuestionAPI {

    private String question;
    private String creation;
    private String category;

    public PlebsQuestionAPI(String question, String creation, String category) {
        this.question = question;
        this.creation = creation;
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public String getCreation() {
        return creation;
    }

    public String getCategory() {
        return category;
    }
}
