package com.schoofi.adapters;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.schoofi.activitiess.DiaryMultipleStudentSelectionScreen;
import com.schoofi.activitiess.R;
import com.schoofi.activitiess.TeacherStudentAttendanceDetails;
import com.schoofi.constants.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 08-02-2017.
 */

public class DiaryMultipleStudentSelectionScreenListViewAdapter extends BaseAdapter implements Filterable {

    private Context context;
    int count,f;
    private int selectedIndex;


    public JSONArray teacherStudentAttendanceArray1;
    private JSONArray filteredArray;
    TeacherStudentAttendanceFilter teacherStudentAttendanceFilter;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    public boolean c;

    public DiaryMultipleStudentSelectionScreenListViewAdapter(Context context, JSONArray teacherStudentAttendanceArray1) {
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
            convertView = layoutInflater.inflate(R.layout.listview_teacher_student_bus_attendance, null);
            holder = new Holder();

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
                holder.studentName.setText("Name: "+filteredArray.getJSONObject(position).getString("stu_name"));
                //holder.studentId.setText(filteredArray.getJSONObject(position).getString("stu_id"));
                holder.studentId.setText("Roll No.: "+filteredArray.getJSONObject(position).getString("class_roll_no"));
			/*Picasso.with(context).load(AppConstants.SERVER_URLS.SERVER_URL+filteredArray.getJSONObject(position).getString("picture")).placeholder(R.drawable.person).
			error(R.drawable.person).transform(new CircleTransform()).into(holder.studentImage);*/
                mDrawableBuilder = TextDrawable.builder().round();
                holder.studentAttendanceImage.setTag(new Integer(position));
                TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(filteredArray.getJSONObject(position).getString("stu_name").charAt(0)), R.color.blue);
                Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+filteredArray.getJSONObject(position).getString("picture")).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.studentImage)
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




    @Override
    public Filter getFilter() {
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
                for(int i=0;i<teacherStudentAttendanceArray1.length();i++){
                    try
                    {
                        if(teacherStudentAttendanceArray1.getJSONObject(i).getString("stu_name").toLowerCase().contains(constraint.toString().toLowerCase()))
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

    static class Holder
    {
        TextView studentName,studentId;
        ImageView studentImage,studentAttendanceImage;
    }
}
