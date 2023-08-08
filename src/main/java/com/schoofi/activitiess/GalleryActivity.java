package com.schoofi.activitiess;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import library.Animations.DescriptionAnimation;
import library.SliderAdapter;
import library.SliderLayout;
import library.SliderTypes.BaseSliderView;
import library.SliderTypes.DefaultSliderView;
import library.Tricks.ViewPagerEx;
import polypicker.model.Image;

public class GalleryActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private SliderLayout mDemoSlider;
    ArrayList<String> myList;
    private int position,position1;
    SliderAdapter sliderAdapter;
    ImageView back,upload,ivImage;
    int pos;
    String value,param;
    String demo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.fragment_image_slider);
        Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Student FeedbackSlider");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        back = (ImageView) findViewById(R.id.img_back);
        upload = (ImageView) findViewById(R.id.img_upload);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
       // myList = (ArrayList<String>) getIntent().getSerializableExtra("array");

        demo = getIntent().getStringExtra("images");

        myList = new ArrayList<String>(Arrays.asList(demo.split(",")));

        //ivImage = (ImageView) findViewById(R.id.imageView);
//        ivImage.setVisibility(View.GONE);
      //  param = getIntent().getStringExtra("param");
        sliderAdapter = new SliderAdapter(this);

		/*if(value.matches("3"))
		{
			upload.setVisibility(View.INVISIBLE);
		}*/

        for(final String name : myList)
        {
            final DefaultSliderView defaultSliderView = new DefaultSliderView(getApplicationContext());

            defaultSliderView.image(AppConstants.SERVER_URLS.IMAGE_URL+ name).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener(){
                @Override public void onSliderClick(BaseSliderView slider) {
                    Log.d("MyActivity", "index selected:" + mDemoSlider.getCurrentPosition());

                    pos= mDemoSlider.getCurrentPosition();



                }
            });

            /*upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
					 *//*mDemoSlider.setVisibility(View.GONE);
					 ivImage.setVisibility(View.VISIBLE);
					 Log.d()
					 Glide.with(StudentImageFeedBackSlider.this).load(AppConstants.SERVER_URLS.IMAGE_URL+myList.get(pos)).crossFade().placeholder(R.drawable.imagenotavailble).error(R.drawable.schoofisplash).into(ivImage);
					 onShareItem(v);
					 ivImage.setVisibility(View.GONE);
					 mDemoSlider.setVisibility(View.VISIBLE);*//*

                    Intent intent = new Intent(StudentImageFeedBackSlider.this,EventImageSharingScreen.class);
                    intent.putExtra("position",position);
                    intent.putExtra("position1",myList.get(position1).toString());
                    intent.putExtra("value",value);
                    intent.putExtra("param",param);
                    startActivity(intent);


                }
            });*/

            mDemoSlider.addSlider(defaultSliderView);
        }




        // mDemoSlider.

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.stopAutoCycle();







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_image_feed_back_slider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        // TODO Auto-generated method stub


    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub

    }

    public void onShareItem(View v) {
        // Get access to bitmap image from view

        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(ivImage);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            // ...sharing failed, handle error
        }
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API > 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
