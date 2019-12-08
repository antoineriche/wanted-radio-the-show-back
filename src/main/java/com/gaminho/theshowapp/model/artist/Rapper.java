package com.gaminho.theshowapp.model.artist;

import com.gaminho.theshowapp.model.media.Media;

import javax.persistence.*;

@Entity
public class Rapper {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne(cascade = {CascadeType.ALL})
    private Media beatToPlay;

    @OneToOne(cascade = {CascadeType.ALL})
    protected ArtistDetails artistDetails;

    public Rapper() {
    }

    public Rapper(long id, Media beatToPlay, ArtistDetails artistDetails) {
        this.id = id;
        this.beatToPlay = beatToPlay;
        this.artistDetails = artistDetails;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JoinColumn(name = "media_id")
    public Media getBeatToPlay() {
        return beatToPlay;
    }

    public void setBeatToPlay(Media beatToPlay) {
        this.beatToPlay = beatToPlay;
    }

    @JoinColumn(name = "artist_details_id")
    public ArtistDetails getArtistDetails() {
        return artistDetails;
    }

    public void setArtistDetails(ArtistDetails artistDetails) {
        this.artistDetails = artistDetails;
    }
}
