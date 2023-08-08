package com.schoofi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mzelzoghbi.zgallery.ZGallery;
import com.mzelzoghbi.zgallery.entities.ZColor;
import com.schoofi.activitiess.DiaryImageSharingScreen;
import com.schoofi.activitiess.DiaryImagesDetailsScreen;
import com.schoofi.activitiess.DiaryImagesScreen;
import com.schoofi.activitiess.DiaryReplyScreen;
import com.schoofi.activitiess.DiarySharingScreen;
import com.schoofi.activitiess.R;
import com.schoofi.activitiess.SchoolDiaryDetailScreen;
import com.schoofi.activitiess.TeacherStudentViewAssignment;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.DiarySubVO;
import com.schoofi.utils.DiaryVO;
import com.schoofi.utils.ParentStudentFeesUnpaidMultilevelChildVO;
import com.schoofi.utils.ParentStudentFessUnpaidMultilevelParentVO;
import com.schoofi.utils.Utils;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Schoofi on 19-01-2017.
 */

public class DiaryMultiLevelListAdapter extends BaseExpandableListAdapter {

    private Context context;
    ArrayList<String> myList;
    ArrayList<String> myList1;
    String array;
    int position;
    private ArrayList<DiaryVO> diaryVOs;
    private JSONArray diaryJSONArray;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    String date1,date2;
    Date date3;
    String attachment;
    ArrayList aList= new ArrayList();
    ArrayList aList1 = new ArrayList();
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;

    public DiaryMultiLevelListAdapter(Context context, ArrayList<DiaryVO> diaryVOs, JSONArray diaryJSONArray) {
        this.context = context;
        this.diaryVOs = diaryVOs;
        this.diaryJSONArray = diaryJSONArray;
    }


    @Override
    public int getGroupCount() {
        return diaryVOs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<DiarySubVO> diarySubVOs = diaryVOs.get(groupPosition).getItems();
        return diarySubVOs.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return diaryVOs.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<DiarySubVO>  diarySubVOs = diaryVOs.get(groupPosition).getItems();

        return diarySubVOs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ExpandableListView eLV = (ExpandableListView) parent;
        eLV.expandGroup(groupPosition);
        final Holder holder;
        DiaryVO diaryVO = (DiaryVO) getGroup(groupPosition);
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.diary_homescreen_parent_layout, parent, false);
            holder.dates = (TextView) convertView.findViewById(R.id.text_date_title);
            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }


            date1 = diaryVOs.get(groupPosition).getDate();
            try {
                date3 = formatter.parse(date1);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            date2 = formatter1.format(date3);
            holder.dates.setText(date2);




        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Holder holder1;
        final DiarySubVO diarySubVO = (DiarySubVO) getChild(groupPosition,childPosition);
        if (convertView == null) {
            holder1 = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.daily_homescreen_child_layout, parent, false);

            holder1.title = (TextView) convertView.findViewById(R.id.title_textView);
            holder1.description = (TextView) convertView.findViewById(R.id.text_description);
            holder1.subject = (TextView) convertView.findViewById(R.id.text_subject_name);
            holder1.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            holder1.attachment = (ImageView) convertView.findViewById(R.id.imageView_gallery);
            holder1.galleryCount = (TextView) convertView.findViewById(R.id.text_gallery_count);
            holder1.emoticon = (TextView) convertView.findViewById(R.id.text_emoticon);
            //holder1.time = (TextView) convertView.findViewById(R.id.text_time);
            holder1.share = (ImageView) convertView.findViewById(R.id.text_view_share);
            holder1.reply = (ImageView) convertView.findViewById(R.id.text_view_reply);
            holder1.initials = (ImageView) convertView.findViewById(R.id.imageview_initials);
            holder1.icon = (ImageView) convertView.findViewById(R.id.imageview_icon);
            holder1.linearLayout = (LinearLayout) convertView.findViewById(R.id.linear_child_layout);
            holder1.ratingParameter = (TextView) convertView.findViewById(R.id.text_rating_parameterId);
            /*holder1.imageView1 = (ImageView) convertView.findViewById(R.id.imageView_1);
            holder1.imageView2 = (ImageView) convertView.findViewById(R.id.imageView_2);
            holder1.imageView3 = (ImageView) convertView.findViewById(R.id.imageView_3);
            holder1.countImages = (TextView) convertView.findViewById(R.id.text_numberDate);
            holder1.frameLayout = (FrameLayout) convertView.findViewById(R.id.frame_layout);*/
            holder1.type = (TextView) convertView.findViewById(R.id.text_view_type);


            convertView.setTag(holder1);
        }

        else
        {
            holder1 = (Holder) convertView.getTag();
        }

        holder1.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),DiaryReplyScreen.class);
                intent.putExtra("diarySubId",diaryVOs.get(groupPosition).getItems().get(childPosition).getDiarySubId());
                intent.putExtra("diaryId",diaryVOs.get(groupPosition).getItems().get(childPosition).getDiaryModuleId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        if(diaryVOs.get(groupPosition).getItems().get(childPosition).getType().matches("Note") ||diaryVOs.get(groupPosition).getItems().get(childPosition).getType().matches("Participation"))
        {
            holder1.ratingParameter.setVisibility(View.INVISIBLE);
        }

        else
        {
            holder1.ratingParameter.setVisibility(View.VISIBLE);
        }


        if(diarySubVO.getRoleId().matches("6"))
        {
            holder1.linearLayout.setBackgroundResource(R.color.graay);
        }
        else
        {
            holder1.linearLayout.setBackgroundResource(R.color.white);
        }

        holder1.title.setText(diarySubVO.getTitle().toString());
        holder1.description.setText(diarySubVO.getDescription().toString());

        holder1.ratingBar.setTag(new Integer(childPosition));
        if(diaryVOs.get(groupPosition).getItems().get(childPosition).getType().matches("Note") || diaryVOs.get(groupPosition).getItems().get(childPosition).getType().matches("Participation") || diaryVOs.get(groupPosition).getItems().get(childPosition).getRating().matches("") ||  diaryVOs.get(groupPosition).getItems().get(childPosition).getRating().matches("null"))
        {
            holder1.ratingBar.setVisibility(View.INVISIBLE);

        }

        else
        {
            holder1.ratingBar.setVisibility(View.VISIBLE);
            holder1.ratingBar.setRating(Float.parseFloat(diaryVOs.get(groupPosition).getItems().get(childPosition).getRating()));
        }
       /* holder1.ratingBar.setClickable(false);
        holder1.ratingBar.setFocusable(false);
        holder1.ratingBar.setEnabled(false);*/

        if(diarySubVO.getType().matches("") || diarySubVO.getType().matches("null"))
        {
            holder1.type.setVisibility(View.GONE);
        }

        else
        {
            holder1.type.setText(diarySubVO.getType());
            holder1.type.setVisibility(View.GONE);
        }


        if(diarySubVO.getType().matches("Note"))
        {
            holder1.icon.setImageResource(R.drawable.noteicon);
        }

        else
        if(diarySubVO.getType().matches("Rating"))
        {
            holder1.icon.setImageResource(R.drawable.rating);
        }

        else
            if(diarySubVO.getType().matches("Participation"))
            {
                holder1.icon.setImageResource(R.drawable.participation);
            }

        holder1.attachment.setTag(new Integer(childPosition));
        holder1.share.setTag(new Integer(childPosition));
        holder1.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(diaryVOs.get(groupPosition).getItems().get(childPosition).getFileUrl().matches("") || diaryVOs.get(groupPosition).getItems().get(childPosition).getFileUrl().matches("null"))
                {
                    Intent intent = new Intent(context.getApplicationContext(), DiarySharingScreen.class);
                    intent.putExtra("title",diarySubVO.getTitle());
                    intent.putExtra("description",diarySubVO.getDescription());
                    if(diaryVOs.get(groupPosition).getItems().get(childPosition).getRating().matches("")|| diaryVOs.get(groupPosition).getItems().get(childPosition).getRating().matches(""))
                    {
                        intent.putExtra("rating","no");
                    }
                    else
                    {
                        intent.putExtra("rating",diarySubVO.getRating());
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

                else
                {
                    Intent intent = new Intent(context.getApplicationContext(), DiaryImageSharingScreen.class);
                    intent.putExtra("title",diarySubVO.getTitle());
                    intent.putExtra("description",diarySubVO.getDescription());
                    intent.putExtra("images",diarySubVO.getFileUrl());
                    if(diaryVOs.get(groupPosition).getItems().get(childPosition).getRating().matches("") || diaryVOs.get(groupPosition).getItems().get(childPosition).getRating().matches(""))
                    {
                        intent.putExtra("rating","no");
                    }
                    else
                    {
                        intent.putExtra("rating",diarySubVO.getRating());
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }



            }
        });
        attachment = diaryVOs.get(groupPosition).getItems().get(childPosition).getFileUrl();
        if(attachment.matches("") || attachment.matches("null"))
        {
            holder1.attachment.setVisibility(View.GONE);
            holder1.galleryCount.setVisibility(View.GONE);

        }

        else
        {
            holder1.attachment.setVisibility(View.VISIBLE);
            holder1.galleryCount.setVisibility(View.VISIBLE);
            holder1.galleryCount.setText(String.valueOf(aList.size()));
        }

        DateFormat format1 = new SimpleDateFormat("hh:mm:ss");
        try {
            Date date = format1.parse(diarySubVO.getTime());
            SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a");
            String result = format2.format(date);
            //holder1.time.setText(result);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        aList = new ArrayList<String>(Arrays.asList(diarySubVO.getFileUrl().split(",")));

        if(diaryVOs.get(groupPosition).getItems().get(childPosition).getSubject().matches("") || diaryVOs.get(groupPosition).getItems().get(childPosition).getSubject().matches("null"))
        {
            holder1.subject.setText("");
        }

        else
        {
            holder1.subject.setText(diaryVOs.get(groupPosition).getItems().get(childPosition).getSubject());
        }

        /*

        Log.d("koi",aList.toString());

        if(aList.isEmpty())
        {
            holder1.imageView1.setVisibility(View.INVISIBLE);
            holder1.imageView2.setVisibility(View.INVISIBLE);
            holder1.frameLayout.setVisibility(View.INVISIBLE);
        }


        *//*else
        if(aList)
        {

        }

*//*
        else
       if(aList.size()==1 && aList.size()<2) {
           holder1.imageView2.setVisibility(View.INVISIBLE);
           holder1.frameLayout.setVisibility(View.INVISIBLE);
           Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(0)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(holder1.imageView1);
       }
        *//*if(aList.size()==2) {
            Log.d("kkk","oiu"+aList.get(1));*//*
        else
        if(aList.size()>1 && aList.size()<3) {
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(0)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(holder1.imageView1);
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(1)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(holder1.imageView2);
            holder1.frameLayout.setVisibility(View.INVISIBLE);
            holder1.imageView2.setVisibility(View.VISIBLE);

        }

        else
        if(aList.size()==3 && aList.size()<4)
        {
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(0)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(holder1.imageView1);
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(1)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(holder1.imageView2);
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+aList.get(2)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(holder1.imageView3);
            holder1.countImages.setText("");
            holder1.frameLayout.setVisibility(View.VISIBLE);
            holder1.imageView2.setVisibility(View.VISIBLE);
        }

        else
        if(aList.size()>3)
        {Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(0)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(holder1.imageView1);
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(1)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(holder1.imageView2);
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+aList.get(2)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(holder1.imageView3);
            holder1.countImages.setText(String.valueOf(aList.size()-2));
            holder1.frameLayout.setVisibility(View.VISIBLE);
            holder1.imageView2.setVisibility(View.VISIBLE);

        }
        *//*}
        else
        }
        {
            holder1.imageView2.setVisibility(View.INVISIBLE);
            holder1.imageView3.setVisibility(View.INVISIBLE);
        }
        if(aList.size()>2)
        {*//*


        *//*}

        else
        {
            holder1.imageView3.setVisibility(View.INVISIBLE);
            holder1.frameLayout.setVisibility(View.INVISIBLE);
        }*//*


 holder1.imageView1.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         Intent intent = new Intent(context.getApplicationContext(),DiaryImagesDetailsScreen.class);

         // Utils.showToast(context.getApplicationContext(),aList.toString());



         intent.putExtra("array",diaryVOs.get(groupPosition).getItems().get(childPosition).getFileUrl());
         intent.putExtra("position",0);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

         context.startActivity(intent);





     }
 });

        holder1.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),DiaryImagesDetailsScreen.class);

                // Utils.showToast(context.getApplicationContext(),aList.toString());



                intent.putExtra("array",diaryVOs.get(groupPosition).getItems().get(childPosition).getFileUrl());
                intent.putExtra("position",1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });

        holder1.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),DiaryImagesDetailsScreen.class);

                // Utils.showToast(context.getApplicationContext(),aList.toString());



                intent.putExtra("array",diaryVOs.get(groupPosition).getItems().get(childPosition).getFileUrl());
                intent.putExtra("position",2);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });*/

       holder1.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Log.d("loiu",);

                if(diarySubVO.getAttachment().matches("file"))
                {
                    /*
                    intent.putExtra("desc","null");
                    intent.putExtra("array",aList.toString());
                    intent.putExtra("array2","0");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);*/
                    aList = new ArrayList<String>(Arrays.asList(diaryVOs.get(groupPosition).getItems().get(childPosition).getFileUrl().split(",")));
                    Intent intent = new Intent(context.getApplicationContext(),TeacherStudentViewAssignment.class);
                    intent.putExtra("array", aList);
                    intent.putExtra("array2","2");
                    intent.putExtra("array1", "n");
                    intent.putExtra("asn_id", "1");
                    intent.putExtra("desc","null");
                    intent.putExtra("title","null");
                    intent.putExtra("subject","null");
                    intent.putExtra("last_date","null");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

                else
                {
                    Intent intent = new Intent(context.getApplicationContext(),DiaryImagesDetailsScreen.class);

                   // Utils.showToast(context.getApplicationContext(),aList.toString());



                    intent.putExtra("array",diaryVOs.get(groupPosition).getItems().get(childPosition).getFileUrl());
                    intent.putExtra("position",0);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);
                }



            }
        });



        mDrawableBuilder = TextDrawable.builder().round();
        TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(diarySubVO.getInitials()), R.color.blue);
        Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL + diarySubVO.getProfileUrl()).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder1.initials) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder1.initials.setImageDrawable(circularBitmapDrawable);
            }
        });

        holder1.ratingBar.setFocusable(false);

        if(diaryVOs.get(groupPosition).getItems().get(childPosition).getShareable().matches("") || diaryVOs.get(groupPosition).getItems().get(childPosition).getShareable().matches("null") || diaryVOs.get(groupPosition).getItems().get(childPosition).getShareable().matches("0"))
        {
            holder1.share.setVisibility(View.INVISIBLE);
        }

        else
        {
            holder1.share.setVisibility(View.VISIBLE);
        }

        if(diarySubVO.getEmoticon().matches("") || diarySubVO.getEmoticon().matches("null"))
        {
            holder1.emoticon.setVisibility(View.GONE);
        }

        else
        {
            holder1.emoticon.setText(diarySubVO.getEmoticon().toString());
        }


       // holder1.time.setText(diarySubVO.getTime().toString());
        holder1.ratingParameter.setText(diarySubVO.getRatingParameter());


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    static class Holder
    {
          TextView dates,title,description,subject,emoticon,time,galleryCount,ratingParameter,countImages,type;
          ImageView attachment,icon,initials,imageView1,imageView2,imageView3,share,reply;
          FrameLayout frameLayout;
          RatingBar ratingBar;
          LinearLayout linearLayout;

    }
}
