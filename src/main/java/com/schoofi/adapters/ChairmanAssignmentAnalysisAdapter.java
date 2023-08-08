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
 * Created by Schoofi on 17-08-2017.
 */

public class ChairmanAssignmentAnalysisAdapter extends BaseAdapter {

    private Context context;
    private JSONArray chairmanAssignmentAnalysisArray;
    private String value;

    @Override
    public int getCount() {
        return chairmanAssignmentAnalysisArray.length();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    public ChairmanAssignmentAnalysisAdapter(Context context, JSONArray chairmanAssignmentAnalysisArray, String value) {
        this.context = context;
        this.chairmanAssignmentAnalysisArray = chairmanAssignmentAnalysisArray;
        this.value = value;
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
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.chairman_assignment_analysis_listview_details, null);
            holder.title = (TextView) convertView.findViewById(R.id.text_label_chairman_assignment_analysis);
            holder.assignmentCount = (TextView) convertView.findViewById(R.id.text_label_chairman_asn_count);
            holder.homeworkCount = (TextView) convertView.findViewById(R.id.text_label_chairman_home_work_count);
            holder.circularCount = (TextView) convertView.findViewById(R.id.text_label_chairman_circular_count);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            if(value.matches("1")) {
                holder.title.setText("Class : " + chairmanAssignmentAnalysisArray.getJSONObject(position).getString("class_name") + "-" + chairmanAssignmentAnalysisArray.getJSONObject(position).getString("section_name"));
                holder.assignmentCount.setText("Total Assignments : " + chairmanAssignmentAnalysisArray.getJSONObject(position).getString("total_Assignment"));
                holder.homeworkCount.setText("Total Homework : " + chairmanAssignmentAnalysisArray.getJSONObject(position).getString("total_home_work"));
                holder.circularCount.setText("Total Circulars : " + chairmanAssignmentAnalysisArray.getJSONObject(position).getString("total_Circular"));
            }

            else
                if(value.matches("2"))
                {
                    holder.title.setText("Teacher Name : " + chairmanAssignmentAnalysisArray.getJSONObject(position).getString("teac_name"));
                    holder.assignmentCount.setText("Total Assignments : " + chairmanAssignmentAnalysisArray.getJSONObject(position).getString("total_Assignment"));
                    holder.homeworkCount.setText("Total Homework : " + chairmanAssignmentAnalysisArray.getJSONObject(position).getString("total_home_work"));
                    holder.circularCount.setText("Total Circulars : " + chairmanAssignmentAnalysisArray.getJSONObject(position).getString("total_Circular"));
                }

                else
                    if(value.matches("3"))
                    {
                        holder.title.setText("Subject Name : " + chairmanAssignmentAnalysisArray.getJSONObject(position).getString("subject_name"));
                        holder.assignmentCount.setText("Total Assignments : " + chairmanAssignmentAnalysisArray.getJSONObject(position).getString("total_Assignment"));
                        holder.homeworkCount.setText("Total Homework : " + chairmanAssignmentAnalysisArray.getJSONObject(position).getString("total_home_work"));
                        //holder.circularCount.setText("Total Circulars : " + chairmanAssignmentAnalysisArray.getJSONObject(position).getString("total_Circular"));
                        holder.circularCount.setVisibility(View.GONE);
                    }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        private TextView title,assignmentCount,homeworkCount,circularCount;
    }
}
