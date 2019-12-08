package com.gaminho.theshowapp.web.service.drive;

import com.gaminho.theshowapp.model.artist.ArtistFileInfo;
import com.gaminho.theshowapp.model.artist.ArtistType;
import com.gaminho.theshowapp.properties.GoogleDriveProperty;
import com.gaminho.theshowapp.utils.Utils;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GoogleDriveAPIHelper {

    private static Logger log = LoggerFactory.getLogger(GoogleDriveAPIHelper.class);

    private static String FILTER_FOLDER_MIME_TYPE = "mimeType='application/vnd.google-apps.folder'";

    public static File findFolderByName(Drive service, String folderName){
        try {
            FileList list = service.files().list()
                    .setQ("mimeType='application/vnd.google-apps.folder' and name = '" + folderName + "'")
                    .setPageSize(1)
                    .execute();
            if( list == null || list.isEmpty()){
                return null;
            } else {
                return list.getFiles().get(0);
            }
        } catch (IOException e) {
            return null;
        }
    }

    public static File findFileByName(Drive service, String fileName){
        try {
            FileList list = service.files().list()
                    .setQ("name = '" + fileName + "'")
                    .setPageSize(1)
                    .execute();
            if( list == null || list.isEmpty()){
                return null;
            } else {
                return list.getFiles().get(0);
            }
        } catch (IOException e) {
            return null;
        }
    }

    public static String getGuestFolderId(Drive service){
        String guestFolderId = GoogleDriveProperty.getGuestFolderId();

        log.debug("Guest folder id known ? {}", StringUtils.isNotEmpty(guestFolderId));

        if(StringUtils.isEmpty(guestFolderId)){
            try {
                FileList list = service.files().list()
                        .setQ(String.format("%s and name = '%s'",
                                FILTER_FOLDER_MIME_TYPE, GoogleDriveProperty.GUESTS_FOLDER))
                        .setFields("nextPageToken, files(id, name, parents)")
                        .execute();

                if(list != null && !list.isEmpty()) {
                    for (File folder : list.getFiles()) {
                        File parent = getFirstParent(service, folder);

                        if (parent.getName().equals(GoogleDriveProperty.SHOW_FOLDER)) {
                            parent = getFirstParent(service, parent);

                            if (parent.getName().equals(GoogleDriveProperty.ROOT_FOLDER)) {
                                GoogleDriveProperty.setGuestFolderId(folder.getId());
                                guestFolderId = folder.getId();
                                break;
                            }
                        }
                    }
                }

            } catch (IOException e) {
                log.error("Exception while finding GUESTS folder", e);
            }
        }

        return guestFolderId;
    }

    public static String getResourcesFolderId(Drive service){
        String resourcesFolderId = GoogleDriveProperty.getResourcesFolderId();
        log.debug("Resources folder id known ? {}", StringUtils.isNotEmpty(resourcesFolderId));

        if(StringUtils.isEmpty(resourcesFolderId)){
            try {
                FileList list = service.files().list()
                        .setQ(String.format("%s and name = '%s'",
                                FILTER_FOLDER_MIME_TYPE, GoogleDriveProperty.RESOURCES_FOLDER))
                        .setFields("nextPageToken, files(id, name, parents)")
                        .execute();

                if(list != null && !list.isEmpty()) {
                    for (File folder : list.getFiles()) {
                        File parent = getFirstParent(service, folder);

                        if (parent.getName().equals(GoogleDriveProperty.SHOW_FOLDER)) {
                            parent = getFirstParent(service, parent);

                            if (parent.getName().equals(GoogleDriveProperty.ROOT_FOLDER)) {
                                GoogleDriveProperty.setResourcesFolderId(folder.getId());
                                resourcesFolderId = folder.getId();
                                break;
                            }
                        }
                    }
                }

            } catch (IOException e) {
                log.error("Exception while finding GUESTS folder", e);
            }
        }

        return resourcesFolderId;
    }

    public static List<File> getGuestFiles(Drive service){
        String guestFolderId = getGuestFolderId(service);

        List<File> l = new ArrayList<>();
        try {
            FileList list = service.files().list()
                    .setQ("mimeType = 'application/json'")  //FIXME: add a filter with name
                    .setPageSize(1000)
                    .setFields("nextPageToken, files(id, name, parents, webContentLink)")
                    .execute();

            if(list != null && !list.isEmpty()) {

                l = list.getFiles().stream()
                        .filter( f -> isInGuestFolder(f, guestFolderId))
                        .collect(Collectors.toList());
            }

        } catch (IOException e) {
            log.error("error while getting Guest files", e);
        }
        return l;
    }

    public static String getGamesFileId(Drive service){
        String resourcesFolderId = getResourcesFolderId(service);

        log.debug("resources folder id: {}", resourcesFolderId);

        try {
            FileList list = service.files().list()
                    .setQ("mimeType = 'application/json'")  //FIXME: add a filter with name
                    .setPageSize(100)
                    .setFields("nextPageToken, files(id, name, parents, webContentLink)")
                    .execute();

            if(list != null && !list.isEmpty()) {

                File file = list.getFiles().stream()
                        .filter( f -> isInResourcesFolder(f, resourcesFolderId))
                        .collect(Collectors.toList()).get(0);
                return file.getId();
            }

        } catch (IOException e) {
            log.error("error while getting Game file", e);
        }
        return null;
    }

    public static JSONObject getGamesFile(Drive service){
        try {
            FileList list = service.files().list()
                    .setQ("mimeType = 'application/json' and name contains 'games'")  //FIXME: add a filter with name
                    .setPageSize(1000)
                    .setFields("nextPageToken, files(id, name, parents, webContentLink)")
                    .execute();

            if(list != null && !list.isEmpty()) {
                String fileId = list.getFiles().get(0).getId();
                Optional<File> file = GoogleDriveAPIHelper.getFileById(service, fileId);
                if(file.isPresent()) {
                    log.debug("Games file found: {}", fileId);
                    try{
                        return new JSONObject(GoogleDriveAPIHelper.downloadFile(service, fileId));
                    } catch (JSONException e){
                        log.error("error while getting games file not found: {}", e.getMessage());
                        return new JSONObject();
                    }

                } else {
                    log.error("game file not found");
                }
            }

        } catch (IOException e) {
            log.error("error while getting Games file", e);
        }

        return null;
    }

    private static boolean isInGuestFolder(File child, String guestFolderId){
        return child.getParents().contains(guestFolderId);
    }

    private static boolean isInResourcesFolder(File child, String resourcesFolderId){
        return child.getParents().contains(resourcesFolderId);
    }



    private static File getFirstParent(Drive service, File child){
        if(hasParent(child)) {
            try {
                return service.files()
                        .get(child.getParents().get(0))
                        .setFields("id, name, parents")
                        .execute();
            } catch (IOException e) {
                log.error("error while retrieving parent", e);
            }
        } else {
            log.error("{} does not have parent", child.getName());
        }
        return null;
    }


    /**
     * Download a file's content.
     *
     * @param service Drive API service instance.
     * @param file Drive File instance.
     * @return InputStream containing the file's content if successful,
     *         {@code null} otherwise.
     */

    public static String downloadFile(Drive service, File file) {
        try {
            OutputStream out = new ByteArrayOutputStream();
            service.files().get(file.getId()).executeMediaAndDownloadTo(out);
            return out.toString();
        } catch (IOException e) {
            log.error("error while downloading file {}", file.getId(), e);
            return null;
        }
    }

    public static String downloadFile(Drive service, String fileId) {
        try {
            OutputStream out = new ByteArrayOutputStream();

            service.files().get(fileId).executeMediaAndDownloadTo(out);
            return out.toString();
        } catch (IOException e) {
            log.error("error while downloading file {}", fileId, e);
            return null;
        }
    }

    public static java.io.File downloadAudioFile(Drive service, String fileId) {
        try {
            InputStream in = service.files().get(fileId).executeMediaAsInputStream();
            Files.copy(in, Paths.get("test.mp3"), StandardCopyOption.REPLACE_EXISTING);
            return new java.io.File("test.mp3");
        } catch (IOException e) {
            log.error("error while downloading file {}", fileId, e);
            return null;
        }
    }

    public static Optional<File> getFileById(Drive service, String fileId){
        Optional<File> f = Optional.empty();
        try {
            f = Optional.of(service.files().get(fileId).execute());
        } catch (IOException e) {
            log.error("Exception while getting file, {}", e.getMessage(), e);
        }
        return f;
    }

    private static boolean hasParent(File child){
        return child.getParents() != null && !child.getParents().isEmpty();
    }


    /**
     * Extract Artist name from file name
     * @param fileName the given file name
     * @return the artist name as String
     */
    public static String extractNameFromGuestFileName(String fileName){
        // NAME PATTERN = (BBX|RAP)_NAME_DATE'T'HOUR.json
        try {
            return Utils.upCaseFirstChar(fileName.split("_")[1]);
        } catch (ArrayIndexOutOfBoundsException e){
            log.error("Can not find Artist name from '{}'", fileName);
            throw e;
        }
    }

    /**
     * Extract {@link com.gaminho.theshowapp.model.artist.ArtistType} from file name
     * @param fileName the given file name
     * @return the type as {@link ArtistType}
     */
    public static com.gaminho.theshowapp.model.artist.ArtistType extractArtistTypeFromGuestFileName(String fileName){
        // NAME PATTERN = (BBX|RAP)_NAME_DATE'T'HOUR.json
        return fileName.contains(ArtistType.BEATBOXER.getSymbol()) ?
                ArtistType.BEATBOXER : ArtistType.RAPPER;
    }

    /**
     * ExtractArtistFileInfo from the given file file
     * @param file the given file
     * @return the {@link ArtistFileInfo}
     */
    public static ArtistFileInfo extractGuestInfoFromGuestFile(File file){
        return new ArtistFileInfo(file.getId(),
                extractNameFromGuestFileName(file.getName()),
                extractArtistTypeFromGuestFileName(file.getName()));
    }

    public static boolean moveFileToFolder(String fileId, String folderId, Drive service) throws IOException {
        File child = service.files().get(fileId).setFields("parents").execute();

        String previousParents = StringUtils.join(child.getParents(), ',');

        service.files().update(fileId, null)
                .setAddParents(folderId)
                .setRemoveParents(previousParents)
                .setFields("id, parents")
                .execute();

        return true;
    }


}
