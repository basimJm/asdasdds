package com.schoofi.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.utils.ChatMainVO;
import com.schoofi.utils.ChatVO;
import com.schoofi.utils.DiaryVO;
import com.schoofi.utils.Preferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StudentChatAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<ChatMainVO> diaryVOs;

    public StudentChatAdapter(Context context, ArrayList<ChatMainVO> diaryVOs) {
        this.context = context;
        this.diaryVOs = diaryVOs;
    }

    String date1,date2,date5,date6;
    Date date3,date7;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String currentDate = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());

    @Override
    public int getGroupCount() {
        return diaryVOs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ChatVO> assignmentMultiLevelVOs = diaryVOs.get(groupPosition).getItems();
        return assignmentMultiLevelVOs.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return diaryVOs.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ChatVO>  assignmentMultiLevelVOs = diaryVOs.get(groupPosition).getItems();

        return assignmentMultiLevelVOs.get(childPosition);
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

        Log.d("ppp", String.valueOf(diaryVOs.size()));

        final Holder holder;
        ChatMainVO diaryVO = (ChatMainVO) getGroup(groupPosition);
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.student_chat_parent_listview, parent, false);
            holder.assignmentDate = (TextView) convertView.findViewById(R.id.text_date_title);
            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }


        date1 = diaryVOs.get(groupPosition).getDate();
        try {
            date3 = formatter.parse(date1);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        date2 = formatter1.format(date3);
        if(date2.matches(currentDate))
        {
            holder.assignmentDate.setText("Today");
        }
        else {
            holder.assignmentDate.setText(date2);
        }

       // Log.d("ppp",date2+"("+currentDate+")");




        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ExpandableListView eLV = (ExpandableListView) parent;

        final Holder holder1;
        Preferences.getInstance().loadPreference(context.getApplicationContext());

        final ChatVO assignmentMultiLevelVO = (ChatVO) getChild(groupPosition,childPosition);
        if (convertView == null) {
            holder1 = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(assignmentMultiLevelVO.getUser_id().matches(Preferences.getInstance().userId)) {

                convertView = infalInflater.inflate(R.layout.right, parent, false);
            }
            else
            {
                convertView = infalInflater.inflate(R.layout.chat_left_textview, parent, false);
            }

            holder1.message = convertView.findViewById(R.id.msg);
            holder1.time = convertView.findViewById(R.id.msg1);

            convertView.setTag(holder1);
        }
        else
        {
            holder1 = (Holder) convertView.getTag();
        }

        holder1.message.setText(assignmentMultiLevelVO.getMessage());
        date5 = assignmentMultiLevelVO.getTime();
        try {
            date7 = formatter1.parse(date5);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mm a");
        date6 = formatter1.format(date7);
        holder1.time.setText(date6);

        //eLV.smoothScrollToPosition(diaryVOs.size());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class Holder
    {
      TextView assignmentDate,message,time;
    }
}
