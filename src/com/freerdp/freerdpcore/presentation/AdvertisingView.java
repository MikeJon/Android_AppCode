package com.freerdp.freerdpcore.presentation;

 
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.jz.vdistack.network.WcNetWorkUtils;
import com.jz.vdistck.imageplay.BitmapCache;

 

public class AdvertisingView extends RelativeLayout  implements Runnable{
	  private ImageView imageView ;
	  private ImageLoader  mImageLoader;
	  private boolean   isrun =false;
	  private Vector<ImagePlayer> vector ;
	  private Object  lock = new Object();
	  private Thread thread ;
	  private Handler   handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try{
				mImageLoader.get(msg.obj.toString(), imageListener);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		  
	  };
	  
	 
//    private AnimationDrawable animationDrawable ;
//    private ProgressBar progressBar ;
	
	 
	
	
	public AdvertisingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(mImageLoader == null){
			RequestQueue mQueue = Volley.newRequestQueue(context);
			mImageLoader = new ImageLoader(mQueue,BitmapCache.getInstance());
		}
		imageView = new ImageView(context);
		imageView.setScaleType(ScaleType.FIT_XY);
		
		RelativeLayout.LayoutParams ill=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		addView(imageView,ill);
	
		
	}
	
	public void startImagePlay(){
		thread =new Thread(this);
		thread.start();
	}
	public boolean isrun(){
		return isrun ;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		vector =getImagePlayerList(WcNetWorkUtils.getHttpClientString("http://180.153.152.42:3000/imagelist/?mac="+WcNetWorkUtils.getMacInfo(getContext())));
		int i = 0;
		isrun = true;
		int time = 0;
		synchronized(lock){
		while(i<vector.size()&&isrun){
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message message = handler.obtainMessage(0);
			ImagePlayer ip = vector.get(i);
			message.obj=ip.url;
			time = ip.time;
			handler.sendMessage(message);
			i++;
			
		}
		 
		}
	}
	private ImageListener imageListener = new ImageListener() {
		
		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			 
		}
		
		
		
		@Override
		public void onResponse(ImageContainer arg0, boolean arg1) {
			 if(arg0.getBitmap() != null){
				 imageView.setImageBitmap(arg0.getBitmap());
			   }
	    }
	};
	
     
	/*******
	 * 停止播放图片
	 */
	public void stopPlay(){
		isrun = false;
		thread.interrupt();
	
	}
	
	public Vector<ImagePlayer> getImagePlayerList(String str){
		Vector<ImagePlayer> vector = new Vector<AdvertisingView.ImagePlayer>();
		try {
			JSONArray jsonArray = new JSONArray(str);
			int l = jsonArray.length();
			
			for(int i = 0 ; i <l;i++){
				JSONObject j = (JSONObject)jsonArray.get(i);
				ImagePlayer imagePlayer = new ImagePlayer();
				imagePlayer.setUrl("http://180.153.152.42:3000/"+j.getString("url"));
				imagePlayer.setTime(j.getString("time"));
				Log.v("WC","KKKLLLLLL:"+imagePlayer.url);
				vector.add(imagePlayer);
			}
			 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return vector;
	}
	
	 
	/******
	 * 
	 * @author asus
	 *
	 */
    public static class ImagePlayer{
    	private String url;    ////播放的路径
    	private int    order ; ///播放的循序
    	private int    time ;  ///播发的时间
    	
    	
    	
    	
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public int getOrder() {
			return order;
		}
		public void setOrder(int order) {
			this.order = order;
		}
		public int getTime() {
			return time;
		}
		public void setTime(int time) {
			this.time = time;
		}
    	
		public void setTime(String time){
			try{
				Integer integer = new Integer(time);
				int i = integer.intValue();
				this.time =integer.intValue()<1000?2000:i;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
    	
    }


	public Object getLock() {
		return lock;
	}
    
    
	
}
