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
 * Created by Schoofi on 24-06-2016.
 */
public class ChairmanStudentFragmentFeesSectionAdapter extends BaseAdapter {

    private Context context;
    private JSONArray ChairmanStudentFragmentFeesSectionArray;
    private int value;
    String Rs,temp;

    public ChairmanStudentFragmentFeesSectionAdapter(Context context, JSONArray ChairmanStudentFragmentFeesSectionArray, int value,String temp) {
        this.context = context;
        this.ChairmanStudentFragmentFeesSectionArray = ChairmanStudentFragmentFeesSectionArray;
        this.value = value;
        this.temp = temp;
    }
    @Override
    public int getCount() {
        return ChairmanStudentFragmentFeesSectionArray.length();
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
            view = layoutInflater.inflate(R.layout.chairman_student_fragment_fees_class_listview, null);
            holder.className = (TextView) view.findViewById(R.id.text_fee_type4);
            holder.amount = (TextView) view.findViewById(R.id.text_fee_type14);
            view.setTag(holder);
        }

        else
        {
            holder = (Holder) view.getTag();
        }

        try {
            Rs = context.getString(R.string.Rs);
            holder.className.setText("Section: "+ChairmanStudentFragmentFeesSectionArray.getJSONObject(i).getString("section_name"));
            if(temp.matches("1"))
            {
                holder.amount.setText("Amount: "+Rs+ChairmanStudentFragmentFeesSectionArray.getJSONObject(i).getString("total_fee_amount"));
            }

            else {
                if (value == 1) {
                    holder.amount.setText("Amount: " + Rs + ChairmanStudentFragmentFeesSectionArray.getJSONObject(i).getString("total_fee_amount"));
                } else if (value == 2) {
                    holder.amount.setText("Amount: " + Rs + ChairmanStudentFragmentFeesSectionArray.getJSONObject(i).getString("total_basic_due"));
                } else if (value == 3) {
                    holder.amount.setText("Amount: " + Rs + ChairmanStudentFragmentFeesSectionArray.getJSONObject(i).getString("receive_amount_without_interest"));
                } else if (value == 4) {
                    holder.amount.setText("Amount: " + Rs + ChairmanStudentFragmentFeesSectionArray.getJSONObject(i).getString("pending_fee"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    static class Holder
    {
        TextView className,amount;
    }
}
