package com.kfirvr.vrcontrolc.utils;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author hj
 * @time 2015-03-17
 * 
 */
public class SharePrefUtil {
	private static String tag = SharePrefUtil.class.getSimpleName();
	public final static String SP_NAME = "config";

	/**
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveBoolean(Context context, String key, boolean value) {
		// if (sp == null)
		SharedPreferences sp = context.getSharedPreferences(SP_NAME,
				Context.MODE_MULTI_PROCESS);
		sp.edit().putBoolean(key, value).commit();
	}

	/**
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveString(Context context, String key, String value) {
		// if (sp == null)
		SharedPreferences sp = context.getSharedPreferences(SP_NAME,
				Context.MODE_MULTI_PROCESS);
		sp.edit().putString(key, value).commit();

	}

	public static void clear(Context context) {
		// if (sp == null)
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().clear().commit();
	}

	/**
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveLong(Context context, String key, long value) {
		// if (sp == null)
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putLong(key, value).commit();
	}

	/**
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveInt(Context context, String key, int value) {
		// if (sp == null)
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putInt(key, value).commit();
	}

	/**
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveFloat(Context context, String key, float value) {
		// if (sp == null)
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putFloat(key, value).commit();
	}

	/**
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(Context context, String key, String defValue) {
		// if (sp == null)
		SharedPreferences sp = context.getSharedPreferences(SP_NAME,
				Context.MODE_MULTI_PROCESS);
		return sp.getString(key, defValue);
	}

	/**
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(Context context, String key, int defValue) {
		// if (sp == null)
		SharedPreferences sp = context.getSharedPreferences(SP_NAME,
				Context.MODE_MULTI_PROCESS);
		return sp.getInt(key, defValue);
	}

	/**
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static long getLong(Context context, String key, long defValue) {
		// if (sp == null)
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
		return sp.getLong(key, defValue);
	}

	/**
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static float getFloat(Context context, String key, float defValue) {
		// if (sp == null)
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
		return sp.getFloat(key, defValue);
	}

	/**
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		// if (sp == null)
		SharedPreferences sp = context.getSharedPreferences(SP_NAME,Context.MODE_MULTI_PROCESS);
				
		return sp.getBoolean(key, defValue);
	}

	
	public static void remove(Context context, String name) {
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().remove(name).commit();
	}
}
