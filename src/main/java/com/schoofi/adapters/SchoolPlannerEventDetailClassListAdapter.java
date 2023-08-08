package com.schoofi.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Schoofi on 27-03-2017.
 */

public class SchoolPlannerEventDetailClassListAdapter extends BaseAdapter {

    private Context context;
    ArrayList<String> myList;

    public SchoolPlannerEventDetailClassListAdapter(Context context, ArrayList<String> myList) {
        this.context = context;
        this.myList = myList;
    }

    @Override
    public int getCount() {
        return myList.size();
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
        {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_dialog_list_view, null);
            holder.className = (TextView) convertView.findViewById(R.id.text_dialogListView);

            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }


            holder.className.setText(myList.get(position));
            holder.className.setTextColor(R.color.black);
            holder.className.setGravity(Gravity.START);
            //holder.className.setTextAppearance();

        return convertView;
    }

    static class Holder
    {
        TextView className;
    }
}
