package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.utils.DiaryFilterScreenLeftVO;

import java.util.ArrayList;

/**
 * Created by Schoofi on 31-01-2017.
 */

public class DiaryFilterScreenLeftAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<DiaryFilterScreenLeftVO> temparr;

    public DiaryFilterScreenLeftAdapter(Context context, ArrayList<DiaryFilterScreenLeftVO> temparr) {
        this.context = context;
        this.temparr = temparr;
    }



    @Override
    public int getCount() {
        return temparr.size();
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
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.diaryhomescreenlistviewleftlayout, null);
            holder.name = (TextView) convertView.findViewById(R.id.text_name);
            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        holder.name.setText(temparr.get(position).getListItemName());
        return convertView;
    }

    static class Holder
    {
        TextView name;
    }
}
