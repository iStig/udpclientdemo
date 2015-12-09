package com.kfirvr.vrcontrolc.ui;

import java.util.HashMap;

import org.json.JSONObject;

import com.kfirvr.vrcontrol.SLog;
import com.kfirvr.vrcontrolc.ConnectSource;
import com.kfirvr.vrcontrolc.server.ClientServer;
import com.kfirvr.vrcontrolc.server.ClientServer.MyBinder;
import com.kfirvr.vrcontrolc.server.IServerListener;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ServerProxy {

	private Context mContext;
	private IServerListener mListener;
	private ClientServer mServer;

	private boolean isConnected = false;

	public ServerProxy(Context context) {
		this.mContext = context;
		Intent sintent = new Intent(mContext, ClientServer.class);
		this.mContext.startService(sintent);
	}

	public void setListener(IServerListener listener) {
		this.mListener = listener;
		if (mServer != null) {
			mServer.setListener(mListener);
		}
	}

	/**
	 * just bindservice
	 */
	public void start() {
		if (!isConnected) {
			Intent intent = new Intent(mContext, ClientServer.class);
			mContext.bindService(intent, mConn, Context.BIND_AUTO_CREATE);
		}

	}

	/**
	 * just unbindService
	 */
	public void stop() {
		if (mServer != null) {
			mServer.setListener(null);
		}
		if (isConnected) {
			mContext.unbindService(mConn);
			isConnected = false;
		}
	}

	/**
	 * quit app and stop service
	 */
	public void quit() {
		SLog.e("quit app and stop service!");
		if (mServer != null) {
			mServer.setListener(null);
			mServer.stopSelf();
		}
		if (isConnected) {
			mContext.unbindService(mConn);
			isConnected = false;
		}
		mServer = null;
		mListener = null;
	}

	public void connectClient(ConnectSource source) {
		mServer.connectClient(source);
	}

	public void disConnectClient() {
		if (mServer != null) {
			mServer.disConnectClient();
		}
	}

	public void sendCmd(JSONObject object) {
		if (mServer != null && object != null) {
			mServer.sendCmd(object);
		} else {
			SLog.e("Error send cmd mServer or object is NULL!");
		}
	}

	public void sendData(byte[] data) {
		mServer.sendData(data);
	}

	public HashMap<String, ConnectSource> getClientList() {
		return mServer.getClientList();
	}

	private ServiceConnection mConn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			isConnected = false;
		}

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder server) {
			// TODO Auto-generated method stub
			SLog.d("onServiceConnected!");
			MyBinder binder = (MyBinder) server;
			mServer = binder.getService();
			if (mListener != null) {
				mServer.setListener(mListener);
			}
			isConnected = true;
		}
	};
}
