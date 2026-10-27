package io.github.SamPSS_Code.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:mcc_database.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initDatabase() {
        String createLibrariesTable = """
                CREATE TABLE IF NOT EXISTS libraries (
                    id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    source_type TEXT NOT NULL,
                    last_scanned_at DATETIME
                );    
                """;

        String createComicsTable = """
                CREATE TABLE IF NOT EXISTS comics (
                    id TEXT PRIMARY KEY,
                    library_id TEXT,
                    file_name TEXT NOT NULL,
                    title TEXT NOT NULL,
                    folder_path TEXT NOT NULL,
                    publisher TEXT,
                    format TEXT NOT NULL,
                    size_bytes INTEGER,
                    cover_cached BOOLEAN DEFAULT 0,
                    current_page INTEGER DEFAULT 0,
                    total_pages INTEGER DEFAULT 0,
                    is_read BOOLEAN DEFAULT 0,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                    last_read_at DATETIME,
                    FOREIGN KEY (library_id) REFERENCES libraries (id)
                );
                """;
        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()){

            stmt.execute(createLibrariesTable);
            stmt.execute(createComicsTable);
        } catch (SQLException e){
            System.err.println("Erro ao inicializar database: " + e.getMessage());
        }
    }
}
