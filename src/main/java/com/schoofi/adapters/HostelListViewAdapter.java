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
import org.json.JSONObject;

/**
 * Created by Schoofi on 19-05-2019.
 */

public class HostelListViewAdapter extends BaseAdapter {

    private Context context;
    private JSONArray hostelRoomListArray;

    public HostelListViewAdapter(Context context, JSONArray hostelRoomListArray) {
        this.context = context;
        this.hostelRoomListArray = hostelRoomListArray;
    }




    @Override
    public int getCount() {
        return hostelRoomListArray.length();
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
            convertView = layoutInflater.inflate(R.layout.hostel_room_listview_layout, null);
            holder.roomNumber = (TextView) convertView.findViewById(R.id.text_room_number);
            holder.roomAvability = (TextView) convertView.findViewById(R.id.text_avability);
            holder.floorName = convertView.findViewById(R.id.text_floor_number);

            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.roomNumber.setText("Room Number: "+hostelRoomListArray.getJSONObject(position).getString("room_no"));
            holder.floorName.setText("Floor: "+hostelRoomListArray.getJSONObject(position).getString("floor_name"));
            holder.roomAvability.setText("Capacity Left: "+String.valueOf((Integer.parseInt(hostelRoomListArray.getJSONObject(position).getString("capacity"))-(Integer.parseInt(hostelRoomListArray.getJSONObject(position).getString("alloted_capacity"))-Integer.parseInt(hostelRoomListArray.getJSONObject(position).getString("reserved_capacity"))))));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;


    }

    static class Holder

    {
        TextView roomNumber,roomAvability,floorName;
    }
}
