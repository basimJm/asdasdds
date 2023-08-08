package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.schoofi.activitiess.R;
import com.schoofi.activitiess.SchoolPlannerClassSectionListEdit;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Schoofi on 10-03-2017.
 */

public class SchoolPlannerClassSectionListEditAdapter extends BaseAdapter {

    private Context context;
    private JSONArray schoolPlannerClassSectionListArray;
    private String indicator;
    ArrayList<String> myList;

    public SchoolPlannerClassSectionListEditAdapter(Context context, JSONArray schoolPlannerClassSectionListArray, String indicator,ArrayList<String> myList) {
        this.context = context;
        this.schoolPlannerClassSectionListArray = schoolPlannerClassSectionListArray;
        this.indicator = indicator;
        this.myList = myList;
    }



    @Override
    public int getCount() {
        return schoolPlannerClassSectionListArray.length();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final Holder holder;
        if(view == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.class_listview_layout_planner, null);

            holder.className = (TextView) view.findViewById(R.id.txt_studentNameTeacher);
            holder.selectionImage = (ImageView) view.findViewById(R.id.imageView_studentAttendance);
            view.setTag(holder);


        }

        else
        {
            holder = (Holder) view.getTag();
        }

        try {
            holder.className.setText(schoolPlannerClassSectionListArray.getJSONObject(i).getString("class_name")+"-"+schoolPlannerClassSectionListArray.getJSONObject(i).getString("section_name"));
            if(indicator.matches("true"))
            {
                holder.selectionImage.setImageResource(R.drawable.greencircletick);
            }

            else
            {
                holder.selectionImage.setImageResource(R.drawable.greycircletick);
            }

            /*int f=0;

            if(f==0) {

                if (schoolPlannerClassSectionListArray.getJSONObject(i).getString("class_section_id").matches(myList.get(i))) {
                    holder.selectionImage.setImageResource(R.drawable.greencircletick);
                    schoolPlannerClassSectionListArray.getJSONObject(i).put("isAdded", "A");
                    f=1;
                } else {
                    holder.selectionImage.setImageResource(R.drawable.crossredcircle);
                    schoolPlannerClassSectionListArray.getJSONObject(i).put("isAdded", "N");
                    f=1;
                }
            }

            else
            {

            }*/

            if(schoolPlannerClassSectionListArray.getJSONObject(i).getString("isAdded").matches("A"))
            {
                holder.selectionImage.setImageResource(R.drawable.greencircletick);
            }

            else
            {
                holder.selectionImage.setImageResource(R.drawable.greycircletick);
            }

            holder.selectionImage.setTag(new Integer(i));

            holder.selectionImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        if(schoolPlannerClassSectionListArray.getJSONObject((Integer) view.getTag()).getString("isAdded").matches("A"))
                        {
                            holder.selectionImage.setImageResource(R.drawable.greycircletick);
                            schoolPlannerClassSectionListArray.getJSONObject((Integer) view.getTag()).put("isAdded","N");
                            Cache.Entry e = new Cache.Entry();
                            e.data = schoolPlannerClassSectionListArray.toString().getBytes();
                            VolleySingleton.getInstance(context).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CLASS_SECTION_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId,e);

                        }

                        else
                        {
                            holder.selectionImage.setImageResource(R.drawable.greencircletick);
                            schoolPlannerClassSectionListArray.getJSONObject((Integer) view.getTag()).put("isAdded","A");
                            Cache.Entry e = new Cache.Entry();
                            e.data = schoolPlannerClassSectionListArray.toString().getBytes();
                            VolleySingleton.getInstance(context).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CLASS_SECTION_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId,e);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    static class Holder
    {
        TextView className;
        ImageView selectionImage;
    }
}
