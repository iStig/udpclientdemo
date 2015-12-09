package com.kfirvr.vrcontrols.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.kfirvr.vrcontrol.SLog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaMetadataRetriever;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Base64;

public class CommonUtil {

	public static final byte[] intToByteArray(int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16),
				(byte) (value >>> 8), (byte) value };
	}

	public static final int byteArrayToInt(byte[] bytes) {
		int result = -1;
		if (bytes == null || bytes.length != 4) {
			return -1;
		}
		result = (bytes[0] & 0xFF) << 24;
		result |= (bytes[1] & 0xFF) << 16;
		result |= (bytes[2] & 0xFF) << 8;
		result |= (bytes[3] & 0xFF);
		return result;
	}

	public static Bitmap stringAsImg(String str) {
		byte[] bytes = Base64.decode(str, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	public static byte[] imageAsBytes(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SLog.d("Leeon is recyclyed before compress:" + image.isRecycled());
		image.compress(CompressFormat.PNG, 50, baos);
		image.recycle();
		byte[] bytes = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			SLog.e("", e);
		}
		SLog.d("Leeon compressed " + bytes.length + " bytes img");
		return bytes;
	}

	public static Bitmap bytesAsImage(byte[] bytes) {
		SLog.d("Leeon decompress " + bytes.length + " bytes data");
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	public static String imgAsString(Bitmap image) {
		String pngString = Base64.encodeToString(imageAsBytes(image),
				Base64.DEFAULT);
		return pngString;
	}

	public static String getVideoThumbnail(String filePath) {
		String result = null;
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(filePath);
			bitmap = retriever.getFrameAtTime();
			if (bitmap != null) {
				result = imgAsString(bitmap);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
