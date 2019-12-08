package com.gaminho.theshowapp.persistence;

import com.gaminho.theshowapp.dao.BeatboxerRepository;
import com.gaminho.theshowapp.dao.RapperRepository;
import com.gaminho.theshowapp.model.artist.ArtistFileInfo;
import com.gaminho.theshowapp.model.artist.ArtistType;
import com.gaminho.theshowapp.model.artist.BeatBoxer;
import com.gaminho.theshowapp.model.artist.Rapper;
import com.gaminho.theshowapp.service.ArtistService;
import com.gaminho.theshowapp.web.service.drive.GoogleDriveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class ReadJSONTask {

    /**
     * DEPUIS LE FORM: TAPER DANS UN FICHIER TEMP
     *
     * DEPUIS LE SITE: TAPER EN BASE DIRECT
     * CRON POUR ALLER CHERCHER LE FICHIER TEMP
     *      EXISTS
     *          > GET QUESTIONS
     *          > SAVE BASE
     *          > REMOVE FILE TEMP
     * SAVE BASE DANS GAMES.JSON
     */

    @Autowired
    ArtistService artistService;

    @Autowired
    BeatboxerRepository beatboxerRepository;

    @Autowired
    RapperRepository rapperRepository;

    private static Logger log = LoggerFactory.getLogger(ReadJSONTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public List<ArtistFileInfo> getGuestFiles() throws GeneralSecurityException, IOException {
        List<ArtistFileInfo> list = GoogleDriveService.getGuestFiles();
        log.info("getGuestFiles request: retrieve {} details", list.size());
        return list;
    }

    public Optional<Rapper> getRapper(String fileId) throws GeneralSecurityException, IOException {
        return GoogleDriveService.getRapper(fileId);
    }

    public Optional<BeatBoxer> getBeatBoxer(String fileId) throws GeneralSecurityException, IOException {
        return GoogleDriveService.getBeatBoxer(fileId);
    }




    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));


//        rapperRepository.findById(10165L).ifPresent(rapper -> {
//            log.debug("{} favorite's song {}",
//                    rapper.getArtistDetails().getArtistName(),
//                    rapper.getArtistDetails().getFavoriteSong().getGoogleDriveURL());
//
//            Media favorite = rapper.getArtistDetails().getFavoriteSong();
//
//            try {
//                InputStream in = new URL(favorite.getGoogleDriveURL()).openStream();
//                Files.copy(in, Paths.get("test.mp3"), StandardCopyOption.REPLACE_EXISTING);
//                log.debug("Successfully downloaded");
//            } catch (IOException e) {
//                log.error("error while downloading file {}", favorite.getName(), e);
//            }
//        });

        List<ArtistFileInfo> list;
        try {
            list = getGuestFiles();
        } catch (GeneralSecurityException | IOException e) {
            log.error("Error while getting guest file: {}", e.getMessage());
            list = new ArrayList<>();
        }

        if(!list.isEmpty()){
            log.debug("Guest file count: {}", list.size());
            list.forEach(gInfo -> {

                final ArtistType artistType = gInfo.getArtistType();
                final String artistName = gInfo.getArtistName();
                final boolean exist = artistService.artistExists(artistType, artistName);

                if (!exist) {

                    try {
                        if (artistType == ArtistType.RAPPER) {
                            Optional<Rapper> optRapper = getRapper(gInfo.getFileId());
                            optRapper.ifPresent(rapper -> {
                                rapperRepository.save(rapper);
                                log.debug("Saving rapper: {}", rapper.getArtistDetails().getArtistName());
                            });
                        } else if (artistType == ArtistType.BEATBOXER) {
                            Optional<BeatBoxer> optBeatBoxer = getBeatBoxer(gInfo.getFileId());
                            optBeatBoxer.ifPresent(beatBoxer -> {
                                beatboxerRepository.save(beatBoxer);
                                log.debug("Saving beat boxer: {}", beatBoxer.getArtistDetails().getArtistName());
                            });
                        }
                    } catch (Exception e) {
                        log.error("Error while getting rapper with file: '{}'",
                                gInfo.getArtistName());
                    }

                } else {
                    log.debug("{} [{}] already exists.", artistName, artistType);
                }
            });
        }

    }

}
