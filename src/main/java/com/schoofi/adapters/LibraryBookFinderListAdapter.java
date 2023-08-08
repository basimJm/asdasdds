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

/**
 * Created by Schoofi on 04-10-2019.
 */

public class LibraryBookFinderListAdapter extends BaseAdapter {


    private Context context;
    private JSONArray libraryBookFinderArray;

    public LibraryBookFinderListAdapter(Context context, JSONArray libraryBookFinderArray) {
        this.context = context;
        this.libraryBookFinderArray = libraryBookFinderArray;
    }

    @Override
    public int getCount() {
        return libraryBookFinderArray.length();
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
            convertView = layoutInflater.inflate(R.layout.library_book_finder_listview, null);
            holder.title = (TextView) convertView.findViewById(R.id.text_book_name);
            holder.author = (TextView) convertView.findViewById(R.id.text_author_name);
            holder.publisher = (TextView) convertView.findViewById(R.id.text_publisher_name);
            holder.avability = (TextView) convertView.findViewById(R.id.text_avability);

            convertView.setTag(holder);
        }

        else
        {
            holder = (Holder) convertView.getTag();
        }

        try {
            holder.title.setText("Title: "+libraryBookFinderArray.getJSONObject(position).getString("title"));
            holder.author.setText("Author: "+libraryBookFinderArray.getJSONObject(position).getString("author"));
            holder.publisher.setText("Publisher: "+libraryBookFinderArray.getJSONObject(position).getString("publisher"));
            holder.avability.setText("Availability: "+String.valueOf((Integer.parseInt(libraryBookFinderArray.getJSONObject(position).getString("quantity"))+(Integer.parseInt(libraryBookFinderArray.getJSONObject(position).getString("books_issued"))))));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    static class Holder
    {
        private TextView title,author,publisher,avability,volume;
    }
}
