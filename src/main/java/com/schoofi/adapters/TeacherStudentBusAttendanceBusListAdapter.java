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

/**
 * Created by Schoofi on 27-09-2016.
 */
public class TeacherStudentBusAttendanceBusListAdapter extends BaseAdapter {

    Context context;
    public JSONArray teacherStudentBusAttendanceArray;

    public TeacherStudentBusAttendanceBusListAdapter(Context context, JSONArray teacherStudentBusAttendanceArray) {
        this.context = context;
        this.teacherStudentBusAttendanceArray = teacherStudentBusAttendanceArray;
    }

    @Override
    public int getCount() {
        return teacherStudentBusAttendanceArray.length();
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

        if(view == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.admin_bus_listview, null);
            holder.busDriverName = (TextView) view.findViewById(R.id.txt_studentId);
            holder.busAddress = (TextView) view.findViewById(R.id.txt_studentId1);
            holder.busNumber = (TextView) view.findViewById(R.id.txt_studentNameTeacher);
            holder.image = (ImageView) view.findViewById(R.id.imageView_studentAttendance);
            holder.image.setVisibility(View.GONE);
            holder.linearLayout = (LinearLayout) view.findViewById(R.id.linear_listView_teacherStudentAttendanceDetails);
            holder.route = (TextView) view.findViewById(R.id.txt_route);
            holder.busAddress.setVisibility(View.GONE);
            view.setTag(holder);
        }

        else
        {
            holder = (Holder) view.getTag();
        }

        try {
            holder.busNumber.setText(teacherStudentBusAttendanceArray.getJSONObject(i).getString("bus_number"));
           // holder.busAddress.setText(teacherStudentBusAttendanceArray.getJSONObject(i).getString("address"));
            holder.busDriverName.setText(teacherStudentBusAttendanceArray.getJSONObject(i).getString("driver_name"));
            holder.route.setText("Route No: " + teacherStudentBusAttendanceArray.getJSONObject(i).getString("route_list"));


        } catch (JSONException e) {
        e.printStackTrace();
        }
        return view;
    }

        static class Holder
        {
            TextView busDriverName,busNumber,busAddress,route;
            ImageView image;
            LinearLayout linearLayout;
        }
}
