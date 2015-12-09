package com.kfirvr.vrcontrolc.ctl.lan;

import org.json.JSONObject;

public interface ISocketListener {
	
	public void onSocketReady(String mac);
	public void onSocketClosed(String mac);
	
	public void onCmdRecv(JSONObject object);
	public void onDataRecv(byte[] data);

}
