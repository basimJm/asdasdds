package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.schoofi.adapters.StudentResultCOScholasticSelfAwarenessAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentHealthStatusResult extends AppCompatActivity {

    ImageView back;

    private JSONArray jsArray,studentHealthStatusArray;
    String termId = "";
    Button next;

    TextView height,weight,bloodGroup,visionL,visionR,dentalHygiene;
    int pos=0,pos1=0,pos2=0,pos3=0,pos4=0,pos5=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_health_status_result);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        height = (TextView) findViewById(R.id.text_height);
        weight = (TextView) findViewById(R.id.text_weight);
        bloodGroup = (TextView) findViewById(R.id.text_blood_group);
        visionL = (TextView) findViewById(R.id.text_visionL);
        visionR = (TextView) findViewById(R.id.text_visionR);
        dentalHygiene = (TextView) findViewById(R.id.text_dentalHygiene);

        termId = getIntent().getStringExtra("termId");

        initData4();



    }

    private void initData4()
    {


        try
        {
            com.android.volley.Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_CO_SCHOLASTIC_RESULTS+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&device_id="+Preferences.getInstance().deviceId+"&sec_id="+Preferences.getInstance().studentSectionId+"&cls_id="+Preferences.getInstance().studentClassId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1+"&stu_id="+Preferences.getInstance().studentId);
            if(e == null)
            {
                studentHealthStatusArray= null;
            }
            else
            {
                studentHealthStatusArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentHealthStatusArray!= null)
        {
            try {
                ArrayList<String> list = new ArrayList<String>();

                for(int i=0;i<studentHealthStatusArray.length();i++)
                {

                    if(studentHealthStatusArray.getJSONObject(i).getString("grading_area").matches("Health Status"))
                    {
                        list.add(studentHealthStatusArray.get(i).toString());
                    }
                    String list1= list.toString();
                    String list2 = "{"+'"'+"Result"+'"'+":"+list1+"}";
                    JSONObject jsonObject = new JSONObject(list2);
                    jsArray = jsonObject.getJSONArray("Result");

                    for(int j=0;j<jsArray.length();j++)
                    {
                        if(studentHealthStatusArray.getJSONObject(j).getString("descriptive_indicator").matches("Height"))
                        {
                            pos=j;
                        }

                        else
                        {

                        }
                            if(studentHealthStatusArray.getJSONObject(j).getString("descriptive_indicator").matches("Weight"))
                            {
                                pos1=j;
                            }

                            else
                            {

                            }
                            if(studentHealthStatusArray.getJSONObject(j).getString("descriptive_indicator").matches("Weight"))
                            {
                                pos1=j;
                            }

                        else
                            {

                            }
                                if(studentHealthStatusArray.getJSONObject(j).getString("descriptive_indicator").matches("Blood Group"))
                                {
                                    pos2=j;
                                }

                                else
                                {

                                }
                                if(studentHealthStatusArray.getJSONObject(j).getString("descriptive_indicator").matches("Vision (L)"))
                                {
                                    pos3=j;
                                }

                                else
                                {

                                }
                                if(studentHealthStatusArray.getJSONObject(j).getString("descriptive_indicator").matches("Vision (R)"))
                                {
                                    pos4=j;
                                }

                                else
                                {

                                }
                                if(studentHealthStatusArray.getJSONObject(j).getString("descriptive_indicator").matches("Dental Hygiene"))
                                {
                                    pos5=j;
                                }
                        else
                                {

                                }
                    }

                    //Utils.showToast(getApplicationContext(),"kkk"+String.valueOf(pos));

                    height.setText("Height: "+studentHealthStatusArray.getJSONObject(pos).getString("evaluation"));
                    weight.setText("Weight: "+studentHealthStatusArray.getJSONObject(pos1).getString("evaluation"));
                    bloodGroup.setText("Blood Group: "+studentHealthStatusArray.getJSONObject(pos2).getString("evaluation"));
                    visionL.setText("Vision(L): "+studentHealthStatusArray.getJSONObject(pos3).getString("evaluation"));
                    visionR.setText("Vision(R): "+studentHealthStatusArray.getJSONObject(pos4).getString("evaluation"));
                    dentalHygiene.setText("Dental Hygiene: "+studentHealthStatusArray.getJSONObject(pos5).getString("evaluation"));





                }
            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }
}
