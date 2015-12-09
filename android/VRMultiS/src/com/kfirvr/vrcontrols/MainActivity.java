package com.kfirvr.vrcontrols;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.kfirvr.vrcontrols.server.IServerListener;
import com.kfirvr.vrcontrols.ui.ServerProxy;

public class MainActivity extends Activity implements IServerListener {

	private ServerProxy mProxy;
	private TextView rec, status;
	private String text;
	private String linkStatus;
	private static final int MSG_RECEIVE_DATA = 1;
	private static final int MSG_CHANGE_STATUS = 2;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_RECEIVE_DATA:
				rec.setText(text);
				break;
			case MSG_CHANGE_STATUS:
				if(linkStatus != null && !linkStatus.isEmpty())
				status.setText(linkStatus);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rec = (TextView) findViewById(R.id.msg);
		status = (TextView) findViewById(R.id.status);
		mProxy = new ServerProxy(this);
		mProxy.setListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mProxy.quit();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mProxy.endSearch();
		mProxy.stop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mProxy.start();
		mProxy.startSearch();
	}

	@Override
	public void onLinkClosed() {
		// TODO Auto-generated method stub
		linkStatus = "未连接";
		mHandler.sendEmptyMessage(MSG_CHANGE_STATUS);
	}

	@Override
	public void onLinkReady() {
		// TODO Auto-generated method stub
		linkStatus = "已连接";
		mHandler.sendEmptyMessage(MSG_CHANGE_STATUS);
	}

	@Override
	public void onCmdRecv(JSONObject object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDataRecv(byte[] data) {
		// TODO Auto-generated method stub
		try {
			text = new String(data, "UTF-8");
			if (text != null) {
				mHandler.sendEmptyMessage(MSG_RECEIVE_DATA);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
