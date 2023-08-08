package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.utils.ParentStudentFeesUnpaidMultilevelChildVO;
import com.schoofi.utils.ParentStudentFessUnpaidMultilevelParentVO;
import com.schoofi.utils.RoyalityChildVO;
import com.schoofi.utils.RoyalityParentVO;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Schoofi on 04-09-2016.
 */
public class RoyalityAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<RoyalityParentVO> parentStudentFessUnpaidMultilevelParentVOs;
    private JSONArray employeeListArray;
    String periodStart,periodEnd,fee_duration,periodStart1,periodEnd1,feeDueDate,feeDueDate1;
    Date date1,date2,date3;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public RoyalityAdapter(Context context, ArrayList<RoyalityParentVO> parentStudentFessUnpaidMultilevelParentVOs, JSONArray employeeListArray) {
        this.context = context;
        this.parentStudentFessUnpaidMultilevelParentVOs = parentStudentFessUnpaidMultilevelParentVOs;
        this.employeeListArray = employeeListArray;

    }

    @Override
    public int getGroupCount() {
        return parentStudentFessUnpaidMultilevelParentVOs.size();
    }

    @Override
    public int getChildrenCount(int i) {
        ArrayList<RoyalityChildVO>  parentStudentFeesUnpaidMultilevelChildVOs = parentStudentFessUnpaidMultilevelParentVOs.get(i).getItems();

        return parentStudentFeesUnpaidMultilevelChildVOs.size();
    }

    @Override
    public Object getGroup(int i) {
        return parentStudentFessUnpaidMultilevelParentVOs.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        ArrayList<RoyalityChildVO>  parentStudentFeesUnpaidMultilevelChildVOs = parentStudentFessUnpaidMultilevelParentVOs.get(i).getItems();

        return parentStudentFeesUnpaidMultilevelChildVOs.get(i1);
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
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        final Holder holder;
        RoyalityParentVO parentStudentFessUnpaidMultilevelParentVO = (RoyalityParentVO) getGroup(i);
        if (view == null) {
            holder = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.royality_parent_layout, viewGroup,false);
            holder.totalFees = (TextView) view.findViewById(R.id.text_date_fees);
            holder.feesPeriod = (TextView) view.findViewById(R.id.text_date_title);


            view.setTag(holder);
        }

        else
        {
            holder = (Holder) view.getTag();
        }





        periodStart = parentStudentFessUnpaidMultilevelParentVO.getFeeDate();

        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        try {

            date1 = formatter.parse(periodStart);

            periodStart1 = formatter1.format(date1);


        } catch (ParseException e) {
            e.printStackTrace();
        }



        holder.totalFees.setText("Total Fee : "+"Rs "+parentStudentFessUnpaidMultilevelParentVO.getFeeTotalAmount()+"/-");

        holder.feesPeriod.setText(periodStart1);



        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        Holder holder1;

        RoyalityChildVO parentStudentFeesUnpaidMultilevelChildVO = (RoyalityChildVO) getChild(i,i1);

        if (view == null) {
            holder1 = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.parent_unpaid_fees_new_multilevel_child, viewGroup,false);
            holder1.feesType = (TextView) view.findViewById(R.id.text_fee_type);
            holder1.feeAmount = (TextView) view.findViewById(R.id.text_fee);
            view.setTag(holder1);
        }

        else
        {
            holder1 = (Holder) view.getTag();
        }




        holder1.feesType.setText(parentStudentFeesUnpaidMultilevelChildVO.getFeesType().toString());
        holder1.feeAmount.setText("Rs "+parentStudentFeesUnpaidMultilevelChildVO.getAmount().toString());

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
