package com.schoofi.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
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

public class DiaryGroupTeacherListAdapter   extends BaseAdapter {
    private Context context;
    int count,f;
    private int selectedIndex;


    public JSONArray teacherStudentAttendanceArray1;
    private JSONArray filteredArray;

    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    public boolean c;

    public DiaryGroupTeacherListAdapter(Context context, JSONArray teacherStudentAttendanceArray1) {
        super();
        this.context = context;
        selectedIndex = -1;
        this.teacherStudentAttendanceArray1 = teacherStudentAttendanceArray1;
        this.filteredArray = teacherStudentAttendanceArray1;

        //inflater=(LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return filteredArray.length();
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

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.teacher_list_group_listview, null);
            holder = new Holder();

            holder.studentAttendanceImage = (ImageView) convertView.findViewById(R.id.imageView_studentAttendance);
            holder.studentImage = (ImageView) convertView.findViewById(R.id.imageView_studentImage);
            holder.studentName = (TextView) convertView.findViewById(R.id.txt_studentNameTeacher);


            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }



        try {
            holder.studentName.setText(filteredArray.getJSONObject(position).getString("teac_name"));

            mDrawableBuilder = TextDrawable.builder().round();
            holder.studentAttendanceImage.setTag(new Integer(position));
            TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(filteredArray.getJSONObject(position).getString("teac_name").charAt(0)), R.color.blue);
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+filteredArray.getJSONObject(position).getString("image")).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.studentImage)
            {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.studentImage.setImageDrawable(circularBitmapDrawable);
                }
            });

            if(filteredArray.getJSONObject(position).getString("isAdded").matches("1"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.greencircletick);

            }

            else
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.greycircletick);

            }


            holder.studentAttendanceImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if(filteredArray.getJSONObject((Integer) v.getTag()).getString("isAdded").matches("1"))
                        {
                            holder.studentAttendanceImage.setImageResource(R.drawable.crossredcircle);
                            teacherStudentAttendanceArray1.getJSONObject((Integer) v.getTag()).put("isAdded","0");
                        }

                        else
                        {
                            holder.studentAttendanceImage.setImageResource(R.drawable.greencircletick);
                            teacherStudentAttendanceArray1.getJSONObject((Integer) v.getTag()).put("isAdded","1");
                        }
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
        TextView studentName,studentId;
        ImageView studentImage,studentAttendanceImage;
    }
}
