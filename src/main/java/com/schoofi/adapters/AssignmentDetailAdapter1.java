package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;

import java.util.ArrayList;

public class AssignmentDetailAdapter1 extends BaseAdapter
{
	Context context;
	ArrayList<String> myList1;


	public AssignmentDetailAdapter1(Context context, ArrayList<String> myList1) {
		super();
		this.context = context;
		this.myList1 = myList1;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myList1.size();
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
		// TODO Auto-generated method stub

		Holder holder;

		if(convertView == null)
		{
			holder = new Holder();
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.student_feedback_gridview_layout, null);
			holder.feedBackImage = (ImageView) convertView.findViewById(R.id.imageStudentFeedbackGridview);
			convertView.setTag(holder);
		}

		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		if(myList1.get(position).endsWith(".JPEG") || myList1.get(position).endsWith(".PNG") || myList1.get(position).endsWith(".JPG") || myList1.get(position).endsWith(".GIF") || myList1.get(position).endsWith(".jpeg") || myList1.get(position).endsWith(".png") || myList1.get(position).endsWith(".jpg") || myList1.get(position).endsWith(".gif"))
		{
			Glide.with(context).load(AppConstants.SERVER_URLS.SERVER_URL+myList1.get(position)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).crossFade().into(holder.feedBackImage);
		}
		
		else
		{
			holder.feedBackImage.setImageResource(R.drawable.fileiconupdate);
		}
		


		return convertView;
	}

	static class Holder
	{
		ImageView feedBackImage;
	}
}
