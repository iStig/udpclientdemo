package com.kfirvr.vrcontrolc.ctl.lan;

import org.json.JSONObject;

public interface INetListener {

	public void onLinkReady();

	public void onLinkClosed();

	public void onConnectStateChange();

	public void onCmdRecv(JSONObject object);

	public void onDataRecv(byte[] data);

}
