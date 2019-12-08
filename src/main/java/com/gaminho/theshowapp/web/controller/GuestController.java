package com.gaminho.theshowapp.web.controller;

import com.gaminho.theshowapp.model.artist.ArtistFileInfo;
import com.gaminho.theshowapp.model.artist.ArtistType;
import com.gaminho.theshowapp.model.artist.BeatBoxer;
import com.gaminho.theshowapp.model.artist.Rapper;
import com.gaminho.theshowapp.model.media.Media;
import com.gaminho.theshowapp.service.ArtistService;
import com.gaminho.theshowapp.web.service.drive.GoogleDriveService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(value = "*")
public class GuestController {

    private Logger log = LoggerFactory.getLogger(GuestController.class);

    @Autowired
    private ArtistService artistService;

    @GetMapping(value="/guests")
    public ResponseEntity<List<ArtistFileInfo>> getArtistFiles() throws GeneralSecurityException, IOException {
        List<ArtistFileInfo> list = GoogleDriveService.getGuestFiles();
        log.info("getArtistFiles request: retrieve {} details", list.size());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value="/guests/rappers")
    public ResponseEntity<List<Rapper>> getRappers() {
        List<Rapper> list = artistService.getAllRappers();
        log.info("getRappers request: retrieve {} details", list.size());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value="/guests/rappers/{artistName}")
    public ResponseEntity<Rapper> getRapperByName(@PathVariable(value = "artistName") String artistName) {
        Optional<Rapper> optRapper = artistService.getRapperByName(artistName);
        log.info("getRapperByName request: retrieve? {}", optRapper.isPresent());
        return ResponseEntity.ok(optRapper.orElseThrow(() -> new EntityNotFoundException("can not find rapper with name: ".concat(artistName))));
    }


    private byte[] getMediaAsByte(final Media media) throws IOException {
        final File initialFile = new File(media.getPathToFile());
        try {
            InputStream targetStream = new FileInputStream(initialFile);
            return IOUtils.toByteArray(targetStream);
        } catch (IOException e) {
            log.error("Sorry, impossible: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Media getMedia(final String artistName, final ArtistType artistType, final int songType){

        if(artistType == ArtistType.RAPPER){
           final Rapper rapper = artistService.getRapperByName(artistName).orElseThrow(() ->
                   new EntityNotFoundException("can not find rapper with name: ".concat(artistName)));

            if(1 == songType){
                return rapper.getArtistDetails().getSongToPlay();
            } else if(2 == songType){
                return rapper.getArtistDetails().getFavoriteSong();
            } else if(3 == songType){
                return rapper.getBeatToPlay();
            } else {
                throw new EntityNotFoundException("Unknown song with index: " + songType);
            }

       } else if (artistType == ArtistType.BEATBOXER){
            final BeatBoxer beatBoxer = artistService.getBeatBoxerByName(artistName).orElseThrow(() ->
                    new EntityNotFoundException("can not find beatboxer with name: ".concat(artistName)));

            if(1 == songType){
                return beatBoxer.getArtistDetails().getSongToPlay();
            } else if(2 == songType){
                return beatBoxer.getArtistDetails().getFavoriteSong();
            } else {
                throw new EntityNotFoundException("Unknown song with index: " + songType);
            }
       } else {
            throw new EntityNotFoundException("Unknown artist type: " + artistType);
        }
    }

    @GetMapping( value = "/guests/rappers/{artistName}/songs/{songType}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> getRapperSongStreamUrl(@PathVariable(value = "artistName") String artistName,
                                                    @PathVariable(value = "songType") int songType) {

        final Media media = getMedia(artistName, ArtistType.RAPPER, songType);
        try {
            return ResponseEntity.ok(getMediaAsByte(media));
        } catch (IOException e) {
            log.error("Impossbile to convert media as bytes: {}", e.getMessage(), e);
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping( value = "/guests/beatboxers/{artistName}/songs/{songType}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> getBeatBoxerSongStreamUrl(@PathVariable(value = "artistName") String artistName,
                                                       @PathVariable(value = "songType") int songType) {

        final Media media = getMedia(artistName, ArtistType.BEATBOXER, songType);
        try {
            return ResponseEntity.ok(getMediaAsByte(media));
        } catch (IOException e) {
            log.error("Impossbile to convert media as bytes: {}", e.getMessage(), e);
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value="/guests/beatboxers")
    public ResponseEntity<List<BeatBoxer>> getBeatBoxers() {
        List<BeatBoxer> list = artistService.getAllBeatBoxers();
        log.info("getBeatBoxers request: retrieve {} details", list.size());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value="/guests/beatboxers/{artistName}")
    public ResponseEntity<BeatBoxer> getBeatBoxerByName(@PathVariable(value = "artistName") String artistName) {
        Optional<BeatBoxer> optBbxer = artistService.getBeatBoxerByName(artistName);
        log.info("getBeatBoxerByName request: retrieve? {}", optBbxer.isPresent());
        return ResponseEntity.ok(optBbxer.orElseThrow(() -> new EntityNotFoundException("can not find beatboxer with name: ".concat(artistName))));
    }



//    @GetMapping(value = "/guests/{fileId}")
//    public ResponseEntity<OArtistDetails> getArtistDetails(@PathVariable(value = "fileId") String fileId) throws GeneralSecurityException, IOException {
//        OArtistDetails OArtistDetails = GoogleDriveService.getArtistDetails(fileId);
//        if(OArtistDetails != null){
//            return ResponseEntity.ok(OArtistDetails);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

}
