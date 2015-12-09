package com.kfirvr.vrcontrolc.ctl.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetUtils {
	
	public static String getSelfIP(Context context){
		String result = null;
		WifiManager wifimanage = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo = wifimanage.getConnectionInfo();
		if (wifiinfo == null) {
			return null;
		}
		int intIP = wifiinfo.getIpAddress();
		result = intToIp(intIP);
		return result;
	}
	
	private static String intToIp(int intIP) {
		String result = null;
		result = (intIP & 0xFF) + "." + ((intIP >> 8) & 0xFF) + "."
				+ ((intIP >> 16) & 0xFF) + "." + ((intIP >> 24) & 0xFF);
		return result;
	}

}
