package com.example.filedownloaderapp.helper;

public class ListItems {

    private String likes;
    private String imageURL;

    public ListItems(String imageURL,String likes) {
        this.likes = likes;
        this.imageURL = imageURL;
    }

    public String getLikes() {
        return likes;
    }

    public String getImageURL() {
        return imageURL;
    }
}
