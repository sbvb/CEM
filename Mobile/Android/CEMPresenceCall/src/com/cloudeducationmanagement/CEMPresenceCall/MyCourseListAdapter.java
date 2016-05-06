package com.cloudeducationmanagement.CEMPresenceCall;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class MyCourseListAdapter extends BaseAdapter {
	
	public ArrayList<HashMap<String,String>> list;
	Activity activity;

	 public MyCourseListAdapter(Activity activity, ArrayList<HashMap<String,String>> list) {
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
	
	private class ViewHolder {
	       TextView txtName;
	       TextView txtId;
	  }

	//@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		LayoutInflater inflater =  activity.getLayoutInflater();
 
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.chooseprogcourse, null);
			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.lblChooseCourse);			 
			holder.txtId = (TextView) convertView.findViewById(R.id.lblId);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, String> map = list.get(position);
	 	holder.txtName.setText(map.get("name"));
	 	holder.txtId.setText(map.get("id"));
	 	
	 		 
	 	return convertView;

	}

}
