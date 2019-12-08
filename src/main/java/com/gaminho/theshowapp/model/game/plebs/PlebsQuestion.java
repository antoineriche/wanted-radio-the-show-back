package com.gaminho.theshowapp.model.game.plebs;

import com.gaminho.theshowapp.error.PlebsQuestionException;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Representation of plebs question
 */
@Entity
@Table(indexes =
        { @Index(columnList="question",name="plebs_question") })
public class PlebsQuestion {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique=true)
    private String question;
    private Date creationDate;
    @Enumerated(EnumType.STRING)
    private PlebsQuestionCategory category;

    public PlebsQuestion() {
    }

    public PlebsQuestion(long id, String question, Date creationDate, PlebsQuestionCategory category) {
        this.id = id;
        this.question = question;
        this.creationDate = creationDate;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public PlebsQuestionCategory getCategory() {
        return category;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setCategory(PlebsQuestionCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "PlebsQuestion{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", creationDate=" + creationDate +
                ", category=" + category +
                '}';
    }

    /**
     * Validate the given {@link PlebsQuestion} and ensure that fields are not null.
     * @param plebsQuestion the given plebs question to validate
     * @throws PlebsQuestionException if question, category or date is blank or null
     * also if creation date is 0-date.
     */
    public static void validate(PlebsQuestion plebsQuestion) throws PlebsQuestionException {
        List<String> errors = new ArrayList<>();
        if (StringUtils.isBlank(plebsQuestion.getQuestion())){
            errors.add("question can not be null");
        } else if (plebsQuestion.getCategory() == null){
            errors.add("category can not be null");
        } else if (plebsQuestion.getCreationDate() == null){
            errors.add("date can not be null");
        } else if (plebsQuestion.getCreationDate().equals(new Date(0))){
            errors.add("date can not be 0");
        }

        if(!errors.isEmpty()){
            throw new PlebsQuestionException(errors.toArray(new String[0]));
        }
    }
}
