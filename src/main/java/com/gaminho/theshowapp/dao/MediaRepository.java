package com.gaminho.theshowapp.dao;

import com.gaminho.theshowapp.model.media.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
}
