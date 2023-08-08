package com.schoofi.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.schoofi.activitiess.DiaryStudentList;
import com.schoofi.activitiess.GalleryActivity;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import library.Animations.DescriptionAnimation;
import library.SliderAdapter;
import library.SliderLayout;
import library.SliderTypes.BaseSliderView;
import library.SliderTypes.DefaultSliderView;

public class DiaryNewDesignAdapter extends BaseAdapter {

    private Context context;
    private JSONArray diaryNewArray;
    ArrayList<String> myList;
    private int position,position1;
    SliderAdapter sliderAdapter;
    String date,subDate,subDate1;
    Date date5,date6;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    ArrayList aList= new ArrayList();

    public DiaryNewDesignAdapter(Context context, JSONArray diaryNewArray) {
        this.context = context;
        this.diaryNewArray = diaryNewArray;
    }

    @Override
    public int getCount() {
        return diaryNewArray.length();
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
            convertView = layoutInflater.inflate(R.layout.diary_new_design_adapter, null);
            holder.title = convertView.findViewById(R.id.title);
            holder.description = convertView.findViewById(R.id.description);
            holder.subject = convertView.findViewById(R.id.subject);
            holder.date = convertView.findViewById(R.id.text_date);
            holder.userImage = convertView.findViewById(R.id.image);
            holder.file = convertView.findViewById(R.id.image1);
            holder.ratingBar = convertView.findViewById(R.id.ratingBar);
            holder.mDemoSlider = convertView.findViewById(R.id.slider);
            holder.file1 = convertView.findViewById(R.id.image2);
            holder.studentList = convertView.findViewById(R.id.text_student_list);
            holder.studentName = convertView.findViewById(R.id.student_name);

            convertView.setTag(holder);

        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {

            if(Preferences.getInstance().userRoleId.matches("4"))
            {
                if(diaryNewArray.getJSONObject(position).getString("diary_module_id").matches("diary_a")) {
                    holder.studentList.setVisibility(View.INVISIBLE);
                    holder.studentName.setVisibility(View.VISIBLE);
                    holder.studentName.setText(diaryNewArray.getJSONObject(position).getString("stu_name"));
                }
                else
                {
                    holder.studentList.setVisibility(View.VISIBLE);
                    holder.studentName.setVisibility(View.GONE);
                    holder.studentList.setTag(new Integer(position));

                    holder.studentList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {
                                JSONObject  jsonObject = diaryNewArray.getJSONObject((Integer) v.getTag());
                                JSONArray jsonArray = jsonObject.getJSONArray("participants");
                                Log.d("tag",jsonArray.toString());
                                Intent intent = new Intent(context, DiaryStudentList.class);
                                intent.putExtra("json",jsonArray.toString());
                                context.startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }


            }
            else
            {
                holder.studentList.setVisibility(View.INVISIBLE);
                holder.studentName.setVisibility(View.GONE);
            }





                holder.title.setText(diaryNewArray.getJSONObject(position).getString("Name") + "-" + diaryNewArray.getJSONObject(position).getString("title"));

            date = diaryNewArray.getJSONObject(position).getString("crr_date");
            try {
                date5 = formatter.parse(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");

            subDate1 = formatter1.format(date5);
            holder.date.setText(subDate1);
            holder.description.setText(diaryNewArray.getJSONObject(position).getString("description"));
            if(diaryNewArray.getJSONObject(position).getString("diary_module_id").matches("diary_a"))
            {
                holder.subject.setText(diaryNewArray.getJSONObject(position).getString("subject")+"("+diaryNewArray.getJSONObject(position).getString("rating_parameter")+")");
                holder.ratingBar.setRating(Float.parseFloat(diaryNewArray.getJSONObject(position).getString("rating")));
            }
            else
            {
                holder.subject.setText(diaryNewArray.getJSONObject(position).getString("subject"));
                holder.ratingBar.setVisibility(View.GONE);
                holder.file1.setVisibility(View.GONE);
            }

            mDrawableBuilder = TextDrawable.builder().round();

            TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(diaryNewArray.getJSONObject(position).getString("Name").charAt(0)), R.color.blue);
            Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL + diaryNewArray.getJSONObject(position).getString("sender_picture")).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.userImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.userImage.setImageDrawable(circularBitmapDrawable);
                }
            });

            if(diaryNewArray.getJSONObject(position).getString("diary_module_id").matches("diary_c")) {
                holder.file.setVisibility(View.INVISIBLE);
                holder.mDemoSlider.setVisibility(View.VISIBLE);
                String image;
                image = diaryNewArray.getJSONObject(position).getString("image");
                if (image.matches("") || image.matches("null")) {
                    holder.mDemoSlider.setVisibility(View.GONE);
                } else {
                    aList = new ArrayList<String>(Arrays.asList(image.split(",")));

                    int i = 0;

                    sliderAdapter = new SliderAdapter(context.getApplicationContext());

                    for (i = 0; i < aList.size(); i++) {
                        final DefaultSliderView defaultSliderView = new DefaultSliderView(context.getApplicationContext());

                        defaultSliderView.image(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(i)).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                Log.d("MyActivity", "index selected:" + holder.mDemoSlider.getCurrentPosition());
                                Intent intent = new Intent(context, GalleryActivity.class);
                                try {
                                    intent.putExtra("images", diaryNewArray.getJSONObject(position).getString("image"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                context.startActivity(intent);


                            }
                        });

                        holder.mDemoSlider.addSlider(defaultSliderView);


                    }

                    holder.mDemoSlider.setPresetTransformer(SliderLayout.Transformer.DepthPage);
                    holder.mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    holder.mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                    holder.mDemoSlider.setDuration(4000);
                    holder.mDemoSlider.stopAutoCycle();

                }
            }





            else
            {
                /*holder.file.setVisibility(View.VISIBLE);
                holder.mDemoSlider.setVisibility(View.GONE);*/


                try {
                    String image;
                    image = diaryNewArray.getJSONObject(position).getString("image");
                    if (image.matches("") || image.matches("null")) {
                        holder.mDemoSlider.setVisibility(View.GONE);
                        holder.file.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        if(image.contains(".jpg")|| image.contains(".jpeg") || image.contains(".png") || image.contains(".PNG") || image.contains(".Png") || image.contains(".JPG") || image.contains(".Jpg") || image.contains(".JPEG") || image.contains(".Jpeg"))
                        {
                            holder.file.setVisibility(View.INVISIBLE);
                            holder.mDemoSlider.setVisibility(View.VISIBLE);
                            aList = new ArrayList<String>(Arrays.asList(image.split(",")));

                            int i = 0;

                            sliderAdapter = new SliderAdapter(context.getApplicationContext());

                            for (i = 0; i < aList.size(); i++) {
                                final DefaultSliderView defaultSliderView = new DefaultSliderView(context.getApplicationContext());

                                defaultSliderView.image(AppConstants.SERVER_URLS.IMAGE_URL + aList.get(i)).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {
                                        Log.d("MyActivity", "index selected:" + holder.mDemoSlider.getCurrentPosition());
                                        Intent intent = new Intent(context, GalleryActivity.class);
                                        try {
                                            intent.putExtra("images", diaryNewArray.getJSONObject(position).getString("image"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        context.startActivity(intent);


                                    }
                                });

                                holder.mDemoSlider.addSlider(defaultSliderView);


                            }

                            holder.mDemoSlider.setPresetTransformer(SliderLayout.Transformer.DepthPage);
                            holder.mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            holder.mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                            holder.mDemoSlider.setDuration(4000);
                            holder.mDemoSlider.stopAutoCycle();


                        }
                        else
                        {
                            holder.mDemoSlider.setVisibility(View.GONE);
                            holder.file.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            holder.mDemoSlider.setTag(new Integer(position));
            holder.mDemoSlider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intent intent = new Intent(context.getApplicationContext(), GalleryActivity.class);
                        intent.putExtra("images",diaryNewArray.getJSONObject(position).getString("image"));
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.file.setTag(new Integer(position));
            holder.file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL+diaryNewArray.getJSONObject((Integer) v.getTag()).getString("image")));
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });



        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }

    static class Holder
    {
        TextView title,description,subject,date,studentList,studentName;
        ImageView userImage,file,file1;
        RatingBar ratingBar;
        private SliderLayout mDemoSlider;


    }
}
