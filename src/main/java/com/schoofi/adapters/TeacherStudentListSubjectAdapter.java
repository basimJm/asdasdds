package com.schoofi.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.schoofi.activitiess.R;
import com.schoofi.activitiess.TeacherStudentProfileImageUpload;
import com.schoofi.constants.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 08-06-2016.
 */
public class TeacherStudentListSubjectAdapter extends BaseAdapter {
    private Context context;
    private JSONArray teacherStudentSubjectListArray;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    String value;

    public TeacherStudentListSubjectAdapter(Context context, JSONArray teacherStudentSubjectListArray,String value) {
        this.context = context;
        this.teacherStudentSubjectListArray = teacherStudentSubjectListArray;
        this.value = value;
    }

    @Override
    public int getCount() {
        return teacherStudentSubjectListArray.length();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final Holder holder;
        if(view == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.teacher_subject_student_list, null);
            holder.statusImage = (ImageView) view.findViewById(R.id.imageView_studentImage);
            holder.studentName = (TextView) view.findViewById(R.id.txt_studentNameTeacher);
            holder.studentRollNo = (TextView) view.findViewById(R.id.txt_studentId);
            holder.attendance = (TextView) view.findViewById(R.id.text_attendance);
            view.setTag(holder);
        }

        else
        {
            holder = (Holder) view.getTag();
        }

        try {
           /* if(teacherStudentSubjectListArray.getJSONObject(i).getString("picture").matches("") || teacherStudentSubjectListArray.getJSONObject(i).getString("picture").matches("null"))
            {
                //holder.statusImage.setImageResource(R.drawable.uploadpicture);
            }

            else {*/
            mDrawableBuilder = TextDrawable.builder().round();
            TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(teacherStudentSubjectListArray.getJSONObject(i).getString("stu_name").charAt(0)), R.color.blue);
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL + teacherStudentSubjectListArray.getJSONObject(i).getString("picture")).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.statusImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.statusImage.setImageDrawable(circularBitmapDrawable);
                }
            });
            // }

            holder.statusImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        Intent intent = new Intent(context, TeacherStudentProfileImageUpload.class);
                        intent.putExtra("email", teacherStudentSubjectListArray.getJSONObject(i).getString("stu_email"));
                        intent.putExtra("value", "1");
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


            holder.studentName.setText("Name: " + teacherStudentSubjectListArray.getJSONObject(i).getString("stu_name"));


            holder.studentRollNo.setText("Roll Number: " + teacherStudentSubjectListArray.getJSONObject(i).getString("class_roll_no"));

            if (value.matches("1"))
            {

                if (teacherStudentSubjectListArray.getJSONObject(i).getString("attendance").matches("null") || teacherStudentSubjectListArray.getJSONObject(i).getString("attendance").matches("0") ||  teacherStudentSubjectListArray.getJSONObject(i).getString("attendance").matches("") ) {
                    holder.attendance.setText("NA");
                } else {
                    holder.attendance.setText("(" + teacherStudentSubjectListArray.getJSONObject(i).getString("attendance") + ")");
                }
        }
        else
            {
                if (teacherStudentSubjectListArray.getJSONObject(i).getString("reply_count").matches("") || teacherStudentSubjectListArray.getJSONObject(i).getString("reply_count").matches("null")) {
                    holder.attendance.setText("");
                } else {
                    holder.attendance.setText("Reply(" + teacherStudentSubjectListArray.getJSONObject(i).getString("reply_count") + ")");
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

        static class Holder
        {
            TextView studentName,studentRollNo,attendance;
            ImageView statusImage;
        }
}
