package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.schoofi.activitiess.R;

import org.json.JSONArray;
import org.json.JSONException;

import polypicker.model.Image;

/**
 * Created by Schoofi on 11-03-2018.
 */

public class AdminStudentBusBoardingRouteAdapter extends BaseAdapter{
    private Context context;
    private JSONArray AdminStudentBusBoardingRouteArray;

    public AdminStudentBusBoardingRouteAdapter(Context context, JSONArray adminStudentBusBoardingRouteArray) {
        this.context = context;
        AdminStudentBusBoardingRouteArray = adminStudentBusBoardingRouteArray;
    }

    @Override
    public int getCount() {
        return AdminStudentBusBoardingRouteArray.length();
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

        if(convertView == null)
        {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.bus_boarding_bus_route_listview, null);
            holder = new Holder();
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linear_listView_teacherStudentAttendanceDetails);

            holder.busRouteName = (TextView) convertView.findViewById(R.id.txt_studentNameTeacher);

            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.busRouteName.setText(AdminStudentBusBoardingRouteArray.getJSONObject(position).getString("bus_route_no"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        LinearLayout linearLayout;
        ImageView studentAttendanceImage;
        TextView busRouteName;
    }
}
