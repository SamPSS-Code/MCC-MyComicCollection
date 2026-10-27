package io.github.SamPSS_Code.model;

import java.time.LocalDateTime;

public class Comic {

    private String id;
    private String libraryId;
    private String fileName;
    private String title;
    private String folderPath;
    private String publisher;
    private String format;
    private long sizeBytes;
    private boolean coverCached;
    private int currentPage;
    private int totalPages;
    private boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime lastReadAt;

    public Comic(){
    }

    public Comic(String id, String libraryId, String fileName, String title,
                 String folderPath, String publisher, String format, long sizeBytes) {
        this.id = id;
        this.libraryId = libraryId;
        this.fileName = fileName;
        this.title = title;
        this.folderPath = folderPath;
        this.publisher = publisher;
        this.format = format;
        this.sizeBytes = sizeBytes;
        this.coverCached = false;
        this.currentPage = 0;
        this.totalPages = 0;
        this.isRead = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public boolean isCoverCached() {
        return coverCached;
    }

    public void setCoverCached(boolean coverCached) {
        this.coverCached = coverCached;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(LocalDateTime lastReadAt) {
        this.lastReadAt = lastReadAt;
    }
}
