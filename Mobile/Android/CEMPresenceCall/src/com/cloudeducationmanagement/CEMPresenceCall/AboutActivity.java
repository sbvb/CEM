package com.cloudeducationmanagement.CEMPresenceCall;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		Button btnClose = (Button)findViewById(R.id.btnAboutClose);
		btnClose.setOnClickListener(new View.OnClickListener(){

        	public void onClick(View v){
        		        		        		
        		//gl.SetName(txtName.getText().toString());
        		//txtName.setText(gl.GetName());
        		
        		//Intent second = new Intent(getApplicationContext(), SecondActivity.class);
        		//startActivity(second);
        		
        		finish();
        	}
		});
	}	
}
