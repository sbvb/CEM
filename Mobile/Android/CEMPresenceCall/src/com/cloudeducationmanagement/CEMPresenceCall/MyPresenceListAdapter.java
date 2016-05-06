package com.cloudeducationmanagement.CEMPresenceCall;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cloudeducationmanagement.CEMPresenceCall.Base64.InputStream;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyPresenceListAdapter extends SimpleCursorAdapter {
	public ArrayList<HashMap<String,String>> list;
	Activity activity;

	 public MyPresenceListAdapter(Activity activity, ArrayList<HashMap<String,String>> list) {
		 super(activity, 0, null, null, null);
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
	
	private class ViewHolderPresence {
	       ImageView imgViewAvatar;
	       CheckBox chkBoxIsPresent;
	       CheckedTextView txtName;	       
	  }

	//@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolderPresence holder;
		LayoutInflater inflater =  activity.getLayoutInflater();
		final HashMap<String, String> map = list.get(position);
 
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.fillpresencelist, null);
			holder = new ViewHolderPresence();
			holder.imgViewAvatar = (ImageView) convertView.findViewById(R.id.fiprelstimgAvatar);			 
			holder.chkBoxIsPresent = (CheckBox) convertView.findViewById(R.id.fiprelstchkbxPresent);
			holder.txtName = (CheckedTextView) convertView.findViewById(R.id.fiprelstTxtName);			
			convertView.setTag(holder);
			
			/*holder.chkBoxIsPresent.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					
					
					element.setSelected(buttonView.isChecked());
					if (buttonView.isChecked()) 
						map.put("isPresent","1");
					else
						map.put("isPresent","0");
					list.set(position,map);
					
				}
			}); 
			holder.chkBoxIsPresent.setTag(list.get(position));*/
		}
		else
		{
			holder = (ViewHolderPresence) convertView.getTag();
		}		
	 	//holder.imgViewAvatar.setText(map.get("imgAvatar"));
	 	//holder.chkBoxIsPresent.setChecked(Boolean.getBoolean(map.get("isPresent")));
	 	holder.txtName.setText(map.get("name"));
	 	
	 	String dataEnc = map.get("imgDataEnc");
	 	if ((!dataEnc.equals("false")) && (!dataEnc.equals("session_expired"))) { 
	 		Bitmap bmp = loadBitmap(dataEnc);
	 		holder.imgViewAvatar.setImageBitmap(bmp);
	 		
	 	}
	 		
	 	return convertView;
	}
	
	private static synchronized Bitmap loadBitmap(String dataEnc) {
	    Bitmap bitmap = null;
	    Bitmap sclBmp = null;
	   	    
	    byte[] data = null;
		try {
			data = Base64.decode(dataEnc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
    	//final byte[] data = dataStream.toByteArray();
		
		if (data != null) {
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        //options.inSampleSize = 1;
	        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
	        
	        sclBmp = bitmap.createScaledBitmap(bitmap, 50, 50, true);
	        
		}

	    return sclBmp;
	}


	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	
			
}
