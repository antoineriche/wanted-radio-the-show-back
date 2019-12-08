package com.gaminho.theshowapp.web.mapper;

import com.gaminho.theshowapp.model.artist.ArtistDetails;
import com.gaminho.theshowapp.model.artist.ArtistType;
import com.gaminho.theshowapp.model.artist.BeatBoxer;
import com.gaminho.theshowapp.model.artist.Rapper;
import com.gaminho.theshowapp.model.media.MediaType;
import com.gaminho.theshowapp.utils.Utils;
import com.google.api.services.drive.Drive;
import org.json.JSONObject;

public class GuestDetailMapper {

    public static Rapper toRapper(JSONObject jDetails, Drive service){
        Rapper rapper = new Rapper();

        rapper.setArtistDetails(getArtistDetailsFromJSON(ArtistType.RAPPER,
                jDetails, service));

        final JSONObject jMedia = jDetails.getJSONObject("medias");
        rapper.setBeatToPlay(MediaMapper.toMedia(
                rapper.getArtistDetails().getArtistName(),
                jMedia, MediaType.BEAT_TO_PLAY, service));

        return rapper;
    }

    private static ArtistDetails getArtistDetailsFromJSON(ArtistType artistType,
                                                          JSONObject jDetails,
                                                          Drive service){

        JSONObject jMedia;
        ArtistDetails artistDetails = new ArtistDetails();

        String artistName = jDetails.getString("artist_name");
        artistName = Utils.upCaseFirstChar(artistName);
        artistDetails.setArtistName(artistName);

        artistDetails.setArtistType(artistType);

        jMedia = jDetails.getJSONObject("medias");

        artistDetails.setFavoriteSong(MediaMapper.toMedia(artistName,
                jMedia, MediaType.FAVORITE_SONG, service));

        artistDetails.setSongToPlay(MediaMapper.toMedia(artistName,
                jMedia, MediaType.SONG_TO_PLAY, service));


        artistDetails.setGameInfo(GameInfoMapper.toGame(
                jDetails.getJSONObject("games")));

        artistDetails.setProject(ProjectMapper.toProject(
                jDetails.getJSONObject("project")));

        artistDetails.setToDiscuss(jDetails.getString("to_discuss"));
        return artistDetails;
    }

    public static BeatBoxer toBeatBoxer(JSONObject jDetails, Drive service){
        BeatBoxer beatBoxer = new BeatBoxer();
        beatBoxer.setArtistDetails(getArtistDetailsFromJSON(ArtistType.BEATBOXER,
                jDetails, service));
        return beatBoxer;
    }

}
