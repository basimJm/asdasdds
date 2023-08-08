package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mzelzoghbi.zgallery.ZGallery;
import com.mzelzoghbi.zgallery.entities.ZColor;
import com.schoofi.adapters.DiaryMultiLevelListAdapter;
import com.schoofi.adapters.SchoolReplyListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.DiarySubVO;
import com.schoofi.utils.DiaryVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SchoolDiaryDetailScreen extends AppCompatActivity {

    private ImageView back;
    private ImageView profile,icon;
    private RatingBar ratingBar;
    private TextView title,description,reply;
    private int groupPosition,childPosition;
    String diaryModuleId,diarySubId,type;
    private ListView replyListView;
    private JSONArray schoolDiaryArray;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    private SchoolReplyListAdapter schoolReplyListAdapter;
    ArrayList<String> myList;
    ArrayList<String> myList1;
    String array,rating;
    ArrayList aList= new ArrayList();
    int position;
    String file,time;

    ImageView imageView1,imageView2,imageView3;
    FrameLayout frameLayout;
    TextView number,time1,parameterId;
    String createdDate,ratingParameterId;
    String result;
    Date date3=null;
    LinearLayout linearLayout;
    String fileType;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("School Diary Detail Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_school_diary_detail_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        groupPosition = getIntent().getExtras().getInt("groupPosition");
        childPosition = getIntent().getExtras().getInt("childPosition");
        diaryModuleId = getIntent().getStringExtra("diaryModuleId");
        diarySubId = getIntent().getStringExtra("diarySubModuleId");
        replyListView = (ListView) findViewById(R.id.list_reply);
        reply = (TextView) findViewById(R.id.img_plus);
        title = (TextView) findViewById(R.id.text_title);
        description = (TextView) findViewById(R.id.text_description);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        profile = (ImageView) findViewById(R.id.text_drawable);
        parameterId = (TextView) findViewById(R.id.text_rating_parameterId);
       /* icon = (ImageView) findViewById(R.id.attatchment);*/
        type = getIntent().getStringExtra("type");
        rating = getIntent().getStringExtra("rating");
        createdDate = getIntent().getStringExtra("date1");
        ratingParameterId = getIntent().getStringExtra("ratingParameter");

        time1 = (TextView) findViewById(R.id.text_created_date);

        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);

        imageView1 = (ImageView) findViewById(R.id.imageView_1);
        imageView2 = (ImageView) findViewById(R.id.imageView_2);
        imageView3 = (ImageView) findViewById(R.id.imageView_3);
        linearLayout = (LinearLayout) findViewById(R.id.yu);

        time = getIntent().getStringExtra("time");

        number = (TextView) findViewById(R.id.text_numberDate);

        file = getIntent().getStringExtra("file");

        fileType = getIntent().getStringExtra("attachment");


        postLocalRegistration();

        if(file.matches("") || file.matches("null"))
        {
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            frameLayout.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.GONE);
        }

        else if (fileType.matches("file"))
        {
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            frameLayout.setVisibility(View.INVISIBLE);
            imageView1.setImageResource(R.drawable.file);
        }

        else {

                aList = new ArrayList<String>(Arrays.asList(file.split(",")));

            myList1 = new ArrayList<String>();

            for (int i = 0; i < aList.size(); i++) {
                myList1.add(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(i));
                //System.out.println(myList1.get(i));
            }

            if (aList.isEmpty()) {
                imageView1.setVisibility(View.INVISIBLE);
                imageView2.setVisibility(View.INVISIBLE);
                frameLayout.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);
            }


        /*else
        if(aList)
        {

        }

*/
            else if (aList.size() == 1 && aList.size() < 2) {
                imageView2.setVisibility(View.INVISIBLE);
                frameLayout.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(0)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(imageView1);
            }
        /*if(aList.size()==2) {
            Log.d("kkk","oiu"+aList.get(1));*/
            else if (aList.size() > 1 && aList.size() < 3) {
                Glide.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(0)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(imageView1);
                Glide.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(1)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(imageView2);
                frameLayout.setVisibility(View.INVISIBLE);
                imageView2.setVisibility(View.VISIBLE);

            } else if (aList.size() == 3 && aList.size() < 4) {
                Glide.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(0)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(imageView1);
                Glide.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(1)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(imageView2);
                Glide.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(2)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(imageView3);
                number.setText("");
                frameLayout.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);
            } else if (aList.size() > 3) {
                Glide.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(0)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(imageView1);
                Glide.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(1)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(imageView2);
                Glide.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(2)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(imageView3);
                number.setText(String.valueOf(aList.size() - 2));
                frameLayout.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);

            }

        }

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fileType.matches("file"))
                {
                    aList = new ArrayList<String>(Arrays.asList(file.split(",")));
                    Intent intent = new Intent(SchoolDiaryDetailScreen.this,TeacherStudentViewAssignment.class);
                    intent.putExtra("array", aList);
                    intent.putExtra("array2","2");
                    intent.putExtra("array1", "n");
                    intent.putExtra("asn_id", "1");
                    intent.putExtra("desc","null");
                    intent.putExtra("title","null");
                    intent.putExtra("subject","null");
                    intent.putExtra("last_date","null");

                    startActivity(intent);
                }

                else {

                    ZGallery.with(SchoolDiaryDetailScreen.this, myList1)
                            .setToolbarTitleColor(ZColor.WHITE) // toolbar title color
                            .setGalleryBackgroundColor(ZColor.WHITE).setSelectedImgPosition(0) // activity background color
                            .setToolbarColorResId(R.color.colorPrimary) // toolbar color
                            .setTitle("Zak Gallery") // toolbar title
                            .show();
                }

            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ZGallery.with(SchoolDiaryDetailScreen.this,myList1)
                        .setToolbarTitleColor(ZColor.WHITE) // toolbar title color
                        .setGalleryBackgroundColor(ZColor.WHITE).setSelectedImgPosition(1) // activity background color
                        .setToolbarColorResId(R.color.colorPrimary) // toolbar color
                        .setTitle("Zak Gallery") // toolbar title
                        .show();

            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ZGallery.with(SchoolDiaryDetailScreen.this,myList1)
                        .setToolbarTitleColor(ZColor.WHITE) // toolbar title color
                        .setGalleryBackgroundColor(ZColor.WHITE).setSelectedImgPosition(2) // activity background color
                        .setToolbarColorResId(R.color.colorPrimary) // toolbar color
                        .setTitle("Zak Gallery") // toolbar title
                        .show();

            }
        });



        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SchoolDiaryDetailScreen.this,DiaryReplyScreen.class);
                intent.putExtra("diarySubId",diarySubId);
                intent.putExtra("diaryId",diaryModuleId);
                startActivity(intent);
            }
        });

        if(type.matches("Note") || rating.matches("") || rating.matches("null") || type.matches("Participation"))
        {
            ratingBar.setVisibility(View.GONE);
        }

        else
        {
            ratingBar.setVisibility(View.VISIBLE);
            ratingBar.setRating(Float.parseFloat(rating));
        }



        /*icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    aList = new ArrayList<String>(Arrays.asList(schoolDiaryArray.getJSONObject(0).getString("image").split(",")));
                    myList1 = new ArrayList<String>();

                    for(int i = 0;i<aList.size();i++)
                    {
                        myList1.add(AppConstants.SERVER_URLS.IMAGE_URL+aList.get(i));
                        //System.out.println(myList1.get(i));
                    }



                    ZGallery.with(SchoolDiaryDetailScreen.this,myList1)
                            .setToolbarTitleColor(ZColor.WHITE) // toolbar title color
                            .setGalleryBackgroundColor(ZColor.WHITE).setSelectedImgPosition(0) // activity background color
                            .setToolbarColorResId(R.color.colorPrimary) // toolbar color
                            .setTitle("Zak Gallery") // toolbar title
                            .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/





    }

    protected void postLocalRegistration()
    {
        setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);


        final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.SCHOOL_DIARY_REPLY_LIST;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                // System.out.println(url1);


                try {
                    responseObject = new JSONObject(response);
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Replies Found");
                        loading.dismiss();
                    }
                    else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    } else if (responseObject.has("responseObject")) {
                        loading.dismiss();

                        schoolDiaryArray = new JSONObject(response).getJSONArray("responseObject");
                        // profileUrl = schoolDiaryArray.getJSONObject(0).getString("school_banner");



                        if(null!=schoolDiaryArray && schoolDiaryArray.length()>=0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = schoolDiaryArray.toString().getBytes();

                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_DIARY_URL + "?token=" + Preferences.getInstance().token + "&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&diary_sub_id="+diarySubId, e);

                            mDrawableBuilder = TextDrawable.builder().round();
                            TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(schoolDiaryArray.getJSONObject(0).getString("initials")), R.color.blue);
                            Glide.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL + schoolDiaryArray.getJSONObject(0).getString("sender_picture")).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(profile) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    profile.setImageDrawable(circularBitmapDrawable);
                                }
                            });

                            title.setText(schoolDiaryArray.getJSONObject(0).getString("title"));
                            description.setText(schoolDiaryArray.getJSONObject(0).getString("description"));

                            DateFormat format1 = new SimpleDateFormat("hh:mm:ss");
                            try {
                                Date date = format1.parse(time);
                                SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a");
                                result = format2.format(date);
                                //holder1.time.setText(result);



                            } catch (ParseException e2) {
                                e2.printStackTrace();
                            }


                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                date3 = formatter.parse(createdDate);
                            } catch (ParseException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }

                            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                            String result1 = formatter1.format(date3);
                            time1.setText(result1+" "+result);

                            if(ratingParameterId.matches("") || ratingParameterId.matches("null"))
                            {
                                parameterId.setVisibility(View.GONE);
                            }

                            else {
                                parameterId.setVisibility(View.VISIBLE);
                                parameterId.setText(ratingParameterId);
                            }

                             replyListView.invalidateViews();
                             schoolReplyListAdapter = new SchoolReplyListAdapter(SchoolDiaryDetailScreen.this,schoolDiaryArray);
                             replyListView.setAdapter(schoolReplyListAdapter);
                             schoolReplyListAdapter.notifyDataSetChanged();


                        }
                    }

                    else
                    {
                        System.out.println("kkk");
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong!!", Toast.LENGTH_LONG).show();
                    setProgressBarIndeterminateVisibility(false);
                    loading.dismiss();
                }


                // mParties = new String[]
                // dynamicToolbarColor();


                setProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Something went wrong!!", Toast.LENGTH_LONG).show();
                setProgressBarIndeterminateVisibility(false);
                loading.dismiss();
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();

                params.put("stu_id",Preferences.getInstance().studentId);
                params.put("token",Preferences.getInstance().token);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("diary_sub_id",diarySubId);
                params.put("value","1");
                /*if(diaryModuleId.matches("") || diaryModuleId.matches("null"))
                {
                    params.put("diary_module_id","A");
                }
                else {
                    params.put("diary_module_id", diaryModuleId);
                }

                if(subjectId.matches("") || subjectId.matches("null"))
                {
                    params.put("subject_id","A");
                }
                else {
                    params.put("subject_id", subjectId);
                }

                if(fromDate.matches("") || fromDate.matches("null"))
                {
                    params.put("from_date","");
                }

                else {
                    params.put("from_date", fromDate);
                }

                if(toDate.matches("") || toDate.matches("null"))
                {
                    params.put("to_date","");
                }

                else {
                    params.put("to_date", toDate);
                }

                if(teacherId.matches("") || teacherId.matches("null"))
                {
                    params.put("teacher_id",teacherId);
                }

                else {
                    params.put("teacher_id", teacherId);
                }*/
                //System.out.print(Preferences.getInstance().userId);




                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestObject);




    }





}
