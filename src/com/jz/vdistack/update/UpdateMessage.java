package com.jz.vdistack.update;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class UpdateMessage {
	
	private double version = 1.0;
	private String apkname;
	

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}
	
	public UpdateMessage(String str){
		try {
			Log.v("WC","KKKKK:"+str);
			JSONObject jsonObject = new JSONObject(str);
			apkname =jsonObject.getString("filename");
			version =jsonObject.getInt("version");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String getApkname() {
		return apkname;
	}

	public void setApkname(String apkname) {
		this.apkname = apkname;
	}
}
