package com.kfirvr.vrcontrolc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.kfirvr.vrcontrol.SLog;
import com.kfirvr.vrcontrolc.ctl.lan.INetListener;

public class ConnectManager {

	private HashMap<String, ConnectSource> mHashClient = new HashMap<String, ConnectSource>();
	private INetListener mListener;

	public void setListener(INetListener listener) {
		this.mListener = listener;
	}

	public void checkConnectState() {
		boolean change = false;
		Iterator iter = mHashClient.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			ConnectSource source = (ConnectSource) entry.getValue();
			if (source.checkOverdue()) {
				if (source.connected != 2) {
					iter.remove();
					change = true;
				}
			}
		}
		if (change) {
			SLog.e("overdue!");
			if (mListener != null)
				mListener.onConnectStateChange();
		} else {
			SLog.e("not overdue!");
		}
	}

	public HashMap<String, ConnectSource> getClientList() {
		return mHashClient;
	}

	public void addClient(ConnectSource source) {
		if (source == null)
			return;
		if (!mHashClient.containsKey(source.mac)) {
			mHashClient.put(source.mac, source);
			if (mListener != null)
				mListener.onConnectStateChange();
		} else {
			mHashClient.put(source.mac, source);
		}
	}

	public void clearAll() {
		mHashClient.clear();
		if (mListener != null)
			mListener.onConnectStateChange();
	}

	public void deleteClient(String mac) {
		if (mac == null)
			return;
		mHashClient.remove(mac);
		if (mListener != null)
			mListener.onConnectStateChange();
	}

	public void connecting(String mac) {
		if (mac == null)
			return;
		if (mHashClient.containsKey(mac)) {
			ConnectSource source = mHashClient.get(mac);
			source.connected = 1;
			if (mListener != null)
				mListener.onConnectStateChange();
		}
	}

	public void connected(String mac) {
		if (mac == null)
			return;
		if (mHashClient.containsKey(mac)) {
			ConnectSource source = mHashClient.get(mac);
			source.connected = 2;
			mHashClient.put(mac, source);
			if (mListener != null)
				mListener.onConnectStateChange();
		}
	}

	public void disConnect() {
		boolean change = false;
		Iterator<Entry<String, ConnectSource>> iter = mHashClient.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			ConnectSource source = (ConnectSource) entry.getValue();
			if (source.connected == 1 || source.connected == 2) {
				source.connected = 0;
				mHashClient.put(source.mac, source);
				change = true;
			}
		}
		if (change) {
			if (mListener != null)
				mListener.onConnectStateChange();
		}
	}

	public ConnectSource getConnectedClient() {
		Iterator<Entry<String, ConnectSource>> iter = mHashClient.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			ConnectSource source = (ConnectSource) entry.getValue();
			if (source.connected == 2) {
				return source;
			}
		}
		return null;
	}

}
