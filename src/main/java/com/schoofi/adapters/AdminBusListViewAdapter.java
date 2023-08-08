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

import java.util.ArrayList;


/**
 * Created by Schoofi on 11-05-2016.
 */
public class AdminBusListViewAdapter extends BaseAdapter {

    Context context;
    public JSONArray adminBusListArray;
    public boolean c;


    public AdminBusListViewAdapter(Context context, JSONArray adminBusListArray) {
        this.context = context;
        this.adminBusListArray = adminBusListArray;
    }

    @Override
    public int getCount() {
        return adminBusListArray.length();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final Holder holder;

        if(view == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.admin_bus_listview, null);
            holder.busDriverName = (TextView) view.findViewById(R.id.txt_studentId);
            holder.busAddress = (TextView) view.findViewById(R.id.txt_studentId1);
            holder.busNumber = (TextView) view.findViewById(R.id.txt_studentNameTeacher);
            holder.image = (ImageView) view.findViewById(R.id.imageView_studentAttendance);
            holder.linearLayout = (LinearLayout) view.findViewById(R.id.linear_listView_teacherStudentAttendanceDetails);
            holder.route = (TextView) view.findViewById(R.id.txt_route);

            view.setTag(holder);
        }

        else
        {
            holder = (Holder) view.getTag();
        }

        try {
            holder.busNumber.setText(adminBusListArray.getJSONObject(i).getString("bus_number"));
            holder.busAddress.setText(adminBusListArray.getJSONObject(i).getString("address"));
            holder.busDriverName.setText(adminBusListArray.getJSONObject(i).getString("driver_name"));
            holder.route.setText("Route No: "+adminBusListArray.getJSONObject(i).getString("route_no"));

            holder.image.setVisibility(View.INVISIBLE);
            holder.image.setImageResource(R.drawable.greycircletick);
           holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(c == false)
                    {
                        holder.image.setImageResource(R.drawable.greencircletick);
                        try {
                            adminBusListArray.getJSONObject(i).put("maps", "P");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        c=true;
                    }

                    else
                    {
                        holder.image.setImageResource(R.drawable.greycircletick);
                        try {
                            adminBusListArray.getJSONObject(i).put("maps", "A");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        c = false;
                    }
                }
            });
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
