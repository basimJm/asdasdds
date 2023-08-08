package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Schoofi on 04-10-2019.
 */

public class UserBookHistoryAdapter extends BaseAdapter {

    private Context context;
    private JSONArray userBookHistoryArray;
    String date1,date2,date3,date4;
    Date date5,date6;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public UserBookHistoryAdapter(Context context, JSONArray userBookHistoryArray) {
        this.context = context;
        this.userBookHistoryArray = userBookHistoryArray;
    }

    @Override
    public int getCount() {
        return userBookHistoryArray.length();
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
            convertView = layoutInflater.inflate(R.layout.user_library_book_history_listview, null);
            holder.title = (TextView) convertView.findViewById(R.id.text_book_name);
            holder.author = (TextView) convertView.findViewById(R.id.text_author_name);
            holder.publisher = (TextView) convertView.findViewById(R.id.text_publisher_name);
            holder.dueDate = (TextView) convertView.findViewById(R.id.text_due_date);
            holder.returnDate = (TextView) convertView.findViewById(R.id.text_return_date);

            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }





        try {
            date1 = userBookHistoryArray.getJSONObject(position).getString("due_date");
            date2 = userBookHistoryArray.getJSONObject(position).getString("return_date");

            try {
                date5 = formatter.parse(date1);
                date6 = formatter.parse(date2);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            date3 = formatter1.format(date5);
            date4 = formatter1.format(date6);
            holder.title.setText("Title: "+userBookHistoryArray.getJSONObject(position).getString("title"));
            holder.author.setText("Author: "+userBookHistoryArray.getJSONObject(position).getString("author"));
            holder.publisher.setText("Publisher: "+userBookHistoryArray.getJSONObject(position).getString("publisher"));
            holder.dueDate.setText("Due Date: "+date3);
            if(userBookHistoryArray.getJSONObject(position).getString("return_date").matches("0000-00-00") || userBookHistoryArray.getJSONObject(position).getString("return_date").matches("") || userBookHistoryArray.getJSONObject(position).getString("return_date").matches("null"))
            {
                holder.returnDate.setVisibility(View.GONE);
            }
            else
            {
                holder.returnDate.setText("Return Date: "+date4);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        private TextView title,author,publisher,dueDate,returnDate;
    }
}
