package com.schoofi.adapters;

/**
 * Created by Schoofi on 19-04-2019.
 */

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.schoofi.activitiess.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ChairmanDashboardHorizontalListviewAdapter extends RecyclerView.Adapter<ChairmanDashboardHorizontalListviewAdapter.ViewHolder> {

    JSONArray chairmanDashBoardHorizontalListViewArray;
    Context context;

    public ChairmanDashboardHorizontalListviewAdapter(Context context, JSONArray chairmanDashBoardHorizontalListViewArray) {
        super();
        this.context = context;
        this.chairmanDashBoardHorizontalListViewArray = chairmanDashBoardHorizontalListViewArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.horizontal_listview_layout_chairman_dashboard, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        try {
            viewHolder.header.setText(chairmanDashBoardHorizontalListViewArray.getJSONObject(i).getString("type"));
            viewHolder.number.setText("Boys "+chairmanDashBoardHorizontalListViewArray.getJSONObject(i).getString("male"));
            viewHolder.number1.setText("Girls "+chairmanDashBoardHorizontalListViewArray.getJSONObject(i).getString("female"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return chairmanDashBoardHorizontalListViewArray.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView header,number,number1;


        public ViewHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.textView3);
            number = (TextView) itemView.findViewById(R.id.textView4);
            number1 = (TextView) itemView.findViewById(R.id.textView5);

        }


    }

}
