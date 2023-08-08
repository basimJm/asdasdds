package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoofi.activitiess.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Schoofi on 02-09-2016.
 */
public class ParentUnpaidFeesNewMultilevelAdapter extends BaseExpandableListAdapter {

    private Context context;
    private JSONArray parentStudentUnpaidMultilevelParentArray,getParentStudentUnpaidMultilevelChildArray;
    String periodStart,periodEnd,fee_duration,periodStart1,periodEnd1,feeDueDate,feeDueDate1;
    Date date1,date2,date3;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


    public ParentUnpaidFeesNewMultilevelAdapter(Context context, JSONArray parentStudentUnpaidMultilevelParentArray, JSONArray getParentStudentUnpaidMultilevelChildArray) {
        this.context = context;
        this.parentStudentUnpaidMultilevelParentArray = parentStudentUnpaidMultilevelParentArray;
        this.getParentStudentUnpaidMultilevelChildArray = getParentStudentUnpaidMultilevelChildArray;
    }

    @Override
    public int getGroupCount() {
        return parentStudentUnpaidMultilevelParentArray.length();
    }

    @Override
    public int getChildrenCount(int i) {
        return getParentStudentUnpaidMultilevelChildArray.length();
    }

    @Override
    public Object getGroup(int i) {
        try {
            return parentStudentUnpaidMultilevelParentArray.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {

        try {
            return getParentStudentUnpaidMultilevelChildArray.getJSONObject(i1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        final Holder holder;
        if (view == null) {
            holder = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.parent_unpaid_fees_new_multilevel_parent, viewGroup,false);
            holder.totalFees = (TextView) view.findViewById(R.id.text_totalFees);
            holder.circleTick = (ImageView) view.findViewById(R.id.checkbox_credentials);
            holder.payBy = (TextView) view.findViewById(R.id.text_payBy);
            view.setTag(holder);
        }

        else
        {
            holder = (Holder) view.getTag();
        }



        try {
            feeDueDate = parentStudentUnpaidMultilevelParentArray.getJSONObject(i).getString("fee_due_date");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            try {
                date3 = formatter.parse(feeDueDate);
                feeDueDate1 = formatter1.format(date3);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.totalFees.setText("Total Fee : "+"Rs "+parentStudentUnpaidMultilevelParentArray.getJSONObject(i).getString("total_fee_amount")+"/-");
            holder.payBy.setText("Pay By : "+feeDueDate1);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        Holder holder1;

        if (view == null) {
            holder1 = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.parent_unpaid_fees_new_multilevel_child, viewGroup,false);
            holder1.feesPeriod = (TextView) view.findViewById(R.id.text_period);
            holder1.feesType = (TextView) view.findViewById(R.id.text_fee_type);
            holder1.feeAmount = (TextView) view.findViewById(R.id.text_fee);
            view.setTag(holder1);
        }

        else
        {
            holder1 = (Holder) view.getTag();
        }

        try {
            periodStart = getParentStudentUnpaidMultilevelChildArray.getJSONObject(i1).getString("fee_start_date");
            periodEnd = getParentStudentUnpaidMultilevelChildArray.getJSONObject(i1).getString("fee_end_date");

            try {
                date1 = formatter.parse(periodStart);
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                periodStart1 = formatter1.format(date1);

                date2 = formatter.parse(periodEnd);

                periodEnd1 = formatter1.format(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder1.feesPeriod.setText("Period : "+periodStart1+"-"+periodEnd1);
            holder1.feesType.setText(getParentStudentUnpaidMultilevelChildArray.getJSONObject(i1).getString("fee_type_text"));
            holder1.feeAmount.setText("Rs "+getParentStudentUnpaidMultilevelChildArray.getJSONObject(i1).getString("fee_amount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    static class Holder
    {
        TextView feesPeriod,totalFees,feesType,feeAmount,payBy;
        ImageView circleTick;

    }
}
