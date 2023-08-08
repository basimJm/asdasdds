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
 * Created by Schoofi on 18-02-2018.
 */

public class HealthEventListAdapter extends BaseAdapter {

    private Context context;
    private JSONArray healthEventListArray;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;

    public HealthEventListAdapter(Context context, JSONArray healthEventListArray) {
        this.context = context;
        this.healthEventListArray = healthEventListArray;
    }

    @Override
    public int getCount() {
        return healthEventListArray.length();
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
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.health_event_list_layout, null);

            holder.eventName = (TextView) convertView.findViewById(R.id.text_event_name);
            holder.eventDate = (TextView) convertView.findViewById(R.id.text_event_date);
            holder.eventDoctor = (TextView) convertView.findViewById(R.id.text_event_doctor);
            holder.eventPlace = (TextView) convertView.findViewById(R.id.text_event_place);
            holder.eventOrganiser = (TextView) convertView.findViewById(R.id.text_event_organisation);
            holder.eventImage = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);

        }

        else {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.eventName.setText(healthEventListArray.getJSONObject(position).getString("program_name"));
            holder.eventDate.setText(healthEventListArray.getJSONObject(position).getString("program_start_date")+"-"+healthEventListArray.getJSONObject(position).getString("program_end_date"));
            holder.eventDoctor.setText(healthEventListArray.getJSONObject(position).getString("doctor_name"));
            holder.eventPlace.setText(healthEventListArray.getJSONObject(position).getString("place"));
            holder.eventOrganiser.setText(healthEventListArray.getJSONObject(position).getString("organiser_name"));

            mDrawableBuilder = TextDrawable.builder().round();
            TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(healthEventListArray.getJSONObject(position).getString("program_name").charAt(0)), R.color.blue);
            String imagePath = healthEventListArray.getJSONObject(position).getString("image");
			/*Picasso.with(context).load(imagePath).placeholder(R.drawable.profilebig).
			error(R.drawable.profilebig).transform(new CircleTransform()).into(holder.studentImage);*/
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+imagePath).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.eventImage)
            {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.eventImage.setImageDrawable(circularBitmapDrawable);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static  class Holder
    {
        private TextView eventName,eventDate,eventDoctor,eventPlace,eventOrganiser;
        private ImageView eventImage;
    }
}
