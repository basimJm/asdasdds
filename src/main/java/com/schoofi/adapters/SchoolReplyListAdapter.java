package com.schoofi.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.schoofi.activitiess.R;
import com.schoofi.activitiess.SchoolDiaryDetailScreen;
import com.schoofi.activitiess.TeacherStudentViewAssignment;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.DiaryVO;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Schoofi on 06-02-2017.
 */

public class SchoolReplyListAdapter extends BaseAdapter {

    private Context context;
    private JSONArray schoolReplyListArray;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    ArrayList<String> myList;
    ArrayList<String> myList1;
    String file;


    public SchoolReplyListAdapter(Context context, JSONArray schoolReplyListArray) {
        this.context = context;
        this.schoolReplyListArray = schoolReplyListArray;
    }

    @Override
    public int getCount() {
        return schoolReplyListArray.length();
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


        final Holder holder ;
        if (convertView == null) {
            holder = new Holder();

            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.diary_reply_listitem, parent, false);

            holder.profileImage = (ImageView) convertView.findViewById(R.id.img_initials);
            holder.icon = (ImageView) convertView.findViewById(R.id.attatchment);
            holder.message = (TextView) convertView.findViewById(R.id.text_reply);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linear);
            convertView.setTag(holder);

        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {

            if(schoolReplyListArray.getJSONObject(position).getString("reply_message").matches("") || schoolReplyListArray.getJSONObject(position).getString("reply_message").matches("null"))
            {
                holder.message.setVisibility(View.GONE);
                holder.profileImage.setVisibility(View.GONE);
                holder.icon.setVisibility(View.GONE);
            }
            else {
                if(schoolReplyListArray.getJSONObject(position).getString("role_id").matches("4"))
                {

                }
                else
                {
                    holder.linearLayout.setBackgroundResource(R.color.graay);
                }
                holder.message.setText(schoolReplyListArray.getJSONObject(position).getString("reply_message"));
                mDrawableBuilder = TextDrawable.builder().round();
                TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(schoolReplyListArray.getJSONObject(position).getString("reply_initials")), R.color.blue);
                Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL + schoolReplyListArray.getJSONObject(position).getString("user_picture")).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.profileImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.profileImage.setImageDrawable(circularBitmapDrawable);
                    }
                });



                if (schoolReplyListArray.getJSONObject(position).getString("file").matches("") || schoolReplyListArray.getJSONObject(position).getString("file").matches("null")) {
                    holder.icon.setVisibility(View.INVISIBLE);
                } else {
                    holder.icon.setVisibility(View.VISIBLE);
                }

                holder.icon.setTag(new Integer(position));

                holder.icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            file = schoolReplyListArray.getJSONObject((Integer) v.getTag()).getString("file");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        myList = new ArrayList<String>(Arrays.asList(file.split(",")));

                        myList1 = new ArrayList<String>();

                        for (int i = 0; i < myList.size(); i++) {
                            myList1.add(AppConstants.SERVER_URLS.IMAGE_URL + myList.get(i));
                                                                   //System.out.println(myList1.get(i));
                        }

                        Intent intent = new Intent(context.getApplicationContext(),TeacherStudentViewAssignment.class);
                        intent.putExtra("array", myList);
                        intent.putExtra("array2","2");
                        intent.putExtra("array1", "n");
                        intent.putExtra("asn_id", "1");
                        intent.putExtra("desc","null");
                        intent.putExtra("title","null");
                        intent.putExtra("subject","null");
                        intent.putExtra("last_date","null");

                        context.startActivity(intent);

                    }
                });
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }

    static class Holder
    {
        ImageView profileImage,icon;
        TextView message;
        LinearLayout linearLayout;

    }
}
