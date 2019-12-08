package com.gaminho.theshowapp.web.service.drive;

import com.gaminho.theshowapp.model.artist.ArtistFileInfo;
import com.gaminho.theshowapp.model.artist.ArtistType;
import com.gaminho.theshowapp.model.artist.BeatBoxer;
import com.gaminho.theshowapp.model.artist.Rapper;
import com.gaminho.theshowapp.web.controller.MainController;
import com.gaminho.theshowapp.web.mapper.GuestDetailMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GoogleDriveService {

    private static Logger log = LoggerFactory.getLogger(GoogleDriveService.class);

    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = MainController.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static Drive getDriveService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static List<ArtistFileInfo> getGuestFiles() throws GeneralSecurityException, IOException {
        List<ArtistFileInfo> OGuestFileInfoList = new ArrayList<>();

        // Build a new authorized API client service.
        Drive service = getDriveService();

        List<File> files = GoogleDriveAPIHelper.getGuestFiles(service);

        if(!files.isEmpty()){
            log.debug("guest size: {}", files.size());
            for(File f : files){
                try {
                    ArtistFileInfo artistFileInfo = GoogleDriveAPIHelper.extractGuestInfoFromGuestFile(f);
                    OGuestFileInfoList.add(artistFileInfo);
                    log.debug("Found details for {}", artistFileInfo);
                } catch (Exception e){
                    log.error("Exception {}", e.getMessage());
                }
            }
        }

        return OGuestFileInfoList;
    }


    public static Optional<Rapper> getRapper(String fileId) throws GeneralSecurityException, IOException {
        log.debug("Getting rapper starts, fileID={}", fileId);
        Rapper rapper = null;
        Drive service = getDriveService();
        Optional<File> file = GoogleDriveAPIHelper.getFileById(service, fileId);

        if(file.isPresent()){
            log.debug("File found for given id {}", fileId);
            JSONObject jDetails = new JSONObject(GoogleDriveAPIHelper.downloadFile(service, fileId));
            rapper = file.get().getName().contains(ArtistType.RAPPER.getSymbol())
                    ? GuestDetailMapper.toRapper(jDetails, service)
                    : null;
        } else {
            log.error("No file found for given id {}", fileId);
        }
        return Optional.ofNullable(rapper);
    }

    public static Optional<BeatBoxer> getBeatBoxer(String fileId) throws GeneralSecurityException, IOException {
        log.debug("Getting beat boxer starts, fileID={}", fileId);
        Drive service = getDriveService();
        BeatBoxer beatBoxer = null;
        Optional<File> file = GoogleDriveAPIHelper.getFileById(service, fileId);

        if(file.isPresent()){
            log.debug("File found for given id {}", fileId);
            JSONObject jDetails = new JSONObject(GoogleDriveAPIHelper.downloadFile(service, fileId));
            beatBoxer = file.get().getName().contains(ArtistType.BEATBOXER.getSymbol())
                    ? GuestDetailMapper.toBeatBoxer(jDetails, service)
                    : null;
        } else {
            log.error("No file found for given id {}", fileId);
        }
        return Optional.ofNullable(beatBoxer);
    }

    public static JSONObject getGames() throws GeneralSecurityException, IOException {
        Drive service = getDriveService();
        return GoogleDriveAPIHelper.getGamesFile(service);
    }

    public static JSONArray getPlebsQuestions() throws GeneralSecurityException, IOException {
        return getGames().has("plebs_questions") ?
                getGames().getJSONArray("plebs_questions") : new JSONArray();
    }



//    public static boolean addAPlebsQuestion(String plebsQuestion) throws GeneralSecurityException, IOException {
//        JSONArray jPlebsQuestion = getPlebsQuestions();
//
//        PlebsQuestion plebs = new PlebsQuestion(plebsQuestion, new Date(),
//                PlebsQuestionCategory.CRAZY_GIRL);
//
//        jPlebsQuestion.put(plebs.toJSONObject());
//        savePlebsQuestion(jPlebsQuestion);
//        return true;
//    }

//    public static boolean removePlebsQuestionWithUuid(String plebsQuestionUuid) throws GeneralSecurityException, IOException {
//        JSONArray jPlebsQuestion = getPlebsQuestions();
//        boolean exists = false;
//        for(int i = 0 ; i < jPlebsQuestion.length() ; i++){
//            Object jChild = jPlebsQuestion.get(i);
//            if(jChild instanceof JSONObject){
//
//                PlebsQuestion plebsQuestion;
//                try {
//                    plebsQuestion = PlebsQuestionMapper.toPlebsQuestion((JSONObject) jChild);
//                    if(plebsQuestionUuid.equalsIgnoreCase(plebsQuestion.getUuid())) {
//                        jPlebsQuestion.remove(i);
//                        exists = true;
//                        log.debug("PlebsQuestion removed: {}", plebsQuestion);
//                        break;
//                    }
//                } catch (PlebsQuestionException e){
//                    log.error("Error while getting plebs question from JSON: {}", e.getMessage());
//                }
//            }
//        }
//
//        if(exists){
//            savePlebsQuestion(jPlebsQuestion);
//            return true;
//        } else {
//            log.debug("PlebsQuestion not found: {}", plebsQuestionUuid);
//            return false;
//        }
//    }

    public static boolean savePlebsQuestion(JSONArray newPlebsArray) throws GeneralSecurityException, IOException {
        Drive service = getDriveService();

        JSONObject jGames = getGames();
        jGames.put("plebs_questions", newPlebsArray);

        File fileMetadata = new File();
        fileMetadata.setName("games.json");

        java.io.File f = new java.io.File("temp.json");
        FileUtils.writeStringToFile(f, jGames.toString(), "UTF-8");
        FileContent mediaContent = new FileContent("application/json", f);

        // TODO: Create new games file
        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id").execute();
        log.debug("File ID: {}", file.getId());

        //TODO: get previous games file id and removed it
        String previousId = GoogleDriveAPIHelper.getGamesFileId(service);
        if(StringUtils.isNotEmpty(previousId)){
            service.files().delete(previousId).execute();
            log.debug("removed previous games file");
        }

        //TODO: moved to resources folder
        GoogleDriveAPIHelper.moveFileToFolder(file.getId(),
                GoogleDriveAPIHelper.getResourcesFolderId(service), service);
        log.debug("moved");
        return true;
    }
}
