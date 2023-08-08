package com.schoofi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.schoofi.activitiess.R;

import org.json.JSONArray;

/**
 * Created by Schoofi on 06-02-2018.
 */

public class ParentVaccinationCommonListViewAdapter extends BaseAdapter {

    private Context context;
    private JSONArray parentVaccinationCommonListViewArray;

    public ParentVaccinationCommonListViewAdapter(Context context, JSONArray parentVaccinationCommonListViewArray) {
        this.context = context;
        this.parentVaccinationCommonListViewArray = parentVaccinationCommonListViewArray;
    }

    @Override
    public int getCount() {
        return parentVaccinationCommonListViewArray.length();
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
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listview_school_gallery, null);
        }
        return convertView;
    }

    static class Holder
    {
        private TextView name,status;
    }
}
