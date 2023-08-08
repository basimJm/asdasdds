package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.utils.ChairmanDashBoardChildListViewVO;
import com.schoofi.utils.ChairmanDashboardListViewVO;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Schoofi on 02-01-2017.
 */

public class ChairmanDashboardListviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ChairmanDashBoardChildListViewVO> chairmanDashBoardChildListViewVOs;
    private ArrayList<ChairmanDashboardListViewVO> temparr;

    public ChairmanDashboardListviewAdapter(Context context, ArrayList<ChairmanDashBoardChildListViewVO> chairmanDashBoardChildListViewVOs, ArrayList<ChairmanDashboardListViewVO> temparr) {
        this.context = context;
        this.chairmanDashBoardChildListViewVOs = chairmanDashBoardChildListViewVOs;
        this.temparr = temparr;
    }

    @Override
    public int getCount() {
        return chairmanDashBoardChildListViewVOs.size();
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

        if(convertView == null) {

            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.chairman_dashboard_listview_layout, null);

            holder.examName = (TextView) convertView.findViewById(R.id.examName);
            holder.totalStudents = (TextView) convertView.findViewById(R.id.text_exam_name_count);
            holder.gradeA = (TextView) convertView.findViewById(R.id.text_exam_name_1_count);
            holder.gradeB = (TextView) convertView.findViewById(R.id.text_exam_name_2_count);
            holder.gradeC = (TextView) convertView.findViewById(R.id.text_exam_name_3_count);
            holder.gradeD = (TextView) convertView.findViewById(R.id.text_exam_name_4_count);
            holder.gradeE = (TextView) convertView.findViewById(R.id.text_exam_name_5_count);
            holder.gradeName1 = (TextView) convertView.findViewById(R.id.text_exam_name_1);
            holder.gradeName2 = (TextView) convertView.findViewById(R.id.text_exam_name_2);
            holder.gradeName3 = (TextView) convertView.findViewById(R.id.text_exam_name_3);
            holder.gradeName4 = (TextView) convertView.findViewById(R.id.text_exam_name_4);
            holder.gradeName5 = (TextView) convertView.findViewById(R.id.text_exam_name_5);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        holder.examName.setText(chairmanDashBoardChildListViewVOs.get(position).getExamName());
        holder.totalStudents.setText(chairmanDashBoardChildListViewVOs.get(position).getTotalExamStudents());
        holder.gradeA.setText(chairmanDashBoardChildListViewVOs.get(position).getExamGrade1());
        holder.gradeB.setText(chairmanDashBoardChildListViewVOs.get(position).getExamGrade2());
        holder.gradeC.setText(chairmanDashBoardChildListViewVOs.get(position).getExamGrade3());
        holder.gradeD.setText(chairmanDashBoardChildListViewVOs.get(position).getExamGrade4());
        holder.gradeE.setText(chairmanDashBoardChildListViewVOs.get(position).getExamGrade5());
        holder.gradeName1.setText(temparr.get(0).getExamName1());
        holder.gradeName2.setText(temparr.get(0).getExamName2());
        holder.gradeName3.setText(temparr.get(0).getExamName3());
        holder.gradeName4.setText(temparr.get(0).getExamName4());
        holder.gradeName5.setText(temparr.get(0).getExamName5());

        return convertView;
    }

    static class Holder
    {
        TextView examName,totalStudents,gradeA,gradeB,gradeC,gradeD,gradeE,gradeName1,gradeName2,gradeName3,gradeName4,gradeName5;
    }
}
