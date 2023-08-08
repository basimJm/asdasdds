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

public class TutorialListViewDesignAdapter extends BaseAdapter {

    private Context context;
    private JSONArray tutorialListArray;
    String date1,date2,date3,date4,value;
    Date date5,date6;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public TutorialListViewDesignAdapter(Context context, JSONArray tutorialListArray) {
        this.context = context;
        this.tutorialListArray = tutorialListArray;
    }

    @Override
    public int getCount() {
        return tutorialListArray.length();
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
            convertView = layoutInflater.inflate(R.layout.tutorial_list_design, null);
            holder.title = convertView.findViewById(R.id.text_student_leave_title);
            holder.uploadedAt = convertView.findViewById(R.id.text_student_leave_starting_date);
            holder.uploadedBy = convertView.findViewById(R.id.text_employee_name);


            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.title.setText(tutorialListArray.getJSONObject(position).getString("title"));
            date1 = tutorialListArray.getJSONObject(position).getString("last_date");


            try {
                date5 = formatter.parse(date1);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            date3 = formatter1.format(date5);
           holder.uploadedAt.setText("Uploaded Date: "+date3);
           holder.uploadedBy.setText("Uploaded By: "+tutorialListArray.getJSONObject(position).getString("Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        private TextView title,uploadedBy,uploadedAt;
    }
}
