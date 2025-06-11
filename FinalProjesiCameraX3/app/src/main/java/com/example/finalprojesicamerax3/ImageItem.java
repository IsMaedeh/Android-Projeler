package com.example.finalprojesicamerax3;

// ImageItem sınıfı: Bir resmin bilgilerini tutar (URL ve isim).
public class ImageItem {
    private String imageUrl;    // Resmin internet adresi (URL).
    private String imageName;   // Resmin adı.

    // Yapıcı (constructor) metod: Nesne oluşturulurken URL ve isim atanır.
    public ImageItem(String imageUrl, String imageName) {
        this.imageUrl = imageUrl;
        this.imageName = imageName;
    }

    // Resmin URL'sini döndürür.
    public String getImageUrl() {
        return imageUrl;
    }

    // Resmin ismini döndürür.
    public String getImageName() {
        return imageName;
    }
}
