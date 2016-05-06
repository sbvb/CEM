package com.cloudeducationmanagement.CEMPresenceCall;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {
	private CheckedTextView _checkbox;
	private ImageView _imageview;

	public CheckableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// find checked text view
		int childCount = getChildCount();
		for (int i = 0; i < childCount; ++i) {
			View v = getChildAt(i);
			if (v instanceof CheckedTextView) {
				_checkbox = (CheckedTextView) v;
			}
			
		}
	}
	public boolean isChecked()
	{
		return _checkbox != null ? _checkbox.isChecked() : false;
	}

	public void setChecked(boolean checked) {
		if (_checkbox != null) {
			_checkbox.setChecked(checked);
		}
	}

	public void toggle() {
		if (_checkbox != null) {
			_checkbox.toggle();
		}
	}
	
	public void setImage(String dataEnc) {
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
		        
		       // if (sclBmp != null)
		        	//_imageview.setImageBitmap(sclBmp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
	}
}
