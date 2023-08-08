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
 * Created by harsh malhotra on 4/5/2016.
 */
public class HealthAndAuditUserTaskListAdapter extends BaseAdapter {

    Context context;
    JSONArray healthAndAuditUserTaskListArray;

    public HealthAndAuditUserTaskListAdapter(Context context, JSONArray healthAndAuditUserTaskListArray) {
        this.context = context;
        this.healthAndAuditUserTaskListArray = healthAndAuditUserTaskListArray;
    }

    @Override
    public int getCount() {
        return healthAndAuditUserTaskListArray.length();
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
            convertView = layoutInflater.inflate(R.layout.health_and_audit_user_listview, null);
            holder.title = (TextView) convertView.findViewById(R.id.text_child_name);
            holder.details = (TextView) convertView.findViewById(R.id.text_child_class);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.title.setText(healthAndAuditUserTaskListArray.getJSONObject(position).getString("task"));
            holder.details.setText(healthAndAuditUserTaskListArray.getJSONObject(position).getString("school_name")+"-"+healthAndAuditUserTaskListArray.getJSONObject(position).getString("school_address")+"-"+healthAndAuditUserTaskListArray.getJSONObject(position).getString("city")+"-"+healthAndAuditUserTaskListArray.getJSONObject(position).getString("state")+"-"+healthAndAuditUserTaskListArray.getJSONObject(position).getString("zipcode"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        TextView title,details;
    }
}
