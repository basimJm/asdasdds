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
 * Created by Schoofi on 08-03-2017.
 */

public class BusRouteListAdapter extends BaseAdapter {

    private Context context;
    private JSONArray teacherStudentBusListArray;

    public BusRouteListAdapter(Context context, JSONArray teacherStudentBusListArray) {
        this.context = context;
        this.teacherStudentBusListArray = teacherStudentBusListArray;
    }

    @Override
    public int getCount() {
        return teacherStudentBusListArray.length();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final Holder holder;

        if(view== null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.bus_route, null);

            holder.routeName = (TextView) view.findViewById(R.id.text_bus_route);
            view.setTag(holder);
        }

        else
        {
            holder = (Holder) view.getTag();
        }

        try {
            holder.routeName.setText(teacherStudentBusListArray.getJSONObject(i).getString("route_no"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    static class Holder {
        TextView routeName;
    }
}
