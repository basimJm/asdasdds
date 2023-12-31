package com.schoofi.utils;



import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.collection.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

@SuppressWarnings("deprecation")
public class VolleySingleton {
    private static VolleySingleton mInstance = null;
    private RequestQueue mRequestQueue,mRequestQueueI;
    private ImageLoader mImageLoader;
 
    private VolleySingleton(Context context){
        mRequestQueueI = Volley.newRequestQueue(context, new HttpClientStack(new DefaultHttpClient()));;
    	 mRequestQueue = Volley.newRequestQueue(context);
         
        mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }
 
    public static VolleySingleton getInstance(Context context){
        if(mInstance == null){
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }
 
    public RequestQueue getRequestQueue(){
        return this.mRequestQueue;
    }
    
    public RequestQueue getHTTPRequestQueue(){
        return this.mRequestQueueI;
    }
 
    public ImageLoader getImageLoader(){
        return this.mImageLoader;
    }
 
}
