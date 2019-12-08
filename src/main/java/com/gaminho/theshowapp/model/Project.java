package com.gaminho.theshowapp.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue
    private long id;
    private String name;

    @ElementCollection(targetClass = String.class)
    @JoinTable(name = "project_links", joinColumns = @JoinColumn(name = "id"))
    private List<String> links;
    private Boolean hasBeenReleased;

    public Project() {
    }

    public Project(long id, String name, List<String> links, Boolean hasBeenReleased, Boolean exists) {
        this.id = id;
        this.name = name;
        this.links = links;
        this.hasBeenReleased = hasBeenReleased;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public Boolean getHasBeenReleased() {
        return hasBeenReleased;
    }

    public void setHasBeenReleased(Boolean hasBeenReleased) {
        this.hasBeenReleased = hasBeenReleased;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", links='" + links + '\'' +
                ", hasBeenReleased=" + hasBeenReleased +
                '}';
    }

}
