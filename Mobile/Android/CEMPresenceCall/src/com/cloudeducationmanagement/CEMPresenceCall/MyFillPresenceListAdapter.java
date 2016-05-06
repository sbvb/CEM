package com.cloudeducationmanagement.CEMPresenceCall;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

public class MyFillPresenceListAdapter extends BaseAdapter{
	
	private Context mContext;
	private LayoutInflater inflator;
	private CheckedTextView txt;
	private int position;
	//private String[] str;
	private ArrayList<HashMap<String,String>> list;
	
	public MyFillPresenceListAdapter(Context context,ArrayList<HashMap<String,String>> list, int i) {
		// TODO Auto-generated constructor stub
		this.mContext=context;
		this.inflator= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
		this.position=i;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final MainListHolder mHolder;

		View v = convertView;
		if (convertView == null)
		{
			mHolder = new MainListHolder();
			
			v = inflator.inflate(R.layout.fillpresencerow, null);
			mHolder.txtName=  (CheckedTextView) v.findViewById(R.id.filprerowLblName);
			mHolder.imgAvatar=  (ImageView) v.findViewById(R.id.filprerowImgAvatar);
		//	txt=(CheckedTextView) v.findViewById(R.id.user_name);
			v.setTag(mHolder);
		} else {
			mHolder = (MainListHolder) v.getTag();
		}
		mHolder.txtName.setText(list.get(position).get("name"));
		mHolder.txtName.setId(Integer.parseInt(list.get(position).get("pid")));
		
		String dataEnc = list.get(position).get("imgDataEnc");
		if ((!dataEnc.equals("false")) && (!dataEnc.equals("session_expired"))) {
			Bitmap bmp = setImage(dataEnc);
			if (bmp != null)
				mHolder.imgAvatar.setImageBitmap(bmp);
			else
				mHolder.imgAvatar.setImageResource(R.drawable.no_image_male);
		} else {
			mHolder.imgAvatar.setImageResource(R.drawable.no_image_male);
		}
		
		if(position==this.position)
		{
			mHolder.txtName.setChecked(true);
		}
		else
			mHolder.txtName.setChecked(false);
		return v;
	}
	
	private Bitmap setImage(String dataEnc) {
		Bitmap bitmap = null;
	    Bitmap sclBmp = null;
	   	    
	    byte[] data = null;
		try {
			data = Base64.decode(dataEnc);
			
			//final byte[] data = dataStream.toByteArray();
			
			if (data != null) {
		        BitmapFactory.Options options = new BitmapFactory.Options();
		        //options.inSampleSize = 1;
		        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
		        
		        sclBmp = bitmap.createScaledBitmap(bitmap, 50, 50, true);
		        		        
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		
		return sclBmp;
	}
	
	class MainListHolder 
	{
		private CheckedTextView txtName;
		private ImageView imgAvatar;
	}

}
