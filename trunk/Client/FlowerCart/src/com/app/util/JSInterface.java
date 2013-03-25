/**
 * 
 */
package com.app.util;

import java.util.Locale;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.app.debug.DebugLog;
import com.app.main.TogglePreference;

/**
 * @author son.truong
 * 
 */
public class JSInterface {

	/*
	 * video view : start
	 */
	public long time;
	public TimerTask timerTask;
	public static Boolean inUUXSite = true;
	public static int countReconnect=0;
	@SuppressWarnings("rawtypes")
	Class cl = JSInterface.class;
	Context mContext;
	Activity activity;

	public JSInterface(Context c, Activity activity) {
		this.mContext = c;
		this.activity = activity;
	}

	public void showToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
	}
	 
	public void showMessage(String title, String message, String jsFunction,
			int messageType) {
		Utils.showMessage(mContext, title, message, jsFunction, messageType,
				WebViewUI.mWebview);
	}
	Intent vodIntent;
	String urlTmp, urlUpdateOffsetTmp;
	int lTmp, tTmp, wTmp, hTmp;
	
	public boolean isTablet() {
		return Constants.isTablet;
	}
	
	
	public boolean isPhone() {
		return !Constants.isTablet;
	}
	
	
	public String getDeviceType() {
		return Constants.isTablet ? "tablet" : "mobile";
	}

	TimerTask task2;

	TimerTask task;

	static long checkTimeoutAt = 0;

	public void showSettings() {
		Intent settingsIntent = new Intent(mContext, TogglePreference.class);
		String language = getCurrentLanguage();
		
//		settingsIntent.putExtra(Constants.START_LANG_KEY, language);
//		activity.startActivityForResult(settingsIntent,
//				Constants.START_ACTIVITY_SETTING);
	}
	
	private String getCurrentLanguage() {
		Resources res = mContext.getApplicationContext().getResources();
		Configuration newConfig = new Configuration(res.getConfiguration());
		Locale locale = newConfig.locale;
		String language = locale.toString();
		
		Log.d("JSInterface", "language: " + language);

		// Ensure that the language is a correct ISO
		if(language.equalsIgnoreCase("english") || language.equalsIgnoreCase("en")) {
			language = "en_US";
		} else if( language.equalsIgnoreCase("Español")) {
			language = "es_MX";
		} else if(language.equalsIgnoreCase("Português")) {
			language = "pt_BR";
		}
		return language;
	}

	// android default share
	/**
	 * Show share dialog
	 * 
	 * @param url
	 */
	 
	public void showShare(String url) {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		String shareBody = url;
		share.putExtra(Intent.EXTRA_SUBJECT, "Likes this product");
		share.putExtra(Intent.EXTRA_TEXT, shareBody);
		mContext.startActivity(Intent.createChooser(share, "Share via"));
	}

	boolean displayedUpdateDailog = false;

	public Boolean checkSimExist() {
		try {
			String carrierName = Utils.getCarrierName(mContext);
			if (carrierName != null && !carrierName.isEmpty()) {
				DebugLog.e("[checkSimExist] - : true", cl);
				return true;
			}
			// in tablet don't have SIM, tm.getSimState() always return
			// !TelephonyManager.SIM_STATE_ABSENT
			// if (tm.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
			// DebugLog.e("[checkSimExist] - : true", cl);
			// return true;
			// }
		} catch (Exception e) {
			DebugLog.e("[checkSimExist] - error : " + e, cl);
		}
		DebugLog.e("[checkSimExist] - : false", cl);
		return false;
	}
	
	public void exitApp() {
		activity.moveTaskToBack(true);
	}

	public void getContentWebview(String html) {
		if (html.indexOf(WebViewUI.rootURL) != -1) {
			DebugLog.e("IN -- getContentWebview", cl);
			inUUXSite = true;
			countReconnect++;
		} else {
			DebugLog.e("OUT -- getContentWebview", cl);
			inUUXSite = false;
			countReconnect=0;
		}
	}
	
	public void showKeyboard() {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
		
	}
	
	public void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
	}
}
