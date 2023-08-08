package com.schoofi.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoofi.activitiess.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Schoofi on 09-08-2016.
 */
public class TeacherCircularViewAdapter extends BaseAdapter {

    private Context context;
    private JSONArray teacherCircularViewArray;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format = new SimpleDateFormat("EEEE");
    String date,date4,month,year;
    String date1,date2;
    Date date3;
    String fontPath = "OpenSans-Regular.ttf";


    public TeacherCircularViewAdapter(Context context, JSONArray teacherCircularViewArray) {
        this.context = context;
        this.teacherCircularViewArray = teacherCircularViewArray;
    }

    @Override
    public int getCount() {
        return teacherCircularViewArray.length();
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        Holder holder;


        if(convertView == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.teacher_circular_listview_layout, null);
            holder.title = (TextView) convertView.findViewById(R.id.text_title);
            holder.date = (TextView) convertView.findViewById(R.id.text_date);
            holder.month = (TextView) convertView.findViewById(R.id.text_month);
            holder.icon = (ImageView) convertView.findViewById(R.id.image_view_icon);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            date = teacherCircularViewArray.getJSONObject(i).getString("created_date");


            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            //date2 = formatter1.format(date3);


            date1 = date.substring(8,10);
            month = date.substring(5,7);
            year = date.substring(0,4);
            //System.out.println(date1+month);
            holder.date.setText(date1);
            Typeface face = Typeface.createFromAsset(context.getAssets(), fontPath);
            holder.date.setTypeface(face);
            holder.month.setTypeface(face);

            switch(Integer.parseInt(month)) {
                case 1:

                    holder.month.setText("JAN");
                    break;

                case 2:
                    holder.month.setText("FEB");
                    break;

                case 3:
                    holder.month.setText("MAR");
                    break;

                case 4:
                    holder.month.setText("APR");
                    break;

                case 5:
                    holder.month.setText("MAY");
                    break;

                case 6:
                    holder.month.setText("JUN");
                    break;

                case 7:
                    holder.month.setText("JUL");
                    break;

                case 8:
                    holder.month.setText("AUG");
                    break;

                case 9:
                    holder.month.setText("SEP");
                    break;

                case 10:
                    holder.month.setText("OCT");
                    break;

                case 11:
                    holder.month.setText("NOV");
                    break;

                case 12:
                    holder.month.setText("DEC");
                    break;

                default:
                    holder.month.setText("MON");
                    break;

            }

            holder.title.setText(teacherCircularViewArray.getJSONObject(i).getString("title"));

            if(teacherCircularViewArray.getJSONObject(i).getString("assign_path").toString().matches("") || teacherCircularViewArray.getJSONObject(i).getString("assign_path").toString().matches("null"))
            {
                holder.icon.setVisibility(View.INVISIBLE);
            }

            else
            {
                holder.icon.setVisibility(View.VISIBLE);
                holder.icon.setImageResource(R.drawable.ic_action_attachment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        TextView title,date,month;
        ImageView icon;
    }
}
