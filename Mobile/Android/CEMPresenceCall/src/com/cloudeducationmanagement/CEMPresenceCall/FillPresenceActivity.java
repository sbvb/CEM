package com.cloudeducationmanagement.CEMPresenceCall;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FillPresenceActivity extends Activity implements OnItemClickListener{

	private String username = "";
	private String sessionId = "";
	private String ip = "";
	private ArrayList<HashMap<String,String>> list;
	MyPresenceListAdapter adapter;
	ListView lview;
	private int[] checked_positions;
	private String mCourseName = "";
	private String mClassName = "";
	private String mClassId = "";
	private String mCcdtId = "";
	private String mDate = "";
	private String mBeginTime = "";
	private String mEndTime = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fillpresence);
				
		GlobalApp gApp = (GlobalApp)getApplicationContext();
		username = gApp.GetUsername();
		sessionId = gApp.GetSessionId();
		ip = gApp.GetIp();
		
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		mCourseName = extras.getString("courseName");
		mClassName = extras.getString("className");
		mClassId = extras.getString("classId");
		mCcdtId = extras.getString("ccdtId");
		mDate = extras.getString("date");
		mBeginTime = extras.getString("beginTime");
		mEndTime = extras.getString("endTime");
		
			
		TextView txtTitle = (TextView)this.findViewById(R.id.filpresTxtTitle);
		txtTitle.setText("Course(\"" + mCourseName + "\"), Class(\"" + mClassName + "\"), Date(\"" + mDate + "\"), Horary(\"" + mBeginTime + "-" + mEndTime + "\")");
		lview = (ListView) this.findViewById(R.id.filpreslistView);
		
		
		list = new ArrayList<HashMap<String,String>>();
								 
		String [] lstStudents = WebservicesConnection.GetStudentsFromEnrollment(username, sessionId, ip, mClassId);		
		for(int i = 0; i < lstStudents.length; i++) {
			if (i == 0) {
				if (lstStudents[0].equals("false") || lstStudents[0].equals("session_expired")) {
					Toast.makeText(getApplicationContext(), "session expired", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(getApplicationContext(), CEMPresenceCallActivity.class);
					startActivity(intent);
				}
				else {
					String [] line = lstStudents[i].split(":");
					HashMap<String,String> temp = new HashMap<String,String>();
					temp.put("name", line[0]);
					temp.put("pid", line[1]);
					temp.put("isPresent", "false");
					
					String fid = WebservicesConnection.GetPhotoFromCandidate(username, sessionId, ip, line[1]);
					if (fid.equals("session_expired")) {
						Toast.makeText(getApplicationContext(), "session expired", Toast.LENGTH_LONG);
						Intent intent = new Intent(getApplicationContext(), CEMPresenceCallActivity.class);
						startActivity(intent);
					} else if (fid.equals("false")) {
						Toast.makeText(getApplicationContext(), "error getting image id", Toast.LENGTH_SHORT);
						temp.put("imgDataEnc", "false");
					} else {						
						String [] photoProperties = fid.split(":");
						
						if (photoProperties[0].equals("false")) {
							temp.put("imgDataEnc", "false");
						} else {
							String imgDataEnc = WebservicesConnection.DownloadFileFromUser(username, sessionId, ip, line[1], photoProperties[1]);
							if (imgDataEnc.equals("session_expired")) {
								Toast.makeText(getApplicationContext(), "session expired", Toast.LENGTH_LONG);
								Intent intent = new Intent(getApplicationContext(), CEMPresenceCallActivity.class);
								startActivity(intent);
							} else if (fid.equals("false")) {
								Toast.makeText(getApplicationContext(), "error downloading image", Toast.LENGTH_SHORT);
								temp.put("imgDataEnc", "false");
							} else {
								temp.put("imgDataEnc", imgDataEnc);							
							}
						}
					}
					
					list.add(temp);
				}
			}
			else {
				String [] line = lstStudents[i].split(":");
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put("name", line[0]);
				temp.put("pid", line[1]);
				temp.put("isPresent", "false");
				temp.put("imgDataEnc", "false");
				
				String fid = WebservicesConnection.GetPhotoFromCandidate(username, sessionId, ip, line[1]);
				if (fid.equals("session_expired")) {
					Toast.makeText(getApplicationContext(), "session expired", Toast.LENGTH_LONG);
					Intent intent = new Intent(getApplicationContext(), CEMPresenceCallActivity.class);
					startActivity(intent);
				} else if (fid.equals("false")) {
					Toast.makeText(getApplicationContext(), "error getting image id", Toast.LENGTH_SHORT);
					temp.put("imgDataEnc", "false");
				} else {
					String [] photoProperties = fid.split(":");

					if (photoProperties[0].equals("false")) {
						temp.put("imgDataEnc", "false");
					} else {
						String imgDataEnc = WebservicesConnection.DownloadFileFromUser(username, sessionId, ip, line[1], photoProperties[1]);
						if (imgDataEnc.equals("session_expired")) {
							Toast.makeText(getApplicationContext(), "session expired", Toast.LENGTH_LONG);
							Intent intent = new Intent(getApplicationContext(), CEMPresenceCallActivity.class);
							startActivity(intent);
						} else if (fid.equals("false")) {
							Toast.makeText(getApplicationContext(), "error downloading image", Toast.LENGTH_SHORT);
							temp.put("imgDataEnc", "false");
						} else {
							temp.put("imgDataEnc", imgDataEnc);							
						}
					}
				}
				
				list.add(temp);
			}
		}
		
		
		/*
		String line = "";
        for(int i = 0; i < 9; i++) {
        	if (i == 0)
        		line = "line " + (i+1);
        	else
        		line += "&line " + (i+1);
        }
        String []str = line.split("&");
        */
        
        lview.setAdapter(new MyFillPresenceListAdapter(this, list,0));
        lview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        lview.setOnItemClickListener(this);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menupresence, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.menuPresenceSend:
	        send();
	        return true;
	    case R.id.menuPresenceAbout:
	        openAbout();
	        return true;
	    case R.id.menuPresenceLogout:
	    	logout();	    	
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void send() {		
		SparseBooleanArray sp = lview.getCheckedItemPositions();
		
		String line = "";
		for(int i = 0; i < sp.size(); i++) {
			if (sp.get(sp.keyAt(i))) {
				HashMap<String,String> map = list.get(i);
					if (line.equals(""))
						line = map.get("pid");
					else
						line += ":" + map.get("pid");
			}
		}
		
				
		//Toast.makeText(getApplicationContext(), line, Toast.LENGTH_LONG).show();
		
		
		String ret = WebservicesConnection.AnswerPresence(username, sessionId, ip, mCcdtId, line);
		if (ret.equals("true")) {
			Toast.makeText(getApplicationContext(), "presence made successful", Toast.LENGTH_LONG).show();
			Intent i = new Intent(this,ChooseProgCourseActivity.class);
			startActivity(i);
		} else if (ret.equals("session_expired")){
			Toast.makeText(getApplicationContext(), "session expired", Toast.LENGTH_LONG).show();
			Intent i = new Intent(this,CEMPresenceCallActivity.class);
			startActivity(i);
		} else { // an error occurs
			Toast.makeText(getApplicationContext(), "presence couldn't be made", Toast.LENGTH_LONG).show();
			Intent i = new Intent(this,ChooseProgCourseActivity.class);
			startActivity(i);
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
		//Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_SHORT).show();
		
		/*
		HashMap<String,String> n = (HashMap<String, String>) adapter.getItem(position);
		String itemIsPresent = n.get("isPresent");
		CheckedTextView chkBxPresence = (CheckedTextView)arg1.findViewById(R.id.filprerowLblName);
		
				
		if (itemIsPresent.equals("false")) {
			n.put("isPresent", "true");
			chkBxPresence.setChecked(true);
		} else {
			n.put("isPresent", "false");
			chkBxPresence.setChecked(false);
		}
		
		itemIsPresent = n.get("isPresent");
		String itemId = n.get("pid");
		
		list.set(position, n);
		*/
		
		//String msg = "isPresent: " + itemIsPresent + ", pid: " + itemId;
		//String msg = "isPresent: " + itemIsPresent;
		//Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		
	}

	

}
