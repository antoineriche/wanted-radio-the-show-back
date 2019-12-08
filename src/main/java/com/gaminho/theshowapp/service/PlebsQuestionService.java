package com.gaminho.theshowapp.service;

import com.gaminho.theshowapp.dao.PlebsQuestionRepository;
import com.gaminho.theshowapp.error.PlebsQuestionException;
import com.gaminho.theshowapp.model.game.plebs.PlebsQuestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PlebsQuestionService {

    private static Logger log = LoggerFactory.getLogger(PlebsQuestionService.class);


    @Autowired
    private PlebsQuestionRepository plebsRepository;


    public List<PlebsQuestion> getAllPlebsQuestion(){
        List<PlebsQuestion> plebs = plebsRepository.findAll();
        log.debug("getAllPlebsQuestion");
        return plebs;
    }

    @Transactional
    public PlebsQuestion savePlebsQuestion(PlebsQuestion plebs){
        try {
            PlebsQuestion.validate(plebs);
            return plebsRepository.save(plebs);
        } catch (PlebsQuestionException e) {
            throw new PlebsQuestionException(e.getMessage());
        }
    }

    @Transactional
    public void deletePlebsQuestion(long id){
        Optional<PlebsQuestion> oldPlebs = plebsRepository.findById(id);
        if(oldPlebs.isPresent()) {
            log.debug("plebs with id '{}' has been found and deleted.", id);
            plebsRepository.deleteById(id);
        }
    }
}
