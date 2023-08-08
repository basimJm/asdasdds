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
 * Created by Schoofi on 12-07-2016.
 */
public class StudentResultSpinnerAdapter extends BaseAdapter {

    private Context context;
    private JSONArray studentResultSpinnerArray;

    public StudentResultSpinnerAdapter(Context context, JSONArray studentResultSpinnerArray) {
        this.context = context;
        this.studentResultSpinnerArray = studentResultSpinnerArray;
    }



    @Override
    public int getCount() {
        return studentResultSpinnerArray.length();
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        Holder holder;

        if(convertView == null)
        {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_dialog_list_view, null);
            holder.exmaTextList = (TextView) convertView.findViewById(R.id.text_dialogListView);

            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.exmaTextList.setText(studentResultSpinnerArray.getJSONObject(i).getString("exam_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    static class Holder
    {
        TextView exmaTextList;
    }
}
