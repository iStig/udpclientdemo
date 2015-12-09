package com.kfirvr.vrcontrols.ctl.lan;

import org.json.JSONObject;

public interface INetListener {
	
	public void onLinkReady();
	public void onLinkClosed();
	
	public void onEngStatus(int status);
	
	public void onCmdRecv(JSONObject object);
	public void onDataRecv(byte[] data);

}
