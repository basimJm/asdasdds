package com.schoofi.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private ArrayList<ChatVO> chatVOS;
    private Context context;
    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;
    String date1,date2,date5,date6;
    Date date3,date7;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String currentDate = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());


    public RecyclerViewAdapter(ArrayList<ChatVO> chatVOS, Context context) {
        this.chatVOS = chatVOS;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        ChatVO chatVOS1 = chatVOS.get(position);
        Preferences.getInstance().loadPreference(context);
        if(chatVOS1.getUser_id().matches(Preferences.getInstance().userId))
        {
            return TYPE_ONE;
        }
        else
        {
            return TYPE_TWO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_ONE)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right, parent, false);
            return new ViewHolder0(view);
        } else if (viewType == TYPE_TWO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_left_textview, parent, false);
            return new ViewHolder1(view);
        } else {
            throw new RuntimeException("The type has to be ONE or TWO");
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {



        switch (holder.getItemViewType()) {
            case TYPE_ONE:
                ViewHolder0 viewHolder0 = (ViewHolder0)holder;
                viewHolder0.message.setText(chatVOS.get(position).getMessage());

                date5 = chatVOS.get(position).getTime();
                try {
                    date7 = formatter1.parse(date5);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mm a");
                date6 = formatter1.format(date7);
                viewHolder0.time.setText(date6);
                if(Preferences.getInstance().userRoleId.matches("4")) {
                    viewHolder0.time.setTag(new Integer(position));
                    viewHolder0.time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (chatVOS.get((Integer) v.getTag()).getIsvisible().matches("NO")) {
                                postAttendance(chatVOS.get((Integer) v.getTag()).getMsgId());
                            }
                        }

                    });
                }

                break;
            case TYPE_TWO:
                ViewHolder1 viewHolder1 = (ViewHolder1)holder;
                viewHolder1.message.setText(chatVOS.get(position).getMessage());
                date5 = chatVOS.get(position).getTime();
                try {
                    date7 = formatter2.parse(date5);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                SimpleDateFormat formatter2 = new SimpleDateFormat("hh:mm a");
                date6 = formatter2.format(date7);
                viewHolder1.time.setText(date6);
                if(Preferences.getInstance().userRoleId.matches("4")) {
                    viewHolder1.time.setTag(new Integer(position));
                    viewHolder1.time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (chatVOS.get((Integer) v.getTag()).getIsvisible().matches("NO")) {
                                postAttendance(chatVOS.get((Integer) v.getTag()).getMsgId());
                            }
                        }

                    });
                }
                break;
            default:
                break;
        }



    }

    @Override
    public int getItemCount() {
        return chatVOS.size();
    }

    public static class ViewHolder0 extends RecyclerView.ViewHolder {
        TextView message,time;



        public ViewHolder0(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.msg);
            time = itemView.findViewById(R.id.msg1);

        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView message,time;



        public ViewHolder1(View itemView1) {
            super(itemView1);
            message = itemView1.findViewById(R.id.msg);
            time = itemView1.findViewById(R.id.msg1);

        }
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
