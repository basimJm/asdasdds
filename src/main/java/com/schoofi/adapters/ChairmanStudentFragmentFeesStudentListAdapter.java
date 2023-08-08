package com.schoofi.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 24-06-2016.
 */
public class ChairmanStudentFragmentFeesStudentListAdapter extends BaseAdapter {

    private Context context;
    private JSONArray ChairmanStudentFragmentFeesStudentListArray;
    String Rs,temp;
    int value;

    public ChairmanStudentFragmentFeesStudentListAdapter(Context context, JSONArray ChairmanStudentFragmentFeesStudentListArray,int value,String temp) {
        this.context = context;
        this.ChairmanStudentFragmentFeesStudentListArray = ChairmanStudentFragmentFeesStudentListArray;
        this.value = value;
        this.temp = temp;
    }

    @Override
    public int getCount() {
        return ChairmanStudentFragmentFeesStudentListArray.length();
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
        final Holder holder;
        if(view == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.chairman_student_fragment_fees_student_listview, null);
            holder.name = (TextView) view.findViewById(R.id.txt_studentNameTeacher);

            holder.interest = (TextView) view.findViewById(R.id.txt_interest);
            holder.image = (ImageView) view.findViewById(R.id.imageView_studentImage);
            view.setTag(holder);
        }

        else
        {
            holder = (Holder) view.getTag();
        }

        try {

            Rs = context.getString(R.string.Rs);
            holder.name.setText(ChairmanStudentFragmentFeesStudentListArray.getJSONObject(i).getString("stu_name"));
           // holder.rollNumber.setText(ChairmanStudentFragmentFeesStudentListArray.getJSONObject(i).getString("class_roll_no"));
            if(temp.matches("1"))
            {
                holder.interest.setText("Amount: "+Rs+ChairmanStudentFragmentFeesStudentListArray.getJSONObject(i).getString("total_fee_amount"));
            }

            else
            {
                if(value==1)
                {
                    holder.interest.setText("Amount: "+Rs+ChairmanStudentFragmentFeesStudentListArray.getJSONObject(i).getString("total_fee_amount"));
                }

                else
                    if(value==2)
                    {
                        holder.interest.setText("Amount: "+Rs+ChairmanStudentFragmentFeesStudentListArray.getJSONObject(i).getString("total_basic_due"));
                    }

                    else if (value == 3) {
                        holder.interest.setText("Amount: " + Rs + ChairmanStudentFragmentFeesStudentListArray.getJSONObject(i).getString("receive_amount_without_interest"));
                    } else if (value == 4) {
                        holder.interest.setText("Amount: " + Rs + ChairmanStudentFragmentFeesStudentListArray.getJSONObject(i).getString("pending_fee"));
                    }

            }



            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+ChairmanStudentFragmentFeesStudentListArray.getJSONObject(i).getString("picture")).asBitmap().placeholder(R.drawable.person).error(R.drawable.person).into(new BitmapImageViewTarget(holder.image)
            {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.image.setImageDrawable(circularBitmapDrawable);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    static class Holder
    {
        TextView name,interest;
        ImageView image;
    }
}
