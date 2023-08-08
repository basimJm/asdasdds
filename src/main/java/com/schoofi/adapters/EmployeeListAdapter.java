package com.schoofi.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Schoofi on 09-05-2018.
 */

public class EmployeeListAdapter extends BaseAdapter {
    private Context context;
    private JSONArray employeeListArray;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;

    public EmployeeListAdapter(Context context, JSONArray employeeListArray) {
        this.context = context;
        this.employeeListArray = employeeListArray;
    }

    @Override
    public int getCount() {
        return employeeListArray.length();
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
        final Holder holder;

        if(convertView == null)
        {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.employee_directory_listview, null);
            holder.employeeName = (TextView) convertView.findViewById(R.id.txt_employee_name);
            holder.employeeImage = (ImageView) convertView.findViewById(R.id.imageView_teacher_image);
            holder.employeeDepartment = (TextView) convertView.findViewById(R.id.txt_employee_department_name);
            holder.teacherNumber = (ImageView) convertView.findViewById(R.id.imageView_call_image);

            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            String emp_first_name,emp_middle_name,emp_last_name;
            emp_first_name = employeeListArray.getJSONObject(position).getString("emp_first_name");
            emp_middle_name = employeeListArray.getJSONObject(position).getString("emp_middle_name");
            emp_last_name = employeeListArray.getJSONObject(position).getString("emp_last_name");

            mDrawableBuilder = TextDrawable.builder().round();
            TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(employeeListArray.getJSONObject(position).getString("emp_first_name").charAt(0)), R.color.blue);
            String imagePath = employeeListArray.getJSONObject(position).getString("emp_image");
            if(imagePath.matches("") || imagePath.matches("null"))
            {
                imagePath = "jdjkjkd";
            }
			/*Picasso.with(context).load(imagePath).placeholder(R.drawable.profilebig).
			error(R.drawable.profilebig).transform(new CircleTransform()).into(holder.studentImage);*/
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+imagePath).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.employeeImage)
            {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.employeeImage.setImageDrawable(circularBitmapDrawable);
                }
            });


            if(emp_first_name.matches("") || emp_first_name.matches("null"))
            {
                emp_first_name = "";
            }
            else
            {
                emp_first_name = employeeListArray.getJSONObject(position).getString("emp_first_name");
                Log.d("erm",emp_first_name);
            }

            if(emp_middle_name.matches("") || emp_middle_name.matches("null"))
            {
                emp_middle_name = "";
            }
            else
            {
                emp_middle_name = " "+employeeListArray.getJSONObject(position).getString("emp_middle_name");
                Log.d("er1m",emp_middle_name);
            }
            if(emp_last_name.matches("") || emp_last_name.matches("null"))
            {
                emp_last_name ="";
            }
            else
            {
                emp_last_name = " "+employeeListArray.getJSONObject(position).getString("emp_last_name");
                Log.d("er2m",emp_last_name);
            }


            String finalText = emp_first_name.concat(emp_middle_name).concat(emp_last_name);
            holder.employeeName.setText(finalText);
            holder.employeeDepartment.setText(employeeListArray.getJSONObject(position).getString("dept_name"));

            holder.teacherNumber.setTag(new Integer(position));

            holder.teacherNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:"+employeeListArray.getJSONObject((Integer) v.getTag()).getString("employee_mobile_no")));
                        context.startActivity(callIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });





        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        TextView employeeName,employeeDepartment;
        ImageView employeeImage,teacherNumber;
    }


}
