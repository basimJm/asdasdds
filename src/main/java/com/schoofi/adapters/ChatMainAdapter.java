package com.schoofi.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.utils.ChatMainVO;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatMainAdapter  extends RecyclerView.Adapter<ChatMainAdapter.ViewHolder>  {

    private JSONArray mMovies;
    Context mContext;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<ChatMainVO> diaryVOs;
    String date1,date2,date5,date6;
    Date date3,date7;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String currentDate = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());


    public ChatMainAdapter(JSONArray movies, Context context, ArrayList<ChatMainVO> diaryVOs) {
        this.mMovies = movies;
        this. mContext = context;
        this.diaryVOs = diaryVOs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_chat_layout, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        date1 = diaryVOs.get(position).getDate();
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
            holder.movieType.setText("Today");
        }
        else {
            holder.movieType.setText(date2);
        }




                   // Log.d("pooo",diaryVOs.get(position).getItems().toString());


                    recyclerViewAdapter = new RecyclerViewAdapter(diaryVOs.get(position).getItems(), mContext);
                    LinearLayoutManager horizontalManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                    holder.horizontalRecyclerView.setLayoutManager(horizontalManager);
                    holder.horizontalRecyclerView.setAdapter(recyclerViewAdapter);






    }
    // }

    @Override
    public int getItemCount() {
        return diaryVOs.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position ;
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieType;
        RecyclerView horizontalRecyclerView;


        public ViewHolder(View itemView) {
            super(itemView);
            movieType = itemView.findViewById(R.id.movieTypeTextView);

            horizontalRecyclerView = itemView.findViewById(R.id.activityMainRecyclerViewHorizontal);
        }
    }
}
