package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.utils.AssignmentMultiLevelVO;
import com.schoofi.utils.DiaryVO;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Schoofi on 28-02-2017.
 */

public class AssignmentMultiLevelListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<DiaryVO> diaryVOs;

    public AssignmentMultiLevelListAdapter(Context context, ArrayList<DiaryVO> diaryVOs) {
        this.context = context;
        this.diaryVOs = diaryVOs;
    }

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    String date1,date2;
    Date date3;
    String attachment;
    ArrayList aList= new ArrayList();
    ArrayList aList1 = new ArrayList();




    @Override
    public int getGroupCount() {
        return diaryVOs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<AssignmentMultiLevelVO> assignmentMultiLevelVOs = diaryVOs.get(groupPosition).getItems1();
        return assignmentMultiLevelVOs.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return diaryVOs.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<AssignmentMultiLevelVO>  assignmentMultiLevelVOs = diaryVOs.get(groupPosition).getItems1();

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
        final Holder holder;
        DiaryVO diaryVO = (DiaryVO) getGroup(groupPosition);
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.diary_homescreen_parent_layout, parent, false);
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
        holder.assignmentDate.setText(date2);




        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Holder holder1;
        final AssignmentMultiLevelVO assignmentMultiLevelVO = (AssignmentMultiLevelVO) getChild(groupPosition,childPosition);
        if (convertView == null) {
            holder1 = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.assignment_new_layout, parent, false);
            holder1.title = (TextView) convertView.findViewById(R.id.text_title);
            holder1.subjectName = (TextView) convertView.findViewById(R.id.text_subject_name);
            holder1.teacherName = (TextView) convertView.findViewById(R.id.text_teacher_name);
            holder1.iconAttachment = (ImageView) convertView.findViewById(R.id.icon_attach);
            convertView.setTag(holder1);
        }

        else
        {
            holder1 = (Holder) convertView.getTag();
        }

        holder1.title.setText(assignmentMultiLevelVO.getTitle());
        if(assignmentMultiLevelVO.getOptionalSubject().matches("") || assignmentMultiLevelVO.getOptionalSubject().matches("null"))
        {
            holder1.subjectName.setText(assignmentMultiLevelVO.getSubjectName());
        }

        else
        {
            holder1.subjectName.setText(assignmentMultiLevelVO.getSubjectName()+"("+assignmentMultiLevelVO.getOptionalSubject()+")");
        }

        holder1.teacherName.setText(assignmentMultiLevelVO.getTeacherName());

        if(assignmentMultiLevelVO.getTeacherfile().matches("") || assignmentMultiLevelVO.getTeacherfile().matches("null"))
        {
            holder1.iconAttachment.setVisibility(View.INVISIBLE);
        }

        else
        {
            holder1.iconAttachment.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class Holder
    {
       private TextView assignmentDate,title,subjectName,teacherName;
        private ImageView iconAttachment;

    }
}


