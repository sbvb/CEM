package com.cloudeducationmanagement.CEMPresenceCall;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CEMPresenceCallActivity extends Activity {
	EditText txtLogin;
	EditText txtPass;
	Button btnLogin;
	GlobalApp gApp = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        gApp = (GlobalApp)getApplicationContext();
                
        txtLogin = (EditText)this.findViewById(R.id.txLogin);
        txtPass = (EditText)this.findViewById(R.id.txtPass);
        
        btnLogin = (Button)this.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener(){

        	public void onClick(View v){
        		        		        		
        		//gl.SetName(txtName.getText().toString());
        		//txtName.setText(gl.GetName());
        		
        		//Intent second = new Intent(getApplicationContext(), SecondActivity.class);
        		//startActivity(second);
        		
        		login();
        		txtLogin.setText("");
        		txtPass.setText("");
        	}
        });        
    }
    
    private String GetIpAddress() {
    	try { 
             for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) { 
                 NetworkInterface intf = en.nextElement(); 
                 for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) { 
                     InetAddress inetAddress = enumIpAddr.nextElement(); 
                     if (!inetAddress.isLoopbackAddress()) { 
                         return inetAddress.getHostAddress().toString(); 
                     } 
                 } 
             } 
         } catch (SocketException ex) { 
        	 Logger.getLogger(CEMPresenceCallActivity.class.getName()).log(Level.SEVERE, null, ex);
         } 
         return null; 
    }
    
    public void login() {
    	String login = txtLogin.getText().toString();
    	String pass = txtPass.getText().toString();
    	    	
    	String ip = GetIpAddress();
    	
    	//Toast.makeText(getApplicationContext(), "ip: " + ip, Toast.LENGTH_LONG).show();
    	
		String status = WebservicesConnection.Login(login, pass, ip);
		if (status.equals("false")) {
			Toast.makeText(getApplicationContext(), "login/password invalid", Toast.LENGTH_LONG).show();
		} else if (status.equals("no_conection")){
			Toast.makeText(getApplicationContext(), "no connection found", Toast.LENGTH_LONG).show();
		} else {		
			if (WebservicesConnection.isTutor(login, status, ip)) {
			
				Toast.makeText(getApplicationContext(), "connected successful", Toast.LENGTH_LONG).show();
				
				gApp.SetUsername(login);
				gApp.SetSessionId(status);
				gApp.SetIp(ip);
				
				Intent i = new Intent(getApplicationContext(), ChooseProgCourseActivity.class);
				startActivity(i);
			} else {
				Toast.makeText(getApplicationContext(), "User must be a tutor", Toast.LENGTH_LONG).show();
				WebservicesConnection.Logout(login, status, ip);
			}
		}
	
    }
}