package com.jz.vdistack.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
/***
 * 判断网络类型
 * @author asus
 *
 */
public class WcNetWorkUtils {
	
	private static final int REQUEST_TIMEOUT = 20*1000; 
	private static final int SO_TIMEOUT = 20*1000;

	public static int isHightBandwidthConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return  isConnectionFast(info.getType(),info.getSubtype());
    }
	
	private static int isConnectionFast(int type, int subType){
        if (type == ConnectivityManager.TYPE_WIFI) {
            return 0;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
	            case TelephonyManager.NETWORK_TYPE_1xRTT:
	                return 1; // ~ 50-100 kbps
	            case TelephonyManager.NETWORK_TYPE_CDMA:
	                return 2; // ~ 14-64 kbps
	            case TelephonyManager.NETWORK_TYPE_EDGE:
	                return 3; // ~ 50-100 kbps
	            case TelephonyManager.NETWORK_TYPE_GPRS:
	                return 4; // ~ 100 kbps
	            case TelephonyManager.NETWORK_TYPE_EVDO_0:
	                return 5; // ~25 kbps 
	            case TelephonyManager.NETWORK_TYPE_LTE:
	                return 6; // ~ 400-1000 kbps
	            case TelephonyManager.NETWORK_TYPE_EVDO_A:
	                return 7; // ~ 600-1400 kbps
	            case TelephonyManager.NETWORK_TYPE_HSDPA:
	                return 8; // ~ 2-14 Mbps
	            case TelephonyManager.NETWORK_TYPE_HSPA:
	                return 9; // ~ 700-1700 kbps
	            case TelephonyManager.NETWORK_TYPE_HSUPA:
	                return 10; // ~ 1-23 Mbps
	            case TelephonyManager.NETWORK_TYPE_UMTS:
	                return 11; // ~ 400-7000 kbps
	            case TelephonyManager.NETWORK_TYPE_EHRPD:
	                return 12; // ~ 1-2 Mbps
	            case TelephonyManager.NETWORK_TYPE_EVDO_B:
	                return 13; // ~ 5 Mbps
	            case TelephonyManager.NETWORK_TYPE_HSPAP:
	                return 14; // ~ 10-20 Mbps
	            case TelephonyManager.NETWORK_TYPE_IDEN:
	                return 15; // ~ 10+ Mbps
	            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
	            default:
	                return -1;
            }
        } else {
            return -1;
        }
    }
	
	
	/*******
	 * 读取mac地址
	 */
	
	public static String getMacInfo(Context context){
		String macAddress = null, ip = null;
		WifiManager wifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info) {
		    macAddress = info.getMacAddress();
		     
		}
		return macAddress;
	}
	
	/****
	 * 
	 * @param context
	 * @return 判断网路连接情况
	 */
	public static boolean getNetWorkState(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager == null){
			return false;
		}
		
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if(networkInfo==null ||!networkInfo.isAvailable()){
			return false;
		}
		
		return true;
	}
	
	public static  String getHttpClientString(String path){  
	    BasicHttpParams httpParams = new BasicHttpParams();  
	    HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);  
	    HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);  
	    HttpClient client = new DefaultHttpClient(httpParams);
	    DefaultHttpClient httpClient = (DefaultHttpClient) client;
		HttpResponse httpResponse=null;
		String result="";
		try {
			httpResponse = httpClient.execute(new HttpGet(path));
			int res = httpResponse.getStatusLine().getStatusCode();
			if(res == 200){
				InputStream in =httpResponse.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in,"GBK"));
				StringBuilder sb = new StringBuilder();  
				String line =null;
				while ((line = reader.readLine()) != null) {   
					sb.append(line.trim());   
		            }   
				reader.close();
				in.close();
				result = sb.toString();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    return result;  
	}  
	
	
	public static String getResultPost(String uri, List <NameValuePair> params) {
		 String Result = null;
			try {
				if(uri==null){
					return "";
				}
		        HttpPost httpRequest = new HttpPost(uri);  
				BasicHttpParams httpParams = new BasicHttpParams();  
			    HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);  
			    HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);  
			    DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
			    httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				HttpResponse httpResponse = httpClient.execute(httpRequest);
				int res = httpResponse.getStatusLine().getStatusCode();
				if (res == 200) {
					 
					StringBuilder builder = new StringBuilder();
					BufferedReader bufferedReader2 = new BufferedReader(
							new InputStreamReader(httpResponse.getEntity()
									.getContent()));
					for (String s = bufferedReader2.readLine(); s != null; s = bufferedReader2
							.readLine()) {
						builder.append(s);
					}		
					Result = builder.toString();
					 
				}
				 
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				return "";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Result;
		}
}
