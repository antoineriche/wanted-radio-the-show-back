package com.gaminho.theshowapp.dao;

import com.gaminho.theshowapp.model.artist.ArtistDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistDetailsRepository extends JpaRepository<ArtistDetails, Long> {

    @Override
    Optional<ArtistDetails> findById(Long aLong);

}
