package com.gaminho.theshowapp.web.controller;

import com.gaminho.theshowapp.error.PlebsQuestionException;
import com.gaminho.theshowapp.model.game.plebs.PlebsQuestion;
import com.gaminho.theshowapp.model.game.plebs.PlebsQuestionCategory;
import com.gaminho.theshowapp.service.PlebsQuestionService;
import com.gaminho.theshowapp.web.mapper.IPlebsQuestionMapper;
import com.gaminho.theshowapp.web.model.PlebsQuestionAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(value = "*")
public class GameController {

    private static Logger log = LoggerFactory.getLogger(GameController.class);

    //FIXME review the response code cause ut leads to client error

    @Autowired
    private PlebsQuestionService plebsService;

    /**
     * GET /plebs : get all plebs question
     * @return the ResponseEntity with status 200 (OK)
     * and the list of {@link PlebsQuestion}
     * or a message of empty data
     */
    @GetMapping(value = "/games/plebs")
    public ResponseEntity<List<PlebsQuestion>> getAllPlebs(){
        List<PlebsQuestion> list = plebsService.getAllPlebsQuestion();
        list.forEach( p -> log.debug("Pleb: {}", p));
        return ResponseEntity.ok(list);
    }

    /**
     * POST /plebs : create a new plebs question
     * @param questionAPI to save
     * @return ResponseEntity with status 201 (created) with PlebsQuestion
     * as body or a status 400 (Bad request)
     */
    @PostMapping(value = "/games/plebs")
    public ResponseEntity<?> createPlebsQuestion(@RequestBody PlebsQuestionAPI questionAPI){
        log.debug("API: {}", questionAPI);
        PlebsQuestion plebs = IPlebsQuestionMapper.INSTANCE.toPlebsQuestionDTO(questionAPI);

        log.debug("createPQ: {}", plebs);
        plebs.setCreationDate(new Date());

        try {
            PlebsQuestion p2 =
                    plebsService.savePlebsQuestion(plebs);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(p2.getId())
                    .toUri();
            return ResponseEntity.created(location).body(p2);
            } catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/games/plebs/{id}")
    public ResponseEntity<?> deletePlebsQuestion(@PathVariable(value = "id") String id) throws GeneralSecurityException, IOException {
        log.debug("Delete Plebs with id: {}", id);
        try {
            plebsService.deletePlebsQuestion(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (Exception e){
            log.error("Exception while deleting plebs: {}", e.getMessage());
            return ResponseEntity.noContent().build();
        }
    }

//    @GetMapping(value = "/games/allGames")    Get all games, without the ones to avoid
//    @DeleteMapping(value = "/games/plebs/{pleb}") Delete one question
}
