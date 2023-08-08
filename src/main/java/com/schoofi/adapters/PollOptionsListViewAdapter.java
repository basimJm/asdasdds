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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.schoofi.activitiess.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * Created by Schoofi on 02-11-2016.
 */

public class PollOptionsListViewAdapter extends BaseAdapter {

    private Context context;
    private JSONArray pollOptionArray;
    int index;


    public PollOptionsListViewAdapter(Context context,JSONArray pollOptionArray,int index) {
        this.context = context;
        this.pollOptionArray = pollOptionArray;
        this.index = index;

    }



    @Override
    public int getCount() {
        return pollOptionArray.length();
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

        Holder holder;
        if(convertView == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.poll_options_listview, null);

            holder.answerText = (TextView) convertView.findViewById(R.id.text_answer_option);
            holder.answerOptionImage = (ImageView) convertView.findViewById(R.id.img_answer_text);

            try {
                holder.answerText.setText(pollOptionArray.getJSONObject(index).getString(""));

            } catch (JSONException e) {
                e.printStackTrace();
            }


            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }






        return convertView;
    }

    static class Holder
    {
        ImageView answerImage,imageAnswerText,imageAnswerImage,answerOptionImage;
        TextView answerText,answerOptionText;
        LinearLayout linearLayoutText,linearLayoutImage;
    }
}
