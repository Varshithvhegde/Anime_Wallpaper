package com.op.test;


import android.Manifest;
import android.app.Activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;


import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class FullImageActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_details);
        // Get intent data
        Intent i = getIntent();
        // Get Selected Image Id and images array
        int position = i.getExtras().getInt("id");
        String[] Images = i.getExtras().getStringArray("Array");

        ViewPager mViewPager = (ViewPager) findViewById(R.id.vp);
        PgAdapt vpagerAdapter = new PgAdapt(this, Images);
        mViewPager.setAdapter(vpagerAdapter);
        mViewPager.setCurrentItem(position);


    }


}

