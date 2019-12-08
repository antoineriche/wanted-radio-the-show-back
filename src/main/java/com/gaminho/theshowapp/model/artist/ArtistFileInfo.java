package com.gaminho.theshowapp.model.artist;

public class ArtistFileInfo {

    private String fileId;
    private String artistName;
    private ArtistType artistType;

    public ArtistFileInfo(String fileId, String artistName, ArtistType artistType) {
        this.fileId = fileId;
        this.artistName = artistName;
        this.artistType = artistType;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public ArtistType getArtistType() {
        return artistType;
    }

    public void setArtistType(ArtistType artistType) {
        this.artistType = artistType;
    }

    @Override
    public String toString() {
        return "ArtistFileInfo{" +
                "fileId='" + fileId + '\'' +
                ", artistName='" + artistName + '\'' +
                ", artistType=" + artistType +
                '}';
    }
}
