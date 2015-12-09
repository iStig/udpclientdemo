package com.kfirvr.vrcontrolc.ctl.lan;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.kfirvr.vrcontrol.SLog;
import com.kfirvr.vrcontrolc.ctl.utils.AbstractTask;
import com.kfirvr.vrcontrolc.ctl.utils.CommonUtil;
import com.kfirvr.vrcontrolc.ctl.utils.LoopTask;
import com.kfirvr.vrcontrolc.ctl.utils.TaskResult;

public class SocketManager {

	private ISocketListener mListener;
	private SocketSource mSource;
	private String mac;
	private int errorCount = 0;
	private long lastEffectiveConnectTimestamp = 0;

	private static final int MSG_SEND_HEARTBEAT = 1;
	private static final int MSG_CHECK_HEARTBEAT = 2;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_SEND_HEARTBEAT:
				if (mSource != null) {
					mSource.sendHeartBeat();
				}
				mHandler.sendEmptyMessageDelayed(MSG_SEND_HEARTBEAT, 30 * 1000);
				break;
			case MSG_CHECK_HEARTBEAT:
				if (mSource != null) {
					mSource.checkHeartBeat();
				}
				mHandler.sendEmptyMessageDelayed(MSG_CHECK_HEARTBEAT,
						3 * 60 * 1000);
				break;
			}
		}
	};

	public void setSocket(Socket socket, String mac) throws IOException {
		this.mac = mac;
		mSource = new SocketSource(socket);
		if (mListener != null) {
			mListener.onSocketReady(mac);
		}
	}

	public void setListener(ISocketListener listener) {
		this.mListener = listener;
	}

	public void clearAll() {
		if (mSource != null) {
			mSource.closeSocket();
		}
		mSource = null;
	}

	public int sendCmd(JSONObject object) {
		int result = 0;
		if (mSource != null) {
			result = mSource.sendCmd(object);
		}
		return result;
	}

	public int sendData(byte[] data) {
		int result = 0;
		if (mSource != null) {
			result = mSource.sendData(data);
		}
		return result;
	}

	private void notifySocketClosed() {
		if (mListener != null) {
			mListener.onSocketClosed(mac);
		}
	}

	private class SocketSource {

		private InputStream is;
		private OutputStream os;
		private TaskResult task;
		private Socket mSocket;

		private ChannelQueue mQueue;
		private boolean isWriting = false;

		public SocketSource(Socket socket) throws IOException {
			this.mSocket = socket;
			mQueue = new ChannelQueue();
			mSocket.setSendBufferSize(1024 * 5);
			is = new BufferedInputStream(socket.getInputStream());
			os = new BufferedOutputStream(socket.getOutputStream());
			task = CommonUtil.runTask(new ReadTask());
			mHandler.removeMessages(MSG_SEND_HEARTBEAT);
			mHandler.sendEmptyMessageDelayed(MSG_SEND_HEARTBEAT, 30 * 1000);
			mHandler.removeMessages(MSG_CHECK_HEARTBEAT);
			mHandler.sendEmptyMessageDelayed(MSG_CHECK_HEARTBEAT, 3 * 60 * 1000);
		}

		public void closeSocket() {
			mHandler.removeMessages(MSG_SEND_HEARTBEAT);
			mHandler.removeMessages(MSG_CHECK_HEARTBEAT);
			if (mSocket != null) {
				try {
					mSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					// e1.printStackTrace();
				}
				if (task != null) {
					task.cancel();
					task = null;
				}
				mSocket = null;
			}
		}

		public int sendCmd(JSONObject object) {
			int result = 0;
			String sCmd = object.toString();
			byte[] data;
			try {
				data = sCmd.getBytes("UTF-8");
				ChannelQueue.ChannelData mData = ChannelQueue.obtainData(0,
						data.length, data);
				mQueue.offerData(mData);
				notifyDataUpdate();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				SLog.e("sendCmd Error! and closed! ", e);
				// closeSocket();
				// notifySocketClosed();
			}
			return result;
		}

		public int sendData(byte[] data) {
			int result = 0;
			try {
				ChannelQueue.ChannelData mData = ChannelQueue.obtainData(1,
						data.length, data);
				mQueue.offerData(mData);
				notifyDataUpdate();
			} catch (Exception e) {
				SLog.e("sendData Error! and closed! : ", e);
				// closeSocket();
				// notifySocketClosed();
			}
			return result;
		}

		private void notifyDataUpdate() {
			if (!isWriting) {
				isWriting = true;
				CommonUtil.runTask(new WriteTask());
			}
		}

		private class WriteTask extends AbstractTask {

			public WriteTask() {
				super("WriteTask");
				// TODO Auto-generated constructor stub
			}

			@Override
			protected void onStart() {
				// TODO Auto-generated method stub
				isWriting = true;
			}

			@Override
			protected void onFinish() {
				// TODO Auto-generated method stub
				isWriting = false;
			}

			@Override
			protected void runTask() {
				// TODO Auto-generated method stub
				try {
					ChannelQueue.ChannelData mData = mQueue.pollData();
					while (mData != null) {
						SLog.d("WriteTask : " + mData.mType + "  "
								+ mData.mLength);
						os.write((byte) mData.mType);
						os.write(CommonUtil.intToByteArray(mData.mLength), 0, 4);
						os.write(mData.data, 0, mData.mLength);
						os.flush();
						mData = mQueue.pollData();
					}
					isWriting = false;
				} catch (Exception e) {
					SLog.e("WriteTask Error! and closed! : ", e);
					isWriting = false;
					closeSocket();
					notifySocketClosed();

				}
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub
				isWriting = false;
			}

		}

		public void checkHeartBeat() {
			if (System.currentTimeMillis() - lastEffectiveConnectTimestamp > 30000) {
				closeSocket();
				notifySocketClosed();
			}
		}

		public void sendHeartBeat() {
			String sCmd = "svr";
			byte[] data;
			try {
				data = sCmd.getBytes("UTF-8");
				ChannelQueue.ChannelData mData = ChannelQueue.obtainData(2,
						data.length, data);
				mQueue.offerData(mData);
				notifyDataUpdate();
			} catch (Exception e) {
				SLog.e("send HeartBeat Error! and closed! ", e);
			}
		}

		private class ReadTask extends LoopTask {

			byte[] data = new byte[1024 * 5];

			public ReadTask() {
				super("ReadTask");
				// TODO Auto-generated constructor stub
			}

			@Override
			protected void doLoopTask() {
				// TODO Auto-generated method stub
				try {
					int length;
					int offset;
					int type = is.read();
					byte[] blen = new byte[4];
					is.read(blen, 0, 4);
					length = CommonUtil.byteArrayToInt(blen);
					SLog.d("S read loop type : " + type + "  length :" + length);
					switch (type) {
					case 0:// cmd
						errorCount = 0;
						lastEffectiveConnectTimestamp = System
								.currentTimeMillis();
						// length = is.read();
						SLog.d("C read type 0 length : " + length);
						if (length <= 0)
							break;
						is.read(data, 0, length);
						String sData = new String(data, 0, length, "UTF-8");
						JSONObject object = new JSONObject(sData);
						SLog.d("C read cmd : " + object.toString());
						if (mListener != null) {
							mListener.onCmdRecv(object);
						}
						break;
					case 1:// data
						errorCount = 0;
						lastEffectiveConnectTimestamp = System
								.currentTimeMillis();
						// length = is.read();
						SLog.d("C read type 1 length : " + length);
						if (length <= 0)
							break;
						byte[] mdata = new byte[length];
						offset = 0;
						int count = 0;
						while (offset < length) {
							count = is.read(mdata, offset, length);
							offset += count;
						}
						if (mListener != null) {
							mListener.onDataRecv(mdata);
						}
						break;
					case -1:
						errorCount++;
						if (errorCount > 8) {
							SLog.e("read task Error! : errorCount :"
									+ errorCount);
							closeSocket();
							notifySocketClosed();
							forceBreak();
						}
						break;
					case 2:
						if (length <= 0)
							break;
						byte[] hData = null;
						if (length > data.length) {
							hData = new byte[length];
							offset = 0;
							int hCount = 0;
							while (offset < length) {
								hCount = is.read(hData, offset,
										(length - offset));
								offset += hCount;
							}
						} else {
							is.read(data, 0, length);
							hData = data;
						}
						String heart = new String(hData, 0, length, "UTF-8");
						if ("svr".equals(heart)) {
							SLog.e("receive heart!");
							errorCount = 0;
							lastEffectiveConnectTimestamp = System
									.currentTimeMillis();
						}
						break;
					}
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
				} catch (Exception e) {
					SLog.e("read task Error! : ", e);
					closeSocket();
					notifySocketClosed();
					forceBreak();
				}
			}

			@Override
			protected void onStart() {
				// TODO Auto-generated method stub
				SLog.e("ReadTask onStart!");
			}

			@Override
			protected void onFinish() {
				// TODO Auto-generated method stub
				SLog.e("ReadTask onFinish!");
			}

		}
	}

}
