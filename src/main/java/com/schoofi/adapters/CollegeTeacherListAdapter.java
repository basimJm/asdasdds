package com.schoofi.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.schoofi.activitiess.R;
import com.schoofi.activitiess.TeacherClassListCollegeAttendance;
import com.schoofi.constants.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 23-08-2019.
 */

public class CollegeTeacherListAdapter extends BaseAdapter implements Filterable {
    private Context context;
    int count,f;
    private int selectedIndex;

    public JSONArray teacherStudentAttendanceArray1;
    private TeacherStudentAttendanceFilter teacherStudentAttendanceFilter;
    private JSONArray filteredArray;

    public CollegeTeacherListAdapter(Context context, JSONArray teacherStudentAttendanceArray1, int f) {
        super();
        this.context = context;
        selectedIndex = -1;
        this.teacherStudentAttendanceArray1 = teacherStudentAttendanceArray1;
        this.filteredArray = teacherStudentAttendanceArray1;
        this.f = f;
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

        if(convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.college_teacher_listview, null);
            holder = new Holder();

            holder.studentAttendanceImage = (ImageView) convertView.findViewById(R.id.imageView);
            holder.studentImage = (ImageView) convertView.findViewById(R.id.imageView_teacher_image);
            holder.studentName = (TextView) convertView.findViewById(R.id.txt_employee_name);
            holder.inTime = (TextView) convertView.findViewById(R.id.txt_in_time);
            holder.outTime = (TextView) convertView.findViewById(R.id.txt_out_time);
            holder.viewClasses = (TextView) convertView.findViewById(R.id.txt_view_classes);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder )convertView.getTag();
        }

        try {
            holder.studentName.setText("Name: "+filteredArray.getJSONObject(position).getString("emp_name"));
            holder.inTime.setText("In-Time: "+filteredArray.getJSONObject(position).getString("in_time"));
            holder.outTime.setText("Out-Time: "+filteredArray.getJSONObject(position).getString("out_time"));

            holder.viewClasses.setTag(new Integer(position));


            holder.viewClasses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    try {
                        String userId;
                        userId = filteredArray.getJSONObject((Integer) v.getTag()).getString("user_id");
                        Intent intent = new Intent(context, TeacherClassListCollegeAttendance.class);
                        intent.putExtra("userId",userId);
                        intent.putExtra("value","2");
                        intent.putExtra("attendance_date",filteredArray.getJSONObject((Integer) v.getTag()).getString("attendance_date"));

                        context.startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // holder.imageViewGallery.setBackgroundResource(R.color.action_bar);


                    /*aList = new ArrayList<String>(Arrays.asList(image.split(",")));
                    Intent intent = new Intent(context,StudentFeedBackImages.class);
                    intent.putExtra("array", aList);
                    intent.putExtra("value",value);
                    intent.putExtra("param","2");
                    intent.putExtra("position",position);
                    context.startActivity(intent);*/
                }
            });
			/*Picasso.with(context).load(AppConstants.SERVER_URLS.SERVER_URL+filteredArray.getJSONObject(position).getString("picture")).placeholder(R.drawable.person).
			error(R.drawable.person).transform(new CircleTransform()).into(holder.studentImage);*/

            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+filteredArray.getJSONObject(position).getString("picture")).asBitmap().placeholder(R.drawable.person).error(R.drawable.person).into(new BitmapImageViewTarget(holder.studentImage)
            {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.studentImage.setImageDrawable(circularBitmapDrawable);
                }
            });

            if (filteredArray.getJSONObject(position).getString("attendance").equals("P")) {
                holder.studentAttendanceImage.setImageResource(R.drawable.green_tick);
                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.green));
            } else if (filteredArray.getJSONObject(position).getString("attendance").equals("A")) {
                holder.studentAttendanceImage.setImageResource(R.drawable.red);
                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.red));
            }

            else if (filteredArray.getJSONObject(position).getString("attendance").equals("CL"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.cl);

                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (filteredArray.getJSONObject(position).getString("attendance").equals("EL"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.el);

                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (filteredArray.getJSONObject(position).getString("attendance").equals("LWP"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.lwp);

                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (filteredArray.getJSONObject(position).getString("attendance").equals("H"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.hd);

                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (filteredArray.getJSONObject(position).getString("attendance").equals("DL"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.dl);

                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (filteredArray.getJSONObject(position).getString("attendance").equals("SP"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.sp);

                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (filteredArray.getJSONObject(position).getString("attendance").equals("CPL"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.cpl);

                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }
            else if (filteredArray.getJSONObject(position).getString("attendance").equals("OT"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.ot);

                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (filteredArray.getJSONObject(position).getString("attendance").equals("OD"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.od);

                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (filteredArray.getJSONObject(position).getString("attendance").equals("RH"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.rh);

                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (filteredArray.getJSONObject(position).getString("attendance").equals("VL"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.vl);

                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (filteredArray.getJSONObject(position).getString("attendance").equals("SL"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.sl);

                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (filteredArray.getJSONObject(position).getString("attendance").equals("WO"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.wo);

                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }

            else if (filteredArray.getJSONObject(position).getString("attendance").equals("HA"))
            {
                holder.studentAttendanceImage.setImageResource(R.drawable.ha);

                holder.studentAttendanceImage.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //holder.studentId.setText(filteredArray.getJSONObject(position).getString("stu_id"));


        return convertView;
    }

    static class Holder
    {
        TextView studentName,inTime,outTime,viewClasses;
        ImageView studentImage,studentAttendanceImage;


    }

    @Override
    public Filter getFilter()
    {

        if(teacherStudentAttendanceFilter==null)
            teacherStudentAttendanceFilter=new TeacherStudentAttendanceFilter();
        return teacherStudentAttendanceFilter;
    }

    public void setSelectedIndex(int position)
    {
        selectedIndex = position;
        notifyDataSetChanged();
    }


    private class TeacherStudentAttendanceFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults filterResults=new FilterResults();
            if(constraint!=null && constraint.length()>0){

                JSONArray array=new JSONArray();
                for(int i=0;i<teacherStudentAttendanceArray1.length();i++){
                    try
                    {
                        if(teacherStudentAttendanceArray1.getJSONObject(i).getString("emp_name").toLowerCase().contains(constraint.toString().toLowerCase()))
                            array.put(teacherStudentAttendanceArray1.getJSONObject(i));
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
                filterResults.count=teacherStudentAttendanceArray1.length();
                filterResults.values=teacherStudentAttendanceArray1;
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
}
