package com.kfirvr.vrcontrol;

import com.kfirvr.vrcontrols.server.MainServer;

import android.app.Application;
import android.content.Intent;

public class MainApplication extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		Intent sintent = new Intent(this, MainServer.class);
		startService(sintent);
	}

}
