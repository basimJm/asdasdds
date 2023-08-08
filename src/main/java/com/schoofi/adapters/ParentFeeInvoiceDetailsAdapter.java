package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;

import java.util.ArrayList;


/**
 * Created by Schoofi on 26-05-2017.
 */

public class ParentFeeInvoiceDetailsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> aList;
    private ArrayList<String> aList1;
    String Rs;
    public ParentFeeInvoiceDetailsAdapter(Context context, ArrayList<String> aList, ArrayList<String> aList1) {
        this.context = context;
        this.aList = aList;
        this.aList1 = aList1;
    }

    @Override
    public int getCount() {
        return aList.size();
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
        if(convertView == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.student_fee_invoice_details_layout, null);

            holder.feeType = (TextView) convertView.findViewById(R.id.text_fee_type_name);
            holder.feeTypePrice = (TextView) convertView.findViewById(R.id.text_fee_type_name_fees);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        Rs = context.getString(R.string.Rs);

        holder.feeType.setText(aList.get(position));
        holder.feeTypePrice.setText(Rs+aList1.get(position)+"/-");
        return convertView;
    }

    static class Holder
    {
        TextView feeType,feeTypePrice;
    }
}
