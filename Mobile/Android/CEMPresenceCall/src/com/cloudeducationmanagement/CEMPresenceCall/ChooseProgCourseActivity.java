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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ChooseProgCourseActivity extends Activity implements OnItemClickListener {
	private String username = "";
	private String sessionId = "";
	private String ip = "";
	private ArrayList<HashMap<String,String>> list;
	MyCourseListAdapter adapter;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosecourse);
		
		GlobalApp gApp = (GlobalApp)getApplicationContext();
		username = gApp.GetUsername();
		sessionId = gApp.GetSessionId();
		ip = gApp.GetIp();
			
		ListView lview = (ListView) this.findViewById(R.id.listviewCourse);
		
        //populateList();
		
		list = new ArrayList<HashMap<String,String>>();		
		String [] lstCourses = WebservicesConnection.GetCoursesFromTutor(username, sessionId, ip);
		for(int i = 0; i < lstCourses.length; i++) {
			if (i == 0) {
				if (lstCourses[0].equals("false") || lstCourses[0].equals("session_expired")) {
					Toast.makeText(getApplicationContext(), "session expired", Toast.LENGTH_LONG).show();
					finish();
				}
				else {
					String [] line = lstCourses[i].split(":");
					HashMap<String,String> temp = new HashMap<String,String>();
					temp.put("name", line[0]);
					temp.put("id", line[1]);
					list.add(temp);
				}
			}
			else {
				String [] line = lstCourses[i].split(":");
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
		//String msg = "name: " + itemName + ", id: " + itemId;
		String msg = "please waiting...";
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();	
		
		Intent i = new Intent(getApplicationContext(), ChooseClassActivity.class);
		i.putExtra("courseName", itemName);
		i.putExtra("courseId", itemId);
		startActivity(i);
		
	}
}
