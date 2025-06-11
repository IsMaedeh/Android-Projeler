package com.example.finalprojesicamerax3;

/**
 * Cloudinary'den gelen silme (delete) işlemi cevabını temsil eden sınıf.
 */
public class DeleteResponse {

    // Cloudinary API'den dönen sonuç (örneğin: "ok" veya "not found").
    private String result;

    // API cevabındaki sonucu döndürür.
    public String getResult() {
        return result;
    }
}
