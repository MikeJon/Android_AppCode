package com.jz.vdistack.update;

import com.jz.vdistack.update.downloader.WcFileDownloader;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

/*****
 * ���ع�����
 * @author asus
 *
 */
public class WcUpdateManage {
	private Context        context;
	private UpdateMessage  upMessage;
	public WcUpdateManage(Context context,UpdateMessage obj){
		this.context = context;
		this.upMessage  = obj;
		if(getVersionChange()){
			//startDownLoadServce();
		}
	}
	
	/*****
	 * �жϰ汾�Ƿ���Ҫ����
	 * @return
	 */
	public boolean getVersionChange(){
		double  versionCode =1.0;
		boolean bool = false;
		 
		try{
			PackageManager pm   = context.getPackageManager();
			PackageInfo    info = pm.getPackageInfo(context.getPackageName(), 0);
			versionCode = info.versionCode;
			 bool = upMessage.getVersion()>versionCode?true:false;
		}catch(NameNotFoundException e){
			e.printStackTrace();
		}
		return bool ;
	}
	/*****
	 * ��ʼ�������ط���
	 */
	private void startDownLoadServce(){
		Intent intent = new Intent(context,UpdateServce.class);
		intent.putExtra("URL", "");
		context.startService(intent);
		
	}
}
