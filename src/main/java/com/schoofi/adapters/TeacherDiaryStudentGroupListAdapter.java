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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;

public class TeacherDiaryStudentGroupListAdapter  extends BaseAdapter {
    private Context context;
    private JSONArray jsonArray;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;

    public TeacherDiaryStudentGroupListAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
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

        final TeacherStudentDiaryScreenAdapter.Holder holder;

        if(convertView == null)
        {
            holder = new TeacherStudentDiaryScreenAdapter.Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.teacher_student_group_listview, null);

            holder.name = convertView.findViewById(R.id.name);
            holder.name1 = convertView.findViewById(R.id.name1);
            holder.icon = convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        }

        else
        {
            holder = (TeacherStudentDiaryScreenAdapter.Holder) convertView.getTag();
        }

        try {




            if(jsonArray.getJSONObject(position).getString("type").matches("group")) {
                holder.name1.setText(jsonArray.getJSONObject(position).getString("group_name"));
                String co = "";

                String word = jsonArray.getJSONObject(position).getString("group_name").toString();



                co = co.concat(Character.toUpperCase(word.charAt(0)) + " ");


                mDrawableBuilder = TextDrawable.builder().round();
                TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(co), ColorTemplate.VORDIPLOM_COLORS[position]);
                textDrawable.setIntrinsicHeight(12);
                Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.icon)
                {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.icon.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
            else
            {
                holder.name.setText(jsonArray.getJSONObject(position).getString("stu_name"));
                String co = "";
                String words = jsonArray.getJSONObject(position).getString("stu_name").toString();



                co = co.concat(Character.toUpperCase(words.charAt(0)) + " ");


                mDrawableBuilder = TextDrawable.builder().round();
                TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(co), ColorTemplate.VORDIPLOM_COLORS[position]);
                textDrawable.setIntrinsicHeight(12);
                Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.icon)
                {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.icon.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }



        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;


    }

    static class Holder

    {
        TextView name,name1;
        ImageView icon;
    }
}
