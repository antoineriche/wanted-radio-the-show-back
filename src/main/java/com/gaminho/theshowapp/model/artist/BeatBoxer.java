package com.gaminho.theshowapp.model.artist;

import com.gaminho.theshowapp.model.media.Media;

import javax.persistence.*;

@Entity
public class BeatBoxer {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne(cascade = {CascadeType.ALL})
    protected ArtistDetails artistDetails;

    public BeatBoxer() {
    }

    public BeatBoxer(long id, Media beatToPlay, ArtistDetails artistDetails) {
        this.id = id;
        this.artistDetails = artistDetails;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JoinColumn(name = "artist_details_id")
    public ArtistDetails getArtistDetails() {
        return artistDetails;
    }

    public void setArtistDetails(ArtistDetails artistDetails) {
        this.artistDetails = artistDetails;
    }
}
