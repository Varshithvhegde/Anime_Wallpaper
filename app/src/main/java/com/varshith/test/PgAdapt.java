package com.varshith.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.varshith.test.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PgAdapt extends PagerAdapter {

    Context mContext;
    String[] images;

    LayoutInflater layoutInflater;
    private static final int MANAGE_REQUEST_CODE = 101;

    PgAdapt(Context context, String[] imgs) {
        this.mContext = context;
        this.images = imgs;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }


    @Override
    public int getCount() {
        return images.length;
    }

    @SuppressLint("LongLogTag")
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = layoutInflater.inflate(R.layout.full_image, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.full_image_view);


        Button downloader = (Button) itemView.findViewById(R.id.button_save);

        String link = images[position].replace("thumb-350", "thumb-1920");

        Glide.with(mContext).clear(imageView);
        Glide.with(mContext).load(link).placeholder(R.drawable.load).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Toast.makeText(mContext, R.string.msg, Toast.LENGTH_LONG).show();

                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                imageView.setOnTouchListener(new ImageMatrixTouchHandler(mContext));
                downloader.setVisibility(View.VISIBLE);

                return false;
            }
        }).into(imageView);


        container.addView(itemView);

        downloader.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                int permission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {

                    makeRequest();
                }
                String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());

                saveimage(time, link);

            }

        });

        return itemView;
    }


    public void saveimage(String filename, String downloadUrlOfImage) {
        try {
            DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + filename + ".jpg");

            dm.enqueue(request);
            Toast.makeText(mContext, "Image download started.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(mContext, "Image download failed.", Toast.LENGTH_SHORT).show();
        }
    }


    public void makeRequest() {
        ActivityCompat.requestPermissions((Activity) mContext,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MANAGE_REQUEST_CODE);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }

}