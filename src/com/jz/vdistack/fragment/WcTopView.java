package com.jz.vdistack.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class WcTopView extends FrameLayout {
	
	private boolean istouche = false;
	public WcTopView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		// TODO Auto-generated method stub
//		if(istouche){
//			return true;
//		}else{
//			return super.onInterceptTouchEvent(ev);
//		}
//		
//	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(istouche){
			return true;
		}else{
			return super.onTouchEvent(event);
		}
		
	}

	public boolean isIstouche() {
		return istouche;
	}

	public void setIstouche(boolean istouche) {
		this.istouche = istouche;
	}
	
	
	
	
	

}
