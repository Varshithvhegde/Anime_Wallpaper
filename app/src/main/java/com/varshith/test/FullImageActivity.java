package com.varshith.test;


import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;


import androidx.viewpager.widget.ViewPager;


import com.varshith.test.R;


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

