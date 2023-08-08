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
 * Created by Schoofi on 03-08-2016.
 */
public class AdminClassSectionListAdapter extends BaseAdapter {

    private Context context;
    private JSONArray adminClassSectionListArray;
    String className;

    public AdminClassSectionListAdapter(Context context, JSONArray adminClassSectionListArray, String className) {
        this.context = context;
        this.adminClassSectionListArray = adminClassSectionListArray;
        this.className = className;
    }

    @Override
    public int getCount() {
        return adminClassSectionListArray.length();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Holder holder;

        if (view == null) {
            holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.admin_class_listview, null);

            holder.adminClassSectionListTextView = (TextView) view.findViewById(R.id.text_admin_class);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        try {
            holder.adminClassSectionListTextView.setText("Class " + className + "-" + adminClassSectionListArray.getJSONObject(i).getString("section_name")+"("+Integer.parseInt(adminClassSectionListArray.getJSONObject(i).getString("total_stu"))+")");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    static class Holder
    {
        TextView adminClassSectionListTextView;
    }
}
