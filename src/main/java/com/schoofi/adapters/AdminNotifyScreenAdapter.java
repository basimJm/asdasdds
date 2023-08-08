package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 11-04-2019.
 */

public class AdminNotifyScreenAdapter extends BaseAdapter {

    private Context context;
    private JSONArray adminNotifyScreenArray;


    public AdminNotifyScreenAdapter(Context context, JSONArray adminNotifyScreenArray) {
        this.context = context;
        this.adminNotifyScreenArray = adminNotifyScreenArray;
    }

    @Override
    public int getCount() {
        return adminNotifyScreenArray.length();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;

        if(convertView == null)
        {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.two_step_first_list_screen, null);
            holder.teacherClass = (TextView) convertView.findViewById(R.id.text_teaccher_class);

            holder.teacherMark = (ImageView) convertView.findViewById(R.id.imageView_studentAttendance);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            Preferences.getInstance().loadPreference(context);
            holder.teacherClass.setText(adminNotifyScreenArray.getJSONObject(position).getString("class_name")+"-"+adminNotifyScreenArray.getJSONObject(position).getString("section_name"));
            if(adminNotifyScreenArray.getJSONObject(position).getString("isAdded").matches("A"))
            {
                holder.teacherMark.setImageResource(R.drawable.greencircletick);

            }

            else
            {
                holder.teacherMark.setImageResource(R.drawable.greycircletick);

            }

            holder.teacherMark.setTag(new Integer(position));
            holder.teacherMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if(adminNotifyScreenArray.getJSONObject((Integer) v.getTag()).getString("isAdded").matches("A"))
                        {
                            holder.teacherMark.setImageResource(R.drawable.greycircletick);
                            adminNotifyScreenArray.getJSONObject((Integer) v.getTag()).put("isAdded","N");
                            Cache.Entry e = new Cache.Entry();
                            e.data = adminNotifyScreenArray.toString().getBytes();
                            VolleySingleton.getInstance(context).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_TWO_STEP_ATTENDANCE_PRIMARY+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId,e);

                        }

                        else
                        {
                            holder.teacherMark.setImageResource(R.drawable.greencircletick);
                            adminNotifyScreenArray.getJSONObject((Integer) v.getTag()).put("isAdded","A");
                            Cache.Entry e = new Cache.Entry();
                            e.data = adminNotifyScreenArray.toString().getBytes();
                            VolleySingleton.getInstance(context).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_TWO_STEP_ATTENDANCE_PRIMARY+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId,e);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        TextView teacherClass,teacherSection;
        ImageView teacherMark;
    }
}
