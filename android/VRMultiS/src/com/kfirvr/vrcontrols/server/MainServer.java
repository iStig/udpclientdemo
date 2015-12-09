package com.kfirvr.vrcontrols.server;

import org.json.JSONObject;

import com.kfirvr.vrcontrol.SLog;
import com.kfirvr.vrcontrols.ctl.lan.INetInterface;
import com.kfirvr.vrcontrols.ctl.lan.INetListener;
import com.kfirvr.vrcontrols.ctl.lan.LanControlS;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class MainServer extends Service implements INetListener {

	private INetInterface mNetControl;
	private boolean isReady = false;
	private IServerListener mListener;

	private static final int MSG_SEARCH_CLIENT = 1;
	private static final int MSG_ON_LINK_READY = 2;
	private static final int MSG_ON_LINK_CLOSED = 3;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_SEARCH_CLIENT:
				if (isReady) {
					if (mNetControl != null) {
						if (!mNetControl.isLink()) {
							mNetControl.startSearch();
						}
						mHandler.removeMessages(MSG_SEARCH_CLIENT);
						mHandler.sendEmptyMessageDelayed(MSG_SEARCH_CLIENT,
								3 * 2000);
					}
				}
				break;
			case MSG_ON_LINK_READY:
				if (mListener != null) {
					mListener.onLinkReady();
				}
				break;
			case MSG_ON_LINK_CLOSED:
				if (mListener != null) {
					mListener.onLinkClosed();
				}
				break;
			}
		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	private MyBinder mBinder = new MyBinder();

	public class MyBinder extends Binder {
		public MainServer getService() {
			return MainServer.this;
		}
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mNetControl = new LanControlS(this);
		mNetControl.setListener(this);
		onStart();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		onStop();
	}

	public void setListener(IServerListener listener) {
		this.mListener = listener;
	}

	public void startSearch() {
		mHandler.removeMessages(MSG_SEARCH_CLIENT);
		mHandler.sendEmptyMessage(MSG_SEARCH_CLIENT);
	}

	public void endSearch() {
		mHandler.removeMessages(MSG_SEARCH_CLIENT);
	}

	public void stopLink() {
		mNetControl.stopLink();
	}

	public void sendCmd(JSONObject object) {
		if (mNetControl != null) {
			mNetControl.sendCmd(object);
		}
	}

	public void sendData(byte[] data) {
		if (mNetControl != null) {
			mNetControl.sendData(data);
		}
	}

	/******************** private *********************/

	private void onStart() {
		mNetControl.startEng();
		isReady = true;
	}

	private void onStop() {
		mNetControl.stopEng();
		isReady = false;
	}

	private void notifyLinkClosed() {
		SLog.d("notifyLinkClosed");
		mHandler.obtainMessage(MSG_ON_LINK_CLOSED, 0, 0).sendToTarget();
	}

	/******************** private end ******************/

	@Override
	public void onLinkReady() {
		// TODO Auto-generated method stub
		mHandler.obtainMessage(MSG_ON_LINK_READY, 0, 0).sendToTarget();
	}

	@Override
	public void onLinkClosed() {
		// TODO Auto-generated method stub
		notifyLinkClosed();
	}

	@Override
	public void onEngStatus(int status) {
		// TODO Auto-generated method stub
		if (isReady && status == INetInterface.ENG_STATUS_IDLE) {
			if (mNetControl != null) {
				mNetControl.startEng();
			}
		}
	}

	@Override
	public void onCmdRecv(JSONObject object) {
		// TODO Auto-generated method stub
		if (object == null)
			return;
		mListener.onCmdRecv(object);
	}

	@Override
	public void onDataRecv(byte[] data) {
		// TODO Auto-generated method stub
		mListener.onDataRecv(data);
	}
}
