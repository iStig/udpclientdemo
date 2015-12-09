package com.kfirvr.vrcontrols.server;

import org.json.JSONObject;

public interface IServerListener {
	public void onLinkClosed();

	public void onLinkReady();

	public void onCmdRecv(JSONObject object);

	public void onDataRecv(byte[] data);
}
