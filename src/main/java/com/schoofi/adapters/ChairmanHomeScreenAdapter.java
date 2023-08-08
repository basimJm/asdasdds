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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;

public class ChairmanHomeScreenAdapter extends BaseAdapter{

	private Context context;
	private JSONArray chairmanHomeScreenArray;

	public ChairmanHomeScreenAdapter(Context context,JSONArray chairmanHomeScreenArray) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.chairmanHomeScreenArray = chairmanHomeScreenArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return chairmanHomeScreenArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if(convertView == null)
		{   holder = new Holder();
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		convertView = layoutInflater.inflate(R.layout.chairman_home_screen_listview, null);
		holder.schoolName = (TextView) convertView.findViewById(R.id.text_schoolName);
		holder.schoolAddress = (TextView) convertView.findViewById(R.id.text_schoolAddress);
		holder.schoolLogo = (ImageView) convertView.findViewById(R.id.imageView_school_logo);
		convertView.setTag(holder);
		}
		else
		{
			holder = (Holder) convertView.getTag();
		}

		try {
			holder.schoolName.setText(chairmanHomeScreenArray.getJSONObject(position).getString("school_name"));
			holder.schoolAddress.setText(chairmanHomeScreenArray.getJSONObject(position).getString("school_address")+"-"+chairmanHomeScreenArray.getJSONObject(position).getString("add2")+"-"+chairmanHomeScreenArray.getJSONObject(position).getString("city")+"-"+chairmanHomeScreenArray.getJSONObject(position).getString("state")+"-"+chairmanHomeScreenArray.getJSONObject(position).getString("zipcode"));
			/*Picasso.with(context).load(AppConstants.SERVER_URLS.SERVER_URL+chairmanHomeScreenArray.getJSONObject(position).getString("school_logo")).placeholder(R.drawable.schoollogo).
			error(R.drawable.schoollogo).transform(new CircleTransform()).into(holder.schoolLogo);*/

			Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+chairmanHomeScreenArray.getJSONObject(position).getString("school_logo")).asBitmap().placeholder(R.drawable.schoollogo).error(R.drawable.schoollogo).into(new BitmapImageViewTarget(holder.schoolLogo)
			{
				@Override
			protected void setResource(Bitmap resource) {
				RoundedBitmapDrawable circularBitmapDrawable =
						RoundedBitmapDrawableFactory.create(context.getResources(), resource);
				circularBitmapDrawable.setCircular(true);
				holder.schoolLogo.setImageDrawable(circularBitmapDrawable);
			}
			});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		return convertView;
	}

	static class Holder
	{
		private TextView schoolAddress,schoolName;
		private ImageView schoolLogo;
	}

}
