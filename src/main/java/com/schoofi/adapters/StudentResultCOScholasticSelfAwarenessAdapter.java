package com.schoofi.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 16-07-2016.
 */
public class StudentResultCOScholasticSelfAwarenessAdapter extends BaseAdapter {

    private Context context;
    private JSONArray studentResultCOScholasticSelfAwarenessArray;
    String sourceString;

    public StudentResultCOScholasticSelfAwarenessAdapter(Context context, JSONArray studentResultCOScholasticSelfAwarenessArray) {
        this.context = context;
        this.studentResultCOScholasticSelfAwarenessArray = studentResultCOScholasticSelfAwarenessArray;
    }

    @Override
    public int getCount() {
        return studentResultCOScholasticSelfAwarenessArray.length();
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
            convertView = layoutInflater.inflate(R.layout.student_result_coscholastic_area5_listview, null);

            holder.selfAwareness = (TextView) convertView.findViewById(R.id.text_selfAwareness);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            sourceString = "<b>"+studentResultCOScholasticSelfAwarenessArray.getJSONObject(position).getString("descriptive_indicator")+"</b>";
            holder.selfAwareness.setText(Html.fromHtml(sourceString)+":"+studentResultCOScholasticSelfAwarenessArray.getJSONObject(position).getString("evaluation"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;


    }

    static class Holder
    {
        TextView selfAwareness;
    }
}
