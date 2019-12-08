package com.gaminho.theshowapp.error;

import org.apache.commons.lang.StringUtils;

public class PlebsQuestionException extends RuntimeException {

    public PlebsQuestionException(String ... messages) {
        super(StringUtils.join(messages, "-"));
    }
}
