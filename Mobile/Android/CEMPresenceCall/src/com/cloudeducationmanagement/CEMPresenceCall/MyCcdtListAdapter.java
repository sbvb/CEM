package com.cloudeducationmanagement.CEMPresenceCall;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyCcdtListAdapter extends BaseAdapter {
	public ArrayList<HashMap<String,String>> list;
	Activity activity;

	 public MyCcdtListAdapter(Activity activity, ArrayList<HashMap<String,String>> list) {
		 super();
		 this.activity = activity;
		 this.list = list;		 
	 }


	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private class ViewHolderCcdt {
	       TextView txtCcdtId;
	       TextView txtDate;
	       TextView txtBeginTime;
	       TextView txtEndTime;
	  }

	//@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolderCcdt holder;
		LayoutInflater inflater =  activity.getLayoutInflater();
 
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.ccdtlist, null);
			holder = new ViewHolderCcdt();
			holder.txtCcdtId = (TextView) convertView.findViewById(R.id.lblCcdtlistccdtId);			 
			holder.txtDate = (TextView) convertView.findViewById(R.id.lblCcdtlistDate);
			holder.txtBeginTime = (TextView) convertView.findViewById(R.id.lblCcdtlistBeginTimeId);
			holder.txtEndTime = (TextView) convertView.findViewById(R.id.lblCcdtlistEndTimeId);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolderCcdt) convertView.getTag();
		}
		HashMap<String, String> map = list.get(position);
	 	holder.txtCcdtId.setText(map.get("ccdtId"));
	 	holder.txtDate.setText(map.get("date"));
	 	holder.txtBeginTime.setText(map.get("beginTime"));
	 	holder.txtEndTime.setText(map.get("endTime"));
	 		 		 
	 	return convertView;

	}
}
