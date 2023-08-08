package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Schoofi on 02-02-2018.
 */

public class AdminHealthCardCommonDetailsListViewAdapter extends BaseAdapter {

    private Context context;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> description = new ArrayList<String>();


    public AdminHealthCardCommonDetailsListViewAdapter(Context context, ArrayList<String> titles,ArrayList<String> description) {
        this.context = context;
        this.titles = titles;
        this.description = description;
    }

    @Override
    public int getCount() {
        return titles.size();
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
        if(convertView == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.admin_health_card_common_listview_details, null);
            holder.title = (TextView) convertView.findViewById(R.id.text_name);
            holder.description = (TextView) convertView.findViewById(R.id.text_description);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        holder.title.setText(titles.get(position));
        holder.description.setText(description.get(position));
        return convertView;
    }

    static class Holder
    {
        private TextView title,description;
    }
}
