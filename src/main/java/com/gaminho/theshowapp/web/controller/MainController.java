package com.gaminho.theshowapp.web.controller;

import com.gaminho.theshowapp.dao.ProjectRepository;
import com.gaminho.theshowapp.dao.RapperRepository;
import com.gaminho.theshowapp.model.Project;
import com.gaminho.theshowapp.model.artist.ArtistDetails;
import com.gaminho.theshowapp.model.artist.ArtistType;
import com.gaminho.theshowapp.model.artist.Rapper;
import com.gaminho.theshowapp.model.game.GameInfo;
import com.gaminho.theshowapp.model.media.Media;
import com.gaminho.theshowapp.utils.PathConstants;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin(value = "*")
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RapperRepository rapperRepository;


    @GetMapping(value="/test1")
    public ResponseEntity<Project> test() {
        Project project = new Project();
        project.setName("Project of " + new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss").format(new Date()));
        List<String> links = new ArrayList<>();
        links.add("http://wwww.google.fr");
        links.add("http://wwww.google.com");
        links.add("http://wwww.google.net");
        project.setHasBeenReleased(true);
        project.setLinks(links);
        return ResponseEntity.ok(projectRepository.save(project));
    }

    @GetMapping(value="/test2")
    public ResponseEntity<List<Project>> test2() {
        List <Project> list =  projectRepository.findAll();
        log.info("Get projects: {}", list.size());
        return ResponseEntity.ok(list);
    }

    @GetMapping(value="/test4")
    public ResponseEntity<Rapper> test4() {
        return ResponseEntity.ok(rapperRepository.save(getRapper()));
    }

    private static Rapper getRapper(){
        Rapper rapper = new Rapper();

        Media media = new Media();
        media.setGoogleDriveURL("http://www.google.fr/beat-to-play");

        rapper.setBeatToPlay(media);

        ArtistDetails artistDetails = new ArtistDetails();
        artistDetails.setArtistName(
                "Rapper of " + new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss").format(new Date()));
        artistDetails.setArtistType(ArtistType.RAPPER);


        media = new Media();
        media.setGoogleDriveURL("http://www.google.fr/favorite");
        artistDetails.setFavoriteSong(media);

        media = new Media();
        media.setGoogleDriveURL("http://www.google.fr/song-to-play");
        artistDetails.setSongToPlay(media);

        GameInfo gameInfo = new GameInfo();
        gameInfo.setImprovisation(true);
        gameInfo.setToAvoid(Arrays.asList("tetetet", "tetetet2"));
        artistDetails.setGameInfo(gameInfo);

        Project project = new Project();
        project.setName("Project of " + new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss").format(new Date()));
        List<String> links = new ArrayList<>();
        links.add("http://wwww.google.fr");
        links.add("http://wwww.google.com");
        links.add("http://wwww.google.net");
        project.setHasBeenReleased(true);
        project.setLinks(links);
        artistDetails.setProject(project);

        artistDetails.setToDiscuss("bla-bla");
        rapper.setArtistDetails(artistDetails);
        return rapper;
    }

    @GetMapping(value = "/test6",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> getAllPlebs(){
        File initialFile = new File("test.mp3");
        try {
            InputStream targetStream = new FileInputStream(initialFile);
            return ResponseEntity.ok(IOUtils.toByteArray(targetStream));
        } catch (IOException e) {
            log.error("Sorry, impossible: {}", e.getMessage(), e);
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/test7")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadFile,
                                        @RequestParam("file-name") String fileName) {

        log.debug("Single file upload: {}", fileName);

        if (uploadFile.isEmpty()) {
            log.error("File is empty");
            return ResponseEntity.noContent()
                    .header("error", "please select a file")
                    .build();
        }

        try {
            saveUploadedFiles(Collections.singletonList(uploadFile), fileName);
        } catch (IOException e) {
            log.error("IOException: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        log.info("Successfully uploaded");
        return ResponseEntity.ok()
                .header("file-name", uploadFile.getOriginalFilename())
                .build();

    }

    private void saveUploadedFiles(final List<MultipartFile> files, final String fileName)
            throws IOException {

        for (final MultipartFile file : files) {

            if (file.isEmpty()) {
                continue;
            }

            final byte[] bytes = file.getBytes();
            final Path path = Paths.get(formatUploadedFileName(file, fileName));
            Files.write(path, bytes);
        }
    }

    /**
     * Format the uploaded file name keeping original extension
     * @param uploadedFile the uploaded file to rename
     * @param customName the custom name
     * @return the formatted file name
     */
    private static String formatUploadedFileName(@NotNull final MultipartFile uploadedFile,
                                                 @NotNull final String customName){
        final String extension = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
        return PathConstants.RES_PATH.concat(customName).concat(".")
                .concat(StringUtils.isNotBlank(extension) ? extension : "");
    }

}
