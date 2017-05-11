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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * This class is used to hook into Google Drive
 *
 * @author Levi Muniz
 * @version 1.0.0
 */

class DriveAPI {

    private static Credential authorize(JsonFactory JSONFactory, HttpTransport httpTransport) throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSONFactory, new InputStreamReader(MeshFS.class.getResourceAsStream("/client_id.json")));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSONFactory, clientSecrets, Collections.singleton(DriveScopes.DRIVE)).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(System.getProperty("user.home"), ".store/MeshFS"))).build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    static void unauthorize() {
        FileUtils.removeFile(System.getProperty("user.home") + java.io.File.separator + ".store/MeshFS/StoredCredential");
    }

    static File uploadFile(java.io.File filePath, String type) throws IOException, GeneralSecurityException {
        JsonFactory JSONFactory = JacksonFactory.getDefaultInstance();
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(JSONFactory, httpTransport);
        Drive drive = new Drive.Builder(httpTransport, JSONFactory, credential).setApplicationName("MeshFS-MeshFS/1.0").build();

        File fileMetadata = new File();
        fileMetadata.setTitle(filePath.getName());

        FileContent mediaContent = new FileContent(type, filePath);

        Drive.Files.Insert insert = drive.files().insert(fileMetadata, mediaContent);
        MediaHttpUploader uploader = insert.getMediaHttpUploader();
        uploader.setDirectUploadEnabled(false);
        return insert.execute();
    }

    static void downloadFile(String fileID) throws IOException, GeneralSecurityException {

        java.io.File parentDir = new java.io.File(System.getProperty("user.home") + java.io.File.separator + "Downloads" + java.io.File.separator);
        if (!parentDir.exists() && !parentDir.mkdirs()) {
          throw new IOException("Unable to create parent directory");
        }

        JsonFactory JSONFactory = JacksonFactory.getDefaultInstance();
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(JSONFactory, httpTransport);
        Drive drive = new Drive.Builder(httpTransport, JSONFactory, credential).setApplicationName("MeshFS-MeshFS/1.0").build();

        File uploadedFile = drive.files().get(fileID).execute();

        OutputStream out = new FileOutputStream(new java.io.File(parentDir, uploadedFile.getTitle()));

        MediaHttpDownloader downloader =
            new MediaHttpDownloader(httpTransport, drive.getRequestFactory().getInitializer());
        downloader.setDirectDownloadEnabled(false);
        downloader.download(new GenericUrl(uploadedFile.getDownloadUrl()), out);
    }

    static List<File> listFiles() throws Exception {
        JsonFactory JSONFactory = JacksonFactory.getDefaultInstance();
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(JSONFactory, httpTransport);
        Drive drive = new Drive.Builder(httpTransport, JSONFactory, credential).setApplicationName("MeshFS-MeshFS/1.0").build();
        List<File> files = new ArrayList<>();
        files.addAll(drive.files().list().setQ("trashed = false and mimeType != 'application/vnd.google-apps.folder'").execute().getItems());
        return files;
    }
}

