package com.freerdp.freerdpcore.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class JzManualBoolmarkPf {
	
	private Context context ;
	private SharedPreferences spf;
	
	public JzManualBoolmarkPf(Context context){
		this.context = context;
		spf = context.getSharedPreferences("VDI", Context.MODE_MULTI_PROCESS);
	}
	
	public boolean  getSdcard(){
		return spf.getBoolean("SDCARD_W", true);
	}
	
	public void setSdcard(boolean bool){
		 
		Editor e =spf.edit();
		e.putBoolean("SDCARD_W", bool);
		e.commit();
	}
}
