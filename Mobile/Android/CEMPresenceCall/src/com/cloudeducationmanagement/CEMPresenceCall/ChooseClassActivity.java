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

public class ChooseClassActivity extends Activity implements OnItemClickListener {
	
	private String username = "";
	private String sessionId = "";
	private String ip = "";
	private ArrayList<HashMap<String,String>> list;
	MyCourseListAdapter adapter;
	private String mCourseName = "";
	private String mCourseId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooseclass);
		
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
			
		TextView txtTitle = (TextView)this.findViewById(R.id.lblChooseClassTitle);
		txtTitle.setText("Course(\"" + mCourseName + "\")");
		ListView lview = (ListView) this.findViewById(R.id.listviewClass);
		
        //populateList();
		
		list = new ArrayList<HashMap<String,String>>();
								 
		String [] lstClasses = WebservicesConnection.GetClassesAssociatedCourse(username, sessionId, ip, mCourseId);		
		for(int i = 0; i < lstClasses.length; i++) {
			if (i == 0) {
				if (lstClasses[0].equals("false") || lstClasses[0].equals("session_expired")) {
					Toast.makeText(getApplicationContext(), "session expired", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(getApplicationContext(), CEMPresenceCallActivity.class);
					startActivity(intent);
				}
				else {
					String [] line = lstClasses[i].split(":");
					HashMap<String,String> temp = new HashMap<String,String>();
					temp.put("name", line[0]);
					temp.put("id", line[1]);
					list.add(temp);
				}
			}
			else {
				String [] line = lstClasses[i].split(":");
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put("name", line[0]);
				temp.put("id", line[1]);
				list.add(temp);
			}
		}
				
        adapter = new MyCourseListAdapter(this, list);
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
		String itemName = n.get("name");
		String itemId = n.get("id");
		String scheduleName = "";
		String scheduleId = "";
				
		String [] lstSchedules = WebservicesConnection.GetScheduleFromClassCourse(username, sessionId, ip, mCourseId, itemId);		
		for(int i = 0; i < lstSchedules.length; i++) {
			if (i == 0) {
				if (lstSchedules[0].equals("false") || lstSchedules[0].equals("session_expired")) {
					Toast.makeText(getApplicationContext(), "session expired", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(getApplicationContext(), CEMPresenceCallActivity.class);
					startActivity(intent);
				}
				else {
					String [] line = lstSchedules[i].split(":");
					scheduleName = line[0];
					scheduleId = line[1];					
				}
			}
			else {
				String [] line = lstSchedules[i].split(":");
				scheduleName = line[0];
				scheduleId = line[1];
			}
		}
		
		//String msg = "name: " + itemName + ", id: " + itemId + ", schName: " + scheduleName + ", schId: " + scheduleId;
		String msg = "please waiting...";
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();		
				
		Intent intent = new Intent(getApplicationContext(), ChooseCcdtPresenceActivity.class);
		intent.putExtra("courseName", mCourseName);
		intent.putExtra("courseId", mCourseId);
		intent.putExtra("className", itemName);
		intent.putExtra("classId", itemId);
		intent.putExtra("scheduleName", scheduleName);
		intent.putExtra("scheduleId", scheduleId);
		startActivity(intent);
		
	}
	
}
