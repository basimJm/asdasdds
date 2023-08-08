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

/**
 * Created by Schoofi on 05-12-2019.
 */

public class KnowledgeResourseSubjectScreenAdapter extends BaseAdapter {

    private Context context;
    private JSONArray knowledgeResourseSubjectArray;

    public KnowledgeResourseSubjectScreenAdapter(Context context, JSONArray knowledgeResourseSubjectArray) {
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
            if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
            {
                holder.taskName.setText(knowledgeResourseSubjectArray.getJSONObject(position).getString("subject"));
                holder.className.setVisibility(View.GONE);
            }

            else {
                holder.taskName.setText(knowledgeResourseSubjectArray.getJSONObject(position).getString("subject"));
                holder.className.setText(knowledgeResourseSubjectArray.getJSONObject(position).getString("class_name"));
            }
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
