package com.kfirvr.vrcontrolc;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import com.kfirvr.vrcontrol.SLog;
import com.kfirvr.vrcontrolc.server.IServerListener;
import com.kfirvr.vrcontrolc.ui.ServerProxy;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements IServerListener {

	private ServerProxy mProxy;
	private MyAdapter adapter;
	private ListView lv;
	private HashMap<String, ConnectSource> mList = new HashMap<String, ConnectSource>();
	private ArrayList<ConnectSource> mData = new ArrayList<ConnectSource>();
	private static final int MSG_CHANGE_LIST = 1;
	private static final int MSG_CHANGE_STATUS = 2;
	private boolean linkStatus = false;

	private TextView status, receive;
	private Button disconnect, send;
	private EditText msg;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_CHANGE_LIST:
				adapter.notifyDataSetChanged();
				break;
			case MSG_CHANGE_STATUS:
				if(linkStatus){
					status.setText("已连接");
				}else{
					status.setText("未连接");
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		mProxy = new ServerProxy(this);
		mProxy.setListener(this);
		lv = (ListView) findViewById(R.id.lv);
		status = (TextView) findViewById(R.id.status);
		receive = (TextView) findViewById(R.id.receive);
		msg = (EditText) findViewById(R.id.msg);
		disconnect = (Button) findViewById(R.id.disconnect);
		disconnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mProxy.disConnectClient();
			}
		});
		send = (Button) findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String text = msg.getText().toString();
				if (!text.isEmpty()) {
					try {
						mProxy.sendData(text.getBytes("UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		adapter = new MyAdapter(this);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mProxy.connectClient(mData.get(arg2));
			}

		});
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
		mProxy.stop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mProxy.start();
	}

	@Override
	public void onLinkClosed() {
		// TODO Auto-generated method stub
		SLog.e("onLinkClosed");
		linkStatus = false;
		mHandler.sendEmptyMessage(MSG_CHANGE_STATUS);
	}

	@Override
	public void onLinkReady() {
		// TODO Auto-generated method stub
		SLog.e("onLinkReady");
		linkStatus = true;
		mHandler.sendEmptyMessage(MSG_CHANGE_STATUS);
	}

	@Override
	public void onCmdRecv(JSONObject object) {
		// TODO Auto-generated method stub
		SLog.e("onCmdRecv");
	}

	@Override
	public void onDataRecv(byte[] data) {
		// TODO Auto-generated method stub
		SLog.e("onDataRecv");
	}

	@Override
	public void onConnectStateChange() {
		// TODO Auto-generated method stub
		SLog.e("onConnectStateChange");
		adapter.setData(getData());
		mHandler.sendEmptyMessage(MSG_CHANGE_LIST);
	}

	private ArrayList<ConnectSource> getData() {
		mData = new ArrayList<ConnectSource>();
		mList = mProxy.getClientList();
		Iterator iter = mList.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			ConnectSource val = (ConnectSource) entry.getValue();
			mData.add(val);
		}
		return mData;
	}

}
