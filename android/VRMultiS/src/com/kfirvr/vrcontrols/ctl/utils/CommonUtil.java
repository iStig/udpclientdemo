package com.kfirvr.vrcontrols.ctl.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.kfirvr.vrcontrol.SLog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Base64;


public class CommonUtil {
	
	private static ExecutorService pool = Executors.newCachedThreadPool();//TODO shutdown
	
	public static void runTask(Runnable task){
		SLog.i("executing runnable....");
		pool.execute(new RunnableProxy(task));
		printPoolStatus();
	}
	
	public static TaskResult runTask(AbstractTask task){
		return runTask(task, false);
	}
	
	public static TaskResult runTask(AbstractTask task, boolean manualResume){
		SLog.i("executing AbstractTask " + task.getName() + "...");
		TaskResult tr = new TaskResult(task);
		tr.setFuture(pool.submit(tr));
		printPoolStatus();
		if(!manualResume){
			task.resume();//try to suspend the task until it's returned	
		}
		return tr;
	}
	
	public static void sendDisconnectIntent(Context context){
		SLog.i("sendDisconnectIntent");
		context.sendBroadcast(new Intent(Constants.ACTION_DISCONNECT_NETWORK));
	}
	
	private static void printPoolStatus(){
		if(pool instanceof ThreadPoolExecutor){
			ThreadPoolExecutor tpe = (ThreadPoolExecutor) pool;
			int running = tpe.getActiveCount();
			long finished = tpe.getCompletedTaskCount();
			int currSize = tpe.getPoolSize();
			long allTasks = tpe.getTaskCount();
			SLog.i("Thread pool status, current running:" + running + ",pool size:" + currSize + ",completed:" + finished + ",all tasks:" + allTasks);
		}
	}
	
	public static final byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	
	public static final int byteArrayToInt(byte[] bytes){
		int result = -1;
		if(bytes == null || bytes.length  != 4){
			return -1;
		}
		result = (bytes[0] & 0xFF) << 24;
		result |= (bytes[1] & 0xFF) << 16;
		result |= (bytes[2] & 0xFF) << 8;
		result |= (bytes[3] & 0xFF);
		return result;
	}
	
	public static Bitmap stringAsImg(String str){
		byte[] bytes = Base64.decode(str, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	
	public static byte[] imageAsBytes(Bitmap image){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SLog.d("Leeon is recyclyed before compress:" + image.isRecycled());
		image.compress(CompressFormat.PNG, 50, baos);
		image.recycle();
		byte[] bytes = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			SLog.e("", e);
		}
		SLog.d("Leeon compressed " + bytes.length + " bytes img");
		return bytes;
	}
	
	public static Bitmap bytesAsImage(byte[] bytes){
		SLog.d("Leeon decompress " + bytes.length + " bytes data");
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	
	public static String imgAsString(Bitmap image){
		String pngString = Base64.encodeToString(imageAsBytes(image), Base64.DEFAULT);
		return pngString;
	}
	
	public static void readLengthedData(int len, byte[] buffer, InputStream input) throws SocketException, IOException {
		//TODO len protection, len vs. buffer.length, large len leads to out of memory
		int left = len;
		int readCount = 0;
		while (readCount != len) {
			int read = input.read(buffer, readCount, left);
			if(read != -1){
				readCount += read;
				left -= read;
				if (readCount > len) {
					throw new IOException("readLengthedData error,required length:" + len
						+ ",but read:" + readCount);
				}
			}else{
				throw new SocketException(Constants.SOCKET_CLOSED);
			}
		}
	}
	
	private static class RunnableProxy implements Runnable{
		private Runnable realRun;
		private static int count = 0;
		private RunnableProxy(Runnable run){
			realRun = run;
		}

		@Override
		public void run() {
			int tmpCount = 0;
			synchronized (RunnableProxy.class) {
				tmpCount = count;
				count++;
			}
			SLog.i("runnable task " + tmpCount + " started");
			try{
				realRun.run();
			}catch(Exception e){
				SLog.e("", e);
			}
			SLog.i("runnable task " + tmpCount + " completed");
		}
		
	}

	public static String getMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}
}
