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
 * Created by Schoofi on 28-08-2019.
 */

public class EmployeeAddressesAdapter extends BaseAdapter {

    private Context context;
    private JSONArray employeeAddressesArray;

    public EmployeeAddressesAdapter(Context context, JSONArray employeeAddressesArray) {
        this.context = context;
        this.employeeAddressesArray = employeeAddressesArray;
    }

    @Override
    public int getCount() {
        return employeeAddressesArray.length();
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
            convertView = layoutInflater.inflate(R.layout.employee_directory_addresses_listview, null);

            holder.addressType = (TextView) convertView.findViewById(R.id.text_addressType);
            holder.addressDetails = (TextView) convertView.findViewById(R.id.text_addressDetails);
            convertView.setTag(holder);

        }
        else
        {
             holder = (Holder) convertView.getTag();
        }

        try {
            holder.addressType.setText(employeeAddressesArray.getJSONObject(position).getString("address_type"));
            holder.addressDetails.setText(employeeAddressesArray.getJSONObject(position).getString("address_line1")+" "+employeeAddressesArray.getJSONObject(position).getString("address_line2") +" "+employeeAddressesArray.getJSONObject(position).getString("locality") +" "+employeeAddressesArray.getJSONObject(position).getString("zipcode") +" "+employeeAddressesArray.getJSONObject(position).getString("state"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        TextView addressType,addressDetails;
    }
}
