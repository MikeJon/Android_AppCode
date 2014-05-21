 package com.jz.vdistack.update.downloader;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WcFileService {

	private Context context;
	private WcDBOpenHelper wcDBOpenHelper;
	
	public WcFileService(Context context){
		this.context = context;
		wcDBOpenHelper = new WcDBOpenHelper(context);
	}
	
	/*****
	 *  for each thread has downloaded file size 
	 * @param path
	 * @return
	 */
	public Map getData(String path){
		
		SQLiteDatabase sqLiteDatabase = wcDBOpenHelper.getReadableDatabase();
		String sql = "select threadid, downlength from filedownlog where downpath=?";
		Cursor cursor ;
		cursor = sqLiteDatabase.rawQuery(sql,new String[]{path});
		Map<Integer,Integer>   data = new HashMap();
		while(cursor.moveToNext()){
			data.put(cursor.getInt(0), cursor.getInt(1));
		}
		cursor.close();
		sqLiteDatabase.close();
		
		return data;
	}
	
	/******
	 * Sava each thread has downloaded file size
	 * @param path
	 * @param map
	 */
	public void save(String path,Map<Integer, Integer> map){
		
		SQLiteDatabase sqLiteDatabase = wcDBOpenHelper.getWritableDatabase();
		String sql="";
		sqLiteDatabase.beginTransaction();
		try {
	            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
	            	sqLiteDatabase.execSQL(
	                        "insert into filedownlog(downpath, threadid, downlength) values(?,?,?)",
	                        new Object[] { path, entry.getKey(), entry.getValue() });
	            }
	            sqLiteDatabase.setTransactionSuccessful();
	        } finally {
	        	sqLiteDatabase.endTransaction();
	        }
		sqLiteDatabase.close();
		 
		
		
	}
	
	public void updata(String path,Map<Integer, Integer> map){
		 SQLiteDatabase db = wcDBOpenHelper.getWritableDatabase();
	        db.beginTransaction();
	        try {
	        	for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
	                db.execSQL(
	                        "update filedownlog set downlength=? where downpath=? and threadid=?",
	                        new Object[] { entry.getValue(), path, entry.getKey() });
	            }
	            db.setTransactionSuccessful();
	        } finally {
	            db.endTransaction();
	        }
	        db.close();
	}
	
	 public void delete(String path) {
	        SQLiteDatabase db = wcDBOpenHelper.getWritableDatabase();
	        db.execSQL("delete from filedownlog where downpath=?",
	                new Object[] { path });
	        db.close();
	    }
}
