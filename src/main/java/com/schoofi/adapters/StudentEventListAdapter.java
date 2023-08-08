package com.schoofi.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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
import com.schoofi.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;



/**
 * Created by harsh malhotra on 3/21/2016.
 */
public class StudentEventListAdapter extends BaseAdapter {

    private Context context;
    private JSONArray studentEventListArray;
    String date,date1,month,year,createdDate1,createdDate2;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    String crrDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    SimpleDateFormat format = new SimpleDateFormat("EEEE");
    String Rs;
    Date hour,minutes;

    Date enddate;
    Date startDate,createdDate3;
    String startTime,endTime;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:SS");
    SimpleDateFormat df = new SimpleDateFormat("HH:MM a");
    SimpleDateFormat ff = new SimpleDateFormat("dd-MMM-yyyy");
    ArrayList aList= new ArrayList();
    String image;
    String value;

    public StudentEventListAdapter(Context context, JSONArray studentEventListArray,String value) {
        this.context = context;
        this.studentEventListArray = studentEventListArray;
        this.value = value;
    }

    @Override
    public int getCount() {
        return studentEventListArray.length();
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
            date = studentEventListArray.getJSONObject(position).getString("program_start_date");
           /* if(studentEventListArray.getJSONObject(position).getString("group_name").matches("0") || studentEventListArray.getJSONObject(position).getString("group_name").matches("null") || studentEventListArray.getJSONObject(position).getString("group_name").matches(""))
            {
                holder.audience.setText("All Teachers");
            }

            else
            {
                holder.audience.setText("Audience: "+studentEventListArray.getJSONObject(position).getString("group_name"));
            }

            holder.createdDtae.setVisibility(View.INVISIBLE);
            holder.audience.setVisibility(View.INVISIBLE);*/

            createdDate1 = studentEventListArray.getJSONObject(position).getString("created_date");
            try {
                createdDate3 = formatter.parse(createdDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //image = studentEventListArray.getJSONObject(position).getString("images");

            if(studentEventListArray.getJSONObject(position).getString("images").matches("") || studentEventListArray.getJSONObject(position).getString("images").matches("null"))
            {
                holder.imageViewGallery.setVisibility(View.INVISIBLE);
            }

            else
            {
                holder.imageViewGallery.setVisibility(View.VISIBLE);
            }

            if(studentEventListArray.getJSONObject(position).getString("sub_event_count").matches("null") || studentEventListArray.getJSONObject(position).getString("sub_event_count").matches("") || studentEventListArray.getJSONObject(position).getString("sub_event_count").matches("0"))
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
                        eventId = studentEventListArray.getJSONObject((Integer) v.getTag()).getString("event_id");

                        Intent intent = new Intent(context,StudentSubEvents.class);
                        intent.putExtra("event_id",eventId);
                        intent.putExtra("value4","5");
                        intent.putExtra("value",value);

                        context.startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });


            //Utils.showToast(context,image);
            holder.imageViewGallery.setTag(new Integer(position));

            holder.imageViewGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        image = studentEventListArray.getJSONObject((Integer) v.getTag()).getString("images");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //holder.imageViewGallery.setBackgroundResource(R.color.action_bar);


                    aList = new ArrayList<String>(Arrays.asList(image.split(",")));
                    Intent intent = new Intent(context,StudentFeedBackImages.class);
                    intent.putExtra("array", aList);
                    intent.putExtra("value",value);
                    intent.putExtra("param","1");
                    intent.putExtra("position",(Integer) v.getTag());
                    context.startActivity(intent);
                }
            });



            createdDate2 = ff.format(createdDate3);
           // holder.createdDtae.setText("Created Date: "+createdDate2);
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(75);

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            holder.eventImageView.setColorFilter(filter);
            //Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+studentEventListArray.getJSONObject(position).getString("event_image")).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(holder.eventImageView);
            date1 = date.substring(8,10);
            month = date.substring(5,7);
            year = date.substring(0,4);
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


            try {
                startDate = formatter.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            holder.date1.setText(date1);

            holder.title.setText(studentEventListArray.getJSONObject(position).getString("program_name"));
            startTime = studentEventListArray.getJSONObject(position).getString("program_start_time");
            endTime = studentEventListArray.getJSONObject(position).getString("program_fees");
            try {
                hour = sdf.parse(startTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Rs = context.getString(R.string.Rs);
            holder.startTime.setText(format.format(startDate)+" At "+df.format(hour));

            //hour = startTime.substring(0,2);
           // Utils.showToast(context,hour);
            //holder.startTime.setText("On: "+date1+"-"+month+"-"+year+" at "+studentEventListArray.getJSONObject(position).getString("program_start_time"));

            try {
                enddate = formatter.parse(studentEventListArray.getJSONObject(position).getString("program_end_date"));

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
            if(endTime.matches("") || endTime.matches("null") || endTime.matches("Free") || endTime.matches("free"))
            {
                holder.EndTime.setText("FREE");
            }

            else
            {
                holder.EndTime.setText("Program Fee: "+Rs+endTime);
            }
            //holder.EndTime.setText("Till: "+formatter1.format(enddate)+"at "+studentEventListArray.getJSONObject(position).getString("program_end_time"));
            holder.eventImageView1.setImageResource(R.drawable.transparentt);
            holder.eventImageView.setVisibility(View.INVISIBLE);


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
