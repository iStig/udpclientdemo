package com.kfirvr.vrcontrol.protocol;

public class CmdUtils {
	
	public static final String NAME_CMD_TYPE = "CMD_NAME";
	
	//BASE CMD
	public static final int CMD_CHECK_PAIR_CODE = 1;
	
	//REQUEST
	public static final int REQUEST_SPACE = 100;
	public static final int REQUEST_360VIDEO_LIST = 101;
	public static final int REQUEST_VRGAME_LIST = 102;
	public static final int REQUEST_VRAPP_LIST = 103;
	public static final int REQUEST_LOCALVIDEO_LIST = 104;
	public static final int REQUEST_LOCALAPP_LIST = 105;
	public static final int REQUEST_DEL_ITEM = 106;
	public static final int REQUEST_DOWNLOAD_ITEM = 107;
	public static final int REQUEST_PAUSE_ITEM = 108;
	public static final int REQUEST_DEL_LOCALVIDEO = 109;
	public static final int REQUEST_DEL_LOCALAPP = 110;
	public static final int REQUEST_LOCALVIDEO_PIC = 111;
	public static final int REQUEST_LOCALAPP_PIC = 112;
			
	//RESPONSE
	public static final int RESPONSE_GET_SPACE = 1000;
	public static final int RESPONSE_GET_360VIDEO_LIST  = 1001;
	public static final int RESPONSE_GET_VRGAME_LIST = 1002;
	public static final int RESPONSE_GET_VRAPP_LIST = 1003;
	public static final int RESPONSE_GET_LOCALVIDEO_LIST = 1004;
	public static final int RESPONSE_GET_LOCALAPP_LIST = 1005;
	public static final int RESPONSE_GET_LOCALVIDEO_PIC = 1006;
	public static final int RESPONSE_GET_LOCALAPP_PIC = 1007;
	public static final int RESPONSE_UPDATE_ITEM = 1008;
	public static final int RESPONSE_DEL_LOCALVIDEO = 1009;
	public static final int RESPONSE_DEL_LOCALAPP = 1010;
	public static final int RESPONSE_ADD_LOCALVIDEO = 1011;
	public static final int RESPONSE_ADD_LOCALAPP = 1012;
	
	public static final String NAME_ITEMLIST = "itemList";
	public static final String NAME_CMDID = "cmdid";
	public static final String NAME_ITEMID = "itemid";
	public static final String NAME_PATH = "path";
	public static final String NAME_PACKAGENAME = "packageName";
	public static final String NAME_TOTALSIZE = "totalSize";
	public static final String NAME_USESIZE = "useSize";
	public static final String NAME_SIZE = "size";
	public static final String NAME_DATE = "idate";
	public static final String NAME_PIC = "pic";
	public static final String NAME_ITEMNAME = "name";
	public static final String NAME_INTRO = "intro";
	public static final String NAME_STATE = "state";
	public static final String NAME_PROGRESS = "progress";
	public static final String NAME_PATH_LIST = "pathList";
	public static final String NAME_PACKAGENAME_LIST = "packageNameList";

	//REPORT
	public static final int REPORT_LINK_STATUS = 10000;
	public static final int REPORT_CMD_STATUS = 10001;
	
}
