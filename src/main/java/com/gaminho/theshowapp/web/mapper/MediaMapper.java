package com.gaminho.theshowapp.web.mapper;


import com.gaminho.theshowapp.model.media.Media;
import com.gaminho.theshowapp.model.media.MediaType;
import com.gaminho.theshowapp.utils.PathConstants;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class MediaMapper {

    private static Logger log = LoggerFactory.getLogger(MediaMapper.class);

    public static Media toMedia(String artistName, JSONObject json, MediaType type, Drive service) {
        String id = json.getString(type.getKey());
        Media media = new Media();
        media.setGoogleDriveId(id);

        try {
            final File file = service.files().get(id)
                    .setFields("webContentLink, name")
                    .execute();


            if (file != null) {
                media.setGoogleDriveURL(file.getWebContentLink());
                media.setName(file.getName());
            }

            final String mediaDriveUrl = media.getGoogleDriveURL();
            if (StringUtils.isNotBlank(mediaDriveUrl)) {
                log.debug("Start downloading media from drive: {}", mediaDriveUrl);
                final InputStream in = downloadFile(service, file);
//OLD                final InputStream in = new URL(mediaDriveUrl).openStream();
                if (null != in) {
                    final String name = generateFileName(media.getName(), type);
                    final String generatePath = generateMediaPath(artistName, name);
                    try {
                        Files.copy(in, Paths.get(generateMediaPath(artistName, name)),
                                StandardCopyOption.REPLACE_EXISTING);
                        media.setPathToFile(generatePath);
                    } catch (Exception e) {
                        log.error("something went wrong during file download: {}",
                                e.getMessage(), e);
                    }
                } else {
                    log.error("Can not download file");
                }
            }

        } catch (IOException e){
            log.error("Error while parsing media Json: {}", e.getMessage());
        }

        return media;
    }

    /**
     * Download a file's content.
     *
     * @param service Drive API service instance.
     * @param file Drive File instance.
     * @return InputStream containing the file's content if successful,
     *         {@code null} otherwise.
     */
    private static InputStream downloadFile(Drive service, File file) {
        if (file.getWebContentLink() != null
                && file.getWebContentLink().length() > 0) {
            try {
                final HttpResponse resp =
                        service.getRequestFactory()
                                .buildGetRequest(new GenericUrl(file.getWebContentLink()))
                                .execute();
                if (resp.getStatusCode() != HttpStatus.OK.value()) {
                    log.error("Error while downloading (code={}): {}",
                            resp.getStatusCode(), resp.getStatusMessage());
                    return null;
                } else {
                    return resp.getContent();
                }
            } catch (IOException e) {
                log.error("IOException: {}", e.getMessage(), e);
                return null;
            }
        } else {
            log.error("No content for given file.");
            return null;
        }
    }

    /**
     * Generate the file name according to media type and drive name
     * @param mediaName the drive name of the media
     * @param mediaType the media type used to prefix the song
     * @return the generated name
     */
    private static String generateFileName(final String mediaName,
                                           @NotNull final MediaType mediaType){

        return String.format("%s-%s", mediaType.getPrefix(),
                StringUtils.isNotEmpty(mediaName) ? mediaName : "unknown");
    }

    /**
     * Generate file path to store the media
     * @param artistName the artist name
     * @param mediaName the media name to store
     * @return the absolute path where media will be saved
     */
    private static String generateMediaPath(@NotNull final String artistName,
                                            final String mediaName){
        final java.io.File parentFolder = new java.io.File(String.format("%s/%s/medias/",
                PathConstants.ARTIST_RES_PATH, artistName));
        boolean parentExists = parentFolder.exists();
        if (!parentExists) {
            parentExists = parentFolder.mkdirs();
            log.debug("Parent folder has been created? {}", parentExists);
        }
        if (parentExists) {
            return String.format("res/artists/%s/medias/%s", artistName, mediaName);
        } else {
            throw new IllegalArgumentException("Parent folder can not be created");
        }
    }
}
