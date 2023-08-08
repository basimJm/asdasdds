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

import com.schoofi.adapters.StudentHomeScreenAdapter;
import com.schoofi.adapters.StudentHomeScreenTabletAdapter;
import com.schoofi.utils.Preferences;

import java.util.ArrayList;


public class MedicalCoordinatorHomeScreen extends AppCompatActivity {

    GridView studentHomeScreenGridView;
    ImageView settings;
    int []studentHomeScreenIcon = {R.drawable.healthcard};
    int []studentHomeScreenIcon1 = {R.drawable.healthcardtablet};
    String []studentHomeScreenIconName = {"HEALTH CARD"};
    int[] COLORS = {
            Color.rgb(102, 155, 76), Color.rgb(10, 173, 162), Color.rgb(248, 88, 129),
            Color.rgb(224, 157, 64), Color.rgb(85, 218, 160), Color.rgb(94, 186, 220), Color.rgb(0, 174, 179),
            Color.rgb(227, 72, 62), Color.rgb(165, 104, 211), Color.rgb(251, 153, 99), Color.rgb(255,228,181),Color.rgb(255,228,181),Color.rgb(255,228,181),Color.rgb(255,228,181)
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_home_screen);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

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

        for(int i=0;i<studentHomeScreenIcon.length;i++)
        {
            studentHomeScreenIconFinal.add(studentHomeScreenIcon[i]);
            studentHomeScreenIconFinal1.add(studentHomeScreenIcon1[i]);
            studentHomeScreenIconNamefinal.add(studentHomeScreenIconName[i]);
        }

        studentHomeScreenGridView = (GridView) findViewById(R.id.studentHomeGridView);
        userName = (TextView) findViewById(R.id.txt_userName);
        buttonGone = (Button) findViewById(R.id.btn_gone);

        buttonGone.setVisibility(View.INVISIBLE);

        settings = (ImageView) findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicalCoordinatorHomeScreen.this,StudentSettings.class);
                startActivity(intent);
            }
        });

        userName.setText(Preferences.getInstance().Name);

        if(u.matches("2"))
        {
            studentHomeScreenGridView.setAdapter(new StudentHomeScreenAdapter(MedicalCoordinatorHomeScreen.this, studentHomeScreenIconFinal,studentHomeScreenIconNamefinal,COLORS));
        }

        else
        {
            studentHomeScreenGridView.setAdapter(new StudentHomeScreenTabletAdapter(MedicalCoordinatorHomeScreen.this, studentHomeScreenIconFinal1,studentHomeScreenIconNamefinal,COLORS));

        }

        studentHomeScreenGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                switch (position) {


                    case 0:
                        Intent intent = new Intent(MedicalCoordinatorHomeScreen.this, AdminHealthCard.class);
                        startActivity(intent);
                        break;








                }

            }
        });
    }


}
