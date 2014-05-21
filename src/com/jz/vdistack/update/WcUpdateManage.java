package com.jz.vdistack.update;

import com.jz.vdistack.update.downloader.WcFileDownloader;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

/*****
 * 下载管理类
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
	 * 判断版本是否需要更新
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
	 * 开始启动下载服务
	 */
	private void startDownLoadServce(){
		Intent intent = new Intent(context,UpdateServce.class);
		intent.putExtra("URL", "");
		context.startService(intent);
		
	}
}
