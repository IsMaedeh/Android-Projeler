package com.example.finalprojesicamerax3;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import android.Manifest;

// Android uygulamasında kullanıcıdan izin istemek ve izin durumunu kontrol etmek için kullanılan yardımcı (utility) sınıf.
public class PermissionManager {

    // Singleton örneği
    private static PermissionManager instance = null;

    // İzin kontrollerinde kullanılan uygulama context'i
    private Context context;

    // Private Yapıcı Metot
    private PermissionManager() {
    }

    // Singleton örneğini döner. Eğer örnek yoksa oluşturur ve context ile başlatır.
    public static  PermissionManager getInstance(Context context) {
        if (instance == null) {
            instance = new PermissionManager();
        }
        instance.init(context);
        return instance;
    }

    // Context bilgisini sınıfa atar.
    private void init(Context context) { this.context = context; }

    // Verilen izinlerin tümünün verilip verilmediğini kontrol eder.
    //true: Tüm izinler verilmiş.
    //false: En az bir izin verilmemiş.
    boolean checkPermissions(String[] permissions) {
        int size = permissions.length;

        for (int i = 0; i < size; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) == PermissionChecker.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // İzinleri kullanıcıdan istemek için kullanılır.
    void askPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    // sonrası, izinlerin verilme durumlarını kontrol eder.
    void handlePermissionResult(Activity activity, int requestCode, String[] permissions,
                                int[] grantResults) {
        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(activity, "İzin verildi.", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(activity, "İzin verilmedi.", Toast.LENGTH_SHORT).show();
                }
            }
//            showPermissionRational(activity, requestCode);
        }
    }

    // Kullanıcı izin vermemişse ve sistem izin isteğini tekrar göstermeyi öneriyorsa, açıklama mesajı gösterir ve tekrar izin ister.
    private  void showPermissionRational(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showMessageOKCancel("You need to allow access to the permission(s)!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    askPermissions(activity,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.CAMERA},
                                            requestCode);
                                }
                            }
                        });
                return;
            }
        }
    }

    // Kullanıcıya basit bir onaylama kutusu (AlertDialog) gösterir.
    void showMessageOKCancel(String msg, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton("Ok", onClickListener)
                .setNegativeButton("Cancel", onClickListener)
                .create()
                .show();
    }
}
