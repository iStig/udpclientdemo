package com.kfirvr.vrcontrols.ctl.utils;

import com.kfirvr.vrcontrol.SLog;

public class TimerTask extends AbstractTask{

	private int secs = 0;
	private ITimeoutAction action;
	private boolean isReseting;
	private boolean isStopping;
	private Object lock = new Object();
	
	public TimerTask(String name, int seconds, ITimeoutAction action){
		super("TimerTask-" + name);
		secs = seconds;
		this.action = action;
	}

	@Override
	protected void onFinish() {
	}
	
	@Override
	protected void onStart() {
	}
	
	@Override
	protected void runTask() {
		long milSecs = secs*1000;
		synchronized (lock) {
			do{
				isReseting = false;
				try {
					lock.wait(milSecs);
				} catch (InterruptedException e) {
				}
			}while(isReseting);
		}
		
		if(!isStopping){
			action.onTimeout();
		}
	}
	
	public void reset(){
		synchronized (lock) {
			if(getStatus() == STATUS_RUNNING){
				isReseting = true;
				lock.notifyAll();
			}else{
				SLog.i("not in running state, can't reset");
			}
		}
	}

	@Override
	public void stop() {
		synchronized (lock) {
			isReseting = false;
			isStopping = true;
			lock.notifyAll();
		}
		SLog.i("timer task " + getName() + " is stopping");
	}

}

