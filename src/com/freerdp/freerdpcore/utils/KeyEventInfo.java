package com.freerdp.freerdpcore.utils;
import android.util.Log;
import android.view.KeyEvent;

import com.freerdp.freerdpcore.utils.KeyboardMapper.KeyProcessingListener;

public class KeyEventInfo {
	
	private static KeyEventInfo keyEventInfo ;
	private boolean iscapslock = false;
	private boolean isshift;
    private KeyProcessingListener listener;
	public boolean isIsshift() {
		return isshift;
	}

	public void setIsshift(boolean isshift) {
		this.isshift = isshift;
	}
	
	public void setIsshift(KeyEvent event) {
		 
	}
	public void setIsshift(int code,boolean bool){
		switch (code) {
		case KeyboardMapper.VK_LSHIFT :
			Log.v("WC","setIsshift:"+code+bool);
			listener.processVirtualKey(code,bool);
			isshift = bool;
			break;

		default:
			break;
		}
	}
	
	private KeyEventInfo(KeyProcessingListener listener){
		this.listener = listener ;
	}
	
	public static KeyEventInfo getInstance(KeyProcessingListener listener){
		
		if(keyEventInfo == null){
			keyEventInfo = new KeyEventInfo(listener);
		}
		
		return keyEventInfo ;
	}
  
	 

	public boolean isIscapslock() {
		return iscapslock;
	}

	public void setIscapslock(boolean iscapslock) {
		this.iscapslock = iscapslock;
	}

}
