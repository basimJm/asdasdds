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
 * Created by Schoofi on 18-07-2016.
 */
public class StudentCOScholasticResult2DAdapter extends BaseAdapter {

    private Context context;
    private JSONArray studentCOSCholasticResult2DArray;

    public StudentCOScholasticResult2DAdapter(Context context, JSONArray studentCOSCholasticResult2DArray) {
        this.context = context;
        this.studentCOSCholasticResult2DArray = studentCOSCholasticResult2DArray;
    }

    @Override
    public int getCount() {
        return studentCOSCholasticResult2DArray.length();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        Holder holder;

        if(convertView == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.co_scholastic_life_skills_listview_layout, null);
            holder.sNo = (TextView) convertView.findViewById(R.id.text_s_no);
            holder.descriptiveIndicators = (TextView) convertView.findViewById(R.id.text_student_descriptive_indicators);
            holder.grade = (TextView) convertView.findViewById(R.id.text_student_descriptive_indicators_grade);
            holder.evaluation = (TextView) convertView.findViewById(R.id.text_student_descriptive_indicators_evaluation);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.sNo.setText(String.valueOf(position+1));
            holder.descriptiveIndicators.setText(studentCOSCholasticResult2DArray.getJSONObject(position).getString("descriptive_indicator"));
            holder.grade.setText(studentCOSCholasticResult2DArray.getJSONObject(position).getString("grade"));
            holder.evaluation.setText(studentCOSCholasticResult2DArray.getJSONObject(position).getString("evaluation"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }

    static class Holder
    {
        TextView sNo,descriptiveIndicators,grade,evaluation;
    }
}
