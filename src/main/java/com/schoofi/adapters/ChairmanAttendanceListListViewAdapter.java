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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Schoofi on 20-07-2016.
 */
public class ChairmanAttendanceListListViewAdapter extends BaseAdapter {

    private Context context;
    private JSONArray chairmanAttendanceListListViewArray;
    String date1,date2;
    Date date3;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public ChairmanAttendanceListListViewAdapter(Context context, JSONArray chairmanAttendanceListListViewArray) {
        this.context = context;
        this.chairmanAttendanceListListViewArray = chairmanAttendanceListListViewArray;
    }

    @Override
    public int getCount() {
        return chairmanAttendanceListListViewArray.length();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        Holder holder;

        if(convertView == null)
        {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.chairman_attendance_list_listview, null);
            holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
            holder.textView2 = (TextView) convertView.findViewById(R.id.textView2);

            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            date1 = chairmanAttendanceListListViewArray.getJSONObject(position).getString("date");
            try {
                date3 = formatter.parse(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            date2 = formatter1.format(date3);
            holder.textView1.setText(date2);
            holder.textView2.setText(chairmanAttendanceListListViewArray.getJSONObject(position).getString("total_p")+"/"+chairmanAttendanceListListViewArray.getJSONObject(position).getString("total_recorded"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;


    }

    static class Holder

    {
        TextView textView1,textView2;
    }
}

