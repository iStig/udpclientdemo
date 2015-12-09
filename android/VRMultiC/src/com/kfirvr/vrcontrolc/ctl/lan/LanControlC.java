package com.kfirvr.vrcontrolc.ctl.lan;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.kfirvr.vrcontrol.SLog;
import com.kfirvr.vrcontrolc.ConnectManager;
import com.kfirvr.vrcontrolc.ConnectSource;
import com.kfirvr.vrcontrolc.ctl.utils.CommonUtil;
import com.kfirvr.vrcontrolc.ctl.utils.LoopTask;
import com.kfirvr.vrcontrolc.ctl.utils.TaskResult;

public class LanControlC implements INetInterface, ISocketListener {

	private Context mContext;
	private INetListener mListener;

	// private ServerSocket mServerScoket;
	// private Socket mClientSocket;

	private TaskResult clientTaskResult;
	private InetAddress serverUdpAddress;
	private DatagramSocket UDPServer;

	private Socket clientSocket;
	private SocketManager mSocketManager;
	private ConnectManager mConnectManager;

	private int mEngType = INetInterface.ENG_TYPE_SERVER;
	private int mEngStatus = INetInterface.ENG_STATUS_IDLE;

	private boolean isClientTaskReady = false;

	private boolean isEngOn = false;
	private int mLinkStatus = INetInterface.LINK_STATUS_IDLE;

	private static final int MSG_CHECK_ENG_STATUS = 1;
	private static final int MSG_CHECK_CONNECT_STATUS = 2;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_CHECK_ENG_STATUS:
				if (isEngOn) {
					if (!isClientTaskReady) {
						if (clientTaskResult != null) {
							clientTaskResult.cancel();
							clientTaskResult = null;
						}
						clientTaskResult = CommonUtil.runTask(new ClientTask());
						notifyConnectStatusCheck();
					}
					if (mLinkStatus != INetInterface.LINK_STATUS_IDLE) {
						switch (mLinkStatus) {
						case INetInterface.LINK_STATUS_LINKED:
							mEngStatus = INetInterface.ENG_STATUS_LINKED;
							break;
						case INetInterface.LINK_STATUS_LINKING:
							mEngStatus = INetInterface.ENG_STATUS_LINKING;
							break;
						}
					} else if (isClientTaskReady) {
						mEngStatus = INetInterface.ENG_STATUS_READY;
					} else {
						mEngStatus = INetInterface.ENG_STATUS_IDLE;
					}
				} else {
					if (isClientTaskReady) {
						if (clientTaskResult != null) {
							clientTaskResult.cancel();
							clientTaskResult = null;
						}
						mHandler.removeMessages(MSG_CHECK_CONNECT_STATUS);
					}
					mEngStatus = INetInterface.ENG_STATUS_IDLE;
				}
				break;
			case MSG_CHECK_CONNECT_STATUS:
				mConnectManager.checkConnectState();
				mHandler.sendEmptyMessageDelayed(MSG_CHECK_CONNECT_STATUS,
						30 * 1000);
				break;
			}
		}

	};

	public LanControlC(Context context) {
		this.mContext = context;
		mConnectManager = new ConnectManager();
		mConnectManager.clearAll();
		mSocketManager = new SocketManager();
		mSocketManager.setListener(this);
		mSocketManager.clearAll();
	}

	public HashMap<String, ConnectSource> getClientList() {
		return mConnectManager.getClientList();
	}

	public void connectClient(final ConnectSource source) {
		if (mEngStatus == INetInterface.ENG_STATUS_READY) {
			mEngStatus = INetInterface.ENG_STATUS_LINKING;
			mLinkStatus = INetInterface.LINK_STATUS_LINKING;
			mConnectManager.connecting(source.mac);
			new Thread(new Runnable() {
				@Override
				public void run() {
					StartToLinkServer(source.ip, source.port, source.mac);
				}
			}).start();
		} else {
			SLog.e("ClientTask : Error mEngStatus and ignore! : " + mEngStatus);
		}
	}

	public void disConnectClient() {
		if (clientSocket != null) {
			try {
				clientSocket.close();
			} catch (IOException e1) {
				SLog.e("", e1);
			}
			clientSocket = null;
		}
		mEngStatus = INetInterface.ENG_STATUS_FAILED;
		mLinkStatus = INetInterface.LINK_STATUS_IDLE;
		mConnectManager.disConnect();
		notifyEngStatusCheck();
	}

	private class ClientTask extends LoopTask {

		public ClientTask() {
			super("ClientTask");
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void doLoopTask() {
			// TODO Auto-generated method stub
			try {
				if (UDPServer == null) {
					UDPServer = new DatagramSocket(INetInterface.LanUDPPort);
				}
				if (serverUdpAddress == null) {
					serverUdpAddress = InetAddress.getByName("255.255.255.255");
				}
				byte[] data = new byte[512];
				DatagramPacket packet = new DatagramPacket(data, 512,
						serverUdpAddress, INetInterface.LanUDPPort);
				SLog.v("UDPLink thread ready to receive !!");
				UDPServer.receive(packet);

				String result = new String(packet.getData(),
						packet.getOffset(), packet.getLength());
				SLog.d("UDPLink thread receive cmd ==" + result);

				String[] datas = result.split(INetInterface.UDP_GAP);
				if (INetInterface.UDP_REQUEST.equals(datas[0])) {
					// if(mEngStatus == INetInterface.ENG_STATUS_READY){
					// mEngStatus = INetInterface.ENG_STATUS_LINKING;
					// mLinkStatus = INetInterface.LINK_STATUS_LINKING;
					// StartToLinkServer(datas[1], Integer.parseInt(datas[2]));
					// }else{
					// SLog.e("ClientTask : Error mEngStatus and ignore! : "+mEngStatus);
					// }
					String ip = datas[1];
					int port = Integer.parseInt(datas[2]);
					String mac = datas[3];
					ConnectSource source = new ConnectSource(ip, port, mac,
							System.currentTimeMillis());
					mConnectManager.addClient(source);
				}
			} catch (Exception e) {
				SLog.e("ClientTask error!");
				e.printStackTrace();
			}
		}

		@Override
		protected void onStart() {
			// TODO Auto-generated method stub
			isClientTaskReady = true;
			SLog.e("ClientTask onStart!");
			notifyEngStatusCheck();
		}

		@Override
		protected void onFinish() {
			// TODO Auto-generated method stub
			if (UDPServer != null) {
				UDPServer.close();
				UDPServer = null;
			}
			isClientTaskReady = false;
			SLog.e("ClientTask onFinish!");
			notifyEngStatusCheck();
		}

	}

	private boolean StartToLinkServer(String ip, int port, String mac) {
		boolean result = false;
		clientSocket = new Socket();
		try {
			SLog.d("StartToLinkServer : " + ip + " " + port);
			clientSocket.connect(new InetSocketAddress(ip, port));
			mSocketManager.setSocket(clientSocket, mac);
			mEngStatus = INetInterface.ENG_STATUS_LINKED;
			mLinkStatus = INetInterface.LINK_STATUS_LINKED;
			mConnectManager.connected(mac);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SLog.e("StartToLinkServer Error!", e);
			if (clientSocket != null) {
				try {
					clientSocket.close();
				} catch (IOException e1) {
					SLog.e("", e1);
				}
				clientSocket = null;
			}
			mEngStatus = INetInterface.ENG_STATUS_FAILED;
			mLinkStatus = INetInterface.LINK_STATUS_IDLE;
			mConnectManager.disConnect();
			notifyEngStatusCheck();
		}
		return result;
	}

	private void notifyEngStatusCheck() {
		mHandler.removeMessages(MSG_CHECK_ENG_STATUS);
		mHandler.sendEmptyMessageDelayed(MSG_CHECK_ENG_STATUS, 300);
	}

	private void notifyConnectStatusCheck() {
		mHandler.removeMessages(MSG_CHECK_CONNECT_STATUS);
		mHandler.sendEmptyMessageDelayed(MSG_CHECK_CONNECT_STATUS, 30 * 1000);
	}

	@Override
	public int startEng() {
		// TODO Auto-generated method stub
		SLog.e("startEng");
		int result = 0;
		if (mEngStatus != INetInterface.ENG_STATUS_IDLE) {
			stopEng();
		}
		if (clientTaskResult != null) {
			clientTaskResult.cancel();
			clientTaskResult = null;
		}
		clientTaskResult = CommonUtil.runTask(new ClientTask());
		notifyConnectStatusCheck();
		isEngOn = true;
		return result;
	}

	@Override
	public int stopEng() {
		// TODO Auto-generated method stub
		SLog.e("stopEng");
		if (clientTaskResult != null) {
			clientTaskResult.cancel();
			clientTaskResult = null;
		}
		mSocketManager.clearAll();
		mLinkStatus = INetInterface.LINK_STATUS_IDLE;
		isEngOn = false;
		mEngStatus = INetInterface.ENG_STATUS_IDLE;
		mHandler.removeMessages(MSG_CHECK_CONNECT_STATUS);
		return 0;
	}

	@Override
	public int getEngStatus(int status) {
		// TODO Auto-generated method stub

		return mEngStatus;
	}

	@Override
	public void setListener(INetListener listener) {
		// TODO Auto-generated method stub
		this.mListener = listener;
		mConnectManager.setListener(listener);
	}

	// @Override
	// public void stopLink() {
	// // TODO Auto-generated method stub
	// mSocketManager.clearAll();
	// mLinkStatus = INetInterface.LINK_STATUS_IDLE;
	// notifyEngStatusCheck();
	// }

	@Override
	public int sendCmd(JSONObject object) {
		// TODO Auto-generated method stub
		int result = 0;
		result = mSocketManager.sendCmd(object);
		return result;
	}

	@Override
	public int sendData(byte[] data) {
		// TODO Auto-generated method stub
		int result = 0;
		result = mSocketManager.sendData(data);
		return result;
	}

	@Override
	public void onSocketReady(String mac) {
		// TODO Auto-generated method stub
		SLog.d("C onSocketReady !");
		mLinkStatus = INetInterface.LINK_STATUS_LINKED;
		if (mListener != null) {
			mListener.onLinkReady();
		}
		notifyEngStatusCheck();
	}

	@Override
	public void onSocketClosed(String mac) {
		// TODO Auto-generated method stub
		SLog.d("C onSocketClosed !");
		mLinkStatus = INetInterface.LINK_STATUS_IDLE;
		mConnectManager.disConnect();
		if (mListener != null) {
			mListener.onLinkClosed();
		}
		notifyEngStatusCheck();
	}

	@Override
	public void onCmdRecv(JSONObject object) {
		// TODO Auto-generated method stub
		SLog.d("C onCmdRecv !");
		if (mListener != null) {
			mListener.onCmdRecv(object);
		}
	}

	@Override
	public void onDataRecv(byte[] data) {
		// TODO Auto-generated method stub
		SLog.d("C onDataRecv !");
		if (mListener != null) {
			mListener.onDataRecv(data);
		}
	}

}
