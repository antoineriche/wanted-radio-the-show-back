package com.gaminho.theshowapp.web.mapper;

import com.gaminho.theshowapp.error.PlebsQuestionException;
import com.gaminho.theshowapp.model.game.plebs.PlebsQuestion;
import com.gaminho.theshowapp.model.game.plebs.PlebsQuestionCategory;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

;

public class PlebsQuestionMapper {

    private static Logger log = LoggerFactory.getLogger(PlebsQuestionMapper.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static PlebsQuestion toPlebsQuestion(JSONObject json) throws PlebsQuestionException {

        String uuid = json.has("id") ? json.getString("id") : "";

        String question = json.has("question") ? json.getString("question") : "";
        long id = -1L;

        Date creationDate = new Date(0);
        try {
            id = Long.parseLong(uuid);
            creationDate = json.has("creation") ?
                    SDF.parse(json.getString("creation")) : new Date(0);
        } catch (ParseException | NumberFormatException e) {
            log.error("Could not parse date: '{}'", json.getString("creation"));
        }

        final String category = (json.has("category") ?
                json.getString("category") : "")
                .toUpperCase();

        Optional<PlebsQuestionCategory> optCategory =
                Arrays.stream(PlebsQuestionCategory.values())
                .filter(cat -> cat.name().equalsIgnoreCase(category))
                .findFirst();

        PlebsQuestion plebsQuestion = new PlebsQuestion(id, question, creationDate,
                optCategory.orElse(null));

        PlebsQuestion.validate(plebsQuestion);
        return plebsQuestion;

    }
}
