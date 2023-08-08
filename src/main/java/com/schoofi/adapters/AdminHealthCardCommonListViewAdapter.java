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
 * Created by Schoofi on 30-01-2018.
 */

public class AdminHealthCardCommonListViewAdapter extends BaseAdapter {

    private Context context;
    private JSONArray adminHealthCardCommonListArray;
    private String value;
    String date1,date2,date3,date4;
    Date date5,date6;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public AdminHealthCardCommonListViewAdapter(Context context, JSONArray adminHealthCardCommonListArray, String value) {
        this.context = context;
        this.adminHealthCardCommonListArray = adminHealthCardCommonListArray;
        this.value = value;
    }

    @Override
    public int getCount() {
        return adminHealthCardCommonListArray.length();
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

        if(convertView == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.admin_health_card_common_listview_layout, null);
            holder.name = (TextView) convertView.findViewById(R.id.text_name);
            holder.duration = (TextView) convertView.findViewById(R.id.text_duration);
            holder.status = (TextView) convertView.findViewById(R.id.text_status);
            holder.optional = (TextView) convertView.findViewById(R.id.text_optional);
            convertView.setTag(holder);

        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            if(value.matches("1")) {
                holder.name.setText(adminHealthCardCommonListArray.getJSONObject(position).getString("vaccine_name"));
                if(adminHealthCardCommonListArray.getJSONObject(position).getString("vaccination_date").matches("0000-00-00"))
                {
                    holder.duration.setText("Vaccination Date:");
                }

                else {
                    date1 = adminHealthCardCommonListArray.getJSONObject(position).getString("vaccination_date");
                    try {
                        date5 = formatter.parse(date1);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                    date3 = formatter1.format(date5);
                    holder.duration.setText("Vaccination Date: " + date3);
                }

                    holder.status.setText("Status: "+adminHealthCardCommonListArray.getJSONObject(position).getString("status"));
                    holder.optional.setVisibility(View.GONE);



            }

            else
                if(value.matches("2"))
                {
                    holder.name.setText(adminHealthCardCommonListArray.getJSONObject(position).getString("disease"));
                    holder.status.setText("Hospitalisation: "+adminHealthCardCommonListArray.getJSONObject(position).getString("hospitalisation"));
                    if(adminHealthCardCommonListArray.getJSONObject(position).getString("hospitalisation").matches("No"))
                    {
                        holder.duration.setVisibility(View.GONE);
                        holder.optional.setVisibility(View.GONE);
                    }

                    else
                    {
                        holder.duration.setVisibility(View.VISIBLE);
                        holder.optional.setVisibility(View.GONE);
                        if(adminHealthCardCommonListArray.getJSONObject(position).getString("hospitalised_from_date").matches("0000-00-00") || adminHealthCardCommonListArray.getJSONObject(position).getString("hospitalised_to_date").matches("0000-00-00"))
                        {
                            holder.duration.setText("Duration:");
                        }

                        else
                        {
                            date1 = adminHealthCardCommonListArray.getJSONObject(position).getString("hospitalised_from_date");
                            try {
                                date5 = formatter.parse(date1);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                            date3 = formatter1.format(date5);

                            date2 = adminHealthCardCommonListArray.getJSONObject(position).getString("hospitalised_to_date");
                            try {
                                date6 = formatter.parse(date1);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy");
                            date4 = formatter2.format(date6);
                            holder.duration.setText(date3+" to "+date4);
                        }





                    }
                }

                else
                    if(value.matches("3"))
                    {
                        holder.name.setText("Insurance Provider: "+adminHealthCardCommonListArray.getJSONObject(position).getString("insurance_company"));
                        holder.status.setText("Coverage: "+adminHealthCardCommonListArray.getJSONObject(position).getString("insurance_coverage"));
                        holder.optional.setText("Type: "+adminHealthCardCommonListArray.getJSONObject(position).getString("insurance_type"));

                        if(adminHealthCardCommonListArray.getJSONObject(position).getString("to_date").matches("0000-00-00"))
                        {
                            holder.duration.setText("Valid Upto:");
                        }

                        else
                        {
                            date2 = adminHealthCardCommonListArray.getJSONObject(position).getString("to_date");
                            try {
                                date6 = formatter.parse(date2);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                            date4 = formatter1.format(date6);
                            holder.duration.setText("Valid Upto: "+date4);
                        }
                    }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        private TextView name,status,duration,optional;
    }
}
