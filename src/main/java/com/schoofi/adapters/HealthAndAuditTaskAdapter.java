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
 * Created by harsh malhotra on 4/7/2016.
 */
public class HealthAndAuditTaskAdapter extends BaseAdapter {

    private Context context;
    private JSONArray healthAndAuditTaskArray;

    public HealthAndAuditTaskAdapter(Context context, JSONArray healthAndAuditTaskArray) {
        this.context = context;
        this.healthAndAuditTaskArray = healthAndAuditTaskArray;
    }

    @Override
    public int getCount() {
        return healthAndAuditTaskArray.length();
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
            holder.exmaTextList.setText(healthAndAuditTaskArray.getJSONObject(position).getString("task"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;


    }

    static class Holder

    {
        TextView exmaTextList;
    }
}