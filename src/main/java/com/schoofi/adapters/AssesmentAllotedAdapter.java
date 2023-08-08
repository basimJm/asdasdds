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
 * Created by Schoofi on 14-11-2019.
 */

public class AssesmentAllotedAdapter extends BaseAdapter {

    private Context context;
    private JSONArray assesmentAllotedArray;

    public AssesmentAllotedAdapter(Context context, JSONArray assesmentAllotedArray) {
        this.context = context;
        this.assesmentAllotedArray = assesmentAllotedArray;
    }



    @Override
    public int getCount() {
        return assesmentAllotedArray.length();
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
    public View getView(int position, View view, ViewGroup parent) {
        final Holder holder;

        if(view == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.assesment_alloted_listview, null);

            holder.adminClassListName = (TextView) view.findViewById(R.id.text_admin_class);
            view.setTag(holder);
        }

        else
        {
            holder = (Holder) view.getTag();
        }

        try {
            holder.adminClassListName.setText(assesmentAllotedArray.getJSONObject(position).getString("test_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    static class Holder
    {
        TextView adminClassListName;
    }
}
