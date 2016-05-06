package com.cloudeducationmanagement.CEMPresenceCall;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChooseCcdtPresenceActivity extends Activity implements OnItemClickListener {
	private String username = "";
	private String sessionId = "";
	private String ip = "";
	private ArrayList<HashMap<String,String>> list;
	MyCcdtListAdapter adapter;
	private String mCourseName = "";
	private String mCourseId = "";
	private String mClassName = "";
	private String mClassId = "";
	private String mScheduleName = "";
	private String mScheduleId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooseccdtpresence);
				
		GlobalApp gApp = (GlobalApp)getApplicationContext();
		username = gApp.GetUsername();
		sessionId = gApp.GetSessionId();
		ip = gApp.GetIp();
		
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		mCourseName = extras.getString("courseName");
		mCourseId = extras.getString("courseId");
		mClassName = extras.getString("className");
		mClassId = extras.getString("classId");
		mScheduleName = extras.getString("scheduleName");
		mScheduleId = extras.getString("scheduleId");
			
		TextView txtTitle = (TextView)this.findViewById(R.id.lblChooseCcdtPresenceTitle);
		txtTitle.setText("Course(\"" + mCourseName + "\"), Class(\"" + mClassName + "\"), Schedule(\"" + mScheduleName + "\")");
		ListView lview = (ListView) this.findViewById(R.id.listviewCcdtPresence);
		
		list = new ArrayList<HashMap<String,String>>();
								 
		String [] lstCcdt = WebservicesConnection.GetCcdtFromScheduleClassCourse(username, sessionId, ip, mCourseId, mClassId, mScheduleId);		
		for(int i = 0; i < lstCcdt.length; i++) {
			if (i == 0) {
				if (lstCcdt[0].equals("false") || lstCcdt[0].equals("session_expired")) {
					Toast.makeText(getApplicationContext(), "session expired", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(getApplicationContext(), CEMPresenceCallActivity.class);
					startActivity(intent);
				}
				else {
					String [] line = lstCcdt[i].split(";");
					HashMap<String,String> temp = new HashMap<String,String>();
					temp.put("ccdtId", line[0]);
					temp.put("date", line[1]);
					temp.put("beginTime", line[2]);
					temp.put("endTime", line[3]);
					list.add(temp);
				}
			}
			else {
				String [] line = lstCcdt[i].split(";");
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put("ccdtId", line[0]);
				temp.put("date", line[1]);
				temp.put("beginTime", line[2]);
				temp.put("endTime", line[3]);
				list.add(temp);
			}
		}
				
        adapter = new MyCcdtListAdapter(this, list);
        lview.setAdapter(adapter);
        
        lview.setOnItemClickListener(this);						
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.menuAbout:
	        openAbout();
	        return true;
	    case R.id.menuLogout:
	    	logout();	    	
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void openAbout() {
		Intent about = new Intent(getApplicationContext(), AboutActivity.class);
		startActivity(about);
	}
	
	private void logout() {		
        if (WebservicesConnection.Logout(username, sessionId, ip)) {
        	Toast.makeText(getApplicationContext(), "disconnected user " + username, Toast.LENGTH_LONG).show();
        	Intent i = new Intent(getApplicationContext(), CEMPresenceCallActivity.class);
    		startActivity(i);
        }
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		// TODO Auto-generated method stub
				
		HashMap<String,String> n = (HashMap<String, String>) adapter.getItem(position);
		String itemCcdtId = n.get("ccdtId");
		String itemDate = n.get("date");
		String itemBeginTime = n.get("beginTime");
		String itemEndTime = n.get("endTime");
				
		//String msg = "ccdtid: " + itemCcdtId + ", date: " + itemDate + ", beginTime: " + itemBeginTime + ", endTime: " + itemEndTime;
		String msg = "please waiting...";
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();		
		
		Intent i = new Intent(getApplicationContext(), FillPresenceActivity.class);
		i.putExtra("courseName", mCourseName);
		i.putExtra("className", mClassName);
		i.putExtra("classId", mClassId);
		i.putExtra("ccdtId", itemCcdtId);
		i.putExtra("date", itemDate);
		i.putExtra("beginTime", itemBeginTime);
		i.putExtra("endTime", itemEndTime);
		startActivity(i);
		
	}

}
