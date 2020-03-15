package com.romanoindustries.booksearch.bookmodel;

import androidx.room.ColumnInfo;

import java.util.ArrayList;
import java.util.List;

public class VolumeInfo {

    private String title;
    private List<String> authors = new ArrayList<>();
    private String publisher;

    @ColumnInfo (name = "published_date")
    private String publishedDate;
    private String description;
    private int pageCount;
    private List<String> categories = new ArrayList<>();

    @ColumnInfo (name = "avg_rating")
    private double avgRating;

    @ColumnInfo (name = "ratings_count")
    private int ratingsCount;

    @ColumnInfo (name = "small_thumb_url")
    private String smallThumbnailURL;

    @ColumnInfo (name = "thumb_url")
    private String thumbnailURL;
    private String language;

    @ColumnInfo (name = "preview_url")
    private String previewUrl;

    @ColumnInfo (name = "info_url")
    private String infoUrl;

    public VolumeInfo() {
    }

    @Override
    public String toString() {
        return "VolumeInfo{" +
                "title='" + title + '\'' +
                ", authors=" + authors +
                ", publisher='" + publisher + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", description='" + description + '\'' +
                ", pageCount=" + pageCount +
                ", categories=" + categories +
                ", avgRating=" + avgRating +
                ", smallThumbnailURL='" + smallThumbnailURL + '\'' +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                ", language='" + language + '\'' +
                ", previewUrl='" + previewUrl + '\'' +
                ", infoUrl='" + infoUrl + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void addAuthorToList(String author) {
        authors.add(author);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void addCategory(String category) {
        categories.add(category);
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public String getSmallThumbnailURL() {
        return smallThumbnailURL;
    }

    public void setSmallThumbnailURL(String smallThumbnailURL) {
        this.smallThumbnailURL = smallThumbnailURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
