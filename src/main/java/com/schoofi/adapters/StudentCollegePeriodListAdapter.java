package com.schoofi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 29-07-2019.
 */

public class StudentCollegePeriodListAdapter extends BaseAdapter {

    private Context context;
    private JSONArray teacherHomeScreenArray;


    public StudentCollegePeriodListAdapter(Context context, JSONArray teacherHomeScreenArray) {

        this.context = context;
        this.teacherHomeScreenArray = teacherHomeScreenArray;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return teacherHomeScreenArray.length();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        try {
            return teacherHomeScreenArray.getJSONObject(position);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder;
        if(convertView == null)
        {   holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.teacher_homescreen_listview_item, null);
            holder.teacherHomeScreenListItemButton = (TextView) convertView.findViewById(R.id.btn_teacher_homescreen_class);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            Preferences.getInstance().loadPreference(context);

                 if(Preferences.getInstance().userRoleId.matches("7") || Preferences.getInstance().userRoleId.matches("8"))
                 {
                     holder.teacherHomeScreenListItemButton.setText("Period Number: "+teacherHomeScreenArray.getJSONObject(position).getString("period_no"));
                 }
                holder.teacherHomeScreenListItemButton.setText(teacherHomeScreenArray.getJSONObject(position).getString("period_no")+"("+teacherHomeScreenArray.getJSONObject(position).getString("period_start_time")+"-"+teacherHomeScreenArray.getJSONObject(position).getString("period_end_time")+")");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertView;
    }

    static class Holder
    {
        private TextView teacherHomeScreenListItemButton;
    }
}
