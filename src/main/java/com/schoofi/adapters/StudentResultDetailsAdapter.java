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
 * Created by Schoofi on 22-07-2016.
 */
public class StudentResultDetailsAdapter extends BaseAdapter {

    private Context context;
    private JSONArray studentResultDetailsArray;

    public StudentResultDetailsAdapter(Context context, JSONArray studentResultDetailsArray) {
        this.context = context;
        this.studentResultDetailsArray = studentResultDetailsArray;
    }

    @Override
    public int getCount() {
        return studentResultDetailsArray.length();
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
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.student_result_details_layout, null);
            holder.termName = (TextView) view.findViewById(R.id.text_termName);
            holder.examName = (TextView) view.findViewById(R.id.text_examName);
            holder.examMarks = (TextView) view.findViewById(R.id.text_examMarks);
            view.setTag(holder);
        }

        else
        {
            holder = (Holder) view.getTag();
        }

        try {
            holder.termName.setText(studentResultDetailsArray.getJSONObject(i).getString("term"));
            holder.examName.setText(studentResultDetailsArray.getJSONObject(i).getString("exam_name"));
            holder.examMarks.setText(studentResultDetailsArray.getJSONObject(i).getString("obtained_marks")+"/"+studentResultDetailsArray.getJSONObject(i).getString("max_marks"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    static class Holder
    {
        TextView termName,examName,examMarks;
    }
}
