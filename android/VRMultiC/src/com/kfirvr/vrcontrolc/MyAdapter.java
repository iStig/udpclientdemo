package com.kfirvr.vrcontrolc;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private LayoutInflater mInflater = null;
	private ArrayList<ConnectSource> mList = new ArrayList<ConnectSource>();

	public MyAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
	}
	
	public void setData(ArrayList<ConnectSource> list){
		mList = new ArrayList<ConnectSource>(list);
	}

	@Override
	public int getCount() {
		return mList.size();
	}
	
	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.vlist, null);
			holder.ip = (TextView) convertView.findViewById(R.id.ip);
			holder.port = (TextView) convertView.findViewById(R.id.port);
			holder.mac = (TextView) convertView.findViewById(R.id.mac);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.ip.setText(mList.get(position).ip);
		holder.port.setText(""+mList.get(position).port);
		holder.mac.setText(mList.get(position).mac);

		return convertView;
	}

	class ViewHolder {
		public TextView ip;
		public TextView port;
		public TextView mac;
	}

}