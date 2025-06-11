package com.example.finalprojesicamerax3;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Cloudinary API'den gelen cevapları temsil eden sınıf.
 * Bu sınıf içinde resim bilgileri (URL ve public ID) tutulur.
 */
public class CloudinaryResponse {

    // Cloudinary'den gelen resim listesi
    private List<ImageData> resources;

    // Resimlerin listesini döndürür.
    public List<ImageData> getResources() {
        return resources;
    }

    // Tek bir resmi temsil eden iç sınıf.
    public static class ImageData {

        // Resmin güvenli (https) URL'si
        @SerializedName("secure_url")
        private String secureUrl;

        // Resmin Cloudinary'deki public ID'si
        @SerializedName("public_id")
        private String publicId;


        // Resmin güvenli URL'sini döndürür.
        public String getSecureUrl() {
            return secureUrl;
        }

        // Resmin public ID'sini döndürür.
        public String getPublicId() {
            return publicId;
        }

//        public void setSecureUrl(String secure_url) {
//            this.secure_url = secure_url;
//        }

    }
}
