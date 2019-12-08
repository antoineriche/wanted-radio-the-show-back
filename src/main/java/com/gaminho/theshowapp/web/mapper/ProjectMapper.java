package com.gaminho.theshowapp.web.mapper;

import com.gaminho.theshowapp.model.Project;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ProjectMapper {

    public static Project toProject(JSONObject json){
        Project project = new Project();

        boolean exists = json.has("has_a_project") && json.getBoolean("has_a_project");

        if(exists){
            project.setLinks(json.has("project_links") ?
                    Arrays.asList(json.getString("project_links"))
                    : new ArrayList<>());
            project.setName(json.has("project_title") ? json.getString("project_title") : "");
            project.setHasBeenReleased(json.has("released") && json.getBoolean("released"));
        } else {
            project.setLinks(new ArrayList<>());
            project.setName("");
            project.setHasBeenReleased(false);
        }

        return project;
    }
}
