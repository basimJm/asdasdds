package com.schoofi.utils;



import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class Utils 
{
	public static boolean isNetworkAvailable(Context context) 
	{
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	public static void clearApplicationData(Context context) 
	{
		File cache = context.getCacheDir();
		File appDir = new File(cache.getParent());
		if (appDir.exists()) {
			String[] children = appDir.list();
			for (String s : children) {
				if (!s.equals("lib")) {
					deleteDir(new File(appDir, s));
					Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
				}
			}
		}
	}
	public static boolean deleteDir(File dir) 
	{
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
	public static int getDateOfMonth(Date date) throws ParseException{  
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DATE);
	}
	@SuppressLint("SimpleDateFormat")
	public static String getMonthName(Date date) throws ParseException{  
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String monthName = new SimpleDateFormat("MMMM").format(cal.getTime());
		return monthName.substring(0,3);
	}
	public static int getCurrentYear(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);

		return year;
	}
	public static int getNoOfDaysInMonth(Date date){  
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return maxDays;
	}
	public static void showToast(Context context, String message)
	{

		Toast.makeText(context,message, Toast.LENGTH_SHORT).show();

	}
	public static boolean validatePhoneNumber(String phoneNo) {
		//validate phone numbers of format "1234567890"
		if (phoneNo.equals("")) return true;
		if (phoneNo.matches("\\d{10}")) return true;
		//validating phone number with -, . or spaces
		else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
		//validating phone number with extension length from 3 to 5
		else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
		//validating phone number where area code is in braces ()
		else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
		//return false if nothing matches the input
		else return false;

	}
}
