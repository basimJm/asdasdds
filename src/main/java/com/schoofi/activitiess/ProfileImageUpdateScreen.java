package com.schoofi.activitiess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class ProfileImageUpdateScreen extends AppCompatActivity {

    private Button choose,upload,cancel;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    int count1 = 0;
    String image;
    int count = 0;
    int count2 = 0;
    private Bitmap bitmap;
    private static final int CAMERA_REQUEST = 1888;
    //String image = getStringImage(bitmap);
    private int PICK_IMAGE_REQUEST = 1;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String date1, date2;
    String subject, details;
    private int STORAGE_PERMISSION_CODE = 23;
    int f;
    String type;
    ImageView back;
    String UPLOAD_URL = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_PARENT_IMAGE_UPLOAD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_profile_image_update_screen);



        choose = (Button) findViewById(R.id.btn_student_leave_choose);
        upload = (Button) findViewById(R.id.btn_student_leave_submit);
        cancel = (Button) findViewById(R.id.btn_student_leave_cancel);
        type = getIntent().getStringExtra("type");
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				/*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
                if (checkAndRequestPermissions()) {
                    selectImage();
                    f = 1;
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image.matches("") || image.matches("null"))
                {
                    Utils.showToast(getApplicationContext(),"select image first");
                }

                else {
                    postLeave();
                }
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileImageUpdateScreen.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),STORAGE_PERMISSION_CODE);
            return false;
        }
        return true;
    }

    private void showFileChooser() {
        //Preferences.getInstance().savePreference(StudentNewLeave.this);
        Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    //gggg= http://www.simplifiedcoding.net/android-volley-tutorial-to-upload-image-to-server/;

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Preferences.getInstance().loadPreference(StudentNewLeave.this);
		studentId = Preferences.getInstance().studentId;
		System.out.println(studentId);

		if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			Uri filePath = data.getData();
			//Utils.showToast(getApplicationContext(), filePath.toString());
			Log.d("harsh", "kk" + filePath.toString());
			Preferences.getInstance().loadPreference(StudentNewLeave.this);
			studentId = Preferences.getInstance().studentId;
			try {
				//Getting the Bitmap from Gallery
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
				//Setting the Bitmap to ImageView
				imageLeave.setImageBitmap(bitmap);

				image = getStringImage(bitmap);
				//image = getStringImage(bitmap);
				//System.out.println(image);
				//Utils.showToast(getApplicationContext(), image);
			} catch (IOException e) {
				e.printStackTrace();
			}


		}
	}*/

	/*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			image = getStringImage(photo);
			Utils.showToast(getApplicationContext(),image);
			//imageView.setImageBitmap(photo);
		}
	}*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                Preferences.getInstance().loadPreference(ProfileImageUpdateScreen.this);


                try {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                    image = getStringImage(thumbnail);

                }
                catch (OutOfMemoryError e)
                {
                    Utils.showToast(getApplicationContext(),"Image size too high Please select different image");
                }
                //Utils.showToast(getApplicationContext(),image);
				/*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
				File destination = new File(Environment.getExternalStorageDirectory(),
						System.currentTimeMillis() + ".jpg");
				FileOutputStream fo;
				try {
					destination.createNewFile();
					fo = new FileOutputStream(destination);
					fo.write(bytes.toByteArray());
					fo.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
                //ivImage.setImageBitmap(thumbnail);
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                //String[] projection = {MediaStore.MediaColumns.DATA};
                Log.d("harsh", "kk" + selectedImageUri.toString());
                Preferences.getInstance().loadPreference(ProfileImageUpdateScreen.this);
                //studentId = Preferences.getInstance().studentId;
                try {
                    //Getting the Bitmap from Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    //Setting the Bitmap to ImageView
                    //imageLeave.setImageBitmap(bitmap);
                    image = getStringImage(bitmap);


                    //Utils.showToast(getApplicationContext(),image);
                    //image = getStringImage(bitmap);
                    //System.out.println(image);
                    //Utils.showToast(getApplicationContext(), image);
                }
                catch (OutOfMemoryError e)
                {
                    Utils.showToast(getApplicationContext(),"Image size too high Please select different image");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

				/*CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
						null);
				Cursor cursor = cursorLoader.loadInBackground();
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
				cursor.moveToFirst();
				String selectedImagePath = cursor.getString(column_index);
				Bitmap bm;
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(selectedImagePath, options);
				final int REQUIRED_SIZE = 200;
				int scale = 1;
				while (options.outWidth / scale / 2 >= REQUIRED_SIZE
						&& options.outHeight / scale / 2 >= REQUIRED_SIZE)
					scale *= 2;
				options.inSampleSize = scale;
				options.inJustDecodeBounds = false;
				bm = BitmapFactory.decodeFile(selectedImagePath, options);*/
                //ivImage.setImageBitmap(bm);
            }
        }
    }


    private void postLeave()
    {
        //Utils.showToast(getApplicationContext(), ""+date2+date1);
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        //Preferences.getInstance().loadPreference(StudentNewLeave.this);
        //System.out.println(Preferences.getInstance().studentId);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //System.out.println(s);
                        //Showing toast message of the response
                        Toast.makeText(ProfileImageUpdateScreen.this, s , Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        System.out.println(volleyError);

                        //Showing toast
                        Utils.showToast(getApplicationContext(), "error2");
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                //Converting Bitmap to String

                // Preferences.getInstance().loadPreference(StudentNewLeave.this);
                //studentId = Preferences.getInstance().studentId;


                //http://www.androidbegin.com/tutorial/android-populating-spinner-json-tutorial/
                //Getting Image Name
                // String name = editTextName.getText().toString().trim();

                //Creating parameters

                Map<String,String> params = new Hashtable<String, String>();
                //image = getStringImage(bitmap);

                //Adding parameters
                params.put("file", image);
                params.put("stu_id", Preferences.getInstance().studentId);
                params.put("email", Preferences.getInstance().userEmailId);
                params.put("u_id", Preferences.getInstance().userId);



                params.put("token", Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("type",type);

                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);

                //params.put("token",Preferences.getInstance().token);
                // params.put("device_id", Preferences.getInstance().deviceId);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}

