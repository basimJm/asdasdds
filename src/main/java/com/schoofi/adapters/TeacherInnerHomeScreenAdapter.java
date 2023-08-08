package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.activitiess.TeacherInnerHomeScreen;

public class TeacherInnerHomeScreenAdapter extends BaseAdapter {
	
	Context context;
	int []imageId;
	int []colorId;
	String []imageTextId;
	private static LayoutInflater inflater = null;
	String fontPath = "fonts/Asap-Regular.otf";
	
	
	public TeacherInnerHomeScreenAdapter(TeacherInnerHomeScreen teacherInnerHomeScreen, int []studentHomeScreenIcon, String []studentHomeScreenIconName, int []Colors)
	{
		context = teacherInnerHomeScreen;
		imageId = studentHomeScreenIcon;
		imageTextId = studentHomeScreenIconName;
		colorId = Colors;
		inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageId.length;
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
		Holder1 holder = new Holder1();
		
		View rowView;
		
		rowView = inflater.inflate(R.layout.studend_home_screen_gridview_layout, null);
		holder.imageView = (ImageView) rowView.findViewById(R.id.studentHomeScreenImageView);
		holder.imageText = (TextView) rowView.findViewById(R.id.text_home);
		holder.linearLayout = (LinearLayout) rowView.findViewById(R.id.linear_student_home_grid_layout);
		//holder.imageText.setText("Category");
		holder.imageView.setImageResource(imageId[position]);
		holder.imageText.setText(imageTextId[position]);
		holder.linearLayout.setBackgroundColor(colorId[position]);
		
		return rowView;
	}
	
	

}

 class Holder1
{
 ImageView imageView;
 TextView imageText;
 LinearLayout linearLayout;
 
}