package com.kfirvr.vrcontrolc.server;

import java.util.HashMap;

import org.json.JSONObject;

import com.kfirvr.vrcontrol.SLog;
import com.kfirvr.vrcontrolc.ConnectSource;
import com.kfirvr.vrcontrolc.ctl.lan.INetInterface;
import com.kfirvr.vrcontrolc.ctl.lan.INetListener;
import com.kfirvr.vrcontrolc.ctl.lan.LanControlC;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ClientServer extends Service implements INetListener {

	private INetInterface mNetControl;
	private IServerListener mListener;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	private MyBinder mBinder = new MyBinder();

	public class MyBinder extends Binder {
		public ClientServer getService() {
			return ClientServer.this;
		}
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SLog.e("ClientServer onCreate");
		mNetControl = new LanControlC(this);
		mNetControl.setListener(this);
		mNetControl.startEng();
	}

	public void setListener(IServerListener listener) {
		this.mListener = listener;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SLog.e("ClientServer onDestroy");
		mNetControl.stopEng();
	}

	public void connectClient(ConnectSource source) {
		mNetControl.connectClient(source);
	}

	public HashMap<String, ConnectSource> getClientList() {
		return mNetControl.getClientList();
	}
	
	public void disConnectClient() {
		mNetControl.disConnectClient();
	}

	public void sendCmd(JSONObject object) {
		if (object != null) {
			mNetControl.sendCmd(object);
		} else {
			SLog.e("Error send cmd mServer or object is NULL!");
		}
	}

	public void sendData(byte[] data) {
		mNetControl.sendData(data);
	}

	@Override
	public void onLinkReady() {
		// TODO Auto-generated method stub
		if (mListener != null)
			mListener.onLinkReady();
	}

	@Override
	public void onLinkClosed() {
		// TODO Auto-generated method stub
		if (mListener != null)
			mListener.onLinkClosed();
	}

	@Override
	public void onCmdRecv(JSONObject object) {
		// TODO Auto-generated method stub
		if (object == null)
			return;
		if (mListener != null)
			mListener.onCmdRecv(object);
	}

	@Override
	public void onDataRecv(byte[] data) {
		// TODO Auto-generated method stub
		if (mListener != null)
			mListener.onDataRecv(data);
	}

	@Override
	public void onConnectStateChange() {
		// TODO Auto-generated method stub
		if (mListener != null)
			mListener.onConnectStateChange();
	}

}
