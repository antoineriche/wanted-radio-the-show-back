package com.gaminho.theshowapp.dao;

import com.gaminho.theshowapp.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
