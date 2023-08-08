package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.DiaryWallChildVO;
import com.schoofi.utils.DiaryWallVO;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DiaryWallScreenAdapter extends BaseExpandableListAdapter {

    private Context context;
    private JSONArray diaryWallScreenArray;
    private ArrayList<DiaryWallVO> diaryVOs;

    public DiaryWallScreenAdapter(Context context, JSONArray diaryWallScreenArray , ArrayList<DiaryWallVO> diaryVOs) {
        this.context = context;
        this.diaryWallScreenArray = diaryWallScreenArray;
        this.diaryVOs = diaryVOs;
    }

    @Override
    public int getGroupCount() {
        return diaryVOs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<DiaryWallChildVO> diarySubVOs = diaryVOs.get(groupPosition).getItems();
        return diarySubVOs.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return diaryVOs.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<DiaryWallChildVO>  diarySubVOs = diaryVOs.get(groupPosition).getItems();

        return diarySubVOs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ExpandableListView eLV = (ExpandableListView) parent;
        eLV.expandGroup(groupPosition);
        final Holder holder;
        DiaryWallVO diaryVO = (DiaryWallVO) getGroup(groupPosition);
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.diary_homescreen_parent_layout, parent, false);
            holder.dates = (TextView) convertView.findViewById(R.id.text_date_title);
            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }


        String str = diaryVOs.get(groupPosition).getType();
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
        holder.dates.setText(cap);

        //holder.dates.setText(diaryVOs.get(groupPosition).getType());




        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Holder holder1;
        final DiaryWallChildVO diarySubVO = (DiaryWallChildVO) getChild(groupPosition,childPosition);
        if (convertView == null) {
            holder1 = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.diary_wall_screen_listview_design, parent, false);


            holder1.decription = convertView.findViewById(R.id.text_description);
            holder1.dateTextView = convertView.findViewById(R.id.text_date);



            convertView.setTag(holder1);
        }

        else
        {
            holder1 = (Holder) convertView.getTag();
        }


            if (diarySubVO.getType().matches("message")) {

                holder1.decription.setText(diarySubVO.getName() + "-" + diarySubVO.getMsg());
            } else {

                holder1.decription.setText(diarySubVO.getName()+"-"+diarySubVO.getTitle());
            }


            try {
                String myDate = diarySubVO.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(myDate);
                long millis = date.getTime();

                String timeAgo = AppConstants.TimeAgo.getTimeAgo(millis);

                holder1.dateTextView.setText(timeAgo);

            } catch (ParseException e) {
                e.printStackTrace();
            }





        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /*@Override
    public int getCount() {
        return diaryWallScreenArray.length();
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
        if(convertView == null)
        {   holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.diary_wall_screen_listview_design, null);
            holder.title = (TextView) convertView.findViewById(R.id.text_type);
            holder.decription = convertView.findViewById(R.id.text_description);
            holder.dateTextView = convertView.findViewById(R.id.text_date);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            if(diaryWallScreenArray.getJSONObject(position).getString("type").matches("message"))
            {
                String str = diaryWallScreenArray.getJSONObject(position).getString("type");
                String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
                holder.title.setText(cap);
                holder.decription.setText(diaryWallScreenArray.getJSONObject(position).getString("name")+"-"+diaryWallScreenArray.getJSONObject(position).getString("msg"));
            }
            else
            {
                String str = diaryWallScreenArray.getJSONObject(position).getString("type");
                String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
                holder.title.setText(cap);
                holder.decription.setText(diaryWallScreenArray.getJSONObject(position).getString("name"));
            }


            try {
                String myDate = diaryWallScreenArray.getJSONObject(position).getString("date_time");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(myDate);
                long millis = date.getTime();

                String timeAgo = AppConstants.TimeAgo.getTimeAgo(millis);

                holder.dateTextView.setText(timeAgo);

            } catch (ParseException e) {
                e.printStackTrace();
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
*/
    static class Holder
    {
        private TextView title,decription,dateTextView,dates;
    }
}
