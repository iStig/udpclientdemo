package com.kfirvr.vrcontrolc.ctl.utils;

import java.util.concurrent.Future;

import com.kfirvr.vrcontrol.SLog;


public class TaskResult implements Runnable{
	private AbstractTask task;
	private String name;
	private volatile boolean isCanceled = false;
	@SuppressWarnings("rawtypes")
	private Future future;
	private Object waitLock = new Object();
	
	TaskResult(AbstractTask task){
		this.task = task;
		name = task.getName();
		task.setListener(new TaskListener() {
			
			@Override
			public void onFinished() {
				synchronized (waitLock) {
					waitLock.notifyAll();
				}
			}
		});
	}
	
	@SuppressWarnings("rawtypes")
	void setFuture(Future future){
		this.future = future;
	}
	
	public AbstractTask getTask(){
		return task;
	}
	
	@Override
	public void run() {
		task.run();
	}
	
	public void resumeTask(){
		if(task != null){
			task.resume();
		}
	}
	
	public AbstractTask wait4End(){
		synchronized (waitLock) {
			if(!isTaskFinished()){
				try {
					SLog.i("waiting for " + task.getName() + " to end");
					waitLock.wait();
					SLog.i("task " + task.getName() + " is ended");
				} catch (InterruptedException e) {
				}
			}else{
				SLog.i("task already completed");
			}
		}
		
		return task;
	}
	
	public boolean isTaskFinished(){
		return task.getStatus() == AbstractTask.STATUS_FINISHED;
	}
	
	public synchronized void cancel(){//cancel may be called many times in different threads, so sync it
		try{
			if(!isCanceled){
				SLog.i("canceling task " + name);
				task.stop();
				future.cancel(true);
			}else{
				SLog.i("task " + name + " already canceled");
			}
		}catch(Exception e){
			SLog.e("", e);
		}
		//task = null;
		//future = null;
		isCanceled = true;
	}
	
	public boolean isCanceled(){
		return isCanceled;
	}
}

