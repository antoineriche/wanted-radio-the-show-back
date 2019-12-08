package com.gaminho.theshowapp.service;

import com.gaminho.theshowapp.dao.ProjectRepository;
import com.gaminho.theshowapp.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private static Logger log = LoggerFactory.getLogger(ProjectService.class);


    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getAllProjects(){
        List<Project> list = projectRepository.findAll();
        log.debug("getAllProjects -> {}", list.size());
        return list;
    }

    @Transactional
    public Project saveProject(final Project project){
        log.debug("saveProject -> {}", project);
        return projectRepository.save(project);
    }

}
