 package com.jz.vdistack.update.downloader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.util.Log;

public  class WcFileDownloader {
	
	private Context  context;
	
	private WcFileService  fileService;
	private int downloadSize ;           //The size of downloaded
	private int fileSize;                //The original file size
	private WcDownloadThread [] threads; //Thread count
	private URL   url;
	private File  savefile;             //native file saved
	private File  logfile ;             //Download the file log
	
	/*The loacation of the last download cache each thread*/
	private Map<Integer ,Integer> data = new ConcurrentHashMap<Integer, Integer>();
	private int   block;       //The size of each thread to download
	private String downloadUrl;//
	
	
	/*******
	 * get Thread count
	 * @return
	 */
	public int getThreadSize(){
		
		return threads.length;
	}
	
	/*****
	 * get file size
	 * @return
	 */
	public int getFileSize(){
		
		return fileSize;
	}
	
	/******
	 * Accumulated has download size
	 * @param size
	 */
	
	protected synchronized void append(int size){
		downloadSize += size;
		
	}
	
	
	
	
	
	/*******
	 * 
	 * @param context
	 * @param dowString
	 * @param filesaveDir
	 * @param threadNum
	 */
	public WcFileDownloader(Context context,String urlString,File filesaveDir,int threadNum) throws RuntimeException{
		this.context = context;
		this.downloadUrl = urlString;
		this.fileService = new WcFileService(context);
		try {
		    url = new URL(urlString);
			
			if(!filesaveDir.exists()){
				filesaveDir.mkdirs();
			}
			this.threads = new WcDownloadThread[threadNum];
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			http.setConnectTimeout(1000*5);
			http.setRequestMethod("GET");
			http.setRequestProperty("Accept-Language", "zh-CN");
			http.setRequestProperty("Referer", urlString);
			http.setRequestProperty("Charset", "UTF-8");
			http.setRequestProperty("Connection","Keep-Alive");
			http.connect();
			if(http.getResponseCode() == 200){
				this.fileSize = http.getContentLength();
				if(this.fileSize < 0){
					throw new RuntimeException("UnKown file size");
				}
				String filename = getFileName(http);
				this.savefile = new File(filesaveDir,filename);
				Map<Integer,Integer> datalog = fileService.getData(urlString);
				if(datalog.size() > 0){
					for(Map.Entry<Integer, Integer> entry :datalog.entrySet()){
						data.put(entry.getKey(), entry.getValue());
					}
				}
				this.block = this.fileSize /this.threads.length+1;
				if(this.data.size() == this.threads.length){
					for (int i = 0; i < this.threads.length; i++) {
                        this.downloadSize += this.data.get(i + 1);
                    }
				}
			} else {
                throw new RuntimeException("server no response ");
            }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Can not connect download path");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/*****
	 * 开启线程调用
	 * @param listener
	 * @return
	 * @throws Exception
	 */
	public int download(WcDownloadProgressListener listener) throws Exception{
		try{
			if(this.data.size() != this.threads.length){
				this.data.clear();
				for(int i = 0 ; i<this.threads.length;i++){
					this.data.put(i+1,this.block*i);
				}
			}
			for(int i = 0; i <this.threads.length;i++){
				int downLength = this.data.get(i+1)-(this.block*i);
				if(downLength <this.block&&this.data.get(i+1)<this.fileSize){
					RandomAccessFile randout = new RandomAccessFile(savefile, "rw");
					if(this.fileSize> 0) randout.setLength(this.fileSize);
					randout.seek(this.data.get(i+1));
					this.threads[i]=new WcDownloadThread(this, this.url, randout, this.block,this.data.get(i+1),i+1);
					this.threads[i].setPriority(7);
					this.threads[i].start();
				}else {
					this.threads[i] =null;
				}
			}
			
			this.fileService.save(this.downloadUrl,this.data);
			boolean notfinish = true;
			while(notfinish){
				Thread.sleep(900);
				notfinish = false;
				for(int i = 0 ; i < threads.length ; i++){
					if(this.threads[i] != null && !this.threads[i].isFinish()){
						notfinish = true;
						if(this.threads[i].getDownLength() == -1){
							RandomAccessFile randout = new RandomAccessFile(savefile, "rw");
							if(this.fileSize> 0) randout.setLength(this.fileSize);
							randout.seek(this.data.get(i+1));
							this.threads[i]=new WcDownloadThread(this, this.url, randout, this.block,this.data.get(i+1),i+1);
							this.threads[i].setPriority(7);
							this.threads[i].start();
						}
					}
				}
			 
				if(listener != null) listener.onDownloadSize(this.downloadSize);
			}
		}catch (Exception e){
			e.printStackTrace();
			throw new Exception("download failed");
		}
		return this.downloadSize ;
	}
	
	/***
	 * sava the record file
	 */
	protected synchronized void saveLogFile(){
		this.fileService.updata(this.downloadUrl, this.data);
	}
	/*****
	 * the last download location update the specifield thread
	 * @param threadId
	 * @param pos
	 */
	protected void update(int threadId,int pos){
		this.data.put(threadId, pos);
	}
	 private String getFileName(HttpURLConnection http){
		 String filename = this.url.toString().substring(this.url.toString().lastIndexOf('/')+1);
		 if(filename == null || "".equals(filename.trim())){
			 for(int i = 0 ;;i++){
				 String mine = http.getHeaderField(i);
				 if(mine == null){
					 break;
				 }
				 if("content-disposition".equals(http.getHeaderFieldKey(i).toLowerCase())){
					 Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
					 if(m.find()){
						 return m.group();
					 }
				 }
			 }
			 filename = UUID.randomUUID()+".tmp";
		 }
		
		 return filename;
	 }
	
	
	
	/*********
	 * Get the http response header fields
	 * @param httpsURLConnection
	 * @return
	 */
	
	public static Map<String ,String> getHttpResponseHeader(HttpsURLConnection httpsURLConnection){
		Map<String,String> header = new LinkedHashMap<String, String>();
		for(int i = 0 ;;i++){
			String mine = httpsURLConnection.getHeaderField(i);
			if(mine == null){
				break;
			}
			header.put(httpsURLConnection.getHeaderFieldKey(i),mine);
		}
		return header;
	}
	
	
	/***********
	 * print http response header fields
	 * @param httpsURLConnection
	 */
	public static void printResponseHeader(HttpsURLConnection httpsURLConnection){
		Map<String, String> header = getHttpResponseHeader(httpsURLConnection);
		for(Map.Entry<String, String> entry : header.entrySet()){
			String key = entry.getKey() != null ?entry.getKey()+":":"";
		}
	}
	
	/*****
	 * 暂停下载
	 */
	
	public void stopDownLoad(){
		
	}

}
