package com.kkolontay.popularmovies.DataModel;


public class MovieReview {
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";
    public static final String ID = "id";
    public static final String URL = "url";
     private String author;
     private String content;
     private  String id;
     private String url;

    public MovieReview() {
    }

    public MovieReview(String author, String content, String id, String url) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

