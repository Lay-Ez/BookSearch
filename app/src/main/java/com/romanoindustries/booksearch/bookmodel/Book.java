package com.romanoindustries.booksearch.bookmodel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "book_table")
public class Book {

    @Embedded
    private VolumeInfo volumeInfo;

    @PrimaryKey
    @ColumnInfo (name = "self_link")
    @NonNull
    private String selfLink;
    private String personalNote;
    private Long savedTime;
    @ColumnInfo (name = "web_reader_link")
    private String webReaderLink;

    public Book(VolumeInfo volumeInfo, String selfLink) {
        this.volumeInfo = volumeInfo;
        this.selfLink = selfLink;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public String getPersonalNote() {
        return personalNote;
    }

    public void setPersonalNote(String personalNote) {
        this.personalNote = personalNote;
    }

    public Long getSavedTime() {
        return savedTime;
    }

    public void setSavedTime(Long savedTime) {
        this.savedTime = savedTime;
    }

    public String getWebReaderLink() {
        return webReaderLink;
    }

    public void setWebReaderLink(String webReaderLink) {
        this.webReaderLink = webReaderLink;
    }
}
