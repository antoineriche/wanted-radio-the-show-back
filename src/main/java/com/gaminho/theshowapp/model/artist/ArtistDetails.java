package com.gaminho.theshowapp.model.artist;

import com.gaminho.theshowapp.model.Project;
import com.gaminho.theshowapp.model.game.GameInfo;
import com.gaminho.theshowapp.model.media.Media;

import javax.persistence.*;

//FIXME : end doing the entity
@Entity
public class ArtistDetails {

    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    private ArtistType artistType;

    @Column(unique=true)
    protected String artistName;

    @OneToOne(cascade = {CascadeType.ALL})
    protected Project project;

    @OneToOne(cascade = {CascadeType.ALL})
    protected Media favoriteSong;

    @OneToOne(cascade = {CascadeType.ALL})
    protected Media songToPlay;

    @OneToOne(cascade = {CascadeType.ALL})
    protected GameInfo gameInfo;
    protected String toDiscuss;

    public ArtistDetails() {
    }

    public ArtistDetails(long id, ArtistType artistType, String artistName, Project project, Media favoriteSong, Media songToPlay, GameInfo gameInfo, String toDiscuss) {
        this.id = id;
        this.artistType = artistType;
        this.artistName = artistName;
        this.project = project;
        this.favoriteSong = favoriteSong;
        this.songToPlay = songToPlay;
        this.gameInfo = gameInfo;
        this.toDiscuss = toDiscuss;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String mArtistName) {
        this.artistName = mArtistName;
    }


    public ArtistType getArtistType() {
        return artistType;
    }


    public void setArtistType(ArtistType artistType){
        this.artistType = artistType;
    }

    public String getToDiscuss() {
        return toDiscuss;
    }

    public void setToDiscuss(String toDiscuss) {
        this.toDiscuss = toDiscuss;
    }

    @JoinColumn(name = "project_id")
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @JoinColumn(name = "media_id")
    public Media getFavoriteSong() {
        return favoriteSong;
    }

    public void setFavoriteSong(Media favoriteSong) {
        this.favoriteSong = favoriteSong;
    }

    @JoinColumn(name = "media_id")
    public Media getSongToPlay() {
        return songToPlay;
    }

    public void setSongToPlay(Media songToPlay){
        this.songToPlay = songToPlay;
    }

    @JoinColumn(name = "game_info_id")
    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }
}
