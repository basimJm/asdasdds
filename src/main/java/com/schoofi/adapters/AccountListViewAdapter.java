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
 * Created by Schoofi on 08-02-2017.
 */

public class AccountListViewAdapter extends BaseAdapter {

    private Context context;
    private JSONArray accountListViewArray;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;

    public AccountListViewAdapter(Context context, JSONArray accountListViewArray) {
        this.context = context;
        this.accountListViewArray = accountListViewArray;
    }

    @Override
    public int getCount() {
        return accountListViewArray.length();
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
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.linked_account_listview, null);
            holder.name = (TextView) convertView.findViewById(R.id.text_name);
            holder.logo = (ImageView) convertView.findViewById(R.id.img_logo);
            convertView.setTag(holder);

        }

        else
        {
            holder = (Holder) convertView.getTag();
        }


        try {

            if(accountListViewArray.getJSONObject(position).getString("linked_account").matches("1"))
            {
                holder.logo.setImageResource(R.drawable.greencircletick);
            }

            else {
                mDrawableBuilder = TextDrawable.builder().round();

                TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(accountListViewArray.getJSONObject(position).getString("social_platform").charAt(0)), R.color.blue);
                Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL + accountListViewArray.getJSONObject(position).getString("logo")).asBitmap().placeholder(textDrawable).error(textDrawable).into(new BitmapImageViewTarget(holder.logo) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.logo.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }

            holder.name.setText(accountListViewArray.getJSONObject(position).getString("social_platform"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    static class Holder
    {
        TextView name;
        ImageView logo;
    }
}
