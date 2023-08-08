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
 * Created by Schoofi on 20-04-2018.
 */

public class AdminVisitorLogsTypeWiseAdapter extends BaseAdapter {

    private Context context;
    private JSONArray adminVisitorLogsArray;
    private String value;

    public AdminVisitorLogsTypeWiseAdapter(Context context, JSONArray adminVisitorLogsArray,String value) {
        this.context = context;
        this.adminVisitorLogsArray = adminVisitorLogsArray;
        this.value = value;
    }

    @Override
    public int getCount() {
        return adminVisitorLogsArray.length();
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
        final Holder holder;

        if(convertView == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.admin_visitor_logs_type_wise_layout, null);
            holder.typeName = (TextView) convertView.findViewById(R.id.text_type);
            holder.typeCount = (TextView) convertView.findViewById(R.id.text_type_count);

            convertView.setTag(holder);

        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        if(value.matches("0"))
        {
            try {
                holder.typeCount.setText("Visitor Count: "+adminVisitorLogsArray.getJSONObject(position).getString("visit_count"));
                holder.typeName.setText("Visitor Type: "+adminVisitorLogsArray.getJSONObject(position).getString("visit_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else
            if(value.matches("1"))
            {
                try {
                    holder.typeCount.setText("Visitor Count: "+adminVisitorLogsArray.getJSONObject(position).getString("visit_count"));
                    holder.typeName.setText("Visitor Type: "+adminVisitorLogsArray.getJSONObject(position).getString("visit_name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            else
            if(value.matches("2"))
            {
                try {
                    holder.typeCount.setText("Visitor Count: "+adminVisitorLogsArray.getJSONObject(position).getString("visit_count"));
                    holder.typeName.setText("Visitor Type: "+adminVisitorLogsArray.getJSONObject(position).getString("visit_name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        return convertView;
    }

    static class Holder
    {
        private TextView typeName,typeCount,total_count;
    }
}
