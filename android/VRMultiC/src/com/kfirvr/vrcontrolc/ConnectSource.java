package com.kfirvr.vrcontrolc;

import com.kfirvr.vrcontrol.SLog;

public class ConnectSource {
	
	private final static long overdueTimeStamp = 30 * 1000l;
	
	public String ip;
	public int port;
	public String mac;
	public int connected = -1; // 0： 未连接 1：已连接
	public long timestamp;
	
	public ConnectSource(String ip,int port,String mac,long timestamp){
		this.ip = ip;
		this.port = port;
		this.mac = mac;
		this.timestamp = timestamp;
	}
	
	public boolean checkOverdue() {
		SLog.e("timestamp :: " + timestamp);
		if(System.currentTimeMillis() - timestamp > overdueTimeStamp){
			return true;
		}
		return false;
	}

}
