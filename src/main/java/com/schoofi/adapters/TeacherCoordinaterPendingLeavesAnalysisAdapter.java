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
 * Created by Schoofi on 07-05-2018.
 */

public class TeacherCoordinaterPendingLeavesAnalysisAdapter extends BaseAdapter {

    private Context context;
    private JSONArray teacherCoordinaterPendingLeavesArray;

    public TeacherCoordinaterPendingLeavesAnalysisAdapter(Context context, JSONArray teacherCoordinaterPendingLeavesArray) {
        this.context = context;
        this.teacherCoordinaterPendingLeavesArray = teacherCoordinaterPendingLeavesArray;
    }

    @Override
    public int getCount() {
        return teacherCoordinaterPendingLeavesArray.length();
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
        Holder holder;
        if(convertView == null)
        {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.teacher_coordinater_pending_leaves_listview_layout, null);
            holder.TeacherName= (TextView) convertView.findViewById(R.id.text_teacher_name);
            holder.className = (TextView) convertView.findViewById(R.id.text_class_name);
            holder.pendingLeaveCount = (TextView) convertView.findViewById(R.id.text_leave_count);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.TeacherName.setText("Teacher Name - "+teacherCoordinaterPendingLeavesArray.getJSONObject(position).getString("teac_name"));
            holder.className.setText("Class Name - "+teacherCoordinaterPendingLeavesArray.getJSONObject(position).getString("class_name")+"-"+teacherCoordinaterPendingLeavesArray.getJSONObject(position).getString("section_name"));
            holder.pendingLeaveCount.setText("Pending Leaves - "+teacherCoordinaterPendingLeavesArray.getJSONObject(position).getString("pending_leaves"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        TextView TeacherName,className,pendingLeaveCount;
    }
}
