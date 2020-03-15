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
}
