package io.github.SamPSS_Code;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.checkerframework.checker.nullness.qual.NonNull;


import java.io.IOException;
import java.util.List;

public class DriveScanner {
    @NonNull
    public static List<File> listRootFolders(Drive driveService) throws IOException{
        String query = "'root' in parents and mimeType = 'application/vnd.google-apps.folder' and trashed = false";
        return driveService.files().list()
                .setQ(query)
                .setPageSize(30)
                .setFields("files(id, name)")
                .execute()
                .getFiles();
    }

    public static void scanFolderRecursively(Drive driveService, String folderId, String indent) throws IOException{
        String query = String.format("'%s' in parents and trashed = false", folderId);
        FileList result = driveService.files().list()
                .setQ(query)
                .setPageSize(100)
                .setFields("files(id, name, mimeType)")
                .execute();

        List<File> items = result.getFiles();
        if (items == null || items.isEmpty()) {
            return;
        }

        for (File item : items) {
            boolean isFolder = "application/vnd.google-apps.folder".equals(item.getMimeType());

            if (isFolder) {
                IO.println(indent + "\uD83D\uDCC1 [" + item.getName() + "]");
                scanFolderRecursively(driveService, item.getId(), indent + " ");
            } else {
                String nameLower = item.getName().toLowerCase();
                if (nameLower.endsWith(".cbz") || nameLower.endsWith(".cbr") || nameLower.endsWith(".pdf") || nameLower.endsWith(".zip")){
                    IO.println(indent + "\uD83D\uDCC4 " + item.getName());
                }
            }
        }
    }
}
