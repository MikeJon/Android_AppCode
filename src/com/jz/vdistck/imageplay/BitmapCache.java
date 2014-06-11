package com.jz.vdistck.imageplay;

import android.graphics.Bitmap;


import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapCache implements ImageCache{
	private LruCache<String, Bitmap> mCache;
    long    lonv = 0;
   
     
    private static BitmapCache bitmapCache ;
    public static BitmapCache getInstance(){
    	if(bitmapCache==null){
    		bitmapCache = new BitmapCache();
    	}
    	return bitmapCache;
    }
    
    
    
    
	private  BitmapCache() {
		final int maxSize = 5 * 1024 * 1024;
		mCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
            
			@Override
			protected Bitmap create(String key) {
				// TODO Auto-generated method stub
				return super.create(key);
			}

			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				// TODO Auto-generated method stub
				
				if(oldValue != null){
					oldValue.recycle();
				}
				super.entryRemoved(evicted, key, oldValue, newValue);
			}
			
			
		};
	}

	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bitmap =mCache.get(url);
		if(bitmap ==null){
		}
		return bitmap;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		Bitmap bitmap1 =mCache.get(url);
	 
		if(bitmap1 !=null&&!bitmap1.isRecycled()){
			bitmap1.recycle();
			
		}
		mCache.put(url, bitmap);
	}
}
