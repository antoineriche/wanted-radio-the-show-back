package com.gaminho.theshowapp.dao;

import com.gaminho.theshowapp.model.artist.Rapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RapperRepository extends JpaRepository<Rapper, Long> {

    @Override
    Optional<Rapper> findById(Long aLong);

    @Override
    List<Rapper> findAllById(Iterable<Long> iterable);

}
