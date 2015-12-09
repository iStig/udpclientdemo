package com.kfirvr.vrcontrols.ctl.lan;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.kfirvr.vrcontrol.SLog;
import com.kfirvr.vrcontrols.ctl.utils.AbstractTask;
import com.kfirvr.vrcontrols.ctl.utils.CommonUtil;
import com.kfirvr.vrcontrols.ctl.utils.NetUtils;
import com.kfirvr.vrcontrols.ctl.utils.TaskResult;

public class LanControlS implements INetInterface, ISocketListener {

	private Context mContext;
	private INetListener mListener;

	// private ServerSocket mServerScoket;
	// private Socket mClientSocket;

	private TaskResult serverTaskResult;
	private InetAddress serverUdpAddress;
	private DatagramSocket UDPClient;

	private SocketManager mSocketManager;
	private boolean isServerTaskReady = false;
	private boolean isEngOn = false;

	private static final int MSG_CHECK_ENG_STATUS = 1;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_CHECK_ENG_STATUS:
				if (isEngOn) {
					if (!isServerTaskReady && !isLink()) {
						if (serverTaskResult != null) {
							serverTaskResult.cancel();
							serverTaskResult = null;
						}
						serverTaskResult = CommonUtil.runTask(new ServerTask());
					}
				} else {
					if (isServerTaskReady) {
						if (serverTaskResult != null) {
							serverTaskResult.cancel();
							serverTaskResult = null;
						}
					}
				}
				break;
			}
		}

	};

	public LanControlS(Context context) {
		this.mContext = context;
		mSocketManager = new SocketManager();
		mSocketManager.reSet(this);
	}

	private void notifyEngStatusCheck() {
		mHandler.removeMessages(MSG_CHECK_ENG_STATUS);
		mHandler.sendEmptyMessageDelayed(MSG_CHECK_ENG_STATUS, 300);
	}

	private class ServerTask extends AbstractTask {

		private ServerSocket serverSocket;

		public ServerTask() {
			super("ServerTask");
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void runTask() {
			// TODO Auto-generated method stub
			try {
				if (serverSocket == null) {
					serverSocket = new ServerSocket(INetInterface.LanTCPPort);
					serverSocket.setSoTimeout(60 * 1000);
				}
				SLog.d("wait socket connect...");
				Socket mClient = serverSocket.accept();
				if (mClient != null) {
					SLog.d("receive Client : " + mClient.toString());
					mSocketManager.addSocket(mClient);
				}
			} catch (Exception e) {
				SLog.e("Maybe ServerTask TimeOut who knows !");
			}
		}

		@Override
		protected void onStart() {
			// TODO Auto-generated method stub
			SLog.d("ServerTask onStart");
			isServerTaskReady = true;
			notifyEngStatusCheck();
		}

		@Override
		protected void onFinish() {
			// TODO Auto-generated method stub
			SLog.d("ServerTask onFinish");
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e1) {
					SLog.e("", e1);
				}
				serverSocket = null;
			}
			isServerTaskReady = false;
			notifyEngStatusCheck();
		}

		@Override
		public void stop() {
			// TODO Auto-generated method stub
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e1) {
					SLog.e("", e1);
				}
				serverSocket = null;
			}
			isServerTaskReady = false;
			notifyEngStatusCheck();
		}

	}

	private class SearchTask extends AbstractTask {

		public SearchTask(String name) {
			super(name);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onStart() {
			// TODO Auto-generated method stub
			SLog.i("SearchTask : " + this.getName() + "  onStart!");
		}

		@Override
		protected void onFinish() {
			// TODO Auto-generated method stub
			SLog.i("SearchTask : " + this.getName() + "  onFinish!");
			if (UDPClient != null) {
				UDPClient.close();
			}
			UDPClient = null;
		}

		@Override
		protected void runTask() {
			// TODO Auto-generated method stub
			try {
				int count = 0;
				while (UDPClient == null) {// TODO a common function
					try {
						UDPClient = new DatagramSocket(
								INetInterface.LanUDPPort + 1);
					} catch (IOException e) {
						SLog.e("", e);
						UDPClient = null;
					}
					count++;
					if (count > 3) {
						break;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						SLog.e("", e);
					}
				}

				if (UDPClient != null) {
					String selfIP = NetUtils.getSelfIP(mContext);
					if (selfIP == null) {
						SLog.e("SearchTask : " + this.getName()
								+ " Error! selFIP == NULL!");
					}
					if (serverUdpAddress == null) {
						serverUdpAddress = InetAddress
								.getByName("255.255.255.255");
					}
					String linkCode = INetInterface.UDP_REQUEST
							+ INetInterface.UDP_GAP;
					linkCode += selfIP + INetInterface.UDP_GAP;
					linkCode += INetInterface.LanTCPPort
							+ INetInterface.UDP_GAP;
					linkCode += CommonUtil.getMacAddress(mContext)
							+ INetInterface.UDP_GAP;
					linkCode += "END";
					SLog.d("send UDP broadcast : " + linkCode);
					byte[] data = linkCode.getBytes();
					DatagramPacket packet = new DatagramPacket(data,
							data.length, serverUdpAddress,
							INetInterface.LanUDPPort);
					UDPClient.send(packet);
				} else {
					SLog.e("Fail search UDPClient == NULL");
				}
			} catch (Exception e) {
				SLog.e("SearchTask : " + this.getName() + " Error!", e);
			}
		}

		@Override
		public void stop() {
			// TODO Auto-generated method stub
			SLog.e("UDPClient stop");
			if (UDPClient != null) {
				UDPClient.close();
			}
			UDPClient = null;
			SLog.i("SearchTask : " + this.getName() + "  stop!");
		}

	}

	@Override
	public int startEng() {
		// TODO Auto-generated method stub
		int result = 0;
		stopEng();
		if (serverTaskResult != null) {
			serverTaskResult.cancel();
			serverTaskResult = null;
		}
		serverTaskResult = CommonUtil.runTask(new ServerTask());
		isEngOn = true;
		return result;
	}

	@Override
	public int stopEng() {
		// TODO Auto-generated method stub
		SLog.d("stopEng");
		if (serverTaskResult != null) {
			serverTaskResult.cancel();
			serverTaskResult = null;
		}
		if (mSocketManager != null) {
			mSocketManager.closeSocket();
		}
		isEngOn = false;
		return 0;
	}

	@Override
	public void setListener(INetListener listener) {
		// TODO Auto-generated method stub
		this.mListener = listener;
	}

	@Override
	public void stopLink() {
		// TODO Auto-generated method stub
		mSocketManager.closeSocket();
	}

	@Override
	public boolean isLink() {
		// TODO Auto-generated method stub
		return mSocketManager.getSocket();
	}

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
	public void startSearch() {
		// TODO Auto-generated method stub
		SLog.d("LanControls Start Search!");
		CommonUtil.runTask(new SearchTask("UDP_BROAD"));
	}

	@Override
	public void onSocketReady() {
		// TODO Auto-generated method stub
		if (mListener != null) {
			mListener.onLinkReady();
		}
		notifyEngStatusCheck();
	}

	@Override
	public void onSocketClosed() {
		// TODO Auto-generated method stub
		if (mListener != null) {
			mListener.onLinkClosed();
		}
		notifyEngStatusCheck();
	}

	@Override
	public void onCmdRecv(JSONObject object) {
		// TODO Auto-generated method stub
		if (mListener != null) {
			mListener.onCmdRecv(object);
		}
	}

	@Override
	public void onDataRecv(byte[] data) {
		// TODO Auto-generated method stub
		if (mListener != null) {
			mListener.onDataRecv(data);
		}
	}

}
