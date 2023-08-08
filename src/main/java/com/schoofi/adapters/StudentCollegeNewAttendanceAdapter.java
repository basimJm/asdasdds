package com.schoofi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 13-01-2020.
 */

public class StudentCollegeNewAttendanceAdapter extends BaseAdapter {

    private Context context;
    private JSONArray teacherHomeScreenArray;
    private String value;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;



    public StudentCollegeNewAttendanceAdapter(Context context,JSONArray teacherHomeScreenArray,String value) {

        this.context = context;
        this.teacherHomeScreenArray = teacherHomeScreenArray;
        this.value = value;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return teacherHomeScreenArray.length();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        try {
            return teacherHomeScreenArray.getJSONObject(position);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder;
        if(convertView == null)
        {   holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.college_student_attendance_listview_design, null);
            holder.subjectName = (TextView) convertView.findViewById(R.id.text_subject_name);
            holder.subjectAttendance = convertView.findViewById(R.id.text_subject_attendance);
            holder.subjectImage = convertView.findViewById(R.id.img_subject_name);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            Preferences.getInstance().loadPreference(context);


            if(value.matches("1")) {


                if (teacherHomeScreenArray.getJSONObject(position).getString("subject_name").length() == 0)
                {

                }
                else {


                    //split the string using 'space'
                    //and print the first character of every word
                    String words[] = teacherHomeScreenArray.getJSONObject(position).getString("subject_name").split(" ");
                    String co = "";
                    for (String word : words) {

                        try {

                            co = co.concat(Character.toUpperCase(word.charAt(0)) + " ");
                        }
                        catch(Exception e)
                        {

                        }
                    }

                    mDrawableBuilder = TextDrawable.builder().round();
                    TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(co), ColorTemplate.VORDIPLOM_COLORS[position]);
                    textDrawable.setIntrinsicHeight(12);
                    Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.subjectImage)
                    {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.subjectImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }

                holder.subjectName.setText(teacherHomeScreenArray.getJSONObject(position).getString("subject_name"));
                holder.subjectAttendance.setText(teacherHomeScreenArray.getJSONObject(position).getString("present_attendance")+"/"+teacherHomeScreenArray.getJSONObject(position).getString("total_attendance"));
                int total_attendance=0,present_attendance=0;
                float attendance_percentage =0;
                total_attendance = (total_attendance+Integer.parseInt(teacherHomeScreenArray.getJSONObject(position).getString("total_attendance")));
                present_attendance = (present_attendance+Integer.parseInt(teacherHomeScreenArray.getJSONObject(position).getString("present_attendance")));
                if(total_attendance==0)
                {
                    attendance_percentage = 0;

                }
                else
                {
                    attendance_percentage = (present_attendance / total_attendance) * 100;
                }

                if(!teacherHomeScreenArray.getJSONObject(position).getString("threshold").equalsIgnoreCase("null")) {

                    if (attendance_percentage > Integer.parseInt(teacherHomeScreenArray.getJSONObject(position).getString("threshold"))) {
                        holder.subjectAttendance.setTextColor(Color.parseColor("#009f4d"));
                    } else if (attendance_percentage < Integer.parseInt(teacherHomeScreenArray.getJSONObject(position).getString("threshold"))) {
                        holder.subjectAttendance.setTextColor(Color.parseColor("#ee4749"));
                    }
                }
                else{
                    holder.subjectAttendance.setTextColor(Color.parseColor("#009f4d"));
                }
            }
            else
            if(value.matches("3"))
            {
               // holder.teacherHomeScreenListItemButton.setText(teacherHomeScreenArray.getJSONObject(position).getString("subject"));
                holder.subjectAttendance.setVisibility(View.GONE);
            }
            else {
                if(Preferences.getInstance().schoolType.matches("College")) {
                    holder.subjectName.setText(teacherHomeScreenArray.getJSONObject(position).getString("subject"));
                    String words[] = teacherHomeScreenArray.getJSONObject(position).getString("subject").split(" ");
                    String co = "";
                    for (String word : words) {

                        co = co.concat(Character.toUpperCase(word.charAt(0)) + " ");
                    }

                    mDrawableBuilder = TextDrawable.builder().round();
                    TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(co), ColorTemplate.VORDIPLOM_COLORS[position]);
                    textDrawable.setIntrinsicHeight(12);
                    Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.subjectImage)
                    {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.subjectImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                    holder.subjectAttendance.setVisibility(View.GONE);
                }
                else
                {
                    holder.subjectName.setText(teacherHomeScreenArray.getJSONObject(position).getString("subject_name"));
                    String words[] = teacherHomeScreenArray.getJSONObject(position).getString("subject_name").split(" ");
                    String co = "";
                    for (String word : words) {

                        co = co.concat(Character.toUpperCase(word.charAt(0)) + " ");
                    }

                    mDrawableBuilder = TextDrawable.builder().round();
                    TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(co), ColorTemplate.VORDIPLOM_COLORS[position]);
                    textDrawable.setIntrinsicHeight(12);
                    Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.subjectImage)
                    {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.subjectImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                    holder.subjectAttendance.setVisibility(View.GONE);
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertView;
    }

    static class Holder
    {
        private TextView subjectName,subjectAttendance;
        private ImageView subjectImage;
    }
}
