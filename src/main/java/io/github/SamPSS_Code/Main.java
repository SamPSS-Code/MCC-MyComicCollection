package io.github.SamPSS_Code;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import io.github.SamPSS_Code.cache.CacheManager;
import io.github.SamPSS_Code.cache.CoverService;
import io.github.SamPSS_Code.database.ComicDAO;
import io.github.SamPSS_Code.database.DatabaseManager;
import io.github.SamPSS_Code.model.Comic;

import java.util.List;
import java.util.Scanner;

public class Main {
    static void main() throws Exception {
        DatabaseManager.initDatabase();
        CacheManager.initCache();

        IO.println("Connecting to Google Drive...");
        Drive driveService = GoogleDriveManager.getDriveService();
        ComicDAO comicDAO = new ComicDAO();

        List<File> folders = DriveScanner.listRootFolders(driveService);

        IO.println("\n--- Folders Founded ---");
        for (int i = 0; i < folders.size(); i++){
            IO.println(String.format("[%d] %s", i + 1, folders.get(i).getName()));
        }

        IO.print("\nChoose your comic folder: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt() - 1;
        scanner.nextLine();

        File selected = folders.get(choice);
        IO.println("\nScanning: " + selected.getName());
        DriveScanner.scanFolderRecursively(driveService, selected.getId(), "",
                selected.getId(), "/" + selected.getName(), comicDAO);
        IO.println("\n scan completed.");

        CoverService coverService = new CoverService(driveService, comicDAO);
        coverService.syncAllCovers();


    }

}
