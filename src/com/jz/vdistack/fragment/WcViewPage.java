package com.jz.vdistack.fragment;

import android.R.bool;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class WcViewPage extends ViewPager{
    
	private boolean ismovew = true;
	public WcViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if(ismovew){
			return super.onInterceptTouchEvent(arg0);
		}else{
			return false;
		}
		
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if(ismovew){
			return super.onTouchEvent(arg0);
		}else{
			return false;
		}
	}

	public boolean isIsmovew() {
		return ismovew;
	}

	public void setIsmovew(boolean ismovew) {
		this.ismovew = ismovew;
	}
	
	

}
