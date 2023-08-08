package com.schoofi.adapters;

import com.schoofi.activitiess.ParentDiaryUploadScreenFirst;
import com.schoofi.activitiess.R;
import com.schoofi.activities.StudentHomeScreen;
import com.schoofi.utils.CircularTextView;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentHomeScreenAdapter extends BaseAdapter {
	
	Context context;
	ArrayList<Integer> studentHomeScreenIconFinal = new ArrayList<Integer>();
	int []colorId;
	ArrayList<String> studentHomeScreenIconNamefinal = new ArrayList<String>();
	private static LayoutInflater inflater = null;
	String fontPath = "fonts/Asap-Regular.otf";
	
	
	public StudentHomeScreenAdapter(Context context, ArrayList<Integer> studentHomeScreenIconFinal1, ArrayList<String> studentHomeScreenIconNamefinal2, int []Colors)
	{
		this.context = context;
		this.studentHomeScreenIconFinal = studentHomeScreenIconFinal1;
		this.studentHomeScreenIconNamefinal = studentHomeScreenIconNamefinal2;
		colorId = Colors;
		inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return studentHomeScreenIconFinal.size();
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
		Holder holder = new Holder();
		
		View rowView;
		
		
		rowView = inflater.inflate(R.layout.studend_home_screen_gridview_layout, null);
		holder.imageView = (ImageView) rowView.findViewById(R.id.studentHomeScreenImageView);
		holder.imageText = (TextView) rowView.findViewById(R.id.text_home);
		holder.linearLayout = (LinearLayout) rowView.findViewById(R.id.linear_student_home_grid_layout);
		holder.circularTextView = (CircularTextView) rowView.findViewById(R.id.circularTextView);

		//holder.imageText.setText("Category");
		holder.imageView.setImageResource(studentHomeScreenIconFinal.get(position));
		//Utils.showToast(context,studentHomeScreenIconNamefinal.get(position).toString());
		holder.imageText.setText(studentHomeScreenIconNamefinal.get(position));

		if(studentHomeScreenIconNamefinal.get(position).matches("LEAVE REQUEST"))
		{
			Preferences.getInstance().loadPreference(context);
			if(Preferences.getInstance().userRoleId.matches("4"))
			{
				holder.circularTextView.setText(Preferences.getInstance().teacherBadgeCount);
				holder.circularTextView.setVisibility(View.VISIBLE);

			}

			else
			{
				holder.circularTextView.setVisibility(View.INVISIBLE);
			}
		}

		else
		{
			holder.circularTextView.setVisibility(View.INVISIBLE);
		}

		holder.linearLayout.setBackgroundColor(colorId[position]);
		
		return rowView;
	}
	
	

}

class Holder
{
 ImageView imageView;
 TextView imageText;
	CircularTextView circularTextView;
 LinearLayout linearLayout;
 
}