package com.gaminho.theshowapp.model.media;

import javax.persistence.*;

@Entity
public class Media {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String streamingURL;
    private String pathToFile;
    private String googleDriveId;
    private String googleDriveURL;

    public Media() {
    }

    public Media(long id, String pathToFile, String name, String googleDriveId) {
        this.id = id;
        this.pathToFile = pathToFile;
        this.name = name;
        this.googleDriveId = googleDriveId;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public String getGoogleDriveId() {
        return googleDriveId;
    }

    public void setGoogleDriveId(String googleDriveId) {
        this.googleDriveId = googleDriveId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreamingURL() {
        return streamingURL;
    }

    public void setStreamingURL(String streamingURL) {
        this.streamingURL = streamingURL;
    }

    public String getGoogleDriveURL() {
        return googleDriveURL;
    }

    public void setGoogleDriveURL(String googleDriveURL) {
        this.googleDriveURL = googleDriveURL;
    }

    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", streamingURL='" + streamingURL + '\'' +
                ", pathToFile='" + pathToFile + '\'' +
                ", googleDriveId='" + googleDriveId + '\'' +
                ", googleDriveURL='" + googleDriveURL + '\'' +
                '}';
    }
}
