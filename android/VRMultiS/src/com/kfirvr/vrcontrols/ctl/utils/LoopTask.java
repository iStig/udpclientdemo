package com.kfirvr.vrcontrols.ctl.utils;

import com.kfirvr.vrcontrol.SLog;


public abstract class LoopTask extends AbstractTask {
	private volatile boolean isRunning = false;
	private volatile boolean isCanceling = false;
	public LoopTask(String name){
		super(name);
	}

	@Override
	protected void runTask() {
		isRunning = true;
		try{
			while(isRunning){
				//Log.d(TAG, getName() + " isRunning in loop " + isRunning);
				doLoopTask();
			}
		}catch(Exception e){
			SLog.e("task " + getName() + " error", e);
		}catch(StopSignal stop){
			SLog.i("task " + getName() + " is forced to stop");
		}
		isCanceling = false;
	}

	@Override
	public void stop(){
		SLog.i("stopping " + getName());
		isRunning = false;
		isCanceling = true;
	}
	
	public void forceBreak(){//TODO should be called only from current thread
		SLog.i("force breaking " + getName());
		stop();
		throw new StopSignal();
	}
	
	public boolean isCanceling(){
		return isCanceling;
	}

	protected abstract void doLoopTask();
	
	private static class StopSignal extends Error{
		private StopSignal(){
			super("forceBreak can only called from the task thread");
		}
		private static final long serialVersionUID = 1L;
	}

}

