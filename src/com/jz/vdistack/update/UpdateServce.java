package com.jz.vdistack.update;

import java.io.File;
import java.text.DecimalFormat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.freerdp.freerdpcore.presentation.JzVDIHomeActivity;
import com.jz.vdistack.R;
import com.jz.vdistack.network.WcNetWorkUtils;
import com.jz.vdistack.update.UpdateDialog.UpdateDialogOnclick;
import com.jz.vdistack.update.downloader.WcDownloadProgressListener;
import com.jz.vdistack.update.downloader.WcFileDownloader;

public class UpdateServce extends Service implements Runnable{
    
	 //����
    private int titleId = 0;
    //�ļ��洢
    private File updateFile = null;
    //֪ͨ��
    private NotificationManager updateNotificationManager = null;
    private Notification       updateNotification = null;
    //֪ͨ����תIntent
    private Intent             updateIntent = null;
    private PendingIntent      updatePendingIntent = null;
	private WcFileDownloader   wcFileDownloader;   //����
	private UpdateThread       updateThread=null;
	private WcUpdateManage     wcUpdateManage ;
	private boolean            isloader=false;
	private UpdateMessage      updateMessage;
	private String             path="http://180.153.152.42:3000/download/";
	private Handler            handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0://��ʾ�µİ汾�Ƿ���Ҫ����
				if(JzVDIHomeActivity.jzVDIHomeActivity !=null){
					UpdateDialog updateDialog = new UpdateDialog(JzVDIHomeActivity.jzVDIHomeActivity);
					updateDialog.setUpdateDialogOnclick(updateDialogOnclick);
					updateDialog.show();
				}
				break;
			case 1:
				double d = (1.0*msg.arg1)/wcFileDownloader.getFileSize();
				int n =(int)(100*d);
				updateNotification.contentView.setTextViewText(R.id.content_view_text1, n+"%"); 
				updateNotification.contentView.setProgressBar(R.id.content_view_progress, 100,n , false);  
				updateNotificationManager.notify(0,updateNotification);
				break;
			case 2:
				float size =(wcFileDownloader.getFileSize()*1.0f)/1024/1024;
				DecimalFormat fnum = new DecimalFormat("##0.00"); 
				String dd=fnum.format(size); 
				updateNotification.contentView.setTextViewText(R.id.txt_filesize,dd+"Mb"); 
				updateNotificationManager.notify(0,updateNotification);
				//Toast.makeText(getApplicationContext(), "����ʧ��", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				updateNotification.contentView.setTextViewText(R.id.txt_filesize,"����ʧ��"); 
				updateNotificationManager.notify(0,updateNotification);
				break;
			case 4:
				updateNotification.flags = Notification.FLAG_AUTO_CANCEL; 
				updateNotificationManager.notify(0,updateNotification);
				installApk();
				break;
			default:
				
				break;
			}
		}
		
	};
	
	private UpdateDialogOnclick updateDialogOnclick = new UpdateDialogOnclick() {
			
			@Override
			public boolean updateDialogOnclick(View v) {
				// TODO Auto-generated method stub
				if(v.getId()==R.id.sue){
					startDowload();
				}
				return false;
			}
		};
	private WcDownloadProgressListener   wdlplistener = new WcDownloadProgressListener() {
		
		@Override
		public void onDownloadSize(int downloadSize) {
			// TODO Auto-generated method stub
			Message message =handler.obtainMessage(1);
			message.arg1 =downloadSize;
			handler.sendMessage(message);
			
		}
	};
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		updateFile =new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/vdi");
		
		 
	}
	
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		
       
        return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
     
	private void startDowload(){
		this.updateNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		updateIntent = new Intent(this, JzVDIHomeActivity.class);
		this.updateNotification = new Notification();
		updatePendingIntent = PendingIntent.getActivity(this,0,updateIntent,0);
		updateNotification.icon  = R.drawable.icon_launcher;
		updateNotification.flags = Notification.FLAG_ONGOING_EVENT; 
		updateNotification.flags |= Notification.FLAG_INSISTENT;
		updateNotification.contentView = new RemoteViews(getPackageName(),R.layout.updatenotifcation);
		updateNotification.contentView.setTextViewText(R.id.content_view_text1, 0+"%");  
		updateNotification.contentView.setProgressBar(R.id.content_view_progress, 100, 0, false);  
        updateNotification.tickerText = "��ʼ����";
        updateNotification.contentIntent = updatePendingIntent;
      //  updateNotification.setLatestEventInfo(this,"wcApiDemo","0%",updatePendingIntent);
        //����֪ͨ
        updateNotificationManager.notify(0,updateNotification);
        new Thread(this).start();
	}
	
	
	
	
	
	
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		if(updateThread ==null&&!isloader){
			updateThread = new UpdateThread();
			updateThread.start();
		}
	}
	
	/******
	 * ��ȡ�汾��Ϣ
	 * @author asus
	 *
	 */
	private class UpdateThread extends Thread{

		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			String result =WcNetWorkUtils.getHttpClientString("http://180.153.152.42:3000/version");
		    updateMessage = new UpdateMessage(result);
			wcUpdateManage = new WcUpdateManage(getApplicationContext(),updateMessage);
			if(wcUpdateManage.getVersionChange()){
				Message message = handler.obtainMessage(0);
				handler.sendMessage(message);
			}
			updateThread = null;
		}
		
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int size =0 ;
		try {
			isloader = true;
			wcFileDownloader = new WcFileDownloader(getApplicationContext(),path+updateMessage.getApkname(),updateFile, 1);
			File apkfile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/vdi", updateMessage.getApkname());
			if (!apkfile.exists()) {
				wcFileDownloader.clearDate();
				wcFileDownloader = new WcFileDownloader(getApplicationContext(),path+updateMessage.getApkname(),updateFile, 1);
			}
			Message message =handler.obtainMessage(2);
			handler.sendMessage(message);
			size =wcFileDownloader.download(wdlplistener);
			if(size ==wcFileDownloader.getFileSize()&&size != 0){
				
				Message message2 =handler.obtainMessage(4);
				handler.sendMessage(message2);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//����ʧ��
			e.printStackTrace();
			Message message =handler.obtainMessage(3);
			handler.sendMessage(message);
			
		}
		
		isloader = false;
	}
	/****
	 * ��װ
	 */
	private void installApk() {
		File apkfile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/vdi", updateMessage.getApkname());
		if (!apkfile.exists()) {
			return;
		}
	 
		// ͨ��Intent��װAPK�ļ�
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		try {
		    startActivity(i);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
