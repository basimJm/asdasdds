package com.schoofi.activitiess;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.schoofi.adapters.StudentHomeScreenAdapter;
import com.schoofi.adapters.StudentHomeScreenTabletAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.schoofi.activitiess.R.id.parent;
import static com.schoofi.activitiess.R.id.view;


public class AdminHealthCardOptionsActivity extends AppCompatActivity {

    GridView studentHomeScreenGridView;
    ImageView settings,back;
    int []studentHomeScreenIcon = {R.drawable.medicalhistory,R.drawable.vaccinationstatus,R.drawable.checkup,R.drawable.diagonsis,R.drawable.medicalform};
    int []studentHomeScreenIcon1 = {R.drawable.medicalhistorytablet,R.drawable.vaccinationstatustablet,R.drawable.checkuptablet,R.drawable.diagonsistablet,R.drawable.medicalhistorytablet};
    String []studentHomeScreenIconName = {"Medical History","Vaccination","Insurance","Check-Up","Check-Up Form"};
    int[] COLORS = {
            Color.rgb(102, 155, 76), Color.rgb(10, 173, 162), Color.rgb(248, 88, 129),
            Color.rgb(224, 157, 64), Color.rgb(224, 157, 64), Color.rgb(224, 157, 64)
    };
    ArrayList<String> studentHomeScreenIconNamefinal = new ArrayList<String>();
    ArrayList<Integer> studentHomeScreenIconFinal = new ArrayList<Integer>();
    ArrayList<Integer> studentHomeScreenIconFinal1 = new ArrayList<Integer>();
    StudentHomeScreenAdapter studentHomeScreenAdapter;
    StudentHomeScreenTabletAdapter studentHomeScreenTabletAdapter;
    TextView userName;
    private TextView tickerView;
    Button buttonGone,buttonLogout;
    String u = "";
    private JSONArray adminHealthCardInitialArray;
    private TextView studentName,className,sectionName,studentSex,studentAdmissionNumber,studentDob,studentHeight,studentWeight,studentAllergies,studentBloodGroup,studentDisability,studentBirthMark;
    private ImageView studentProfilePicture;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    String date1,date2,date3,date4,date5,date6;
    String studentDob2,studentFatherDob2,studentMotherDob2,studentAdmissionDate2,studentAdmissionDate3,studentDob3,studentFatherDob3,studentMotherDob3;
    Date studentDob1,studentFatherDob1,studentMotherDob1,studentAdmissionDate1;
    Date date7,date8,date9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admin_health_card_options);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.0){
            // 6.5inch device or bigger
            u = "1";
        }else{
            // smaller device
            u = "2";

        }

        studentName = (TextView) findViewById(R.id.text_student_name);
        studentDob = (TextView) findViewById(R.id.text_student_dob);
        studentAdmissionNumber = (TextView) findViewById(R.id.text_student_admissionNo);
        className = (TextView) findViewById(R.id.text_student_class);
        sectionName = (TextView) findViewById(R.id.text_student_section);
        studentSex = (TextView) findViewById(R.id.text_student_sex);
        studentBloodGroup = (TextView) findViewById(R.id.text_student_email);
        studentHeight = (TextView) findViewById(R.id.text_student_height);
        studentWeight = (TextView) findViewById(R.id.text_student_weight);
        studentDisability = (TextView) findViewById(R.id.text_student_disability);
        studentBirthMark = (TextView) findViewById(R.id.text_student_birth_mark);
        studentAllergies = (TextView) findViewById(R.id.text_student_allergies);
        studentProfilePicture = (ImageView) findViewById(R.id.student_profile);

        RequestQueue queue = VolleySingleton.getInstance(AdminHealthCardOptionsActivity.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_PROFILE+"?token="+Preferences.getInstance().token+"&device_id="+ Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(AdminHealthCardOptionsActivity.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(AdminHealthCardOptionsActivity.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("profile"))
                    {
                        adminHealthCardInitialArray= new JSONObject(response).getJSONArray("profile");
                        if(null!=adminHealthCardInitialArray && adminHealthCardInitialArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = adminHealthCardInitialArray.toString().getBytes();
                            VolleySingleton.getInstance(AdminHealthCardOptionsActivity.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_PROFILE+"?token="+Preferences.getInstance().token+"&device_id="+ Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId,e);
									/*username.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_name"));
									userSchool.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("school_name"));
									userClass.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("class_name")+" "+responseObject.getJSONArray("profile").getJSONObject(0).getString("section_name"));
									userFathersName.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("father_name"));
									userMotherName.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_name"));*/
                            //Picasso.with(UserProfile.this).load(AppConstants.SERVER_URLS.SERVER_URL+responseObject.getJSONArray("profile").getJSONObject(0).getString("picture")).placeholder(R.drawable.profilebig).error(R.drawable.profilebig).into(userProfileImage);
                            //Glide.with(UserProfile.this).load(AppConstants.SERVER_URLS.SERVER_URL+responseObject.getJSONArray("profile").getJSONObject(0).getString("picture")).crossFade().placeholder(R.drawable.profilebig).error(R.drawable.profilebig).into(userProfileImage);

                            mDrawableBuilder = TextDrawable.builder().round();
                            TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_name").charAt(0)), R.color.blue);

                            Glide.with(AdminHealthCardOptionsActivity.this).load(AppConstants.SERVER_URLS.IMAGE_URL+responseObject.getJSONArray("profile").getJSONObject(0).getString("picture")).placeholder(textDrawable).error(textDrawable).into(studentProfilePicture);

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_name").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_name").matches("null"))
                            {
                                studentName.setText("-");
                            }

                            else
                            {
                                studentName.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_name"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("date_of_birth").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("date_of_birth").matches("null") || responseObject.getJSONArray("profile").getJSONObject(0).getString("date_of_birth").matches("0000-00-00"))
                            {
                                studentDob.setText("DOB "+"-");
                            }

                            else
                            {
                                studentDob2 = responseObject.getJSONArray("profile").getJSONObject(0).getString("date_of_birth");
                                try {
                                    studentDob1 = formatter.parse(studentDob2);
                                } catch (ParseException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }

                                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                                studentDob3 = formatter1.format(studentDob1);
                                studentDob.setText("Dob: "+studentDob3);

                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("admn_No").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("admn_No").matches("null"))
                            {
                                studentAdmissionNumber.setText("Admn No: -");
                            }

                            else
                            {
                                studentAdmissionNumber.setText("Admn No: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("admn_No"));
                            }



                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("gender").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("gender").matches("null"))
                            {
                                studentSex.setText("Gender: -");
                            }

                            else
                            {
                                studentSex.setText("Gender: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("gender"));
                            }



                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("class_name").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("class_name").matches("null"))
                            {
                                className.setText("Class: -");
                            }

                            else
                            {
                                className.setText("Class: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("class_name"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("section_name").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("section_name").matches("null"))
                            {
                                sectionName.setText("Section: -");
                            }

                            else
                            {
                                sectionName.setText("Section: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("section_name"));
                            }



                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("disability").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("disability").matches("null"))
                            {
                                studentDisability.setText("Disability: -");
                            }

                            else
                            {
                                studentDisability.setText("Disability: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("disability"));
                            }



                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("birth_mark").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("birth_mark").matches("null"))
                            {
                                studentBirthMark.setText("Birth Mark: -");
                            }

                            else
                            {
                                studentBirthMark.setText("Birth Mark: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("birth_mark"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("height").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("height").matches("null"))
                            {
                                studentHeight.setText("Height: -");
                            }

                            else
                            {
                                studentHeight.setText("Height: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("height"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("weight").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("weight").matches("null"))
                            {
                                studentWeight.setText("Weight: -");
                            }

                            else
                            {
                                studentWeight.setText("Weight: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("weight"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("blood_group").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("blood_group").matches("null"))
                            {
                                studentBloodGroup.setText("Blood Group: -");
                            }

                            else
                            {
                                studentBloodGroup.setText("Blood Group: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("blood_group"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("allergies").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("allergies").matches("null"))
                            {
                                studentAllergies.setText("Alergies: -");
                            }

                            else
                            {
                                studentAllergies.setText("Alergies: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("allergies"));
                            }


















                        }


                    }
                    else
                        Utils.showToast(AdminHealthCardOptionsActivity.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(AdminHealthCardOptionsActivity.this, "Error fetching modules! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(AdminHealthCardOptionsActivity.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(AdminHealthCardOptionsActivity.this, "Unable to fetch data, kindly enable internet settings!");
        }

        int size=0;

        for(int i=0;i<studentHomeScreenIcon.length;i++)
        {
            studentHomeScreenIconFinal.add(studentHomeScreenIcon[i]);
            studentHomeScreenIconFinal1.add(studentHomeScreenIcon1[i]);
            studentHomeScreenIconNamefinal.add(studentHomeScreenIconName[i]);
            size++;
        }

        for(int uw=0;uw<studentHomeScreenIcon.length;uw++) {

            if (Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6")) {
                if (studentHomeScreenIconNamefinal.get(uw).matches("Check-Up Form")) {
                    studentHomeScreenIconFinal.remove(uw);
                    studentHomeScreenIconFinal1.remove(uw);
                    studentHomeScreenIconNamefinal.remove(uw);
                }
            }
        }

        studentHomeScreenGridView = (GridView) findViewById(R.id.healthCardGridView);

        if(u.matches("2"))
        {
            studentHomeScreenGridView.setAdapter(new StudentHomeScreenAdapter(AdminHealthCardOptionsActivity.this, studentHomeScreenIconFinal,studentHomeScreenIconNamefinal,COLORS));
        }

        else
        {
            studentHomeScreenGridView.setAdapter(new StudentHomeScreenTabletAdapter(AdminHealthCardOptionsActivity.this, studentHomeScreenIconFinal1,studentHomeScreenIconNamefinal,COLORS));

        }

        studentHomeScreenGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                switch (position) {
                    case 0: Intent intent = new Intent(AdminHealthCardOptionsActivity.this,AdminHealthCardCommonListViewActivity.class);
                        intent.putExtra("value","2");
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent1 = new Intent(AdminHealthCardOptionsActivity.this,AdminHealthCardCommonListViewActivity.class);
                        intent1.putExtra("value","1");
                        startActivity(intent1);
                        break;


                    case 2: Intent intent3 = new Intent(AdminHealthCardOptionsActivity.this,AdminHealthCardCommonListViewActivity.class);
                        intent3.putExtra("value","3");
                        startActivity(intent3);
                        break;

                    case 3: Intent intent4 = new Intent(AdminHealthCardOptionsActivity.this,ChechUPHistory.class);
                        startActivity(intent4);
                        break;

                    case 4: Intent intent5 = new Intent(AdminHealthCardOptionsActivity.this,HealthEventActivity.class);
                        startActivity(intent5);
                        break;
                }

            }
        });
    }


}
