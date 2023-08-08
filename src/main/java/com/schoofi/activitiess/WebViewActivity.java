package com.schoofi.activitiess;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.schoofi.utils.Preferences;

import org.json.JSONArray;

public class WebViewActivity extends Activity{
	
	private ProgressDialog pd;
    private String url;
    WebView webView;
    private int position;
    String token = Preferences.getInstance().token;
	String schoolId = Preferences.getInstance().schoolId;
	String classId = Preferences.getInstance().studentClassId;
	String sectionId = Preferences.getInstance().studentSectionId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	private JSONArray studentAnnouncementArray;
    
    private class MyWebviewClient extends WebViewClient {
        private MyWebviewClient() {
        }
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            if (!WebViewActivity.this.pd.isShowing()) {
                WebViewActivity.this.pd.show();
            }
            return true;


        }

        public void onPageStarted(WebView webView, String url, Bitmap favicon) {
            if (WebViewActivity.this.pd.isShowing()) {
                WebViewActivity.this.pd.dismiss();
            }
            super.onPageStarted(webView, url, favicon);
        }

        public void onPageFinished(WebView WebView, String url) {
            WebViewActivity.this.pd.dismiss();
            super.onPageFinished(WebView, url);
        }
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_web_view);
    	position = getIntent().getExtras().getInt("position");
    	url = getIntent().getStringExtra("url");
    	WebView webView = (WebView) findViewById(R.id.webView1);
        this.pd = new ProgressDialog(this);
        this.pd.setMessage("loading page...");
        this.pd.show();
        
        
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new MyWebviewClient());
        webView.loadUrl(this.url);
    }
		
		 

	
}
