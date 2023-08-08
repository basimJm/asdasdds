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

public class StockSubTypeListAdapter extends BaseAdapter{
	
	private Context context;
	private JSONArray stockSubTypeListArray;

	public StockSubTypeListAdapter(Context context,JSONArray stockSubTypeListArray) {
		super();
		this.context = context;
		this.stockSubTypeListArray = stockSubTypeListArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return stockSubTypeListArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		Holder holder;
		if(convertView == null)
		{
			holder = new Holder();
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.stock_sub_type_list, null);
			holder.stockTypeName = (TextView) convertView.findViewById(R.id.text_stockSubTitle);
			holder.stockTypeQuantity = (TextView) convertView.findViewById(R.id.text_stockSubQuantity);
			convertView.setTag(holder);
		}	
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		try {
			holder.stockTypeName.setText(stockSubTypeListArray.getJSONObject(position).getString("item_name"));
			holder.stockTypeQuantity.setText(stockSubTypeListArray.getJSONObject(position).getString("quantity"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
	
		static class Holder
		{
			TextView stockTypeName,stockTypeQuantity;
		}

}
	
	
