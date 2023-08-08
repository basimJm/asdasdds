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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.schoofi.activities.StudentAttendance;
import com.schoofi.activities.StudentHomeScreen;
import com.schoofi.activitiess.R;
import com.schoofi.activitiess.StudentAttendancePrimaryOptionScreen;
import com.schoofi.activitiess.StudentCollegeAttendancePrimaryScreen;
import com.schoofi.activitiess.TeacherClassListCollegeAttendance;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 03-08-2016.
 */
public class AdminStudentListAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private JSONArray adminStudentListArray;
    private TeacherStudentAttendanceFilter teacherStudentAttendanceFilter;
    private JSONArray filteredArray;

    public AdminStudentListAdapter(Context context, JSONArray adminStudentListArray) {
        this.context = context;
        this.adminStudentListArray = adminStudentListArray;
        this.filteredArray = adminStudentListArray;
    }


    @Override
    public int getCount() {
        return filteredArray.length();
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
            view = layoutInflater.inflate(R.layout.admin_student_list_listview, null);
            holder.profileImage = (ImageView) view.findViewById(R.id.imageView_studentImage);
            holder.name = (TextView) view.findViewById(R.id.txt_studentNameTeacher);
            holder.rollNumber = (TextView) view.findViewById(R.id.txt_studentId);
            holder.viewAttendance = (TextView) view.findViewById(R.id.text_view_attendance);
            view.setTag(holder);
        }

        else
        {
            holder = (Holder) view.getTag();
        }

        try {
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+filteredArray.getJSONObject(i).getString("picture")).asBitmap().placeholder(R.drawable.profilebig).error(R.drawable.profilebig).into(new BitmapImageViewTarget(holder.profileImage)
            {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.profileImage.setImageDrawable(circularBitmapDrawable);
                }
            });

            holder.name.setText("Name: "+filteredArray.getJSONObject(i).getString("stu_name"));
            holder.rollNumber.setText("Roll No.: "+filteredArray.getJSONObject(i).getString("class_roll_no"));

            holder.viewAttendance.setTag(new Integer(i));
            holder.viewAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Preferences.getInstance().loadPreference(context.getApplicationContext());
                        Preferences.getInstance().studentId = filteredArray.getJSONObject((Integer) v.getTag()).getString("stu_id");
                        Preferences.getInstance().studentClassId = filteredArray.getJSONObject((Integer) v.getTag()).getString("class_id");
                        Preferences.getInstance().studentSectionId = filteredArray.getJSONObject((Integer) v.getTag()).getString("section_id");
                        Preferences.getInstance().studentCourse = filteredArray.getJSONObject((Integer) v.getTag()).getString("course_id");
                        Preferences.getInstance().savePreference(context.getApplicationContext());



                            //intent.putExtra("attendance_date",filteredArray.getJSONObject((Integer) v.getTag()).getString("attendance_date"));





                                Intent intent = new Intent(context, StudentAttendancePrimaryOptionScreen.class);
                                context.startActivity(intent);





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public Filter getFilter()
    {

        if(teacherStudentAttendanceFilter==null)
            teacherStudentAttendanceFilter=new TeacherStudentAttendanceFilter();
        return teacherStudentAttendanceFilter;
    }




    private class TeacherStudentAttendanceFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults filterResults=new FilterResults();
            if(constraint!=null && constraint.length()>0){

                JSONArray array=new JSONArray();
                for(int i=0;i<adminStudentListArray.length();i++){
                    try
                    {
                        if(adminStudentListArray.getJSONObject(i).getString("stu_name").toLowerCase().contains(constraint.toString().toLowerCase()))
                            array.put(adminStudentListArray.getJSONObject(i));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                filterResults.count=array.length();
                filterResults.values=array;
            }
            else
            {
                filterResults.count=adminStudentListArray.length();
                filterResults.values=adminStudentListArray;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint,FilterResults results)
        {
            filteredArray = (JSONArray) results.values;
            notifyDataSetChanged();
        }
    }

    static class Holder
    {
        ImageView profileImage;
        TextView name,rollNumber,viewAttendance;
    }
}
