package com.schoofi.activitiess;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.DiaryWallScreenAdapter;
import com.schoofi.adapters.TeacherCircularViewAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.fragments.FirstFragment;
import com.schoofi.fragments.FourthFragment;
import com.schoofi.fragments.SecondFragment;
import com.schoofi.fragments.ThirdFragment;
import com.schoofi.utils.DiaryWallChildVO;
import com.schoofi.utils.DiaryWallVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.riddhimanadib.library.BottomBarHolderActivity;
import me.riddhimanadib.library.BottomNavigationBar;
import me.riddhimanadib.library.NavigationPage;

public class DiaryWallScreen extends AppCompatActivity {

    private ImageView back;
    private ExpandableListView diaryWallListView;
    private SwipyRefreshLayout swipyRefreshLayout;
    private JSONArray circularArray;
    private DiaryWallScreenAdapter teacherCircularViewAdapter;
    JSONObject jsonObject,jsonObject1;
    JSONArray jsonArray;
    private ArrayList<DiaryWallVO> diaryVOs;
    private ArrayList<DiaryWallChildVO> assignmentMultiLevelVOs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_diary_wall_screen);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        diaryWallListView = (ExpandableListView) findViewById(R.id.listViewInnerAllAssignment);

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                //initData();
                getStudentFeedList();

            }
        });

        Preferences.getInstance().loadPreference(getApplicationContext());

        diaryWallListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    if(diaryVOs.get(groupPosition).getItems().get(childPosition).getType().matches("message"))
                    {
                        if(Preferences.getInstance().userRoleId.matches("4"))
                        {
                            Intent intent = new Intent(DiaryWallScreen.this,TeacherStudentGroupChatList.class);
                            intent.putExtra("type",diaryVOs.get(groupPosition).getItems().get(childPosition).getChatType());
                            intent.putExtra("recipient_id",diaryVOs.get(groupPosition).getItems().get(childPosition).getSenderId());
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(DiaryWallScreen.this,StudentChatScreen.class);
                            intent.putExtra("type",diaryVOs.get(groupPosition).getItems().get(childPosition).getChatType());
                            intent.putExtra("recipient_id",diaryVOs.get(groupPosition).getItems().get(childPosition).getSenderId());
                            startActivity(intent);
                        }
                    }
                    else
                    if(diaryVOs.get(groupPosition).getItems().get(childPosition).getType().matches("assignment"))
                    {
                        if(Preferences.getInstance().userRoleId.matches("4"))
                        {
                            Intent intent = new Intent(DiaryWallScreen.this,TeacherNewAssignmentVersion.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(DiaryWallScreen.this,StudentNewAssignemntVerion.class);
                            startActivity(intent);
                        }
                    }
                    else
                    if(diaryVOs.get(groupPosition).getItems().get(childPosition).getType().matches("Celebration"))
                    {
                        Intent intent = new Intent(DiaryWallScreen.this,DiaryNewDesignActivity.class);
                        intent.putExtra("type","Celebration");
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(DiaryWallScreen.this,DiaryNewDesignActivity.class);
                        intent.putExtra("type","Achievements");
                        startActivity(intent);
                    }

                return true;
            }
        });



      //  initData();
        getStudentFeedList();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Preferences.getInstance().loadPreference(getApplicationContext());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.action_recents:
                          if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
                          {
                              Intent intent = new Intent(DiaryWallScreen.this,TeacherStudentDiaryScreen.class);
                              startActivity(intent);
                          }
                          else
                              if(Preferences.getInstance().userRoleId.matches("4"))
                              {
                                  Intent intent = new Intent(DiaryWallScreen.this,TeacherDiaryStudentGroupList.class);
                                  startActivity(intent);
                              }
                        break;
                    case R.id.action_favorites:
                        Intent intent = new Intent(DiaryWallScreen.this,DiaryNewDesignActivity.class);
                        intent.putExtra("type","Celebration");
                        startActivity(intent);
                        break;
                    case R.id.action_nearby:
                        Intent intent1 = new Intent(DiaryWallScreen.this,DiaryNewDesignActivity.class);
                        intent1.putExtra("type","Achievements");
                        startActivity(intent1);
                        break;


                    /*case R.id.action_nearby2:
                        if(Preferences.getInstance().userRoleId.matches("4")) {
                            Intent intent2 = new Intent(DiaryWallScreen.this, TeacherNewAssignmentVersion.class);
                            startActivity(intent2);
                        }
                        else
                            if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
                            {
                                Intent intent2 = new Intent(DiaryWallScreen.this, StudentNewAssignemntVerion.class);
                                intent2.putExtra("value","1");
                                startActivity(intent2);
                            }
                        break;*/
                }
                return true;
            }
        });
    }







    protected void getStudentFeedList()
    {

        RequestQueue queue = VolleySingleton.getInstance(DiaryWallScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.DIARY_WALL+"?token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&role_id="+Preferences.getInstance().userRoleId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(getApplicationContext(),"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        circularArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=circularArray && circularArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = circularArray.toString().getBytes();
                            diaryVOs = new ArrayList<DiaryWallVO>();
                            VolleySingleton.getInstance(DiaryWallScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.DIARY_WALL+"?token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&role_id="+Preferences.getInstance().userRoleId,e);
                            diaryWallListView.invalidateViews();

                            for(int i=0;i<circularArray.length();i++)
                            {
                                DiaryWallVO diaryVO = new DiaryWallVO();
                                diaryVO.setType(circularArray.getJSONObject(i).getString("crr_type"));
                                jsonObject = circularArray.getJSONObject(i);
                                jsonArray = jsonObject.getJSONArray("bifurcation");

                                assignmentMultiLevelVOs = new ArrayList<DiaryWallChildVO>();

                                for(int j=0;j<jsonArray.length();j++)
                                {
                                    jsonObject1 = jsonArray.getJSONObject(j);
                                    DiaryWallChildVO assignmentMultiLevelVO = new DiaryWallChildVO();

                                    assignmentMultiLevelVO.setType(jsonObject1.getString("type"));
                                    assignmentMultiLevelVO.setDate(jsonObject1.getString("date_time"));

                                    if(jsonObject1.getString("type").matches("message"))
                                    {
                                        assignmentMultiLevelVO.setMsg(jsonObject1.getString("msg"));
                                        assignmentMultiLevelVO.setName(jsonObject1.getString("sender_name"));
                                        assignmentMultiLevelVO.setChatType(jsonObject1.getString("chat_type"));
                                        assignmentMultiLevelVO.setSenderId(jsonObject1.getString("from_user_id"));
                                    }
                                    else
                                    {
                                        assignmentMultiLevelVO.setTitle(jsonObject1.getString("title"));
                                        assignmentMultiLevelVO.setName(jsonObject1.getString("name"));
                                    }

                                    assignmentMultiLevelVOs.add(assignmentMultiLevelVO);
                                }
                                diaryVO.setItems(assignmentMultiLevelVOs);
                                diaryVOs.add(diaryVO);
                            }

                            teacherCircularViewAdapter= new DiaryWallScreenAdapter(DiaryWallScreen.this,circularArray,diaryVOs);
                            diaryWallListView.setAdapter(teacherCircularViewAdapter);
                            teacherCircularViewAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
                            //assignId = AssignmentArray.getJSONObject(index)

                        }
                    }
                    else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");

                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");

            }
        })
        {
			};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStudentFeedList();
    }
}
