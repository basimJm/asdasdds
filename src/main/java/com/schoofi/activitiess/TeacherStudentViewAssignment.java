package com.schoofi.activitiess;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoofi.adapters.StudentFeedBackGridAdapter;
import com.schoofi.constants.AppConstants;

import java.util.ArrayList;

public class TeacherStudentViewAssignment extends AppCompatActivity {

    ArrayList<String> myList;
    GridView studentFeedBackImagesGrid;
    StudentFeedBackGridAdapter studentFeedBackGridAdapter;
    TextView description,title,submitBy,subject;
    String description1,title1,submitBy1,subject1;
    ImageView back;
    View view1,view2,view3,view4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.teacher_view_assignment_new_layout);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        studentFeedBackImagesGrid = (GridView) findViewById(R.id.studentHomeGridView);




        description1 = getIntent().getStringExtra("desc");
        title1 = getIntent().getStringExtra("title");
        submitBy1 = getIntent().getStringExtra("last_date");
        subject1 = getIntent().getStringExtra("subject");
        title = (TextView) findViewById(R.id.text_title);
        submitBy = (TextView) findViewById(R.id.text_submit_by);
        subject = (TextView) findViewById(R.id.text_subject_name);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);

        title.setText(title1);
        submitBy.setText("Submit By: "+submitBy1);
        subject.setText(subject1);

        if(title1.matches("") || title1.matches("null"))
        {
            view1.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
        }

        if(submitBy1.matches("") || submitBy1.matches("null"))
        {
            view2.setVisibility(View.GONE);
            submitBy.setVisibility(View.GONE);
        }

        if(subject1.matches("") || subject1.matches("null"))
        {
            view3.setVisibility(View.GONE);
            subject.setVisibility(View.GONE);
        }

        description = (TextView) findViewById(R.id.text_description);
        if(description1.matches("") || description1.matches("null"))
        {
            description.setVisibility(View.GONE);
            view4.setVisibility(View.GONE);
        }
        else
        {
            description.setVisibility(View.VISIBLE);
            description.setText("Description: "+description1);
        }

        if(getIntent().getStringExtra("array2").matches("1"))
        {
            studentFeedBackImagesGrid.setVisibility(View.GONE);
        }

        else {

            myList = (ArrayList<String>) getIntent().getSerializableExtra("array");
            studentFeedBackGridAdapter = new StudentFeedBackGridAdapter(getApplicationContext(), myList);
            studentFeedBackImagesGrid.setAdapter(studentFeedBackGridAdapter);

            studentFeedBackImagesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub

                    if (myList.get(position).endsWith("DOC") || myList.get(position).endsWith("doc") || myList.get(position).endsWith("Doc") || myList.get(position).endsWith("docx") || myList.get(position).endsWith("Docx") || myList.get(position).endsWith("DOCX") || myList.get(position).endsWith("pdf") || myList.get(position).endsWith("PDF") || myList.get(position).endsWith("Pdf") || myList.get(position).endsWith("txt") || myList.get(position).endsWith("Txt") || myList.get(position).endsWith("TXT") || myList.get(position).endsWith("mp3") || myList.get(position).endsWith("MP3") || myList.get(position).endsWith("Mp3")  || myList.get(position).endsWith("mp4")  || myList.get(position).endsWith("MP4") || myList.get(position).endsWith("Mp4") || myList.get(position).endsWith("MOV") || myList.get(position).endsWith("mov") || myList.get(position).endsWith("Mov")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL + myList.get(position)));
                        startActivity(intent);
                    } else
                        if(myList.get(position).endsWith("PNG") || myList.get(position).endsWith("png") || myList.get(position).endsWith("Png") || myList.get(position).endsWith("JPG") || myList.get(position).endsWith("jpg") || myList.get(position).endsWith("Jpg") || myList.get(position).endsWith("JPEG") || myList.get(position).endsWith("Jpeg") || myList.get(position).endsWith("jpeg")){
                        Intent intent = new Intent(TeacherStudentViewAssignment.this, StudentImageFeedBackSlider.class);
                        intent.putExtra("array", myList);
                        intent.putExtra("position", position);
                        intent.putExtra("value","3");
                        intent.putExtra("param","p");
                        startActivity(intent);
                    }
                        else
                        {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL + myList.get(position)));
                            startActivity(intent);
                        }


                }
            });
        }
    }
    }

