package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;

public class TeacherVideoSubjectListAdapter extends BaseAdapter {
    private Context context;
    private JSONArray knowledgeResourseSubjectArray;

    public TeacherVideoSubjectListAdapter(Context context, JSONArray knowledgeResourseSubjectArray) {
        this.context = context;
        this.knowledgeResourseSubjectArray = knowledgeResourseSubjectArray;
    }

    @Override
    public int getCount() {
        return knowledgeResourseSubjectArray.length();
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
        {   holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.knowledge_resourse_subject_listview, null);
            holder.taskName = (TextView) convertView.findViewById(R.id.text_subjectName);
            holder.className =(TextView) convertView.findViewById(R.id.text_className);
            convertView.setTag(holder);

        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {

                holder.taskName.setText(knowledgeResourseSubjectArray.getJSONObject(position).getString("subject_name"));
                holder.className.setVisibility(View.GONE);



        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        TextView taskName,className;
    }
}
