package com.example.finalprojesicamerax3;

import android.view.PixelCopy;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

/**
 * BasicAuthInterceptor, her HTTP isteğine "Authorization" başlığı ekleyen bir interceptor'dur.
 * Bu başlık ile temel (Basic) kimlik doğrulama sağlanır.
 */
public class BasicAuthInterceptor implements Interceptor{
    private String credentials;

    // Kullanıcı adı ve şifre ile Basic Auth bilgisi oluşturulur.
    public BasicAuthInterceptor(String user, String password) {
        this.credentials = Credentials.basic(user, password);
    }

    // Her HTTP isteğine Authorization başlığı ekler.
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("Authorization", credentials).build();
        return chain.proceed(authenticatedRequest);
    }
}
