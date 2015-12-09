package com.kfirvr.vrcontrols.ctl.lan;

import org.json.JSONObject;

public interface ISocketListener {
	
	public void onSocketReady();
	public void onSocketClosed();
	
	public void onCmdRecv(JSONObject object);
	public void onDataRecv(byte[] data);

}
