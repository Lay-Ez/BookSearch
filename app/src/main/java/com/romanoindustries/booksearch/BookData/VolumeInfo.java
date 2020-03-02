package com.romanoindustries.booksearch.BookData;

import java.util.ArrayList;
import java.util.List;

public class VolumeInfo {

    private String title;
    private List<String> authors = new ArrayList<>();
    private String publisher;
    private String publishedDate;
    private String description;
    private int pageCount;
    private List<String> categories = new ArrayList<>();
    private double avgRating;
    private String smallThumbnailURL;
    private String thumbnailURL;
    private String language;
    private String previewLink;
    private String infoLink;

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
                ", previewLink='" + previewLink + '\'' +
                ", infoLink='" + infoLink + '\'' +
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

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
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
}
