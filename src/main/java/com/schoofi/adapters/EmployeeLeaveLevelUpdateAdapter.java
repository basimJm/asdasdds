package com.schoofi.adapters;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 10-06-2019.
 */

public class EmployeeLeaveLevelUpdateAdapter extends BaseAdapter {

    private Context context;
    private JSONArray employeeLeaveLevelUpdateArray;

    public EmployeeLeaveLevelUpdateAdapter(Context context, JSONArray employeeLeaveLevelUpdateArray) {
        this.context = context;
        this.employeeLeaveLevelUpdateArray = employeeLeaveLevelUpdateArray;
    }

    @Override
    public int getCount() {
        return employeeLeaveLevelUpdateArray.length();
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
            convertView = layoutInflater.inflate(R.layout.employee_level_update_listview_details, null);
            holder.levelName = (TextView) convertView.findViewById(R.id.text_level_name);
            holder.status = (TextView) convertView.findViewById(R.id.text_status);
            holder.reason = (TextView) convertView.findViewById(R.id.text_reason);

            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            Log.d("oppp",employeeLeaveLevelUpdateArray.getJSONObject(position).getString("emp_level"));
            holder.levelName.setText("Level: "+employeeLeaveLevelUpdateArray.getJSONObject(position).getString("emp_level"));
            if(employeeLeaveLevelUpdateArray.getJSONObject(position).getString("approved_or_rejected").matches("0"))
            {
               holder.status.setText("Status: "+"Pending");
               holder.reason.setVisibility(View.GONE);
               holder.status.setTextColor(ContextCompat.getColor(context, R.color.orange));
            }
            else
                if(employeeLeaveLevelUpdateArray.getJSONObject(position).getString("approved_or_rejected").matches("1"))
                {
                    holder.status.setText("Status: "+"Approved");
                    holder.status.setTextColor(ContextCompat.getColor(context, R.color.green));
                    if(employeeLeaveLevelUpdateArray.getJSONObject(position).getString("approved_or_rejected").matches("0"))
                    {
                        holder.reason.setVisibility(View.GONE);
                    }
                    else
                    {
                        holder.reason.setVisibility(View.VISIBLE);
                        holder.reason.setText("Reason: "+employeeLeaveLevelUpdateArray.getJSONObject(position).getString("rejection_reason"));
                    }
                }

                else
                if(employeeLeaveLevelUpdateArray.getJSONObject(position).getString("approved_or_rejected").matches("2"))
                {
                    holder.status.setText("Status: "+"Rejected");
                    holder.status.setTextColor(ContextCompat.getColor(context, R.color.red));
                    if(employeeLeaveLevelUpdateArray.getJSONObject(position).getString("approved_or_rejected").matches("0"))
                    {
                        holder.reason.setVisibility(View.GONE);
                    }
                    else
                    {
                        holder.reason.setVisibility(View.VISIBLE);
                        holder.reason.setText("Reason: "+employeeLeaveLevelUpdateArray.getJSONObject(position).getString("rejection_reason"));
                    }
                }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;


    }

    static class Holder

    {
        TextView levelName,status,reason;
    }
}
