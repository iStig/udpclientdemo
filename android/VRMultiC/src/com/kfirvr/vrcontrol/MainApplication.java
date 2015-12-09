package com.kfirvr.vrcontrol;

import com.kfirvr.vrcontrolc.server.ClientServer;

import android.app.Application;
import android.content.Intent;

public class MainApplication extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		Intent sintent = new Intent(this, ClientServer.class);
		startService(sintent);
	}

}
