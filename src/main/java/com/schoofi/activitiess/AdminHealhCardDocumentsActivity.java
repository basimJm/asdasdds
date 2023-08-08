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

import com.mzelzoghbi.zgallery.ZGallery;
import com.mzelzoghbi.zgallery.entities.ZColor;
import com.schoofi.adapters.StudentFeedBackGridAdapter;
import com.schoofi.constants.AppConstants;

import java.util.ArrayList;
import java.util.Arrays;

public class AdminHealhCardDocumentsActivity extends AppCompatActivity {

    ArrayList<String> myList;
    GridView studentFeedBackImagesGrid;
    StudentFeedBackGridAdapter studentFeedBackGridAdapter;
    ImageView back,share;
    ArrayList<String> myList1;
    String value,param;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_admin_healh_card_documents);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        studentFeedBackImagesGrid = (GridView) findViewById(R.id.studentHomeGridView);

        myList = (ArrayList<String>) getIntent().getSerializableExtra("array");
        //value = getIntent().getStringExtra("value");
        share = (ImageView) findViewById(R.id.img_upload);
        share.setVisibility(View.GONE);

        myList1 = new ArrayList<String>();
        position = getIntent().getExtras().getInt("position");
        param = getIntent().getStringExtra("param");

        //Utils.showToast(getApplicationContext(),String.valueOf(position)+param+value);

        for(int i=0;i<myList.size();i++)
        {
            myList1.add(AppConstants.SERVER_URLS.IMAGE_URL+myList.get(i));
        }

        studentFeedBackGridAdapter = new StudentFeedBackGridAdapter(getApplicationContext(), myList1);
        studentFeedBackImagesGrid.setAdapter(studentFeedBackGridAdapter);

        studentFeedBackImagesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position1, long id) {
                // TODO Auto-generated method stub



                if (myList1.get(position).endsWith("DOC") || myList1.get(position).endsWith("doc") || myList1.get(position).endsWith("Doc") || myList1.get(position).endsWith("docx") || myList1.get(position).endsWith("Docx") || myList1.get(position).endsWith("DOCX") || myList1.get(position).endsWith("pdf") || myList1.get(position).endsWith("PDF") || myList1.get(position).endsWith("Pdf") || myList1.get(position).endsWith("txt") || myList1.get(position).endsWith("Txt") || myList1.get(position).endsWith("TXT"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(myList1.get(position)));
                    startActivity(intent);
                }

                else {

                    ZGallery.with(AdminHealhCardDocumentsActivity.this, myList1)
                            .setToolbarTitleColor(ZColor.WHITE) // toolbar title color
                            .setGalleryBackgroundColor(ZColor.WHITE).setSelectedImgPosition(0) // activity background color
                            .setToolbarColorResId(R.color.colorPrimary) // toolbar color
                            .setTitle("Zak Gallery") // toolbar title
                            .show();
                }

            }
        });
    }
}
