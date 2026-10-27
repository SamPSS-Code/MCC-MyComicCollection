package io.github.SamPSS_Code;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import io.github.SamPSS_Code.database.ComicDAO;
import io.github.SamPSS_Code.model.Comic;

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

    public static void scanFolderRecursively(Drive driveService, String folderId, String indent,
                                             String libraryId, String currentPath, ComicDAO comicDAO) throws IOException{
        String query = String.format("'%s' in parents and trashed = false", folderId);
        FileList result = driveService.files().list()
                .setQ(query)
                .setPageSize(100)
                .setFields("files(id, name, mimeType, size)")
                .execute();

        List<File> items = result.getFiles();
        if (items == null || items.isEmpty()) {
            return;
        }
        boolean containsImages = false;
        long totalImagesSize = 0L;
        for (File item : items) {
            boolean isFolder = "application/vnd.google-apps.folder".equals(item.getMimeType());

            if (isFolder) {
                IO.println(indent + "\uD83D\uDCC1 [" + item.getName() + "]");
                String nextPath = currentPath + "/" + item.getName();
                scanFolderRecursively(driveService, item.getId(), indent + " ",libraryId, nextPath, comicDAO);
            } else {
                String fileName = item.getName();
                String nameLower = fileName.toLowerCase();

                if (nameLower.endsWith(".cbz") || nameLower.endsWith(".cbr") || nameLower.endsWith(".pdf")
                        || nameLower.endsWith(".zip") || nameLower.endsWith(".7z")){
                    IO.println(indent + "\uD83D\uDCC4 " + fileName);
                    int dotIndex = fileName.lastIndexOf('.');

                    String format = (dotIndex != -1) ? fileName.substring(dotIndex + 1).toUpperCase() : "UNKNOW";

                    String title = (dotIndex != -1) ? fileName.substring(0,dotIndex) : fileName;

                    long sizeBytes = (item.getSize() != null) ? item.getSize() : 0L;

                    String publisher = currentPath.contains("/") ?
                            currentPath.substring(currentPath.lastIndexOf('/') + 1) : "general";

                    Comic comic = new Comic(
                            item.getId(),
                            libraryId,
                            fileName,
                            title,
                            currentPath,
                            publisher,
                            format,
                            sizeBytes
                    );
                    comicDAO.saveOrUpdate(comic);
                } else if (nameLower.endsWith(".jpg") || nameLower.endsWith(".jpeg") ||
                        nameLower.endsWith(".webp") || nameLower.endsWith(".png")){
                    containsImages = true;
                    totalImagesSize += (item.getSize() != null) ? item.getSize() : 0L;
                }
            }
        }

        if (containsImages) {
            String folderName = "Pasta_Solta";
            String publisher = "general";
            String title = "Pasta_Solta";

            if (currentPath != null && currentPath.contains("/")) {
                String[] parts = currentPath.split("/");
                if (parts.length > 0) {
                    folderName = parts[parts.length - 1];
                    title = folderName;
                }
                if (parts.length > 1) {
                    publisher = parts[parts.length - 2];
                    title = publisher + " - " + folderName;
                }
            }
            IO.println(indent + "\\uD83D\\uDCDA [FOLDER AS COMIC]" + folderName);

            Comic comicDir = new Comic(
                    folderId,
                    libraryId,
                    folderName,
                    title,
                    currentPath,
                    publisher,
                    "DIR",
                    totalImagesSize
            );
            comicDAO.saveOrUpdate(comicDir);
        }
    }
}
