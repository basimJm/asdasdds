package com.schoofi.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.schoofi.activitiess.R;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.ChatMainVO;
import com.schoofi.utils.DiaryGroupClassVO;
import com.schoofi.utils.DiaryGroupStudentVO;
import com.schoofi.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TeacherGroupCreationExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<DiaryGroupClassVO> diaryVOs;
    public JSONArray AssignmentArray;

    public TeacherGroupCreationExpandableListAdapter(Context context, ArrayList<DiaryGroupClassVO> diaryVOs, JSONArray AssignmentArray) {
        this.context = context;
        this.diaryVOs = diaryVOs;
        this.AssignmentArray = AssignmentArray;
    }

    @Override
    public int getGroupCount() {
        return diaryVOs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<DiaryGroupStudentVO> assignmentMultiLevelVOs = diaryVOs.get(groupPosition).getItems();
        return assignmentMultiLevelVOs.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return diaryVOs.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<DiaryGroupStudentVO>  assignmentMultiLevelVOs = diaryVOs.get(groupPosition).getItems();

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

        final Holder holder;
        final DiaryGroupClassVO diaryVO = (DiaryGroupClassVO) getGroup(groupPosition);
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.diary_group_class_listview, parent, false);

            holder.className = convertView.findViewById(R.id.text_class_name);
            holder.imageClassCheck = convertView.findViewById(R.id.img_class_tick);

            convertView.setTag(holder);


        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.className.setText(diaryVO.getClassName()+"-"+diaryVO.getSectionName());
        if(diaryVO.getIsAdded().matches("1"))
        {
            holder.imageClassCheck.setImageResource(R.drawable.greencircletick);
        }
        else
        {
            holder.imageClassCheck.setImageResource(R.drawable.greycircletick);
        }


        holder.imageClassCheck.setTag(new Integer(groupPosition));

        holder.imageClassCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(diaryVO.getIsAdded().matches("1"))
                {
                    holder.imageClassCheck.setImageResource(R.drawable.greycircletick);
                    diaryVO.setIsAdded("0");
                }
                else
                {
                    holder.imageClassCheck.setImageResource(R.drawable.greencircletick);
                    diaryVO.setIsAdded("1");
                }
            }
        });








        return convertView;


    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Holder holder1;
        Preferences.getInstance().loadPreference(context.getApplicationContext());

        final DiaryGroupStudentVO assignmentMultiLevelVO = (DiaryGroupStudentVO) getChild(groupPosition,childPosition);
        if (convertView == null) {
            holder1 = new Holder();
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = infalInflater.inflate(R.layout.listview_teacher_student_attendance_details, parent, false);
                holder1.studentName = convertView.findViewById(R.id.txt_studentNameTeacher);
                holder1.studentRollNumber = convertView.findViewById(R.id.txt_studentId);
                holder1.studentImage = convertView.findViewById(R.id.imageView_studentImage);
                holder1.studentCheck = convertView.findViewById(R.id.imageView_studentAttendance);


            convertView.setTag(holder1);
        }
        else
        {
            holder1 = (Holder) convertView.getTag();
        }

        holder1.studentName.setText(assignmentMultiLevelVO.getStudentName());
        holder1.studentRollNumber.setText(assignmentMultiLevelVO.getStudentRoll());
        Glide.with(context).load(AppConstants.SERVER_URLS.IMAGE_URL+assignmentMultiLevelVO.getStudentPicture()).asBitmap().placeholder(R.drawable.person).error(R.drawable.person).into(new BitmapImageViewTarget(holder1.studentImage)
        {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder1.studentImage.setImageDrawable(circularBitmapDrawable);
            }
        });

        if(assignmentMultiLevelVO.getIsAdded().matches("1"))
        {
            holder1.studentCheck.setImageResource(R.drawable.greencircletick);
        }
        else
        {
            holder1.studentCheck.setImageResource(R.drawable.greycircletick);
        }

        holder1.studentCheck.setTag(new Integer(childPosition));

        holder1.studentCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(assignmentMultiLevelVO.getIsAdded().matches("1"))
                {
                    assignmentMultiLevelVO.setIsAdded("0");
                    holder1.studentCheck.setImageResource(R.drawable.greycircletick);
                    try {
                        AssignmentArray.getJSONObject(groupPosition).getJSONArray("bifurcation").getJSONObject(childPosition).put("isAdded","0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    assignmentMultiLevelVO.setIsAdded("1");
                    holder1.studentCheck.setImageResource(R.drawable.greencircletick);
                    try {
                        AssignmentArray.getJSONObject(groupPosition).getJSONArray("bifurcation").getJSONObject(childPosition).put("isAdded","1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class Holder
    {
        TextView className,studentName,studentRollNumber;
        ImageView imageClassCheck,studentCheck,studentImage;
    }
}
