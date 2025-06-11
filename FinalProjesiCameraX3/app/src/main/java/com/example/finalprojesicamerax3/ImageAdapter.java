package com.example.finalprojesicamerax3;

import static android.app.ProgressDialog.show;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// ImageAdapter sınıfı: RecyclerView için adapter. Görselleri ve isimlerini gösterir.
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final Context context;                  // Uygulama veya aktivite bağlamı.
    private final ArrayList<ImageItem> imageList;   // Görsellerin bulunduğu liste.

    // Yapıcı (constructor) metod: Context ve görsel listesi alınır.
    public ImageAdapter(Context context, ArrayList<ImageItem> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    // Yeni bir ViewHolder oluşturulur.
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.single_image_recycler_adapter, parent, false);
        return new ImageViewHolder(view);
    }

    // Her bir satıra veriler atanır (görsel ve isim).
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        ImageItem currentItem = imageList.get(position);

        // Glide YENI
        // Glide kütüphanesi ile görsel yükleniyor.
        Glide.with(context).load(currentItem.getImageUrl()).into(holder.imageView);
        holder.textViewName.setText(currentItem.getImageName());

        holder.itemView.setOnLongClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) {
                return false;   // Geçersiz pozisyon → işlem yok
            }
            ImageItem itemToDelete = imageList.get(currentPosition);

            // Silme onayı isteyen dialog kutusu açılır.
            new AlertDialog.Builder(context)
                .setTitle("Fotoğraf Silmek")
                .setMessage("Bu fotoğrafı silmek ister misin?")
                .setPositiveButton("Evet", (dialog, which) -> {
                    // Eğer context Gallery2 ise, silme fonksiyonu çağırılır.
                    if (context instanceof Gallery2) {
                        ((Gallery2) context).deleteImage(currentItem, position);
                    }
                })
               .setNegativeButton("Hayır", null)
               .show();

            return true; // Olay işlendi bilgisini döndür
        });
    }

    // Listenin eleman sayısını döndürür.
    @Override
    public int getItemCount() {
        return imageList.size();
    }

    // ViewHolder sınıfı: Tek bir satırdaki elemanları tutar.
    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;    // Görsel görüntüleme alanı
        TextView textViewName;  // Görselin adı
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }
}
