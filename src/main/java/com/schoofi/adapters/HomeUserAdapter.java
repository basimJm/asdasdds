package com.schoofi.adapters;



import com.schoofi.activitiess.R;
import com.schoofi.utils.Preferences;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeUserAdapter extends BaseAdapter {

	private Context context;
	//String[] menu;
	//String[] hhh = {"lll","lll"};
	//int []studentHomeScreenIcon1 = {R.drawable.attendancetablet,R.drawable.leaverequesttablet,R.drawable.scheduletablet,R.drawable.assignmentstablet,R.drawable.announcementtablet,R.drawable.feedbacktablet,R.drawable.examscheduletablet,R.drawable.resulttablet,R.drawable.eventstablet,R.drawable.polltablet,R.drawable.gpstablet,R.drawable.feestablet,R.drawable.feestablet};
	String []studentHomeScreenIconName = {"ATTENDANCE","LEAVE REQUEST","TIME TABLE","ASSIGNMENTS/HOMEWORKS","ANNOUNCEMENTS/NOTICES","FEEDBACK","EXAM SCHEDULE","RESULT","EVENTS","POLL","BUS TRACKING","FEES","Group Discussion","Bus Attendance","Diary","YEARLY PLANNER","Bus Notification","HEALTH AND SAFETY"};
	ArrayList<String> studentHomeScreenIconNamefinal = new ArrayList<String>();
	public ArrayList<Integer> studentHomeScreenIconFinal = new ArrayList<Integer>();
	//ArrayList<Integer> studentHomeScreenIconFinal1 = new ArrayList<Integer>();
	public ArrayList<String> partialNameArray1 = new ArrayList<String>();
	String permissions;
	ArrayList<String> permissionsArray= new ArrayList<String>();

	int[] images = {
			R.drawable.navswitch,
			R.drawable.navdashboard,
			R.drawable.navattendance,
			R.drawable.navfees,
			R.drawable.navresult,
			R.drawable.navpol,
			R.drawable.navevents,
			R.drawable.navleave,
			R.drawable.navannouncement,
			R.drawable.navfeedback,
            R.drawable.gpsnav,
			R.drawable.navyearlyplanner,
			R.drawable.healthandsafetynav,

			R.drawable.healthandsafetynav,
			R.drawable.healthandsafetynav,

			R.drawable.healthandsafetynav,
			R.drawable.healthandsafetynav,
			R.drawable.navbell,
			R.drawable.person,
			R.drawable.aboutus,
			R.drawable.aboutschool,
			R.drawable.changepassword,
			R.drawable.help,
			R.drawable.logout,
			R.drawable.gpsnav,
			R.drawable.gpsnav,
			R.drawable.gpsnav,
			R.drawable.gpsnav,
			R.drawable.gpsnav,

	};

	int[] images1 = {R.drawable.navdashboard,
			R.drawable.navattendance,
			R.drawable.navfees,
			R.drawable.navresult,
			R.drawable.navpol,
			R.drawable.navevents,
			R.drawable.navleave,
			R.drawable.navannouncement,
			R.drawable.navfeedback,
			R.drawable.gpsnav,
			R.drawable.navyearlyplanner,
			R.drawable.healthandsafetynav,

			R.drawable.healthandsafetynav,
			R.drawable.healthandsafetynav,
			R.drawable.healthandsafetynav,
			R.drawable.healthandsafetynav,
			R.drawable.navbell,

			R.drawable.person,
			R.drawable.aboutus,
			R.drawable.aboutschool,
			R.drawable.changepassword,
			R.drawable.help,
			R.drawable.logout,
			R.drawable.gpsnav,
			R.drawable.gpsnav,
			R.drawable.gpsnav,
			R.drawable.gpsnav,
			R.drawable.gpsnav,
	};

	public HomeUserAdapter(Context context ,ArrayList<String> permissionArray1) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.permissionsArray = permissionArray1;
		if(Preferences.getInstance().userRoleId.matches("8"))
		{
			permissions = Preferences.getInstance().permissions;
			permissionsArray = new ArrayList<String>(Arrays.asList(permissions.split(",")));
			partialNameArray1.add("DASHBOARD");
			partialNameArray1.add("ATTENDANCE");
			partialNameArray1.add("FEES");
			partialNameArray1.add("BUS TRACKING");
			partialNameArray1.add("YEARLY PLANNER");
			partialNameArray1.add("EVENTS");
			partialNameArray1.add("ANNOUNCEMENTS/NOTICES");
			partialNameArray1.add("RESULTS");
			partialNameArray1.add("POLL");

			studentHomeScreenIconFinal.add(R.drawable.navdashboard);
			studentHomeScreenIconFinal.add(R.drawable.navattendance);
			studentHomeScreenIconFinal.add(R.drawable.navfees);
			studentHomeScreenIconFinal.add(R.drawable.gpsnav);
			studentHomeScreenIconFinal.add(R.drawable.navyearlyplanner);
			studentHomeScreenIconFinal.add(R.drawable.navevents);
			studentHomeScreenIconFinal.add(R.drawable.navannouncement);
			studentHomeScreenIconFinal.add(R.drawable.navresult);
			studentHomeScreenIconFinal.add(R.drawable.navpoll);

			for(int i =0;i<permissionsArray.size();i++)
			{
				if(permissionsArray.get(i).matches("A"))
				{

					studentHomeScreenIconNamefinal.add(studentHomeScreenIconName[i]);


				}
				else
				{

				}
			}

			for (int j=0;j<studentHomeScreenIconNamefinal.size();j++)
			{
				if(studentHomeScreenIconNamefinal.get(j).matches("DASHBOARD") || studentHomeScreenIconNamefinal.get(j).matches("ATTENDANCE") || studentHomeScreenIconNamefinal.get(j).matches("FEES") || studentHomeScreenIconNamefinal.get(j).matches("RESULT") || studentHomeScreenIconNamefinal.get(j).matches("POLL") || studentHomeScreenIconNamefinal.get(j).matches("TIME TABLE") || studentHomeScreenIconNamefinal.get(j).matches("EXAM SCHEDULE") || studentHomeScreenIconNamefinal.get(j).matches("ASSIGNMENTS/HOMEWORKS") || studentHomeScreenIconNamefinal.get(j).matches("EVENTS") || studentHomeScreenIconNamefinal.get(j).matches("Bus Attendance") || studentHomeScreenIconNamefinal.get(j).matches("Diary")  || studentHomeScreenIconNamefinal.get(j).matches("Bus Notification") || studentHomeScreenIconNamefinal.get(j).matches("BUS TRACKING") || studentHomeScreenIconNamefinal.get(j).matches("YEARLY PLANNER") || studentHomeScreenIconNamefinal.get(j).matches("ANNOUNCEMENTS/NOTICES"))
				{

				}

				else
				{
					partialNameArray1.add(studentHomeScreenIconNamefinal.get(j));
					if(studentHomeScreenIconNamefinal.get(j).matches("LEAVE REQUEST"))
					{
						studentHomeScreenIconFinal.add(R.drawable.navleave);
					}



						else
						if(studentHomeScreenIconNamefinal.get(j).matches("FEEDBACK"))
						{
							studentHomeScreenIconFinal.add(R.drawable.navfeedback);
						}

						else
						if(studentHomeScreenIconNamefinal.get(j).matches("HEALTH AND SAFETY"))
						{
							studentHomeScreenIconFinal.add(R.drawable.healthandsafetynav);
						}


				}
			}


            partialNameArray1.add("NOTIFICATIONS");
			partialNameArray1.add("PROFILE");
			partialNameArray1.add("ABOUT US");
			partialNameArray1.add("ABOUT SCHOOL");
			partialNameArray1.add("CHANGE PASSWORD");
			partialNameArray1.add("HELP");
			partialNameArray1.add("LOGOUT");

			studentHomeScreenIconFinal.add(R.drawable.navbell);
			studentHomeScreenIconFinal.add(R.drawable.person);
			studentHomeScreenIconFinal.add(R.drawable.aboutus);
			studentHomeScreenIconFinal.add(R.drawable.aboutschool);
			studentHomeScreenIconFinal.add(R.drawable.changepassword);
			studentHomeScreenIconFinal.add(R.drawable.help);
			studentHomeScreenIconFinal.add(R.drawable.logout);
			//studentHomeScreenIconFinal.add(R.drawable.logout);

		}

		else
		{
			//menu = context.getResources().getStringArray(R.array.menu);
			//menu = hhh;
			permissions = Preferences.getInstance().permissions;
			permissionsArray = new ArrayList<String>(Arrays.asList(permissions.split(",")));
			partialNameArray1.add("SWITCH SCHOOL");
			partialNameArray1.add("DASHBOARD");
			partialNameArray1.add("ATTENDANCE");
			partialNameArray1.add("FEES");
			partialNameArray1.add("BUS TRACKING");
			partialNameArray1.add("YEARLY PLANNER");
			partialNameArray1.add("EVENTS");
			partialNameArray1.add("ANNOUNCEMENTS/NOTICES");
			partialNameArray1.add("RESULTS");
			partialNameArray1.add("POLL");


			studentHomeScreenIconFinal.add(R.drawable.navswitch);
			studentHomeScreenIconFinal.add(R.drawable.navdashboard);
			studentHomeScreenIconFinal.add(R.drawable.navattendance);
			studentHomeScreenIconFinal.add(R.drawable.navfees);
			studentHomeScreenIconFinal.add(R.drawable.gpsnav);
			studentHomeScreenIconFinal.add(R.drawable.navyearlyplanner);
			studentHomeScreenIconFinal.add(R.drawable.navevents);
			studentHomeScreenIconFinal.add(R.drawable.navannouncement);
			studentHomeScreenIconFinal.add(R.drawable.navresult);
			studentHomeScreenIconFinal.add(R.drawable.navpoll);


			for(int i =0;i<permissionsArray.size();i++)
			{
				if(permissionsArray.get(i).matches("A"))
				{

					studentHomeScreenIconNamefinal.add(studentHomeScreenIconName[i]);


				}
				else
				{

				}
			}

			for (int j=0;j<studentHomeScreenIconNamefinal.size();j++)
			{
				if(studentHomeScreenIconNamefinal.get(j).matches("DASHBOARD") || studentHomeScreenIconNamefinal.get(j).matches("ATTENDANCE") || studentHomeScreenIconNamefinal.get(j).matches("FEES") || studentHomeScreenIconNamefinal.get(j).matches("RESULT") || studentHomeScreenIconNamefinal.get(j).matches("POLL") || studentHomeScreenIconNamefinal.get(j).matches("TIME TABLE") || studentHomeScreenIconNamefinal.get(j).matches("EXAM SCHEDULE") || studentHomeScreenIconNamefinal.get(j).matches("ASSIGNMENTS/HOMEWORKS") || studentHomeScreenIconNamefinal.get(j).matches("EVENTS") || studentHomeScreenIconNamefinal.get(j).matches("Bus Attendance") || studentHomeScreenIconNamefinal.get(j).matches("Diary")  || studentHomeScreenIconNamefinal.get(j).matches("Bus Notification") || studentHomeScreenIconNamefinal.get(j).matches("BUS TRACKING") || studentHomeScreenIconNamefinal.get(j).matches("YEARLY PLANNER") || studentHomeScreenIconNamefinal.get(j).matches("ANNOUNCEMENTS/NOTICES"))
				{

				}

				else
				{
					partialNameArray1.add(studentHomeScreenIconNamefinal.get(j));
					if(studentHomeScreenIconNamefinal.get(j).matches("LEAVE REQUEST"))
					{
						studentHomeScreenIconFinal.add(R.drawable.navleave);
					}



					else
					if(studentHomeScreenIconNamefinal.get(j).matches("FEEDBACK"))
					{
						studentHomeScreenIconFinal.add(R.drawable.navfeedback);
					}

					else
					if(studentHomeScreenIconNamefinal.get(j).matches("HEALTH AND SAFETY"))
					{
						studentHomeScreenIconFinal.add(R.drawable.healthandsafetynav);
					}
				}
			}


			partialNameArray1.add("NOTIFICATIONS");
			partialNameArray1.add("PROFILE");
			partialNameArray1.add("ABOUT US");
			partialNameArray1.add("ABOUT SCHOOL");
			partialNameArray1.add("CHANGE PASSWORD");
			partialNameArray1.add("HELP");
			partialNameArray1.add("LOGOUT");

			studentHomeScreenIconFinal.add(R.drawable.navbell);
			studentHomeScreenIconFinal.add(R.drawable.person);
			studentHomeScreenIconFinal.add(R.drawable.aboutus);
			studentHomeScreenIconFinal.add(R.drawable.aboutschool);
			studentHomeScreenIconFinal.add(R.drawable.changepassword);
			studentHomeScreenIconFinal.add(R.drawable.help);
			studentHomeScreenIconFinal.add(R.drawable.logout);


		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return partialNameArray1.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return partialNameArray1.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = null;
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.navigation_item, parent, false);
		}
		else
		{
			row = convertView;	
		}
		TextView titleTextView= (TextView) row.findViewById(R.id.textView1);
		ImageView titleImageView = (ImageView) row.findViewById(R.id.item_icons);
		titleTextView.setText(partialNameArray1.get(position));
		if(Preferences.getInstance().userRoleId.matches("8"))
		{
			titleImageView.setImageResource(studentHomeScreenIconFinal.get(position));
		}

		else
		{
			titleImageView.setImageResource(studentHomeScreenIconFinal.get(position));
		}
		return row;
	}


}
