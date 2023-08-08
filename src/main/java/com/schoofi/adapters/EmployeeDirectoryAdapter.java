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

public class EmployeeDirectoryAdapter extends BaseAdapter {
    private Context context;
    private JSONArray teacherDirectoryArray;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;

    public EmployeeDirectoryAdapter(Context context, JSONArray teacherDirectoryArray) {
        this.context = context;
        this.teacherDirectoryArray = teacherDirectoryArray;
    }

    @Override
    public int getCount() {
        return teacherDirectoryArray.length();
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
            convertView = layoutInflater.inflate(R.layout.employee_directory_listview_layout, null);
            holder.teacherName = (TextView) convertView.findViewById(R.id.txt_visitor_name);
            holder.teacherImage = (ImageView) convertView.findViewById(R.id.imageView_teacher_image);
            holder.teacherNumber = (ImageView) convertView.findViewById(R.id.imageView_call_image);
            holder.departmentName = (TextView) convertView.findViewById(R.id.txt_visitor_name1);

            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            mDrawableBuilder = TextDrawable.builder().round();
            TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(teacherDirectoryArray.getJSONObject(position).getString("teac_name").charAt(0)), R.color.blue);
            String imagePath = teacherDirectoryArray.getJSONObject(position).getString("image");
			/*Picasso.with(context).load(imagePath).placeholder(R.drawable.profilebig).
			error(R.drawable.profilebig).transform(new CircleTransform()).into(holder.studentImage);*/
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+imagePath).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.teacherImage)
            {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.teacherImage.setImageDrawable(circularBitmapDrawable);
                }
            });
            holder.teacherName.setText(teacherDirectoryArray.getJSONObject(position).getString("teac_name"));

            if(teacherDirectoryArray.getJSONObject(position).getString("contact").matches(" ") || teacherDirectoryArray.getJSONObject(position).getString("contact").matches("") ||  teacherDirectoryArray.getJSONObject(position).getString("contact").matches("null"))
            {
                holder.teacherNumber.setClickable(false);
            }
            holder.teacherNumber.setTag(new Integer(position));

            holder.teacherNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:"+teacherDirectoryArray.getJSONObject((Integer) v.getTag()).getString("contact")));
                        context.startActivity(callIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            /*holder.teacherNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   *//* try {
                        image = studentEventListArray.getJSONObject((Integer) v.getTag()).getString("images");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*//*


                    try {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(teacherDirectoryArray.getJSONObject((Integer) v.getTag()).getString("contact")));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        context.startActivity(callIntent);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }




                    *//*aList = new ArrayList<String>(Arrays.asList(image.split(",")));
                    Intent intent = new Intent(context,StudentFeedBackImages.class);
                    intent.putExtra("array", aList);
                    intent.putExtra("value",value);
                    intent.putExtra("param","1");
                    intent.putExtra("position",(Integer) v.getTag());
                    context.startActivity(intent);*//*
                }
            });*/
            // holder.subject.setText(teacherTimeTableArray.getJSONObject(position).getString("subject"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        TextView teacherName,departmentName;
        ImageView teacherImage,teacherNumber;
    }

   /* private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.CALL_PHONE);
        int locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),STORAGE_PERMISSION_CODE);
            return false;
        }
        return true;
    }*/
}
