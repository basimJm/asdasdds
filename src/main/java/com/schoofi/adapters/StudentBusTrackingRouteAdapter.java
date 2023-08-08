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
 * Created by Schoofi on 03-04-2017.
 */

public class StudentBusTrackingRouteAdapter extends BaseAdapter {

    private Context context;
    private JSONArray studentBusTrackingRouteArray;

    public StudentBusTrackingRouteAdapter(Context context, JSONArray studentBusTrackingRouteArray) {
        this.context = context;
        this.studentBusTrackingRouteArray = studentBusTrackingRouteArray;
    }

    @Override
    public int getCount() {
        return studentBusTrackingRouteArray.length();
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
            convertView = layoutInflater.inflate(R.layout.teacher_homescreen_listview_item, null);
            holder.studentBusTrackingRouteName = (TextView) convertView.findViewById(R.id.btn_teacher_homescreen_class);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.studentBusTrackingRouteName.setText(studentBusTrackingRouteArray.getJSONObject(position).getString("bus_route_no")+"("+studentBusTrackingRouteArray.getJSONObject(position).getString("start_time")+"-"+studentBusTrackingRouteArray.getJSONObject(position).getString("end_time")+")");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    static  class Holder
    {
        private TextView studentBusTrackingRouteName;
    }
}
