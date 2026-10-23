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

}
