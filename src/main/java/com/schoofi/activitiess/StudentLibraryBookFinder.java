package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.LibraryBookFinderListAdapter;
import com.schoofi.adapters.LibraryBookFinderListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudentLibraryBookFinder extends AppCompatActivity {

    private ImageView back;
    private ListView libraryBookListView;
    private JSONArray libraryBookListArray;
    private LibraryBookFinderListAdapter libraryBookFinderListAdapter;
    private EditText authorName,bookName;
    private Button searchButton;
    private SwipyRefreshLayout swipyRefreshLayout;
    private String value="",search="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_library_book_finder);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        libraryBookListView = (ListView) findViewById(R.id.student_leave_list);
        authorName = (EditText) findViewById(R.id.edit_leave_subject);
        bookName = (EditText) findViewById(R.id.edit_leave_subject1);
        searchButton = (Button) findViewById(R.id.btn_student_leave_submit);
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
                if(!authorName.getText().toString().matches("") && !bookName.getText().toString().matches(""))
                {
                    Utils.showToast(getApplicationContext(),"Please fill only one filed!");
                }
                else
                if(!authorName.getText().toString().matches("") && bookName.getText().toString().matches(""))
                {
                    search = authorName.getText().toString();
                    value = "2";
                    initData();
                    getChairmanStudentLeaveList();
                }

                else
                if(authorName.getText().toString().matches("") && !bookName.getText().toString().matches(""))
                {
                    search = bookName.getText().toString();
                    value = "1";
                    initData();
                    getChairmanStudentLeaveList();
                }
                else
                {
                    Utils.showToast(getApplicationContext(),"Please fill atleast one field");
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!authorName.getText().toString().matches("") && !bookName.getText().toString().matches(""))
                {
                    Utils.showToast(getApplicationContext(),"Please fill only one filed!");
                }
                else
                    if(!authorName.getText().toString().matches("") && bookName.getText().toString().matches(""))
                    {
                        search = authorName.getText().toString();
                        value = "2";
                        initData();
                        getChairmanStudentLeaveList();
                    }

                    else
                    if(authorName.getText().toString().matches("") && !bookName.getText().toString().matches(""))
                    {
                        search = bookName.getText().toString();
                        value = "1";
                        initData();
                        getChairmanStudentLeaveList();
                    }
                    else
                    {
                        Utils.showToast(getApplicationContext(),"Please fill atleast one field");
                    }
            }
        });

    }



    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.LIBRARY_BOOK_FINDER+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&search="+search+"&condition="+value);
            if(e == null)
            {
                libraryBookListArray= null;
            }
            else
            {
                libraryBookListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(libraryBookListArray!= null)
        {

            libraryBookFinderListAdapter = new LibraryBookFinderListAdapter(StudentLibraryBookFinder.this, libraryBookListArray);
            libraryBookListView.setAdapter(libraryBookFinderListAdapter);
            libraryBookFinderListAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(StudentLibraryBookFinder.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.LIBRARY_BOOK_FINDER/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                Log.d("po",response);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(StudentLibraryBookFinder.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(StudentLibraryBookFinder.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("book_details"))
                    {
                        libraryBookListArray= new JSONObject(response).getJSONArray("book_details");


                        if(null!=libraryBookListArray && libraryBookListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = libraryBookListArray.toString().getBytes();
                            VolleySingleton.getInstance(StudentLibraryBookFinder.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.LIBRARY_BOOK_FINDER+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&search="+search+"&condition="+value,e);



                            libraryBookListView.invalidateViews();

                            libraryBookFinderListAdapter = new LibraryBookFinderListAdapter(StudentLibraryBookFinder.this, libraryBookListArray);
                            libraryBookListView.setAdapter(libraryBookFinderListAdapter);
                            libraryBookFinderListAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
                        }
                    }
                    else
                        Utils.showToast(StudentLibraryBookFinder.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(StudentLibraryBookFinder.this, "Error fetching modules! Please try after sometime.");
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
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(StudentLibraryBookFinder.this);
                Map<String,String> params = new HashMap<String, String>();
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("sch_id",Preferences.getInstance().schoolId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                //params.put("cls_id", classId);
                //params.put("sec_id", sectionId);
                //params.put("crr_date",currentDate);
                params.put("condition",value);
                params.put("search",search);
                params.put("device_id", Preferences.getInstance().deviceId);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(StudentLibraryBookFinder.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(StudentLibraryBookFinder.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa()
    {
        System.out.println("aaa");
    }
}
