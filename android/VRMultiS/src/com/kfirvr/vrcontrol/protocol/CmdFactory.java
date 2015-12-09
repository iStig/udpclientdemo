package com.kfirvr.vrcontrol.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kfirvr.vrcontrol.SLog;

public class CmdFactory {

	public static JSONObject createRequestSpace() {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMD_TYPE, CmdUtils.REQUEST_SPACE);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static JSONObject createResponseSpace(long totalSize, long useSize) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMD_TYPE, CmdUtils.RESPONSE_GET_SPACE);
			object.put(CmdUtils.NAME_TOTALSIZE, totalSize);
			object.put(CmdUtils.NAME_USESIZE, useSize);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static SpaceParam decodeResponseSpace(JSONObject object) {
		SpaceParam param = new SpaceParam();
		if (object == null)
			return param;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.RESPONSE_GET_SPACE) {
				SLog.e("Error Cmd type : " + type + " need:"
						+ CmdUtils.RESPONSE_GET_SPACE);
				return null;
			}
			param.totalSize = object.getInt(CmdUtils.NAME_TOTALSIZE);
			param.useSize = object.getInt(CmdUtils.NAME_USESIZE);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			param = null;
		}
		return param;
	}

	public static JSONObject createRequest360VideoList() {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMD_TYPE, CmdUtils.REQUEST_360VIDEO_LIST);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static JSONObject createResponse360VideoList(ItemParam[] list) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMDID, CmdUtils.RESPONSE_GET_360VIDEO_LIST);
			JSONArray mArray = new JSONArray();
			for (int i = 0; i < list.length; i++) {
				JSONObject item = new JSONObject();
				item.put(CmdUtils.NAME_ITEMID, list[i].itemid);
				item.put(CmdUtils.NAME_ITEMNAME, list[i].name);
				item.put(CmdUtils.NAME_INTRO, list[i].intro);
				item.put(CmdUtils.NAME_STATE, list[i].state);
				item.put(CmdUtils.NAME_PROGRESS, list[i].progress);
				mArray.put(item);
			}
			object.put(CmdUtils.NAME_ITEMLIST, mArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static ItemParam[] decodeResponse360VideoList(JSONObject object) {
		ItemParam[] result = null;
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.RESPONSE_GET_360VIDEO_LIST) {
				SLog.e("Error Cmd type : " + type + " need:"
						+ CmdUtils.RESPONSE_GET_360VIDEO_LIST);
				return null;
			}
			JSONArray mArray = object.getJSONArray(CmdUtils.NAME_ITEMLIST);
			if (mArray != null && mArray.length() > 0) {
				result = new ItemParam[mArray.length()];
				for (int i = 0; i < mArray.length(); i++) {
					result[i].itemid = object.getInt(CmdUtils.NAME_ITEMID);
					result[i].name = object.getString(CmdUtils.NAME_ITEMNAME);
					result[i].intro = object.getString(CmdUtils.NAME_INTRO);
					result[i].state = object.getInt(CmdUtils.NAME_STATE);
					result[i].progress = object.getInt(CmdUtils.NAME_PROGRESS);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = null;
		}
		return result;
	}

	public static JSONObject createRequestVRGameList() {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMD_TYPE, CmdUtils.REQUEST_VRGAME_LIST);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static JSONObject createResponseVRGameList(ItemParam[] list) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMDID, CmdUtils.RESPONSE_GET_VRGAME_LIST);
			JSONArray mArray = new JSONArray();
			for (int i = 0; i < list.length; i++) {
				JSONObject item = new JSONObject();
				item.put(CmdUtils.NAME_ITEMID, list[i].itemid);
				item.put(CmdUtils.NAME_ITEMNAME, list[i].name);
				item.put(CmdUtils.NAME_INTRO, list[i].intro);
				item.put(CmdUtils.NAME_STATE, list[i].state);
				item.put(CmdUtils.NAME_PROGRESS, list[i].progress);
				mArray.put(item);
			}
			object.put(CmdUtils.NAME_ITEMLIST, mArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static ItemParam[] decodeResponseVRGameList(JSONObject object) {
		ItemParam[] result = null;
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.RESPONSE_GET_VRGAME_LIST) {
				SLog.e("Error Cmd type : " + type + " need:"
						+ CmdUtils.RESPONSE_GET_VRGAME_LIST);
				return null;
			}
			JSONArray mArray = object.getJSONArray(CmdUtils.NAME_ITEMLIST);
			if (mArray != null && mArray.length() > 0) {
				result = new ItemParam[mArray.length()];
				for (int i = 0; i < mArray.length(); i++) {
					result[i].itemid = object.getInt(CmdUtils.NAME_ITEMID);
					result[i].name = object.getString(CmdUtils.NAME_ITEMNAME);
					result[i].intro = object.getString(CmdUtils.NAME_INTRO);
					result[i].state = object.getInt(CmdUtils.NAME_STATE);
					result[i].progress = object.getInt(CmdUtils.NAME_PROGRESS);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = null;
		}
		return result;
	}

	public static JSONObject createRequestVRAppList() {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMD_TYPE, CmdUtils.REQUEST_VRAPP_LIST);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static JSONObject createResponseVRAppList(ItemParam[] list) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMDID, CmdUtils.RESPONSE_GET_VRAPP_LIST);
			JSONArray mArray = new JSONArray();
			for (int i = 0; i < list.length; i++) {
				JSONObject item = new JSONObject();
				item.put(CmdUtils.NAME_ITEMID, list[i].itemid);
				item.put(CmdUtils.NAME_ITEMNAME, list[i].name);
				item.put(CmdUtils.NAME_INTRO, list[i].intro);
				item.put(CmdUtils.NAME_STATE, list[i].state);
				item.put(CmdUtils.NAME_PROGRESS, list[i].progress);
				mArray.put(item);
			}
			object.put(CmdUtils.NAME_ITEMLIST, mArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static ItemParam[] decodeResponseVRAppList(JSONObject object) {
		ItemParam[] result = null;
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.RESPONSE_GET_VRAPP_LIST) {
				SLog.e("Error Cmd type : " + type + " need:"
						+ CmdUtils.RESPONSE_GET_VRAPP_LIST);
				return null;
			}
			JSONArray mArray = object.getJSONArray(CmdUtils.NAME_ITEMLIST);
			if (mArray != null && mArray.length() > 0) {
				result = new ItemParam[mArray.length()];
				for (int i = 0; i < mArray.length(); i++) {
					result[i].itemid = object.getInt(CmdUtils.NAME_ITEMID);
					result[i].name = object.getString(CmdUtils.NAME_ITEMNAME);
					result[i].intro = object.getString(CmdUtils.NAME_INTRO);
					result[i].state = object.getInt(CmdUtils.NAME_STATE);
					result[i].progress = object.getInt(CmdUtils.NAME_PROGRESS);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = null;
		}
		return result;
	}

	public static JSONObject createRequestLocalVideoList() {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMD_TYPE, CmdUtils.REQUEST_LOCALVIDEO_LIST);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static JSONObject createResponseLocalVideoList(LocalVideoParam[] list) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMDID,
					CmdUtils.RESPONSE_GET_LOCALVIDEO_LIST);
			JSONArray mArray = new JSONArray();
			for (int i = 0; i < list.length; i++) {
				JSONObject item = new JSONObject();
				item.put(CmdUtils.NAME_PIC, list[i].pic);
				item.put(CmdUtils.NAME_ITEMNAME, list[i].name);
				item.put(CmdUtils.NAME_SIZE, list[i].size);
				item.put(CmdUtils.NAME_DATE, list[i].date);
				item.put(CmdUtils.NAME_PATH, list[i].path);
				mArray.put(item);
			}
			object.put(CmdUtils.NAME_ITEMLIST, mArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static LocalVideoParam[] decodeResponseLocalVideoList(
			JSONObject object) {
		LocalVideoParam[] result = null;
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.RESPONSE_GET_LOCALVIDEO_LIST) {
				SLog.e("Error Cmd type : " + type + " need:"
						+ CmdUtils.RESPONSE_GET_LOCALVIDEO_LIST);
				return null;
			}
			JSONArray mArray = object.getJSONArray(CmdUtils.NAME_ITEMLIST);
			if (mArray != null && mArray.length() > 0) {
				result = new LocalVideoParam[mArray.length()];
				for (int i = 0; i < mArray.length(); i++) {
					result[i].pic = object.getString(CmdUtils.NAME_PIC);
					result[i].name = object.getString(CmdUtils.NAME_ITEMNAME);
					result[i].size = object.getLong(CmdUtils.NAME_SIZE);
					result[i].date = object.getLong(CmdUtils.NAME_DATE);
					result[i].path = object.getString(CmdUtils.NAME_PATH);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = null;
		}
		return result;
	}

	public static JSONObject createRequestLocalAppList() {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMD_TYPE, CmdUtils.REQUEST_LOCALAPP_LIST);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static JSONObject createResponseLocalAppList(LocalAppParam[] list) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMDID, CmdUtils.RESPONSE_GET_LOCALAPP_LIST);
			JSONArray mArray = new JSONArray();
			for (int i = 0; i < list.length; i++) {
				JSONObject item = new JSONObject();
				item.put(CmdUtils.NAME_PIC, list[i].pic);
				item.put(CmdUtils.NAME_ITEMNAME, list[i].name);
				item.put(CmdUtils.NAME_PACKAGENAME, list[i].packageName);
				mArray.put(item);
			}
			object.put(CmdUtils.NAME_ITEMLIST, mArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static LocalAppParam[] decodeResponseLocalAppList(JSONObject object) {
		LocalAppParam[] result = null;
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.RESPONSE_GET_LOCALAPP_LIST) {
				SLog.e("Error Cmd type : " + type + " need:"
						+ CmdUtils.RESPONSE_GET_LOCALAPP_LIST);
				return null;
			}
			JSONArray mArray = object.getJSONArray(CmdUtils.NAME_ITEMLIST);
			if (mArray != null && mArray.length() > 0) {
				result = new LocalAppParam[mArray.length()];
				for (int i = 0; i < mArray.length(); i++) {
					result[i].pic = object.getString(CmdUtils.NAME_PIC);
					result[i].name = object.getString(CmdUtils.NAME_ITEMNAME);
					result[i].packageName = object
							.getString(CmdUtils.NAME_PACKAGENAME);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = null;
		}
		return result;
	}

	public static JSONObject createRequestDelItem(int itemid) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMD_TYPE, CmdUtils.REQUEST_DEL_ITEM);
			object.put(CmdUtils.NAME_ITEMID, itemid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static int decodeRequestDelItem(JSONObject object) {
		int result = -1;
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.REQUEST_DEL_ITEM) {
				SLog.e("Error Cmd type : " + type + " need : "
						+ CmdUtils.REQUEST_DEL_ITEM);
				return result;
			}
			result = object.getInt(CmdUtils.NAME_ITEMID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = -1;
		}
		return result;
	}

	public static JSONObject createRequestDownloadItem(int itemid) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMD_TYPE, CmdUtils.REQUEST_DOWNLOAD_ITEM);
			object.put(CmdUtils.NAME_ITEMID, itemid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static int decodeRequestDownloadItem(JSONObject object) {
		int result = -1;
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.REQUEST_DOWNLOAD_ITEM) {
				SLog.e("Error Cmd type : " + type + " need : "
						+ CmdUtils.REQUEST_DOWNLOAD_ITEM);
				return result;
			}
			result = object.getInt(CmdUtils.NAME_ITEMID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = -1;
		}
		return result;
	}

	public static JSONObject createRequestPauseItem(int itemid) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMD_TYPE, CmdUtils.REQUEST_PAUSE_ITEM);
			object.put(CmdUtils.NAME_ITEMID, itemid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static int decodeRequestPauseItem(JSONObject object) {
		int result = -1;
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.REQUEST_PAUSE_ITEM) {
				SLog.e("Error Cmd type : " + type + " need : "
						+ CmdUtils.REQUEST_PAUSE_ITEM);
				return result;
			}
			result = object.getInt(CmdUtils.NAME_ITEMID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = -1;
		}
		return result;
	}

	public static JSONObject createRequestDelLocalVideo(String path) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMD_TYPE, CmdUtils.REQUEST_DEL_LOCALVIDEO);
			object.put(CmdUtils.NAME_PATH, path);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static String decodeRequestDelLocalVideo(JSONObject object) {
		String result = null;
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.REQUEST_DEL_LOCALVIDEO) {
				SLog.e("Error Cmd type : " + type + " need : "
						+ CmdUtils.REQUEST_DEL_LOCALVIDEO);
				return result;
			}
			result = object.getString(CmdUtils.NAME_PATH);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = null;
		}
		return result;
	}

	public static JSONObject createRequestDelLocalApp(String packageName) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMD_TYPE, CmdUtils.REQUEST_DEL_LOCALAPP);
			object.put(CmdUtils.NAME_PACKAGENAME, packageName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static String decodeRequestDelLocalApp(JSONObject object) {
		String result = null;
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.REQUEST_DEL_LOCALAPP) {
				SLog.e("Error Cmd type : " + type + " need : "
						+ CmdUtils.REQUEST_DEL_LOCALAPP);
				return result;
			}
			result = object.getString(CmdUtils.NAME_PACKAGENAME);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = null;
		}
		return result;
	}

	public static JSONObject createRequestLocalVideoPic(String[] list) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMD_TYPE, CmdUtils.REQUEST_LOCALVIDEO_PIC);
			JSONArray mArray = new JSONArray();
			for (int i = 0; i < list.length; i++) {
				JSONObject item = new JSONObject();
				item.put(CmdUtils.NAME_PATH, list[i]);
				mArray.put(item);
			}
			object.put(CmdUtils.NAME_PATH_LIST, mArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static String[] decodeRequestLocalVideoPic(JSONObject object) {
		String[] result = null;
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.REQUEST_LOCALVIDEO_PIC) {
				SLog.e("Error Cmd type : " + type + " need : "
						+ CmdUtils.REQUEST_LOCALVIDEO_PIC);
				return result;
			}
			JSONArray mArray = object.getJSONArray(CmdUtils.NAME_PATH_LIST);
			if (mArray != null && mArray.length() > 0) {
				result = new String[mArray.length()];
				for (int i = 0; i < mArray.length(); i++) {
					result[i] = object.getString(CmdUtils.NAME_PATH);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = null;
		}
		return result;
	}

	public static JSONObject createRequestLocalAppPic(String[] list) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMD_TYPE, CmdUtils.REQUEST_LOCALAPP_PIC);
			JSONArray mArray = new JSONArray();
			for (int i = 0; i < list.length; i++) {
				JSONObject item = new JSONObject();
				item.put(CmdUtils.NAME_PACKAGENAME, list[i]);
				mArray.put(item);
			}
			object.put(CmdUtils.NAME_PACKAGENAME_LIST, mArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static String[] decodeRequestLocalAppPic(JSONObject object) {
		String[] result = null;
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.REQUEST_LOCALAPP_PIC) {
				SLog.e("Error Cmd type : " + type + " need : "
						+ CmdUtils.REQUEST_LOCALAPP_PIC);
				return result;
			}
			JSONArray mArray = object
					.getJSONArray(CmdUtils.NAME_PACKAGENAME_LIST);
			if (mArray != null && mArray.length() > 0) {
				result = new String[mArray.length()];
				for (int i = 0; i < mArray.length(); i++) {
					result[i] = object.getString(CmdUtils.NAME_PACKAGENAME);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = null;
		}
		return result;
	}

	public static JSONObject createResponseItemUpdate(ItemParam[] list) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMDID, CmdUtils.RESPONSE_UPDATE_ITEM);
			JSONArray mArray = new JSONArray();
			for (int i = 0; i < list.length; i++) {
				JSONObject item = new JSONObject();
				item.put(CmdUtils.NAME_ITEMID, list[i].itemid);
				item.put(CmdUtils.NAME_STATE, list[i].state);
				item.put(CmdUtils.NAME_PROGRESS, list[i].progress);
				mArray.put(item);
			}
			object.put(CmdUtils.NAME_ITEMLIST, mArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static ItemParam[] decodeResponseItemUpdate(JSONObject object) {
		ItemParam[] result = null;
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.RESPONSE_UPDATE_ITEM) {
				SLog.e("Error Cmd type : " + type + " need:"
						+ CmdUtils.RESPONSE_UPDATE_ITEM);
				return null;
			}
			JSONArray mArray = object.getJSONArray(CmdUtils.NAME_ITEMLIST);
			if (mArray != null && mArray.length() > 0) {
				result = new ItemParam[mArray.length()];
				for (int i = 0; i < mArray.length(); i++) {
					result[i].itemid = object.getInt(CmdUtils.NAME_ITEMID);
					result[i].state = object.getInt(CmdUtils.NAME_STATE);
					result[i].progress = object.getInt(CmdUtils.NAME_PROGRESS);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = null;
		}
		return result;
	}

	public static JSONObject createResponseLocalVideoDel(LocalVideoParam param) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMDID, CmdUtils.RESPONSE_DEL_LOCALVIDEO);
			object.put(CmdUtils.NAME_PATH, param.path);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static String decodeResponseLocalVideoDel(JSONObject object) {
		String result = null;
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.RESPONSE_DEL_LOCALVIDEO) {
				SLog.e("Error Cmd type : " + type + " need:"
						+ CmdUtils.RESPONSE_DEL_LOCALVIDEO);
				return null;
			}
			result = object.getString(CmdUtils.NAME_PATH);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = null;
		}
		return result;
	}

	public static JSONObject createResponseLocalAppDel(LocalAppParam param) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMDID, CmdUtils.RESPONSE_DEL_LOCALAPP);
			object.put(CmdUtils.NAME_PACKAGENAME, param.packageName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static String decodeResponseLocalAppDel(JSONObject object) {
		String result = null;
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.RESPONSE_DEL_LOCALAPP) {
				SLog.e("Error Cmd type : " + type + " need:"
						+ CmdUtils.RESPONSE_DEL_LOCALAPP);
				return null;
			}
			result = object.getString(CmdUtils.NAME_PACKAGENAME);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = null;
		}
		return result;
	}

	public static JSONObject createResponseLocalVideoAdd(LocalVideoParam param) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMDID, CmdUtils.RESPONSE_ADD_LOCALVIDEO);
			object.put(CmdUtils.NAME_PIC, param.pic);
			object.put(CmdUtils.NAME_ITEMNAME, param.name);
			object.put(CmdUtils.NAME_SIZE, param.size);
			object.put(CmdUtils.NAME_DATE, param.date);
			object.put(CmdUtils.NAME_PATH, param.path);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static LocalVideoParam decodeResponseLocalVideoAdd(JSONObject object) {
		LocalVideoParam result = new LocalVideoParam();
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.RESPONSE_ADD_LOCALVIDEO) {
				SLog.e("Error Cmd type : " + type + " need:"
						+ CmdUtils.RESPONSE_ADD_LOCALVIDEO);
				return null;
			}
			result.pic = object.getString(CmdUtils.NAME_PIC);
			result.name = object.getString(CmdUtils.NAME_ITEMNAME);
			result.size = object.getLong(CmdUtils.NAME_SIZE);
			result.date = object.getLong(CmdUtils.NAME_DATE);
			result.path = object.getString(CmdUtils.NAME_PATH);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = null;
		}
		return result;
	}

	public static JSONObject createResponseLocalAppAdd(LocalAppParam param) {
		JSONObject object = new JSONObject();
		try {
			object.put(CmdUtils.NAME_CMDID, CmdUtils.RESPONSE_ADD_LOCALAPP);
			object.put(CmdUtils.NAME_PACKAGENAME, param.packageName);
			object.put(CmdUtils.NAME_PIC, param.pic);
			object.put(CmdUtils.NAME_ITEMNAME, param.name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			object = null;
		}
		return object;
	}

	public static LocalAppParam decodeResponseLocalAppAdd(JSONObject object) {
		LocalAppParam result = new LocalAppParam();
		if (object == null)
			return result;
		try {
			int type = object.getInt(CmdUtils.NAME_CMD_TYPE);
			if (type != CmdUtils.RESPONSE_ADD_LOCALAPP) {
				SLog.e("Error Cmd type : " + type + " need:"
						+ CmdUtils.RESPONSE_ADD_LOCALAPP);
				return null;
			}
			result.pic = object.getString(CmdUtils.NAME_PIC);
			result.name = object.getString(CmdUtils.NAME_ITEMNAME);
			result.packageName = object.getString(CmdUtils.NAME_PACKAGENAME);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			result = null;
		}
		return result;
	}

}
