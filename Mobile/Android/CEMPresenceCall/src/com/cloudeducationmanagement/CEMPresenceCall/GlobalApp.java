package com.cloudeducationmanagement.CEMPresenceCall;

import android.app.Application;

public class GlobalApp extends Application {

	private String mUsername = "";
	private String mSessionId = "";
	private String mIp = "";
	
	public GlobalApp() {
		// TODO Auto-generated constructor stub
		
	}

	public void SetUsername(String username) {
		mUsername = username;
	}
	
	public String GetUsername() {
		return mUsername;
	}

	public void SetSessionId(String sessionId) {
		mSessionId = sessionId;
	}
	
	public String GetSessionId() {
		return mSessionId;
	}
	
	public void SetIp(String ip) {
		mIp = ip;
	}
	
	public String GetIp() {
		return mIp;
	}
}
