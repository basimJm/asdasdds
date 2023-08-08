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
public class HealthAndAuditListAdapter extends BaseAdapter {

    private Context context;
    private JSONArray healthAndAuditListArray;

    public HealthAndAuditListAdapter(Context context, JSONArray healthAndAuditListArray) {
        this.context = context;
        this.healthAndAuditListArray = healthAndAuditListArray;
    }

    @Override
    public int getCount() {
        return healthAndAuditListArray.length();
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
            convertView = layoutInflater.inflate(R.layout.health_and_audit_audit_listview, null);
            holder.task = (TextView) convertView.findViewById(R.id.text_taskName);
            holder.assignedTo = (TextView) convertView.findViewById(R.id.text_taskAssignedName);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.task.setText(healthAndAuditListArray.getJSONObject(position).getString("task"));
            holder.assignedTo.setText(healthAndAuditListArray.getJSONObject(position).getString("name"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertView;
    }

    static class Holder
    {
        private TextView task,assignedTo;
    }

}
