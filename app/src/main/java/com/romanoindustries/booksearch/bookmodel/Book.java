package com.romanoindustries.booksearch.bookmodel;

public class Book {

    private VolumeInfo volumeInfo;
    private String selfLink;

    public Book(VolumeInfo volumeInfo, String selfLink) {
        this.volumeInfo = volumeInfo;
        this.selfLink = selfLink;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }
}
