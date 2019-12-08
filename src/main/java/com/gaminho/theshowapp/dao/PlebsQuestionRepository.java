package com.gaminho.theshowapp.dao;

import com.gaminho.theshowapp.model.game.plebs.PlebsQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlebsQuestionRepository extends JpaRepository<PlebsQuestion, Long> {

}
