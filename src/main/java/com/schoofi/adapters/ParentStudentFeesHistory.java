package com.schoofi.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by harsh malhotra on 4/13/2016.
 */
public class ParentStudentFeesHistory extends BaseAdapter {

    private Context context;
    private JSONArray parentStudentFeesHistoryArray;
    String periodStart,periodEnd,periodStart1,periodEnd1,paymentDate,paymentDate1;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Date date1,date2,date3;
    String Rs;




    public ParentStudentFeesHistory(Context context, JSONArray parentStudentFeesHistoryArray) {
        this.context = context;
        this.parentStudentFeesHistoryArray = parentStudentFeesHistoryArray;
    }

    @Override
    public int getCount() {
        return parentStudentFeesHistoryArray.length();
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
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.parent_student_listview_history, null);

            holder.period = (TextView) convertView.findViewById(R.id.text_period);
            holder.status = (TextView) convertView.findViewById(R.id.text_totalFees);
            holder.paidOn = (TextView) convertView.findViewById(R.id.text_payBy);

            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            periodStart = parentStudentFeesHistoryArray.getJSONObject(position).getString("fee_start_date");
            periodEnd = parentStudentFeesHistoryArray.getJSONObject(position).getString("fee_end_date");
            paymentDate = parentStudentFeesHistoryArray.getJSONObject(position).getString("payment_date");

            /*switch(Integer.parseInt(periodStart))
            {
                case 1: periodStart1 = "JAN";
                    break;
                case 2: periodStart1 = "FEB";
                    break;
                case 3: periodStart1 = "MAR";
                    break;
                case 4: periodStart1 = "APR";
                    break;
                case 5: periodStart1 = "MAY";
                    break;
                case 6: periodStart1 = "JUN";
                    break;
                case 7: periodStart1 = "JUL";
                    break;
                case 8: periodStart1 = "AUG";
                    break;
                case 9: periodStart1 = "SEP";
                    break;
                case 10: periodStart1 = "OCT";
                    break;
                case 11: periodStart1 = "NOV";
                    break;
                case 12: periodStart1 = "DEC";
                    break;
                default:
                    Log.d("kkkk","jjjj");
                    break;



            }

            switch(Integer.parseInt(periodEnd))
            {
                case 1: periodEnd1 = "JAN";
                    break;
                case 2: periodEnd1 = "FEB";
                    break;
                case 3: periodEnd1 = "MAR";
                    break;
                case 4: periodEnd1 = "APR";
                    break;
                case 5: periodEnd1 = "MAY";
                    break;
                case 6: periodEnd1 = "JUN";
                    break;
                case 7: periodEnd1 = "JUL";
                    break;
                case 8: periodEnd1 = "AUG";
                    break;
                case 9: periodEnd1 = "SEP";
                    break;
                case 10: periodEnd1 = "OCT";
                    break;
                case 11: periodEnd1 = "NOV";
                    break;
                case 12: periodEnd1 = "DEC";
                    break;
                default:
                    Log.d("kkkk","jjjj");
                    break;



            }*/

            try {
                date1 = formatter.parse(periodStart);
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                periodStart1 = formatter1.format(date1);

                date2 = formatter.parse(periodEnd);

                periodEnd1 = formatter1.format(date2);

                date3 = formatter4.parse(paymentDate);
                paymentDate1 = formatter1.format(date3);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Rs = context.getString(R.string.Rs);

            holder.period.setText("Invoice Number : "+parentStudentFeesHistoryArray.getJSONObject(position).getString("inv_no"));
            holder.status.setText("Paid Amount : "+Rs+parentStudentFeesHistoryArray.getJSONObject(position).getString("paid_amount")+"/-");
            //holder.paidOn.setText("Pay By : "+parentStudentFeesHistoryArray.getJSONObject(position).getString("total_fee_amount")+" by "+parentStudentFeesUnpaidArray.getJSONObject(position).getString("due_date")+" of every month")

           // Utils.showToast(context,date1);

                holder.paidOn.setText("Payment Date : "+paymentDate1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    static class Holder
    {
        TextView period,status,paidOn;
    }
}
