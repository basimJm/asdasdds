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
 * Created by harsh malhotra on 4/28/2016.
 */
public class AdmissionStatusListViewAdapter extends BaseAdapter {

    private Context context;
    private JSONArray admissionStatusArray;

    public AdmissionStatusListViewAdapter(Context context, JSONArray admissionStatusArray) {
        this.context = context;
        this.admissionStatusArray = admissionStatusArray;
    }



    @Override
    public int getCount() {
        return admissionStatusArray.length();
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
        if(convertView == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listview_admission_status, null);
            holder.studentName = (TextView) convertView.findViewById(R.id.student_name);
            holder.studentSchool = (TextView) convertView.findViewById(R.id.student_school_applied);
            holder.admissionStatus = (TextView) convertView.findViewById(R.id.student_admission_status);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.studentName.setText(admissionStatusArray.getJSONObject(position).getString("candidate_name"));
            holder.admissionStatus.setText(admissionStatusArray.getJSONObject(position).getString("confirmation_status"));
            holder.studentSchool.setText(admissionStatusArray.getJSONObject(position).getString("required_school"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        TextView studentName,studentSchool,admissionStatus;
    }
}
