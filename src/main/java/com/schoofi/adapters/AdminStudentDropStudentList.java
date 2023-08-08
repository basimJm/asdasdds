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
 * Created by Schoofi on 20-06-2018.
 */

public class AdminStudentDropStudentList extends BaseAdapter {

    private Context context;
    private JSONArray parentHomeScreenArray;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;

    public AdminStudentDropStudentList(Context context,JSONArray parentHomeScreenArray) {
        super();
        this.context = context;
        this.parentHomeScreenArray = parentHomeScreenArray;
    }

    @Override
    public int getCount() {
        return parentHomeScreenArray.length();
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
            convertView = layoutInflater.inflate(R.layout.parent_home_screen_listview, null);
            holder.studentImage = (ImageView) convertView.findViewById(R.id.imageView_childImage);
            holder.childName = (TextView) convertView.findViewById(R.id.text_child_name);
            holder.childClass = (TextView) convertView.findViewById(R.id.text_child_class);
            holder.childSchool = (TextView) convertView.findViewById(R.id.text_school_Name);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            mDrawableBuilder = TextDrawable.builder().round();
            TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(parentHomeScreenArray.getJSONObject(position).getString("stu_name").charAt(0)), R.color.blue);
            String imagePath = parentHomeScreenArray.getJSONObject(position).getString("picture");
			/*Picasso.with(context).load(imagePath).placeholder(R.drawable.profilebig).
			error(R.drawable.profilebig).transform(new CircleTransform()).into(holder.studentImage);*/
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+imagePath).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.studentImage)
            {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.studentImage.setImageDrawable(circularBitmapDrawable);
                }

            });
            holder.childClass.setText("Father's Name: "+parentHomeScreenArray.getJSONObject(position).getString("father_name"));
            holder.childName.setText(parentHomeScreenArray.getJSONObject(position).getString("stu_name")+"("+parentHomeScreenArray.getJSONObject(position).getString("class_name")+"-"+parentHomeScreenArray.getJSONObject(position).getString("section_name")+")");
            holder.childSchool.setText("Mother's Name: "+parentHomeScreenArray.getJSONObject(position).getString("mother_name"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        ImageView studentImage;
        TextView childName,childSchool,childClass;
    }
}
