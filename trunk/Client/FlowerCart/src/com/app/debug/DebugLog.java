package com.app.debug;

import android.util.Log;

import com.app.util.Constants;

@SuppressWarnings("rawtypes")
public class DebugLog {
	private static final boolean DEBUG = true;
	private static final boolean ERROR = true;
	private static final boolean INFO = true;
	private static final String DEBUG_TAG = "Thinh.Nguyen";

	// private static final DateFormat dateFormat = new SimpleDateFormat(
	// "MM/dd HH:mm");

	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.d(tag, "Vesion : " + Constants.APP_VERSION 
					+ formatDateNow() + " - Debug : " + msg);
		}
	}

	private static String formatDateNow() {
		// return " - Time : " + dateFormat.format(Calendar.getInstance().getTime());
		return "";
	}

	public static void d(String msg, Class c) {

		if (DEBUG) {
			String className = c.getName();
			try {
				className = className.substring(className.lastIndexOf("."));
			} catch (Exception e) {
			}

			Log.d(DEBUG_TAG, "[" + className + "]" + " Version : "
					+ Constants.APP_VERSION + formatDateNow()
					+ " - Debug : " + msg);
		}
	}

	public static void e(String msg, Class c) {
		if (ERROR) {
			String className = c.getName();
			try {
				className = className.substring(className.lastIndexOf("."));
			} catch (Exception e) {
			}
			Log.e(DEBUG_TAG, "[" + className + "]" + " Version : "
					+ Constants.APP_VERSION + formatDateNow()
					+ " - Error : " + msg);
		}
	}

	public static void i(String msg, Class c) {
		if (INFO) {
			String className = c.getName();
			try {
				className = className.substring(className.lastIndexOf("."));
			} catch (Exception e) {
			}
			Log.i(DEBUG_TAG, "[" + className + "]" + " Version : "
					+ Constants.APP_VERSION + formatDateNow()
					+ " - Info : " + msg);
		}
	}

	public static void iHLS(String msg, Class c) {
		if (INFO) {
			String className = c.getName();
			try {
				className = className.substring(className.lastIndexOf("."));
			} catch (Exception e) {
			}
			Log.i("HLS", "[" + className + "]" + " Version : "
					+ Constants.APP_VERSION + formatDateNow()
					+ " - Info : " + msg);
		}
	}

	public static void iHLS1(String msg, Class c) {
		if (INFO) {
			String className = c.getName();
			try {
				className = className.substring(className.lastIndexOf("."));
			} catch (Exception e) {
			}
			Log.i("HLS Bitrate : ", "[" + className + "]" + " Version : "
					+ Constants.APP_VERSION + formatDateNow()
					+ " - Info : " + msg);
		}
	}
}
