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
 * Created by harsh malhotra on 4/8/2016.
 */
public class ChairmanTaskAreaListViewAdapter extends BaseAdapter {

    private Context context;
    public JSONArray ChairmanTaskAreaArray;
    public boolean c;

    public ChairmanTaskAreaListViewAdapter(Context context, JSONArray ChairmanTaskAreaArray) {
        this.context = context;
        this.ChairmanTaskAreaArray = ChairmanTaskAreaArray;
    }

    @Override
    public int getCount() {
        return ChairmanTaskAreaArray.length();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder;

        if(convertView == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.chairman_task_area_listview, null);

            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linear_listView_teacherStudentAttendanceDetails);
            holder.subTaskName = (TextView) convertView.findViewById(R.id.txt_studentNameTeacher);
            holder.status = (ImageView) convertView.findViewById(R.id.imageView_studentAttendance);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.subTaskName.setText(ChairmanTaskAreaArray.getJSONObject(position).getString("value"));
            holder.status.setImageResource(R.drawable.greycircletick);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {

                if(ChairmanTaskAreaArray.getJSONObject(position).getString("isAdded").matches("1"))
                {
                    holder.status.setImageResource(R.drawable.greycircletick);
                    //System.out.print("kkkk");

                        ChairmanTaskAreaArray.getJSONObject(position).put("isAdded","0");
                    //System.out.print("Added"+ChairmanTaskAreaArray.toString());



                }

                else
                {
                    holder.status.setImageResource(R.drawable.greencircletick);
                    //System.out.print("kkkk1");

                    ChairmanTaskAreaArray.getJSONObject(position).put("isAdded","1");
                    //System.out.print("Removed"+ChairmanTaskAreaArray.toString());


                }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return convertView;
    }

    static class Holder
    {
        TextView subTaskName;
        LinearLayout linearLayout;
        ImageView status;
    }
}
