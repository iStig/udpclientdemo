package com.kfirvr.vrcontrolc.ctl.utils;

import com.kfirvr.vrcontrol.SLog;



public abstract class AbstractTask implements Runnable {
	
	public static final int STATUS_IDLE = 0;
	public static final int STATUS_RUNNING = 1;
	public static final int STATUS_FINISHED = 2;
	public static final int STATUS_SUSPEND = 3;
	
	private String name;
	//private boolean isRunning = false;
	private volatile int status = STATUS_IDLE;
	private Object holdLock = new Object();
	private boolean holdFlag = true;
	private TaskListener listener;
	
	private void hold(){
		synchronized (holdLock) {
			if(holdFlag){
				SLog.i("task " + name + " holding");
				try {
					holdLock.wait(5*1000);
				} catch (InterruptedException e) {
				}
			}
		}
		//anyway we sleep a tiny time waiting for task reference return by CommonUtil.runTask()
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
	}
	
	void resume(){
		synchronized (holdLock) {
			holdFlag = false;
			holdLock.notifyAll();
		}
	}
	
	public AbstractTask(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	void setListener(TaskListener listener){
		this.listener = listener;
	}
	
	public int getStatus(){
		return status;
	}

	@Override
	public void run() {
		status = STATUS_SUSPEND;
		hold();
		SLog.i("task " + name + " started");
		status = STATUS_RUNNING;
		try{
			onStart();
		}catch(Exception e){
			SLog.e("", e);
		}
		try{
			//isRunning = true;
			runTask();	
			//isRunning = false;
		}catch(Exception e){
			SLog.e( "", e);
		}
		try{
			onFinish();
		}catch(Exception e){
			SLog.e("", e);
		}
		status = STATUS_FINISHED;
		
		if(listener != null){
			listener.onFinished();
		}
		SLog.i("task " + name + " end");
	}
	
	protected abstract void onStart();
	
	protected abstract void onFinish();
	
	protected abstract void runTask();
	
	public abstract void stop();

}

interface TaskListener {
	public void onFinished();
}

