package com.jz.vdistack.update;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
/***
 * ≈–∂œÕ¯¬Á¿‡–Õ
 * @author asus
 *
 */
public class WcNetWorkUtils {
	
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
}
