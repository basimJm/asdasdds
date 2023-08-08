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
 * Created by Schoofi on 27-06-2018.
 */

public class ChairmanDiscountSectionAdapter extends BaseAdapter {
    private Context context;
    private JSONArray ChairmanStudentFragmentFeesClassArray;
    private int value;
    private String temp;
    String Rs;
    String className;

    public ChairmanDiscountSectionAdapter(Context context, JSONArray ChairmanStudentFragmentFeesClassArray,String className) {
        this.context = context;
        this.ChairmanStudentFragmentFeesClassArray = ChairmanStudentFragmentFeesClassArray;
        this.className = className;

    }

    @Override
    public int getCount() {
        return ChairmanStudentFragmentFeesClassArray.length();
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

        Holder holder;
        if(view == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.chairman_discount_analysis_class_listview, null);
            holder.className = (TextView) view.findViewById(R.id.text_fee_type4);
            holder.amount = (TextView) view.findViewById(R.id.text_fee_type14);
            holder.totalStudents = (TextView) view.findViewById(R.id.text_fee_type15);
            view.setTag(holder);
        }

        else
        {
            holder = (Holder) view.getTag();
        }

        try {
            Rs = context.getString(R.string.Rs);
            // Utils.showToast(context,""+value);
            holder.className.setText("Class : "+className+"-"+ChairmanStudentFragmentFeesClassArray.getJSONObject(i).getString("section_name"));

                holder.amount.setText("Discount Amount: "+Rs+ChairmanStudentFragmentFeesClassArray.getJSONObject(i).getString("total_discount"));
            holder.totalStudents.setText("Total Students: "+ChairmanStudentFragmentFeesClassArray.getJSONObject(i).getString("total_stu"));


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
