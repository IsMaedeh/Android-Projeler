package com.example.finalprojesicamerax3;

public class ImageItem {
    private String imageUrl;
    private String imageName;

    public ImageItem(String imageUrl, String imageName) {
        this.imageUrl = imageUrl;
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageName() {
        return imageName;
    }
}
