package com.okamipride.scanlanip;

import java.net.InetAddress;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListIpAdapter extends BaseAdapter {

	List<InetAddress> list = null;
	
	public ListIpAdapter (List<InetAddress> iplist) {
		list = iplist;
	}
	
	@Override
	public int getCount() {
		return (list == null) ? 0 : (list.size());
	}

	@Override
	public Object getItem(int position) {
		return (list == null) ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 if (convertView == null) {
	            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
	            convertView = inflater.inflate(R.layout.item_view, parent, false);
	     }
		 
		 TextView hostname = (TextView) convertView.findViewById(R.id.txtv_hostname);
		 TextView ip = (TextView) convertView.findViewById(R.id.txtv_ipaddr);
		 if (list != null) {
			 InetAddress addr = list.get(position);
			 if (addr != null) {
				 hostname.setText(addr.getHostName());
				 ip.setText(addr.getHostAddress());
			 } else {
				 hostname.setText("");
				 ip.setText("");
			 } 		
		 } else {
			 hostname.setText("");
			 ip.setText("");
		 }		 
		return convertView;
	}
}
