package com.schoofi.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;

/**
 * Created by Schoofi on 13-09-2016.
 */
public class ChairmanStudentResultClassListAdapter extends BaseAdapter {

    private Context context;
    private JSONArray chairmanStudentResultClassListArray;

    public ChairmanStudentResultClassListAdapter(Context context, JSONArray chairmanStudentResultClassListArray) {
        this.context = context;
        this.chairmanStudentResultClassListArray = chairmanStudentResultClassListArray;
    }

    @Override
    public int getCount() {
        return chairmanStudentResultClassListArray.length();
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
        return view;
    }

    static class Holder
    {
        TextView className;
    }
}
