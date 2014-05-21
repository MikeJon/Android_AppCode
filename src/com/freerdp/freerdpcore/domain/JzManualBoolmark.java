package com.freerdp.freerdpcore.domain;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.freerdp.freerdpcore.domain.BookmarkBase.PerformanceFlags;
import com.freerdp.freerdpcore.domain.BookmarkBase.ScreenSettings;
import com.jz.vdistack.network.WcNetWorkUtils;

public class JzManualBoolmark {
	
	private ManualBookmark manualBookmark;
	private JzManualBoolmarkPf jmbpf;
	
	
	public JzManualBoolmark(String str,Context context) throws JSONException, Exception{
		jmbpf = new JzManualBoolmarkPf(context);
		setManualBookmark(str,context);
	}
	

	public ManualBookmark getManualBookmark() {
		return manualBookmark;
	}

	 
	
	
	private void setManualBookmark(String str,Context context) throws JSONException,Exception{
		JSONObject jsonObject = new JSONObject(str);
		manualBookmark = new ManualBookmark();
		manualBookmark.setHostname(jsonObject.getString("ip_ext"));
		manualBookmark.setPassword(jsonObject.getString("password"));
		manualBookmark.setUsername(jsonObject.getString("username"));
		try{
			Integer integer = new Integer(jsonObject.getString("port_ext"));
			manualBookmark.setPort(integer.intValue());
		}catch(Exception e){
			 
		}
		ScreenSettings screenSettings = new ScreenSettings();
		
		screenSettings.setColors(32);
		manualBookmark.setScreenSettings(screenSettings);
		
		PerformanceFlags  performanceFlags = new PerformanceFlags();
		if(WcNetWorkUtils.isHightBandwidthConnection(context)==0){
			setWifiManualBookmark(context);
//			performanceFlags.setWallpaper(true);
//			performanceFlags.setFontSmoothing(true);
//			performanceFlags.setRemoteFX(true);
//			performanceFlags.setMenuAnimations(true);
//			performanceFlags.setFullWindowDrag(true);
//			performanceFlags.setTheming(true);
//			performanceFlags.setDesktopComposition(true);
//			manualBookmark.setPerformanceFlags(performanceFlags);
//			manualBookmark.getAdvancedSettings().setRedirectSDCard(true);
		}else{
			set3GManualBookmark(context);
//			performanceFlags.setWallpaper(true);
//			performanceFlags.setFontSmoothing(true);
//			performanceFlags.setRemoteFX(true);
//			performanceFlags.setMenuAnimations(true);
//			performanceFlags.setFullWindowDrag(true);
//			performanceFlags.setTheming(true);
//			performanceFlags.setDesktopComposition(true);
//			manualBookmark.setPerformanceFlags(performanceFlags);
//			manualBookmark.getAdvancedSettings().setRedirectSDCard(true);
		}
		
		
		 
	}
	
	
	private void setWifiManualBookmark(Context context){
		manualBookmark.getAdvancedSettings().setRedirectSDCard(jmbpf.getSdcard());
	}
	
	private void set3GManualBookmark(Context context){
		
	}
	

}
