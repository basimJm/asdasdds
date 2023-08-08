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
 * Created by Schoofi on 12-09-2016.
 */
public class ChairmanStudentExamListAdapter extends BaseAdapter {

    private Context context;
    private JSONArray chairmanStudentExamListArray;

    public ChairmanStudentExamListAdapter(Context context, JSONArray chairmanStudentExamListArray) {
        this.context = context;
        this.chairmanStudentExamListArray = chairmanStudentExamListArray;
    }

    @Override
    public int getCount() {
        return chairmanStudentExamListArray.length();
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
            view = layoutInflater.inflate(R.layout.admin_class_listview, null);
            holder.examName = (TextView) view.findViewById(R.id.text_admin_class);
            view.setTag(holder);
        }

        else
        {
            holder = (Holder) view.getTag();
        }

        try {
            if(chairmanStudentExamListArray.getJSONObject(i).getString("exam_name").matches("") || chairmanStudentExamListArray.getJSONObject(i).getString("exam_name").matches("null") || chairmanStudentExamListArray.getJSONObject(i).getString("exam_name").matches("0"))
            {
                holder.examName.setText(chairmanStudentExamListArray.getJSONObject(i).getString("full_name"));
            }

            else
            {
                holder.examName.setText(chairmanStudentExamListArray.getJSONObject(i).getString("full_name")+"-"+chairmanStudentExamListArray.getJSONObject(i).getString("exam_name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    static class Holder
    {
        TextView examName;
    }
}
