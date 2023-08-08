package com.schoofi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.schoofi.activitiess.R;
import com.schoofi.activitiess.StudentFeedBackImages;
import com.schoofi.activitiess.StudentSubEvents;
import com.schoofi.constants.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Schoofi on 16-05-2016.
 */
public class TeacherEventListAdapter extends BaseAdapter {
    private Context context;
    private JSONArray teacherEventListArray;
    String date,date1,month,year,createdDate1,createdDate2;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat ff = new SimpleDateFormat("dd-MMM-yyyy");
    Date enddate,createdDate3;
    String value;
    ArrayList aList= new ArrayList();
    String image;
    String crrDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    public TeacherEventListAdapter(Context context, JSONArray teacherEventListArray,String value) {
        this.context = context;
        this.teacherEventListArray = teacherEventListArray;
        this.value = value;
    }

    @Override
    public int getCount() {
        return teacherEventListArray.length();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder;

        if(convertView == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.eventlist_listview, null);

            holder.date1 = (TextView) convertView.findViewById(R.id.text_numberDate);
            holder.date2 = (TextView) convertView.findViewById(R.id.text_characterDate);
            holder.title = (TextView) convertView.findViewById(R.id.text_eventTitle);
            holder.startTime = (TextView) convertView.findViewById(R.id.text_eventStatrtingTime);
            holder.EndTime = (TextView) convertView.findViewById(R.id.text_eventEndingTime);
            holder.eventImageView = (ImageView) convertView.findViewById(R.id.event_image);
            holder.eventImageView1 = (ImageView) convertView.findViewById(R.id.event_image1);
            /*holder.createdDtae = (TextView) convertView.findViewById(R.id.text_eventCreatedDate);
            holder.audience = (TextView) convertView.findViewById(R.id.text_eventAudience);*/
            holder.imageViewGallery = (ImageView) convertView.findViewById(R.id.imageview_gallery);
            holder.imageViewEventTime = (ImageView) convertView.findViewById(R.id.imageview_event_time);
            holder.subEventsImageView = (ImageView)convertView.findViewById(R.id.imageview_sub_events);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            //Picasso.with(context).load(AppConstants.SERVER_URLS.SERVER_URL+studentEventListArray.getJSONObject(position).getString("event_image")).placeholder(R.drawable.profilebig).error(R.drawable.profilebig).into(holder.eventImageView);
            date = teacherEventListArray.getJSONObject(position).getString("program_start_date");


            /*if(teacherEventListArray.getJSONObject(position).getString("group_name").matches("0") || teacherEventListArray.getJSONObject(position).getString("group_name").matches("null") || teacherEventListArray.getJSONObject(position).getString("group_name").matches(""))
            {
                holder.audience.setText("All Teachers");
            }

            else
            {
                holder.audience.setText("Audience: "+teacherEventListArray.getJSONObject(position).getString("group_name"));
            }
*/
            createdDate1 = teacherEventListArray.getJSONObject(position).getString("created_date");
            try {
                createdDate3 = formatter.parse(createdDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            createdDate2 = ff.format(createdDate3);
            //holder.createdDtae.setText("Created Date: "+createdDate2);
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+teacherEventListArray.getJSONObject(position).getString("event_image")).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(holder.eventImageView);
            date1 = date.substring(8,10);
            month = date.substring(5,7);
            year = date.substring(0,4);
           /* holder.createdDtae.setVisibility(View.INVISIBLE);
            holder.audience.setVisibility(View.INVISIBLE);*/

            if(teacherEventListArray.getJSONObject(position).getString("images").matches("") || teacherEventListArray.getJSONObject(position).getString("images").matches("null"))
            {
                holder.imageViewGallery.setVisibility(View.INVISIBLE);
            }

            else
            {
                holder.imageViewGallery.setVisibility(View.VISIBLE);
            }
            //System.out.println(date1+month);

            switch(Integer.parseInt(month)) {
                case 1:

                    holder.date2.setText("Jan");
                    break;

                case 2:
                    holder.date2.setText("Feb");
                    break;

                case 3:
                    holder.date2.setText("Mar");
                    break;

                case 4:
                    holder.date2.setText("Apr");
                    break;

                case 5:
                    holder.date2.setText("May");
                    break;

                case 6:
                    holder.date2.setText("Jun");
                    break;

                case 7:
                    holder.date2.setText("Jul");
                    break;

                case 8:
                    holder.date2.setText("Aug");
                    break;

                case 9:
                    holder.date2.setText("Sep");
                    break;

                case 10:
                    holder.date2.setText("Oct");
                    break;

                case 11:
                    holder.date2.setText("Nov");
                    break;

                case 12:
                    holder.date2.setText("Dec");
                    break;

                default:
                    holder.date2.setText("Month");
                    break;

            }

            holder.imageViewGallery.setTag(new Integer(position));

            holder.imageViewGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        image = teacherEventListArray.getJSONObject((Integer) v.getTag()).getString("images");
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

            if(teacherEventListArray.getJSONObject(position).getString("sub_event_count").matches("null") || teacherEventListArray.getJSONObject(position).getString("sub_event_count").matches("") || teacherEventListArray.getJSONObject(position).getString("sub_event_count").matches("0"))
            {
                holder.subEventsImageView.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.subEventsImageView.setVisibility(View.VISIBLE);
            }

            holder.subEventsImageView.setTag(new Integer(position));

            holder.subEventsImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String eventId = "";
                    try {
                        eventId = teacherEventListArray.getJSONObject((Integer) v.getTag()).getString("event_id");

                        Intent intent = new Intent(context,StudentSubEvents.class);
                        intent.putExtra("event_id",eventId);
                        intent.putExtra("value4","5");

                        context.startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            holder.date1.setText(date1);

            holder.title.setText(teacherEventListArray.getJSONObject(position).getString("program_name"));
            holder.startTime.setText("On: "+date1+"-"+month+"-"+year+" at "+teacherEventListArray.getJSONObject(position).getString("program_start_time"));

            try {
                enddate = formatter.parse(teacherEventListArray.getJSONObject(position).getString("program_end_date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date Date89=null;

            try {
                Date89 = formatter.parse(crrDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            holder.imageViewEventTime.setVisibility(View.INVISIBLE);

            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            holder.EndTime.setText("Till: "+formatter1.format(enddate)+"at "+teacherEventListArray.getJSONObject(position).getString("program_end_time"));
            holder.eventImageView1.setImageResource(R.drawable.transparentt);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static  class Holder
    {
        TextView date1,date2,title,startTime,EndTime,createdDtae,audience;
        ImageView eventImageView,eventImageView1,imageViewGallery,imageViewEventTime,subEventsImageView;
    }
}
