package io.github.SamPSS_Code.database;

import io.github.SamPSS_Code.model.Comic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComicDAO {

    public void saveOrUpdate(Comic comic){
        String sql = """
                INSERT INTO comics(
                    id, library_id, file_name, title, folder_path, publisher, format, size_bytes
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    ON CONFLICT(id) DO UPDATE SET
                        file_name = excluded.file_name,
                        title = excluded.title,
                        folder_path = excluded.folder_path,
                        publisher = excluded.publisher,
                        format = excluded.format,
                        size_bytes = excluded.size_bytes;
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, comic.getId());
            pstmt.setString(2, comic.getLibraryId());
            pstmt.setString(3, comic.getFileName());
            pstmt.setString(4, comic.getTitle());
            pstmt.setString(5, comic.getFolderPath());
            pstmt.setString(6, comic.getPublisher());
            pstmt.setString(7, comic.getFormat());
            pstmt.setLong(8, comic.getSizeBytes());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while updating comic in SQLite: " + e.getMessage());
        }
    }

    public List<Comic> getAll(){
        List<Comic> comics = new ArrayList<>();
        String sql = "SELECT * FROM comics ORDER BY title ASC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){

            while (rs.next()){
                Comic comic = new Comic();
                comic.setId(rs.getString("id"));
                comic.setLibraryId(rs.getString("library_id"));
                comic.setFileName(rs.getString("file_name"));
                comic.setTitle(rs.getString("title"));
                comic.setFolderPath(rs.getString("folder_path"));
                comic.setPublisher(rs.getString("publisher"));
                comic.setFormat(rs.getString("format"));
                comic.setSizeBytes(rs.getLong("size_bytes"));
                comic.setCoverCached(rs.getBoolean("cover_cached"));
                comic.setCurrentPage(rs.getInt("current_page"));
                comic.setTotalPages(rs.getInt("total_pages"));
                comic.setRead(rs.getBoolean("is_read"));

                comics.add(comic);

            }

        } catch (SQLException e){
            System.err.println("Error while listing comics: " + e.getMessage());
        }
        return comics;
    }
    public void updateCoverCache(String id, boolean cached){
        String sql = "UPDATE comics SET cover_cached = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, cached ? 1 : 0);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("Error trying to update cover_cached for " + id + ": " + e.getMessage());
        }
    }

}
