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

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 29-01-2018.
 */

public class HealthCardStudentListAdapter extends BaseAdapter {

    private Context context;
    private JSONArray studentListArray;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;

    public HealthCardStudentListAdapter(Context context, JSONArray studentListArray) {
        this.context = context;
        this.studentListArray = studentListArray;
    }

    @Override
    public int getCount() {
        return studentListArray.length();
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
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.health_card_student_listview_layout, null);
            holder.studentName = (TextView) convertView.findViewById(R.id.txt_studentNameTeacher);
            holder.studentRoll = (TextView) convertView.findViewById(R.id.txt_studentId);
            holder.studentImage = (ImageView) convertView.findViewById(R.id.imageView_studentImage);
            convertView.setTag(holder);


        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.studentName.setText(studentListArray.getJSONObject(position).getString("stu_name"));
            holder.studentRoll.setText(studentListArray.getJSONObject(position).getString("class_roll_no"));

            mDrawableBuilder = TextDrawable.builder().round();
            TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(studentListArray.getJSONObject(position).getString("stu_name").charAt(0)), R.color.blue);

            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+studentListArray.getJSONObject(position).getString("picture")).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.studentImage)
            {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.studentImage.setImageDrawable(circularBitmapDrawable);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        TextView studentName,studentRoll;
        ImageView studentImage;
    }
}
