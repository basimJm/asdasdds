package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoofi.activitiess.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ResultPDFAdapter extends BaseAdapter {

    private Context context;
    private JSONArray resultPDFArray;

    public ResultPDFAdapter(Context context, JSONArray resultPDFArray) {
        this.context = context;
        this.resultPDFArray = resultPDFArray;
    }

    @Override
    public int getCount() {
        return resultPDFArray.length();
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
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.result_pdf_list, null);

            holder.title = (TextView) convertView.findViewById(R.id.text_poll_title);

            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {


            holder.title.setText(resultPDFArray.getJSONObject(position).getString("report_title")+"( "+resultPDFArray.getJSONObject(position).getString("session")+" )");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }
    static class Holder
    {
        TextView title;

    }
}
