package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 04-08-2016.
 */
public class ChairmanAttendanceDetailsListViewAdapter extends BaseAdapter {

    private Context context;
    private JSONArray chairmanAttendanceDetailsListViewArray;

    public ChairmanAttendanceDetailsListViewAdapter(Context context, JSONArray chairmanAttendanceDetailsListViewArray) {
        this.context = context;
        this.chairmanAttendanceDetailsListViewArray = chairmanAttendanceDetailsListViewArray;
    }

    @Override
    public int getCount() {
        return chairmanAttendanceDetailsListViewArray.length();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder;

        if(view == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.chairman_attendance_listview_details, null);
            holder.className = (TextView) view.findViewById(R.id.text_class_name);
            holder.teacherName = (TextView) view.findViewById(R.id.text_teacher_name);
            view.setTag(holder);
        }

        else
        {
            holder = (Holder) view.getTag();
        }

        try {
            holder.className.setText(chairmanAttendanceDetailsListViewArray.getJSONObject(i).getString("class_name")+"-"+chairmanAttendanceDetailsListViewArray.getJSONObject(i).getString("section_name"));
            holder.teacherName.setText(chairmanAttendanceDetailsListViewArray.getJSONObject(i).getString("teac_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    static class Holder
    {
        TextView className,teacherName;
    }
}
