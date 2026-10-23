package io.github.SamPSS_Code;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.util.List;
import java.util.Scanner;

public class Main {

    static void main() throws Exception {
        IO.println("Connecting to Google Drive...");
        Drive driveService = GoogleDriveManager.getDriveService();

        List<File> folders = DriveScanner.listRootFolders(driveService);

        IO.println("\n--- Folders Founded ---");
        for (int i = 0; i < folders.size(); i++){
            IO.println(String.format("[%d] %s", i + 1, folders.get(i).getName()));
        }

        IO.print("\nChoose your comic folder: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt() - 1;

        File selected = folders.get(choice);
        IO.println("\nEscaneando: " + selected.getName());
        DriveScanner.scanFolderRecursively(driveService, selected.getId(), "");
    }

    /*GeneralSecurityException, IOException {
        try{
            String comicsFolderId = "1DZ8dn8Gh3c1DlQ5N_0QQ6Epya7qPOLJR";
            String query = "name = 'Comics' and mimeType = 'application/vnd.google-apps.folder' and trashed = false";

            IO.println("Conecting to Google Drive...");

            com.google.api.services.drive.Drive driveService = GoogleDriveManager.getDriveService();
            com.google.api.services.drive.model.FileList result = driveService.files().list()
                    .setQ(query)
                    .setPageSize(30)
                    .setFields("files(id, name, mimeType, size)")
                    .execute();
            java.util.List<com.google.api.services.drive.model.File> files = result.getFiles();
            if (files == null){
                IO.println("No comics folder were found in your Google Drive");
            } else {
                com.google.api.services.drive.model.File comicsFolder = files.get(0);
                IO.println("Comics folder located! Real ID: " + comicsFolder.getId());

                String contentQuery = String.format("'%s' in parents and trashed = false", comicsFolder.getId());
                com.google.api.services.drive.model.FileList comicsList = driveService.files().list()
                        .setQ(contentQuery)
                        .setPageSize(30)
                        .setFields("files(id, name, mimeType, size)")
                        .execute();
                IO.print("\nContent found inside Comics:\n");
                for (com.google.api.services.drive.model.File file : comicsList.getFiles()) {
                    IO.println("- " + file.getName());
                }
            }
        } catch (Exception e) {
            IO.println("Error while connecting to Google Drive: " + e.getMessage());
            e.printStackTrace();
        }
    }*/
}
