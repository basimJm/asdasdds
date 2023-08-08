package com.schoofi.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.schoofi.activitiess.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import polypicker.ImagePickerActivity;
import polypicker.utils.ImageInternalFetcher;

public class TeacherAssignedReply extends Fragment{
	
	private static final String TAG = TeacherAssignedReply.class.getSimpleName();
	private static final int INTENT_REQUEST_GET_IMAGES = 13;
	private static final int INTENT_REQUEST_GET_N_IMAGES = 14;
    private Context mContext;
    
    private Button upload;
	private int STORAGE_PERMISSION_CODE = 23;
    int count;
    int i;
    
    private Bitmap bitmap;
    String imageLeave,imageLeave1;
	
	private ViewGroup mSelectedImagesContainer;
	private ViewGroup mSelectedImagesNone;
	HashSet<Uri> mMedia = new HashSet<Uri>();
	 ArrayList<String> images = new ArrayList<String>();
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.teacher_assigned_feedback_reply, container, false);
		mContext = getActivity();
		mSelectedImagesContainer = (ViewGroup) view.findViewById(R.id.selected_photos_container);
		upload = (Button) view.findViewById(R.id.btn_upload);
        
		upload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(Build.VERSION.SDK_INT >22) {

					if (checkAndRequestPermissions()) {
						// carry on the normal flow, as the case of  permissions  granted.

						getNImages();
					}
				}

				else
				{
					getNImages();
				}
				
				
				
			}
		});
		return view;
}
	
	private void getImages() {
		Intent intent = new Intent(mContext, ImagePickerActivity.class);
		startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
	}
	
	private void getNImages() {
		Intent intent = new Intent(mContext, ImagePickerActivity.class);
		
		// limit image pick count to only 3 images.
		intent.putExtra(ImagePickerActivity.EXTRA_SELECTION_LIMIT, 3);
		startActivityForResult(intent, INTENT_REQUEST_GET_N_IMAGES);
		//System.out.println("lkkjgj");
	}

	@Override
	public void onActivityResult(int requestCode, int resuleCode, Intent intent) {
		super.onActivityResult(requestCode, resuleCode, intent);
		
		//System.out.println("lkkjj");
		
		TeacherAssignedReply teacherAssignedReply = new TeacherAssignedReply();

		if (resuleCode == AppCompatActivity.RESULT_OK) {
			if (requestCode == INTENT_REQUEST_GET_IMAGES || requestCode == INTENT_REQUEST_GET_N_IMAGES && resuleCode == AppCompatActivity.RESULT_OK && intent != null && intent.getData() != null) {
				
				Parcelable[] parcelableUris = intent.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
				
				
				
            	
            	
                
                if(parcelableUris ==null) {
                	return;
                }

                // Java doesn't allow array casting, this is a little hack
                
                
                Uri[] uris = new Uri[parcelableUris.length];
                //Uri uris1 = intent.getData();
                System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);
                //Toast.makeText(getApplicationContext(), uris[0].getPath(), Toast.LENGTH_SHORT).show();
                
                /*for(int i=0;i<=uris.length;i++)
                {
                	
                }
                selectedpath = uris[0].getPath();
                
                
                
                
                File file = new File(selectedpath);
                
                FileBody bin1 = new FileBody(file);
                Toast.makeText(getApplicationContext(), file.toString(), Toast.LENGTH_LONG).show();
               
                
                MultipartEntity reqEntity = new MultipartEntity();*/
                
                
                
                
                
                
                if(uris != null) {
                	for (Uri uri : uris) {
                		Log.i(TAG, " uri: " + uri);
                		mMedia.add(uri);
					}
                	
                	
                /* for(int i=0;i<mMedia.size();i++)
                 {
                	 
                	 
                	 
                 }*/
                  // Toast.makeText(getApplicationContext(), filePath.toString(), Toast.LENGTH_LONG).show();
		         // Toast.makeText(getApplicationContext(), selectedPaths.toString(), Toast.LENGTH_LONG).show();
                 
                 
                	
                	
                	
                	showMedia();
                	
                	
                		String urt = uris[0].getPath().toString();
                		Log.d("tag", "kio"+urt);
                		File imgFile = new  File(urt);
                		FileInputStream fis = null;
                		try {
                		    fis = new FileInputStream(imgFile);
                		    } catch (FileNotFoundException e) {
                		    e.printStackTrace();
                		}
                		//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                		//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                		imageLeave = getStringImage(fis);
                		//images.add(imageLeave);
                		
                		String urt1 = uris[1].getPath().toString();
                		Log.d("tag", "kio1"+urt1);
                		File imgFile1 = new  File(urt1);
                		FileInputStream fis1 = null;
                		try {
                		    fis1= new FileInputStream(imgFile1);
                		    } catch (FileNotFoundException e) {
                		    e.printStackTrace();
                		}
                		//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                		//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                		imageLeave1 = getStringImage(fis1);
                		Log.d("harsh", "ll"+imageLeave1);
                		
                		//images.add(imageLeave);
                		
                	}
                	
                	//images.add("jkkkjkjj");
                	//images.add(imageLeave);
                	//images.add(imageLeave);
               // images.add(imageLeave);
                //images.add(imageLeave);
                images.add(imageLeave);
                images.add(imageLeave1);
                //images.add("jjkgfg");
                	
                	count = i;
                		
                		
                		
                	//Log.d("array", "kk"+images.toString());
                	//Toast.makeText(getApplicationContext(), images.toString(), Toast.LENGTH_LONG).show();
                	
                	/*try {*/
        				//Getting the Bitmap from Gallery
                		/*String urt = uris[0].getPath().toString();
                		Log.d("tag", ""+urt);
                		File imgFile = new  File(urt);
                		FileInputStream fis = null;
                		try {
                		    fis = new FileInputStream(imgFile);
                		    } catch (FileNotFoundException e) {
                		    e.printStackTrace();
                		}
                		//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                		//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                		imageLeave = getStringImage(fis);*/
                		
                		//bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
        				//Toast.makeText(getApplicationContext(), imageLeave.toString(), Toast.LENGTH_LONG).show();
                		Log.d("harsh", "kkkco"+images.toString());
        			/*} catch (IOException e) {
        				e.printStackTrace();
        			}*/
        			
        			
                }
			}
	}

	
	public String getStringImage(FileInputStream fis){
		Bitmap bm = BitmapFactory.decodeStream(fis);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bm.compress(Bitmap.CompressFormat.JPEG, 100 , baos);    
		byte[] b = baos.toByteArray(); 
		String encImage = Base64.encodeToString(b, Base64.DEFAULT);
		return encImage;
	}
	
	private void showMedia() {
		// Remove all views before
		// adding the new ones.
		mSelectedImagesContainer.removeAllViews();
		
		Iterator<Uri> iterator = mMedia.iterator();
		ImageInternalFetcher imageFetcher = new ImageInternalFetcher(getActivity(), 500);
		while(iterator.hasNext()) {
			Uri uri = iterator.next();
			//Toast.makeText(getActivity(), uri.toString(), Toast.LENGTH_SHORT).show();
			
			// showImage(uri);
			Log.i(TAG, " uri: " + uri);
			if(mMedia.size() >= 1) {
                mSelectedImagesContainer.setVisibility(View.VISIBLE);
            }
			
			View imageHolder = LayoutInflater.from(getActivity()).inflate(R.layout.media_layout, null);
			
			// View removeBtn = imageHolder.findViewById(R.id.remove_media);
			// initRemoveBtn(removeBtn, imageHolder, uri);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
            
            if(!uri.toString().contains("content://")) {
    			// probably a relative uri
            	uri = Uri.fromFile(new File(uri.toString()));
            	
    		}
            
            imageFetcher.loadImage(uri, thumbnail);
            
            mSelectedImagesContainer.addView(imageHolder);
            
            // set the dimension to correctly 
            // show the image thumbnail.
            int wdpx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
            int htpx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
            //Toolbar toolBar;
		}
	}

	private  boolean checkAndRequestPermissions() {
		int permissionSendMessage = ContextCompat.checkSelfPermission(getActivity(),
				android.Manifest.permission.READ_EXTERNAL_STORAGE);
		int locationPermission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
		List<String> listPermissionsNeeded = new ArrayList<>();
		if (locationPermission != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
		}
		if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
		}
		if (!listPermissionsNeeded.isEmpty()) {
			ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),STORAGE_PERMISSION_CODE);
			return false;
		}
		return true;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		//Checking the request code of our request
		if(requestCode == STORAGE_PERMISSION_CODE){

			//If permission is granted
			if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

				//Displaying a toast
				//Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
				getImages();
				/*if(count1 ==0) {
					requestStoragePermission1();
				}

				else
				{
					getImages();
				}*/
			}else{
				//Displaying another toast if permission is not granted
				Toast.makeText(getActivity(),"Oops you just denied the permission", Toast.LENGTH_LONG).show();
			}
		}
	}
}
