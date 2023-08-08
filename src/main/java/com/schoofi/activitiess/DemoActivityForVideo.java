package com.schoofi.activitiess;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.googlecode.mp4parser.BasicContainer;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.schoofi.utils.FilePath;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DemoActivityForVideo extends AppCompatActivity {

    private static final String TAG = FragmentsActivity.class.getSimpleName();
    private static final int PICK_FILE_REQUEST = 1;
    private static final int READ_REQUEST_CODE = 42;
    private String selectedFilePath,selectedFilePath1;
    private int STORAGE_PERMISSION_CODE = 23;
    Uri uri;
    Button video1,video2,merge;
    private int GALLERY = 1;
    ArrayList<String> path ;
    private String selectedVideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_for_video);

        video1 = (Button) findViewById(R.id.video1);
        video2 = (Button) findViewById(R.id.video2);
        merge = (Button) findViewById(R.id.submit);
        path = new ArrayList<>();

        video1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideoFromGallary();
            }
        });

        video2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideoFromGallary();
            }
        });

        merge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendTwoVideos(path.get(0),path.get(1));
            }
        });
    }




    private void showFileChooser1() {


        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("result",""+resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            Log.d("what","cancle");
            return;
        }
        if (requestCode == GALLERY) {
            Log.d("what", "gale");
            if (data != null) {
                Uri selectedImage = data.getData();
                File selectedCachedFile =  getImageUriFile(DemoActivityForVideo.this,selectedImage);
                if(selectedCachedFile == null){
                    return;
                }
                selectedVideoPath =selectedCachedFile.getAbsolutePath();
                path.add(selectedVideoPath);

                Log.d("paths",path.toString());


            }
        }





    }

    /*public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }*/


    public static File getImageUriFile(Context context, Uri uri) {
        if (uri == null) return null;
        FileInputStream input = null;
        FileOutputStream output = null;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "MP4_" + timeStamp + "_.mp4";

        String filePath = new File(context.getCacheDir(), imageFileName).getAbsolutePath();
        try {
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
            if (pfd == null) return null;
            FileDescriptor fd = pfd.getFileDescriptor();
            input = new FileInputStream(fd);
            output = new FileOutputStream(filePath);
            int read;
            byte[] bytes = new byte[4096];
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }

            input.close();
            output.close();
            return new File(filePath);
        } catch (IOException ignored) {
        }
        return null;
    }

    private String appendTwoVideos(String firstVideoPath, String secondVideoPath)
    {
        try {
            Movie[] inMovies = new Movie[2];

            inMovies[0] = MovieCreator.build(firstVideoPath);
            inMovies[1] = MovieCreator.build(secondVideoPath);

            List<Track> videoTracks = new LinkedList<>();
            List<Track> audioTracks = new LinkedList<>();

            for (Movie m : inMovies) {
                for (Track t : m.getTracks()) {
                    if (t.getHandler().equals("soun")) {
                        audioTracks.add(t);
                    }
                    if (t.getHandler().equals("vide")) {
                        videoTracks.add(t);
                    }
                }
            }

            Movie result = new Movie();

            if (audioTracks.size() > 0) {
                result.addTrack(new AppendTrack(audioTracks
                        .toArray(new Track[audioTracks.size()])));
            }
            if (videoTracks.size() > 0) {
                result.addTrack(new AppendTrack(videoTracks
                        .toArray(new Track[videoTracks.size()])));
            }

            BasicContainer out = (BasicContainer) new DefaultMp4Builder().build(result);

            @SuppressWarnings("resource")
            FileChannel fc = new RandomAccessFile(Environment.getExternalStorageDirectory() + "/wishbyvideo.mp4", "rw").getChannel();
            out.writeContainer(fc);
            fc.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/wishbyvideo.mp4";
        Log.d("pppp",mFileName);
        return mFileName;
    }

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int locationPermission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (locationPermission1 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),STORAGE_PERMISSION_CODE);
            return false;
        }
        return true;
    }

    public void chooseVideoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(galleryIntent, GALLERY);
    }




}