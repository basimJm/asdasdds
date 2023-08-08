package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.ChairmanEmployeeDepartmentWiseCount;
import com.schoofi.activitiess.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Schoofi on 13-04-2019.
 */

public class ChairmanEmployeeDepartwiseCountAdapter extends BaseAdapter {

    private Context context;
    private JSONArray chairmanEmployeeDepartmentWiseCountArray,employeeDataArray;


    public ChairmanEmployeeDepartwiseCountAdapter(Context context, JSONArray chairmanEmployeeDepartmentWiseCountArray)
    {
        super();
        this.context = context;
        this.chairmanEmployeeDepartmentWiseCountArray = chairmanEmployeeDepartmentWiseCountArray;
    }


    @Override
    public int getCount() {
        return chairmanEmployeeDepartmentWiseCountArray.length();
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
            convertView = layoutInflater.inflate(R.layout.employee_attendance_without_graph_listview_layout, null);
            holder.departmentName = (TextView) convertView.findViewById(R.id.text_date);
            holder.present = (TextView) convertView.findViewById((R.id.text_in_time));
            holder.absent = (TextView) convertView.findViewById((R.id.text_out_time));
            holder.leave = (TextView) convertView.findViewById((R.id.text_out_time1));
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }


        try {


            holder.departmentName.setText(chairmanEmployeeDepartmentWiseCountArray.getJSONObject(position).getString("dept_name"));
            int total=0,present=0,absent=0,leave=0;
            System.out.print(chairmanEmployeeDepartmentWiseCountArray.getJSONObject(position).getString("emp_data"));

             if(chairmanEmployeeDepartmentWiseCountArray.getJSONObject(position).getString("emp_data").matches("null"))
             {
                 total=0;
                 present = 0;
                 absent = 0;
                 leave = 0;
                 holder.present.setText((present+"/"+(total)));
                 holder.absent.setText((absent+"/"+(total)));
                 holder.leave.setText((leave+"/"+(total)));
             }
             else
             {
                 employeeDataArray = new JSONArray(chairmanEmployeeDepartmentWiseCountArray.getJSONObject(position).getString("emp_data"));
                 for(int i=0;i<employeeDataArray.length();i++)
                 {
                     total = i;
                     if(employeeDataArray.getJSONObject(i).getString("attendance").matches("P"))
                     {
                         present = present+1;
                     }
                     else
                         if(employeeDataArray.getJSONObject(i).getString("attendance").matches("A"))
                     {
                         absent = absent+1;
                     }

                     else

                     {
                        leave = leave+1;
                     }

                 }
                 holder.present.setText((present+"/"+(total+1)));
                 holder.absent.setText((absent+"/"+(total+1)));
                 holder.leave.setText((leave+"/"+(total+1)));

             }


        } catch (JSONException e) {
            e.printStackTrace();
        }




        return convertView;
    }

    static class Holder
    {
        private TextView departmentName,present,absent,leave;
    }
}
