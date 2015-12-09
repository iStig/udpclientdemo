package com.kfirvr.vrcontrolc.server;

import org.json.JSONObject;

public interface IServerListener {
	public void onLinkReady();

	public void onLinkClosed();

	public void onConnectStateChange();

	public void onCmdRecv(JSONObject object);

	public void onDataRecv(byte[] data);
}