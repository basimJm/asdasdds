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
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.QuizQuestionScreenVO;

import java.util.ArrayList;



/**
 * Created by Schoofi on 02-11-2016.
 */

public class AnswerListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QuizQuestionScreenVO> temparr;
    private String type;

    public AnswerListViewAdapter(Context context, ArrayList<QuizQuestionScreenVO> temparr, String type) {
        this.context = context;
        this.temparr = temparr;
        this.type = type;
    }



    @Override
    public int getCount() {
        return temparr.size();
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
            convertView = layoutInflater.inflate(R.layout.answer_listview, null);
            holder.answerImage = (ImageView) convertView.findViewById(R.id.img_answer);
            holder.answerText = (TextView) convertView.findViewById(R.id.text_answer_option);
            holder.answerOptionImage = (ImageView) convertView.findViewById(R.id.img_answer_img);
            holder.answerOptionText = (TextView) convertView.findViewById(R.id.text_answer_option_image);
            holder.imageAnswerImage = (ImageView) convertView.findViewById(R.id.img_answer_text);
            holder.linearLayoutText = (LinearLayout) convertView.findViewById(R.id.linear_text);
            holder.linearLayoutImage = (LinearLayout) convertView.findViewById(R.id.linear_image);
            holder.optionName = (TextView) convertView.findViewById(R.id.text_1);
            holder.optionName1 = (TextView) convertView.findViewById(R.id.text_2);
            holder.optionName2 = (TextView) convertView.findViewById(R.id.text_3);
            holder.linearLayoutTextImage = (LinearLayout) convertView.findViewById(R.id.linear_inside);


           // holder.answerOptionText.setVisibility(View.GONE);

            if(temparr.get(position).getSolutionType().matches("") || temparr.get(position).getSolutionType().matches("null"))
            {
                //holder.answerImage.setVisibility(View.GONE);
                holder.linearLayoutImage.setVisibility(View.GONE);
                holder.linearLayoutTextImage.setVisibility(View.GONE);
                holder.answerText.setText(temparr.get(position).getSolutionName());
                holder.optionName.setText(temparr.get(position).getSolutionId());

            }

            else
                if(temparr.get(position).getSolutionName().matches("") || temparr.get(position).getSolutionName().matches("null"))
            {
                //holder.answerText.setVisibility(View.GONE);
                holder.linearLayoutText.setVisibility(View.GONE);
                holder.linearLayoutTextImage.setVisibility(View.GONE);
                Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+temparr.get(position).getSolutionName()).placeholder(R.drawable.icon).crossFade().into(holder.answerImage);
                holder.optionName1.setText(temparr.get(position).getSolutionId());


            }
            else
                {
                    holder.linearLayoutText.setVisibility(View.GONE);
                    holder.linearLayoutImage.setVisibility(View.GONE);
                    holder.answerOptionText.setText(temparr.get(position).getSolutionName());
                    Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+temparr.get(position).getSolutionName()).placeholder(R.drawable.icon).crossFade().into(holder.answerOptionImage);
                    holder.optionName2.setText(temparr.get(position).getSolutionId());

                }
            if(type.matches("2"))
            {
                if(temparr.get(position).getSolutionId().matches("1"))
                {
                    //convertView.setBackgroundResource(R.drawable.green_background);
                    holder.answerOptionImage.setImageResource(R.drawable.greentick);
                    holder.imageAnswerImage.setImageResource(R.drawable.greentick);
                    holder.answerText.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.greentick,0);
                    holder.answerText.setText(temparr.get(position).getSolutionName());
                    holder.answerOptionText.setText("");
                }

                else
                if(temparr.get(position).getSolutionId().matches("2"))
                {
                    holder.answerOptionImage.setImageResource(R.drawable.crossicon);
                    holder.imageAnswerImage.setImageResource(R.drawable.crossicon);
                    holder.answerText.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.croosicon,0);
                    holder.answerText.setText(temparr.get(position).getSolutionName());
                    holder.answerOptionText.setText("");
                }

                else
                if(temparr.get(position).getSolutionId().matches("3"))
                {
                    holder.answerOptionImage.setImageResource(R.drawable.greentick);
                    holder.imageAnswerImage.setImageResource(R.drawable.greentick);
                    holder.answerText.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.greentick,0);
                    holder.answerText.setText(temparr.get(position).getSolutionName()+" Not Attempted");
                    holder.answerOptionText.setText("Not Attempted: ");
                }
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
        TextView answerText,answerOptionText,optionName,optionName1,optionName2;
        LinearLayout linearLayoutText,linearLayoutImage,linearLayoutTextImage;
    }
}
