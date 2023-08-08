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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Schoofi on 11-03-2018.
 */

public class AdminVisitorLogPrimaryDetailsAdapter extends BaseAdapter {

    private Context context;
    private JSONArray adminVisitorLogPrimaryDetailArray;

    public AdminVisitorLogPrimaryDetailsAdapter(Context context, JSONArray adminVisitorLogPrimaryDetailArray) {
        this.context = context;
        this.adminVisitorLogPrimaryDetailArray = adminVisitorLogPrimaryDetailArray;
    }



    @Override
    public int getCount() {
        return adminVisitorLogPrimaryDetailArray.length();
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
            convertView = layoutInflater.inflate(R.layout.admin_visitor_log_primary_details_listview, null);
            holder.visitorImage = (ImageView) convertView.findViewById(R.id.imageView_visitor_image);
            holder.visitorName = (TextView) convertView.findViewById(R.id.txt_visitor_name);
            holder.visitorType = (TextView) convertView.findViewById(R.id.txt_vistor_type);
            holder.visitorPersonToMeet = (TextView) convertView.findViewById(R.id.txt_person_to_meet);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+adminVisitorLogPrimaryDetailArray.getJSONObject(position).getString("visitor_picture")).asBitmap().placeholder(R.drawable.profilebig).error(R.drawable.profilebig).into(new BitmapImageViewTarget(holder.visitorImage)
            {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.visitorImage.setImageDrawable(circularBitmapDrawable);
                }
            });

            holder.visitorName.setText(adminVisitorLogPrimaryDetailArray.getJSONObject(position).getString("visitor_name"));
            holder.visitorType.setText(adminVisitorLogPrimaryDetailArray.getJSONObject(position).getString("visitor_type_name"));
            holder.visitorPersonToMeet.setText(adminVisitorLogPrimaryDetailArray.getJSONObject(position).getString("visit_person"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        ImageView visitorImage;
        TextView visitorName,visitorType,visitorPersonToMeet;
    }
}
