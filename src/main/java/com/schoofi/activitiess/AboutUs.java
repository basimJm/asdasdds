package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;

public class AboutUs extends AppCompatActivity
{
	
		private Button aboutUs,privacyPolicy,termsAndConditions;
	    private ImageView back;
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_about_us);
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("About Us");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());

		aboutUs = (Button) findViewById(R.id.btn_aboutUs);
		privacyPolicy  = (Button) findViewById(R.id.btn_privacyPolicy);
		termsAndConditions = (Button) findViewById(R.id.btn_termsAndConditions);
		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		aboutUs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AboutUs.this,WebViewActivity.class);
				String url = /*"http://www.schoofi.com/privacy.php";*/  "https://drive.google.com/file/d/0B7e14kKbW68ibUlqRmc2M1hVRG8/view?usp=sharing";
				intent.putExtra("url",url);
				startActivity(intent);
				
			}
		});
		
		privacyPolicy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AboutUs.this,WebViewActivity.class);
				String url = /*"http://www.schoofi.com/privacy.php";*/  "https://drive.google.com/file/d/0B7e14kKbW68iUEh6Z1R1d3Nvc3M/view?usp=sharing";
				intent.putExtra("url",url);
				startActivity(intent);
				
			}
		});
		
		termsAndConditions.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AboutUs.this,WebViewActivity.class);
				String url = "https://drive.google.com/file/d/0B7e14kKbW68iNGtGRS1fSk9aT2s/view?usp=sharing";
				intent.putExtra("url",url);
				startActivity(intent);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about_us, menu);
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

	
}
