package com.schoofi.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoofi.activitiess.ParentSingleUnpaidFeesDetailsScreen;
import com.schoofi.activitiess.ParentStudentFeesHistoryDetails;
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
public class ParentStudentFeesUnpaidAdapter extends BaseAdapter {

    private Context context;
    private JSONArray parentStudentFeesUnpaidArray;
    String periodStart,periodEnd,fee_duration,periodStart1,periodEnd1,feeDueDate,feeDueDate1;
    Date date1,date2,date3;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public ParentStudentFeesUnpaidAdapter(Context context, JSONArray parentStudentFeesUnpaidArray) {
        this.context = context;
        this.parentStudentFeesUnpaidArray = parentStudentFeesUnpaidArray;
    }

    @Override
    public int getCount() {
        return parentStudentFeesUnpaidArray.length();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder;
        if(convertView == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.parent_student_fees_unpaid_list_view, null);

            holder.period = (TextView) convertView.findViewById(R.id.text_period);
            holder.fees = (TextView) convertView.findViewById(R.id.text_totalFees);
            holder.payBy = (TextView) convertView.findViewById(R.id.text_payBy);
            //holder.payNow = (Button) convertView.findViewById(R.id.btn_pay);
            holder.checkBox = (ImageView) convertView.findViewById(R.id.checkbox_credentials);

            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            periodStart = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee_start_date");
            periodEnd = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee_end_date");
            feeDueDate = parentStudentFeesUnpaidArray.getJSONObject(position).getString("fee_due_date");

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

                date3 = formatter.parse(feeDueDate);

                feeDueDate1 = formatter1.format(date3);
            } catch (ParseException e) {
                e.printStackTrace();
            }





            holder.period.setText("Period : "+periodStart1+"-"+periodEnd1);
            holder.fees.setText("Total Fee : "+"Rs "+parentStudentFeesUnpaidArray.getJSONObject(position).getString("total_fee_amount")+"/-");
            holder.payBy.setText("Pay By : "+feeDueDate1);
            /*holder.payNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Utils.showToast(context,"kkkk");
                    Intent intent = new Intent(context, ParentSingleUnpaidFeesDetailsScreen.class);
                    intent.putExtra("position",position);
                    intent.putExtra("periodStart",periodStart1);
                    intent.putExtra("periodEnd",periodEnd1);
                    context.startActivity(intent);
                }
            });*/


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }

    static  class Holder
    {
        TextView period,fees,payBy;
        ImageView checkBox;
    }
}
