package com.schoofi.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by harsh malhotra on 4/6/2016.
 */
public class HealthAndAuditUserSubTaskListAdapter extends BaseAdapter {

    private Context context;
    private JSONArray healthAndAuditUserSubTaskListArray;

    public HealthAndAuditUserSubTaskListAdapter(Context context, JSONArray healthAndAuditUserSubTaskListArray) {
        this.context = context;
        this.healthAndAuditUserSubTaskListArray = healthAndAuditUserSubTaskListArray;
    }

    @Override
    public int getCount() {
        return healthAndAuditUserSubTaskListArray.length();
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
            convertView = layoutInflater.inflate(R.layout.health_and_audit_task_listview_layout, null);
            holder.taskName = (TextView) convertView.findViewById(R.id.text_taskName);
            convertView.setTag(holder);

        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.taskName.setText(healthAndAuditUserSubTaskListArray.getJSONObject(position).getString("value"));

            if(healthAndAuditUserSubTaskListArray.getJSONObject(position).getString("status").matches("close"))
            {
                holder.taskName.setTextColor(Color.parseColor("#009F4D"));
            }
            else
            {
                  holder.taskName.setTextColor(Color.parseColor("#000000"));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        TextView taskName;
    }
}
