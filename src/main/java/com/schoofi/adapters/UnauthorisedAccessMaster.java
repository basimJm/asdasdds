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
 * Created by Schoofi on 15-06-2018.
 */

public class UnauthorisedAccessMaster extends BaseAdapter {
    private Context context;
    private JSONArray unauthorisedAccessArray;

    public UnauthorisedAccessMaster(Context context, JSONArray unauthorisedAccessArray) {
        this.context = context;
        this.unauthorisedAccessArray = unauthorisedAccessArray;
    }

    @Override
    public int getCount() {
        return unauthorisedAccessArray.length();
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
            convertView = layoutInflater.inflate(R.layout.unauthorised_access_listitem, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.zoneName= (TextView) convertView.findViewById(R.id.zone_name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.name.setText(unauthorisedAccessArray.getJSONObject(position).getString("name_on_card"));
            holder.zoneName.setText(unauthorisedAccessArray.getJSONObject(position).getString("zone_name"));
            holder.type.setText(unauthorisedAccessArray.getJSONObject(position).getString("card_issued_type"));
            holder.time.setText(unauthorisedAccessArray.getJSONObject(position).getString("date_time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }

    static class Holder
    {
        TextView name,zoneName,type,time;
    }
}
