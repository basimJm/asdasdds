package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Schoofi on 22-06-2016.
 */
public class NotificationIntentAdapter extends BaseAdapter {

    private Context context;
    private JSONArray notificationIntentAdapterArray;
    String date,subDate,subDate1;
    Date date5,date6;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public NotificationIntentAdapter(Context context, JSONArray notificationIntentAdapterArray) {
        this.context = context;
        this.notificationIntentAdapterArray = notificationIntentAdapterArray;
    }


    @Override
    public int getCount() {
        return notificationIntentAdapterArray.length();
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
        Holder holder;
        if(view == null)
        {   holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.notification_listview_class, null);
            holder.title = (TextView) view.findViewById(R.id.textView_student_announcement_title);
            holder.timeAgo = (TextView) view.findViewById(R.id.textView_student_announcement_person);
            holder.image = (ImageView) view.findViewById(R.id.imageViewBaseImage);
            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        try {
            holder.title.setText(notificationIntentAdapterArray.getJSONObject(i).getString("payload"));
           if(notificationIntentAdapterArray.getJSONObject(i).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_ATTENDANCE_TAG))
            {
                holder.image.setImageResource(R.drawable.attendancenotification);
            }

            else
                if(notificationIntentAdapterArray.getJSONObject(i).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_LEAVE_TAG))
                {
                    holder.image.setImageResource(R.drawable.leavenotification);
                }

                else
                    if(notificationIntentAdapterArray.getJSONObject(i).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_ASSIGNMENT_TAG))
                    {
                        holder.image.setImageResource(R.drawable.assignmentnotification);
                    }

                    else
                        if(notificationIntentAdapterArray.getJSONObject(i).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_ANNOUNCEMENT_TAG))
                        {
                            holder.image.setImageResource(R.drawable.announcementnotification);
                        }

                        else
                        if(notificationIntentAdapterArray.getJSONObject(i).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_FEEDBACK_TAG))
                        {
                            holder.image.setImageResource(R.drawable.feedbacknotification);
                        }

                        else
                        if(notificationIntentAdapterArray.getJSONObject(i).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_EXAM_SCHEDULE_TAG))
                        {
                            holder.image.setImageResource(R.drawable.examschedulenotification);
                        }

                        else
                        if(notificationIntentAdapterArray.getJSONObject(i).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_RESULT_TAG))
                        {
                            holder.image.setImageResource(R.drawable.resultnotification);
                        }

                        else
                        if(notificationIntentAdapterArray.getJSONObject(i).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_EVENT_TAG))
                        {
                            holder.image.setImageResource(R.drawable.eventnotification);
                        }

                        else
                        if(notificationIntentAdapterArray.getJSONObject(i).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_POLL_TAG))
                        {
                            holder.image.setImageResource(R.drawable.pollnotification);
                        }

                        else
                        if(notificationIntentAdapterArray.getJSONObject(i).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_BUS_TRACKING_TAG))
                        {
                            holder.image.setImageResource(R.drawable.bustrackingnotification);
                        }

                        else
                        if(notificationIntentAdapterArray.getJSONObject(i).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_FEES_TAG))
                        {
                            holder.image.setImageResource(R.drawable.feesnotificationicon);
                        }

                        else
                        if(notificationIntentAdapterArray.getJSONObject(i).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_FEES_TAG))
                        {
                            holder.image.setImageResource(R.drawable.feesnotificationicon);
                        }

                        else
                        if(notificationIntentAdapterArray.getJSONObject(i).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_BIRTHDAY_TAG))
                        {
                            holder.image.setImageResource(R.drawable.birthdaynotification);
                        }

                        else
                        if(notificationIntentAdapterArray.getJSONObject(i).getString("tag").matches(AppConstants.SERVER_URLS.NOTIFICATION_TEACHER_ANNIVERSERY_TAG))
                        {
                            holder.image.setImageResource(R.drawable.anniverserynotification);
                        }





            //Glide.with(context).load(AppConstants.SERVER_URLS.SERVER_URL+notificationIntentAdapterArray.getJSONObject(i).getString("image")).placeholder(R.drawable.feesnotificationicon).error(R.drawable.feesnotificationicon).crossFade().into(holder.image);
            date = notificationIntentAdapterArray.getJSONObject(i).getString("date_time");
            subDate = date.substring(0,9);




                try {
                   date5 = formatter.parse(subDate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");

                subDate1 = formatter1.format(date5);

            holder.timeAgo.setText(subDate1);
            holder.timeAgo.setVisibility(View.GONE);

            holder.image.setVisibility(View.GONE);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return view;
    }

    static class Holder
    {
        private TextView title,timeAgo;
        private ImageView image;
    }
}
