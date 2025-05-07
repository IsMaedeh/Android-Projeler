package com.example.finalprojesicamerax3;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CloudinaryResponse {
    private List<ImageData> resources;

    public List<ImageData> getResources() {
        return resources;
    }

    public static class ImageData {
        @SerializedName("secure_url")
        private String secureUrl;

        @SerializedName("public_id")
        private String publicId;


        public String getSecureUrl() {
            return secureUrl;
        }

        public String getPublicId() {
            return publicId;
        }

//        public void setSecureUrl(String secure_url) {
//            this.secure_url = secure_url;
//        }

    }
}
