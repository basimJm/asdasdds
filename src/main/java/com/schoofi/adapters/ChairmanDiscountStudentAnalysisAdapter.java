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

/**
 * Created by Schoofi on 28-06-2018.
 */

public class ChairmanDiscountStudentAnalysisAdapter extends BaseAdapter {
    private Context context;
    private JSONArray teacherCoordinaterPendingLeavesArray;
    String Rs;

    public ChairmanDiscountStudentAnalysisAdapter(Context context, JSONArray teacherCoordinaterPendingLeavesArray) {
        this.context = context;
        this.teacherCoordinaterPendingLeavesArray = teacherCoordinaterPendingLeavesArray;
    }

    @Override
    public int getCount() {
        return teacherCoordinaterPendingLeavesArray.length();
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
    public View getView(int position, View view, ViewGroup parent) {
        Holder holder;
        if(view == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.chairman_discount_analysis_class_listview, null);
            holder.className = (TextView) view.findViewById(R.id.text_fee_type4);
            holder.amount = (TextView) view.findViewById(R.id.text_fee_type14);
            holder.totalStudents = (TextView) view.findViewById(R.id.text_fee_type15);
            holder.totalStudents.setVisibility(View.GONE);
            view.setTag(holder);
        }

        else
        {
            holder = (Holder) view.getTag();
        }

        try {
            Rs = context.getString(R.string.Rs);
            // Utils.showToast(context,""+value);
            holder.className.setText("Student Name : "+teacherCoordinaterPendingLeavesArray.getJSONObject(position).getString("stu_name"));

            holder.amount.setText("Discount Amount: "+Rs+teacherCoordinaterPendingLeavesArray.getJSONObject(position).getString("discount_amount"));
            //holder.totalStudents.setText("Total Students: "+ChairmanStudentFragmentFeesClassArray.getJSONObject(i).getString("total_stu"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    static class Holder
    {
        TextView className,amount,totalStudents;
    }
}
