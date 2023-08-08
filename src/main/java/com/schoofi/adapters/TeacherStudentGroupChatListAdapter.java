package com.schoofi.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.ChatMainVO;
import com.schoofi.utils.ChatVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TeacherStudentGroupChatListAdapter  extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<ChatMainVO> diaryVOs;

    public TeacherStudentGroupChatListAdapter(Context context, ArrayList<ChatMainVO> diaryVOs) {
        this.context = context;
        this.diaryVOs = diaryVOs;
    }

    String date1,date2,date5,date6;
    Date date3,date7;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String currentDate = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());

    @Override
    public int getGroupCount() {
        return diaryVOs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ChatVO> assignmentMultiLevelVOs = diaryVOs.get(groupPosition).getItems();
        return assignmentMultiLevelVOs.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return diaryVOs.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ChatVO>  assignmentMultiLevelVOs = diaryVOs.get(groupPosition).getItems();

        return assignmentMultiLevelVOs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ExpandableListView eLV = (ExpandableListView) parent;
        eLV.expandGroup(groupPosition);

        Log.d("ppp", String.valueOf(diaryVOs.size()));

        final Holder holder;
        ChatMainVO diaryVO = (ChatMainVO) getGroup(groupPosition);
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.student_chat_parent_listview, parent, false);
            holder.assignmentDate = (TextView) convertView.findViewById(R.id.text_date_title);
            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }


        date1 = diaryVOs.get(groupPosition).getDate();
        try {
            date3 = formatter.parse(date1);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        date2 = formatter1.format(date3);
        if(date2.matches(currentDate))
        {
            holder.assignmentDate.setText("Today");
        }
        else {
            holder.assignmentDate.setText(date2);
        }





        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ExpandableListView eLV = (ExpandableListView) parent;

        final Holder holder1;
        Preferences.getInstance().loadPreference(context.getApplicationContext());

        final ChatVO assignmentMultiLevelVO = (ChatVO) getChild(groupPosition,childPosition);
        if (convertView == null) {
            holder1 = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(assignmentMultiLevelVO.getUser_id().matches(Preferences.getInstance().userId)) {

                convertView = infalInflater.inflate(R.layout.right, parent, false);
            }
            else
            {
                convertView = infalInflater.inflate(R.layout.chat_left_textview, parent, false);
            }

            holder1.message = convertView.findViewById(R.id.msg);
            holder1.time = convertView.findViewById(R.id.msg1);

            convertView.setTag(holder1);
        }
        else
        {
            holder1 = (Holder) convertView.getTag();
        }

        holder1.message.setText(assignmentMultiLevelVO.getMessage());
        date5 = assignmentMultiLevelVO.getTime();
        try {
            date7 = formatter1.parse(date5);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mm a");
        date6 = formatter1.format(date7);
        holder1.time.setText(date6);

        //eLV.smoothScrollToPosition(diaryVOs.size());

        holder1.message.setTag(new Integer(childPosition));

        holder1.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(assignmentMultiLevelVO.getIsvisible().matches("NO"))
                {
                    postAttendance(assignmentMultiLevelVO.getMsgId());
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class Holder
    {
        TextView assignmentDate,message,time;
    }



    protected void postAttendance(final String msgID)
    {

        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.TEACHER_STUDENT_MESSAGE_VISIBLE;
        final ProgressDialog loading = ProgressDialog.show(context, "Uploading...", "Please wait...", false, false);

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                //System.out.println(url1);

                try
                {

                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        //Utils.showToast(TeacherStudentAttendanceDetails.this,"Error Submitting Comment");
                        loading.dismiss();
                        Toast.makeText(context.getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        loading.dismiss();
                        Toast.makeText(context.getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                        //Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {
                        loading.dismiss();
                        Toast.makeText(context.getApplicationContext(), "uploaded", Toast.LENGTH_LONG).show();
                       // finish();
                    }

                    else
                    {
                        System.out.println("kkk");
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    //Utils.showToast(TeacherStudentAttendanceDetails.this, "Error submitting alert! Please try after sometime.");
                    Toast.makeText(context.getApplicationContext(), "catch", Toast.LENGTH_LONG).show();
                }


            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context.getApplicationContext(), "catch1", Toast.LENGTH_LONG).show();
                //setProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                //.getInstance().loadPreference(TeacherStudentAttendanceDetails.this);
                Map<String,String> params = new HashMap<String, String>();


                params.put("from_user_id",Preferences.getInstance().userId);



                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("token",Preferences.getInstance().token);
                params.put("date_time",currentDate);
                params.put("msg_id",msgID);

                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestObject);




    }


}
