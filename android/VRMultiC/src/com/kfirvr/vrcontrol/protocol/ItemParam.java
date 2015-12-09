package com.kfirvr.vrcontrol.protocol;

public class ItemParam {
	
	public int itemid;
	public String pic;
	public String name;
	public String intro;
	public int state;  // 0:未下载 1：下载中 2：暂停下载 3：等待下载 4：下载完成 5：下载失败 6：未安装 
	public int progress;
	public String content;

}
