package com.schoofi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.utils.AssesmentQuestionNumberVO;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Schoofi on 17-12-2019.
 */

public class AssesmentQuestionNumberAdapter extends RecyclerView.Adapter<AssesmentQuestionNumberAdapter.ViewHolder> {

    private ArrayList<AssesmentQuestionNumberVO> temparr;
    Context context;
    int pos;

    public AssesmentQuestionNumberAdapter(Context context, ArrayList<AssesmentQuestionNumberVO> temparr,int pos) {
        super();
        this.context = context;
        this.temparr = temparr;
        this.pos = pos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.assesment_number_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {


            viewHolder.number.setText(temparr.get(i).getFieldName());
            if(pos==i)
            {
                viewHolder.number.setTextColor(Color.RED);
            }
            else
            {
                viewHolder.number.setTextColor(Color.BLACK);
            }




    }

    @Override
    public int getItemCount() {
        return temparr.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView number;


        public ViewHolder(View itemView) {
            super(itemView);

            number = (TextView) itemView.findViewById(R.id.number_layout);



        }
    }


    }

