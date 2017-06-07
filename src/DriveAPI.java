import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import org.json.simple.JSONObject;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;


/**
 * This class is used to implement the Google Drive Client Library
 *
 * @author Levi Muniz
 * @version 1.0.0
 */

class DriveAPI {

    private DriveAPI() {}

    /**
     * This method allows a user to authorize and store their Google Drive credentials
     *
     * @param JSONFactory   JsonFactory to load client secrets
     * @param httpTransport trusted transport to connect to the API servers
     * @param userId        UUID of UserAccount to authenticate as
     * @return              authorized Credential
     * @throws IOException  on error connecting to API servers
     */

    private static Credential authorize(JsonFactory JSONFactory, HttpTransport httpTransport, String userId) throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSONFactory, new InputStreamReader(MeshFS.class.getResourceAsStream(java.io.File.separator + "client_id.json")));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSONFactory, clientSecrets, Collections.singleton(DriveScopes.DRIVE)).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(System.getProperty("user.home"), ".store" + java.io.File.separator + "MeshFS"))).build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(userId);
    }

    /**
     * This method is used to upload a file to the user's drive.
     *
     * @param filePath                      path of file to be uploaded
     * @param name                          name of the file
     * @param type                          metadata type
     * @param userId                        UUID of UserAccount to authenticate as
     * @return                              the file that was uploaded
     * @throws IOException                  on error connecting to API servers
     * @throws GeneralSecurityException     if trusted transport cannot be established
     */

    static File uploadFile(java.io.File filePath, String name, String type, String userId) throws IOException, GeneralSecurityException {
        JsonFactory JSONFactory = JacksonFactory.getDefaultInstance();
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(JSONFactory, httpTransport, userId);
        Drive drive = new Drive.Builder(httpTransport, JSONFactory, credential).setApplicationName("MeshFS-MeshFS/1.0").build();

        File fileMetadata = new File();
        fileMetadata.setTitle(name);
        fileMetadata.setParents(Collections.singletonList(
                new ParentReference().setId("root")));

        FileContent mediaContent = new FileContent(type, filePath);

        Drive.Files.Insert insert = drive.files().insert(fileMetadata, mediaContent);
        MediaHttpUploader uploader = insert.getMediaHttpUploader();
        uploader.setDirectUploadEnabled(false);
        return insert.execute();
    }

    /**
     * This method is used to download a file from a user's Google Drive
     *
     * @param fileID                        the file ID to download from Google Drive
     * @param userId                        UUID of UserAccount to authenticate as
     * @return                              the downloaded file
     * @throws IOException                  on error writing file or connecting to API servers
     * @throws GeneralSecurityException     if trusted transport cannot be established
     */

    static java.io.File downloadFile(String fileID, String userId) throws IOException, GeneralSecurityException {

        java.io.File parentDir = new java.io.File(System.getProperty("user.home") + java.io.File.separator + "Downloads" + java.io.File.separator);
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            throw new IOException("Unable to create parent directory");
        }

        JsonFactory JSONFactory = JacksonFactory.getDefaultInstance();
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(JSONFactory, httpTransport, userId);
        Drive drive = new Drive.Builder(httpTransport, JSONFactory, credential).setApplicationName("MeshFS-MeshFS/1.0").build();

        File uploadedFile = drive.files().get(fileID).execute();

        java.io.File outputFile = new java.io.File(parentDir, uploadedFile.getTitle());
        OutputStream out = new FileOutputStream(outputFile);

        MediaHttpDownloader downloader =
                new MediaHttpDownloader(httpTransport, drive.getRequestFactory().getInitializer());
        downloader.setDirectDownloadEnabled(false);
        downloader.download(new GenericUrl(uploadedFile.getDownloadUrl()), out);
        return outputFile;
    }

    private static List<File> listFiles(String parentId, String userId) throws IOException, GeneralSecurityException {
        JsonFactory JSONFactory = JacksonFactory.getDefaultInstance();
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(JSONFactory, httpTransport, userId);
        Drive drive = new Drive.Builder(httpTransport, JSONFactory, credential).setApplicationName("MeshFS-MeshFS/1.0").build();
        List<File> files = new ArrayList<>();
        files.addAll(drive.files().list().setQ("trashed = false and mimeType != 'application/vnd.google-apps.folder' and '" + parentId + "' in parents").execute().getItems());
        return files;
    }

    private static List<File> listFolders(String parentId, String user) throws IOException, GeneralSecurityException {
        JsonFactory JSONFactory = JacksonFactory.getDefaultInstance();
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(JSONFactory, httpTransport, user);
        Drive drive = new Drive.Builder(httpTransport, JSONFactory, credential).setApplicationName("MeshFS-MeshFS/1.0").build();
        List<File> files = new ArrayList<>();
        files.addAll(drive.files().list().setQ("trashed = false and mimeType = 'application/vnd.google-apps.folder' and '" + parentId + "' in parents").execute().getItems());
        return files;
    }

    /**
     * This is used to build a JSONObject from google drive one layer at a time
     *
     * @param user                  the userID for the google account
     * @param masterJson            the JSONObject of the google drive info
     * @param parentFolderLocation  the virtual filepath of the parent folder in the masterJSONObject
     */
    @SuppressWarnings("unchecked")

    static void googleJsonBuilder(String user, JSONObject masterJson, String parentFolderLocation) throws IOException, GeneralSecurityException {
        JSONObject itemToRead = masterJson;
        String[] parentFolders = parentFolderLocation.split("/");
        for (String parentFolder : parentFolders) {
            itemToRead = (JSONObject) itemToRead.get(parentFolder);
        }
        if (itemToRead.get("generated").toString().equals("false")) {
            String parentId = itemToRead.get("ID").toString();
            List<String> itemTitles = new ArrayList<>();
            List<File> folders = listFolders(parentId, user);
            itemTitles = fileInfoBuilder(folders, itemTitles, itemToRead, true);
            List<File> files = listFiles(parentId, user);
            fileInfoBuilder(files, itemTitles, itemToRead, false);
            itemToRead.replace("generated", "true");
        }
    }

    @SuppressWarnings("unchecked")

    private static List<String> fileInfoBuilder(List<File> files, List<String> itemTitles, JSONObject itemToRead, boolean folder){
        for (File file : files) {
            StringBuilder itemName = new StringBuilder(file.getTitle());
            if (itemTitles.contains(itemName.toString())) {
                int duplicateCount = 1;
                while (itemTitles.contains(itemName + " (" + duplicateCount + ")")) {
                    duplicateCount++;
                }
                itemName.append(" (").append(duplicateCount).append(")");
            }
            itemTitles.add(itemName.toString());
            JSONObject folderInfo = new JSONObject();

            folderInfo.put("ID", file.getId());
            itemToRead.put(itemName.toString(), folderInfo);
            if (folder){
                folderInfo.put("type", "directory");
            } else {
                folderInfo.put("type", "file");
            }
        }
        return itemTitles;
    }
}

