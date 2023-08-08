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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.schoofi.activitiess.BusAdminStudentMarkList;
import com.schoofi.activitiess.R;
import com.schoofi.activitiess.TeacherStudentAttendanceDetails;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 29-03-2017.
 */

public class BusStudentMarkScreenAdapter extends BaseAdapter {

    private Context context;
    int count,f;
    private int selectedIndex;
    TeacherStudentAttendanceDetails teacherStudentAttendanceDetails;

    public JSONArray teacherStudentAttendanceArray1;
    private JSONArray filteredArray;
    public boolean c;
    String bus_no,busRoute,date;

    public BusStudentMarkScreenAdapter(Context context, JSONArray teacherStudentAttendanceArray1,String bus_no,String busRoute,String date) {
        this.context = context;
        this.teacherStudentAttendanceArray1 = teacherStudentAttendanceArray1;
        this.bus_no = bus_no;
        this.busRoute = busRoute;
        this.date = date;
    }

    @Override
    public int getCount() {
        return teacherStudentAttendanceArray1.length();
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
            convertView = layoutInflater.inflate(R.layout.listview_teacher_student_bus_attendance, null);
            holder = new Holder();
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linear_listView_teacherStudentAttendanceDetails);
            holder.studentAttendanceImage = (ImageView) convertView.findViewById(R.id.imageView_studentAttendance);
            holder.studentImage = (ImageView) convertView.findViewById(R.id.imageView_studentImage);
            holder.studentName = (TextView) convertView.findViewById(R.id.txt_studentNameTeacher);
            holder.studentId = (TextView) convertView.findViewById(R.id.txt_studentId);

            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.studentName.setText("Name: "+teacherStudentAttendanceArray1.getJSONObject(position).getString("stu_name"));
            //holder.studentId.setText(filteredArray.getJSONObject(position).getString("stu_id"));
            holder.studentId.setText("Class: "+teacherStudentAttendanceArray1.getJSONObject(position).getString("class_name")+"-"+teacherStudentAttendanceArray1.getJSONObject(position).getString("section_name"));
			/*Picasso.with(context).load(AppConstants.SERVER_URLS.SERVER_URL+filteredArray.getJSONObject(position).getString("picture")).placeholder(R.drawable.person).
			error(R.drawable.person).transform(new CircleTransform()).into(holder.studentImage);*/

            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+teacherStudentAttendanceArray1.getJSONObject(position).getString("picture")).asBitmap().placeholder(R.drawable.person).error(R.drawable.person).into(new BitmapImageViewTarget(holder.studentImage)
            {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.studentImage.setImageDrawable(circularBitmapDrawable);
                }
            });

            if(teacherStudentAttendanceArray1.getJSONObject(position).getString("isAdded").matches("A"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.greencircletick);

            }

            else
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.crossredcircle);

            }

            holder.studentAttendanceImage.setTag(new Integer(position));
            holder.studentAttendanceImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if(teacherStudentAttendanceArray1.getJSONObject((Integer) v.getTag()).getString("isAdded").matches("A"))
                        {
                            holder.studentAttendanceImage.setImageResource(R.drawable.crossredcircle);
                            teacherStudentAttendanceArray1.getJSONObject((Integer) v.getTag()).put("isAdded","N");
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentAttendanceArray1.toString().getBytes();
                            VolleySingleton.getInstance(context).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_ROUTE_STUDENT_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&bus_no="+bus_no+"&route_number="+busRoute+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&day="+date,e);

                        }

                        else
                        {
                            holder.studentAttendanceImage.setImageResource(R.drawable.greencircletick);
                            teacherStudentAttendanceArray1.getJSONObject((Integer) v.getTag()).put("isAdded","A");
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentAttendanceArray1.toString().getBytes();
                            VolleySingleton.getInstance(context).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_ROUTE_STUDENT_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&bus_no="+bus_no+"&route_number="+busRoute+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&day="+date,e);

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
        LinearLayout linearLayout;
    }
}
