package com.kfirvr.vrcontrolc.ctl.lan;

import java.util.LinkedList;
import java.util.Queue;

public class ChannelQueue {
	
	private final Object mLockObject = new Object();
	private Queue<ChannelQueue.ChannelData> mQueue;
	
	public ChannelQueue(){
		mQueue = new LinkedList<ChannelQueue.ChannelData>();
	}
	
	public static ChannelData obtainData(int type, int length, byte[] data){
		ChannelData mCdata = new ChannelData();
		mCdata.mType = type;
		mCdata.mLength = length;
		mCdata.data = data;
		return mCdata;
	}
	
	public int size(){
		synchronized (mLockObject) {
			return mQueue.size();
		}
	}
	
	public void clearData(){
		synchronized (mLockObject) {
			mQueue.clear();
		}
	}
	
	/**
	 * 队列追加data
	 * @param data
	 * @return
	 */
	public boolean offerData(ChannelData data){
		if(data == null) return false;
		synchronized (mLockObject) {
			return mQueue.offer(data);
		}
	}
	
	/**
	 * 获取头数据，但不删除
	 * @return
	 */
	public ChannelData peekData(){
		synchronized (mLockObject) {
			if(mQueue.size() <= 0){
				return null;
			}
			return mQueue.peek();
		}
	}
	
	/**
	 * 获取头数据并删除
	 * @return
	 */
	public ChannelData pollData(){
		synchronized (mLockObject) {
			if(mQueue.size() <= 0){
				return null;
			}
			return mQueue.poll();
		}
	}
	
	public static class ChannelData{
		int mType;
		int mLength;
		byte[] data;
	}

}
