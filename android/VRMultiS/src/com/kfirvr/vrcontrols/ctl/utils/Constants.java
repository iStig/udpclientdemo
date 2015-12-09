package com.kfirvr.vrcontrols.ctl.utils;


/**
 * @author Leeon
 *
 * 2013-8-5 下午2:07:09
 */

public class Constants {
	public static final String SOCKET_CMD_SERVER = "SOCKET_CMD_SERVER";//TODO for client side, we may need to hide this public constant
	public static final String SOCKET_DATA_SERVER = "SOCKET_DATA_SERVER";
	
	public static final String SERVICE_CTL_RESULT_GOOD = "CTL_000";
	public static final String SERVICE_CTL_RESULT_COMMON_ERR = "CTL_001";
	public static final String SERVICE_CTL_RESULT_NO_TOKEN = "CTL_002";
	public static final String SERVICE_CTL_RESULT_NO_ACTIVE_CODE = "CTL_003";
	public static final String SERVICE_CTL_RESULT_WRONG_PARAM = "CTL_004";
	public static final String SERVICE_CTL_RESULT_NOT_IMPLEMENTED = "CTL_005";
	
	
	public static final int CODE_GOOD = 0;
	public static final int CODE_NETWORK_ERROR = -1;
	public static final int CODE_LOCAL_IO_ERROR = -2;
	public static final int CODE_SERVICE_ERROR = -3;
	public static final int CODE_LOCAL_IO_NOT_READY_ERROR = -4;
	public static final int CODE_UNKNOWN_ERROR = -5;
	public static final int CODE_LOCAL_UNCONNECTED_ERROR = -6;
	public static final int CODE_LOCAL_SDK_ERROR = -7;
	public static final int CODE_LOCAL_NOT_REGISTERED_ERROR = -8;
	public static final int CODE_LOCAL_IO_INACTIVE_ERROR = -9;
	public static final int CODE_LOCAL_CODEC_ERROR = -10;
	
	public static final String SOCKET_CLOSED = "SocketClosed";
	
	
	public static final String ACTION_START_SERVICE = "action.f1.start.service";
	public static final String PRE_COMM_DATA = "com.aphone.f1.collaboration_preferences";
	public static final String SERVICE_STARTED = "android.intent.action.KAISER_SERVICE_STARTED";
	
	public static final String PACKAGE_MASTER = ":M:";
	public static final String PACKAGE_SLAVE = ":S:";
	
	public static final String ROBOT_DIAL_NUMBER = "number";
	public static final String ROBOT_ID = "000";
	public static final String ACTION_ROBOT_CALL_SCREEN = "com.aphone.f1.enhancement.ROBOT_DIALED";
	public static final String ACTION_DISCONNECT_NETWORK = "com.aphone.f1.server.DISCONNECT_NETWORK";
	
	public static final int CODEC_BUFFER_SIZE = 100*1024;
	
	private Constants(){
		
	}
}

