package com.kfirvr.vrcontrols.ctl.lan;

import org.json.JSONObject;

public interface INetInterface {
	
	public static final int ENG_TYPE_CLIENT = 1;
	public static final int ENG_TYPE_SERVER = 2;
	
	public static final int ENG_STATUS_IDLE = 0;
	public static final int ENG_STATUS_READY = 1;
	public static final int ENG_STATUS_FAILED = 2;
	
	public static final int LanTCPPort = 55000;
	public static final int LanUDPPort = 59031;
	public static final long TIME_OUT_CONNECT = 1000 * 10;
	public static final long TIME_RESTART_GAP = 1000 * 5;
	
	public static final String UDP_GAP = "&&";
	public static final String UDP_REQUEST = "00";
	public static final String UDP_SEND = "01";
	public static final String UDP_PAIR_CODE ="svr_pair_v1";
	
	public int startEng();
	public int stopEng();
	public void setListener(INetListener listener);
	
	public void startSearch();
	public void stopLink();
	public boolean isLink();
	
	public int sendCmd(JSONObject object);
	public int sendData(byte[] data);
	

}
