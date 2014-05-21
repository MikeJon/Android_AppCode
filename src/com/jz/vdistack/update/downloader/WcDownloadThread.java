 package com.jz.vdistack.update.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class WcDownloadThread  extends Thread{
	
	
	private RandomAccessFile saveFile;
	private URL downUrl;
	private int block;
	private int threadId = -1;
	private int startPos;
	
	private int downLength;
	private boolean finish = false;
	private WcFileDownloader downloader;
	
	public WcDownloadThread(WcFileDownloader wcFileDownloader ,URL downUrl,RandomAccessFile randomAccessFile,
			                int block,int startPos,int threadId){
		
		downloader =wcFileDownloader;
		this.downUrl = downUrl;
		saveFile   = randomAccessFile;
		this.block = block;
		this.startPos = startPos;
		this.threadId = threadId;
		this.downLength = startPos - (block*(threadId-1));
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(downLength <block){
			try {
				HttpURLConnection http =(HttpURLConnection)downUrl.openConnection();
				http.setRequestMethod("GET");
				http.setRequestProperty("Accept-Language", "zh-CN");
				http.setRequestProperty("Referer", downUrl.toString());
				http.setRequestProperty("Charset", "UTF-8");
				http.setRequestProperty("Range", "bytes="+this.startPos+"-");
				http.setRequestProperty("Connection", "Keep-Alive");
				InputStream inStream = http.getInputStream();
				int max = 1024*1024;
				byte [] buffer = new byte[max];
				int offset = 0;
				while(downLength<block &&(offset = inStream.read(buffer,0,max)) != -1){
					saveFile.write(buffer,0,offset );
					downLength += offset;
					downloader.update(threadId, block*(threadId-1)+downLength);
					downloader.saveLogFile();
					downloader.append(offset);
					int spare = block -downLength;
					if(spare <max)max =spare;
				}
				saveFile.close();
				inStream.close();
				this.finish = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public boolean isFinish(){
		return finish;
	}
	
	public long getDownLength(){
		
		return downLength;
	}
	
	public void stopThread(){
		
	}

}
