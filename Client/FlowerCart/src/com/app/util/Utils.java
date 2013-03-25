package com.app.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.app.debug.DebugLog;
import com.app.main.R;
import com.app.main.TogglePreference;
import com.app.model.ShareItem;

public class Utils {
	public static final String TAG = Utils.class.getName();
	@SuppressWarnings("rawtypes")
	static Class cl = Utils.class;

	public static Document stringToDom(String data)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(data)));
	}

	public static boolean containsNum(String str) {

		if (str == null || str.length() == 0)
			return true;

		for (int i = 0; i < str.length(); i++) {

			if (Character.isDigit(str.charAt(i)))
				return true;
		}

		return false;

	}

	public static boolean containsCharacter(String str) {

		if (str == null || str.length() == 0)
			return true;

		try {
			Integer.parseInt(str);
		} catch (Exception e) {
			return true;
		}

		return false;

	}

	public static boolean isValidZipCode(String str) {

		if (str == null || str.length() < 5)
			return false;

		try {
			Integer.parseInt(str);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static boolean isValidEmailAddress(String emailAddress) {

		String expression = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = emailAddress;
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher.matches();

	}

	public static boolean isValidPassword(String password) {

		int numCount = 0;

		if (password == null || password.length() < 6) {
			return false;
		}

		for (int i = 0; i < password.length(); i++) {

			if (Character.isDigit(password.charAt(i))) {
				numCount++;

				if (numCount >= 2) {
					return true;
				}
			}
		}

		return false;

	}

	public static Bitmap getImageBitmap(String url) {
		Bitmap bm = null;

		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			conn.setConnectTimeout(2000);
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
		} catch (IOException e) {
			Log.e(TAG, "Error getting bitmap", e);
		}
		return bm;
	}

	public static Bitmap getImageFromServer(String url) {
		return null;
	}

	public static Bitmap getImageBitmapResized(String url, int outputSize,
			byte[] MEMORY) {
		Bitmap bm = null;
		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			conn.setConnectTimeout(10000);
			conn.connect();
			InputStream is = conn.getInputStream();
			if (is == null) {
				Log.e(TAG, "Bitmap is null");
				return null;
			}

			// BufferedInputStream bis = new BufferedInputStream();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.outHeight = opts.outWidth = outputSize;
			opts.inTempStorage = MEMORY;
			bm = BitmapFactory.decodeStream(is, null, opts);
			// bis.close();
			is.close();
		} catch (IOException e) {
			Log.e(TAG, "Error getting bitmap", e);
		}

		if (bm == null) {
			Log.e(TAG, "Bitmap is null");
		}
		return bm;
	}

	public static Bitmap downloadBitmap(String url) {
		Bitmap poster = null;
		InputStream bmpInput = null;
		HttpClient client = new DefaultHttpClient();
		try {
			HttpGet req = new HttpGet(new URI(url));
			HttpResponse response = client.execute(req);
			bmpInput = response.getEntity().getContent();

			if (bmpInput != null) {
				poster = BitmapFactory.decodeStream(bmpInput);
				Log.i(TAG, "url" + url);
			}
		} catch (URISyntaxException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return poster;
	}

	public static Bitmap getImageBitmapResized(String url, int outputSize) {
		Bitmap bm = null;
		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			conn.setConnectTimeout(10000);
			conn.connect();
			InputStream is = conn.getInputStream();
			if (is == null) {
				Log.e(TAG, "IP stream is null");
				return null;
			} else {
				Log.i(TAG, "IP stream =" + is.available());
			}

			// BufferedInputStream bis = new BufferedInputStream();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.outHeight = opts.outWidth = outputSize;
			// opts.inTempStorage = MEMORY;
			bm = BitmapFactory.decodeStream(is, null, opts);
			// bis.close();
			is.close();
		} catch (IOException e) {
			Log.e(TAG, "Error getting bitmap", e);
		}

		if (bm == null) {
			Log.e(TAG, "after decoding Bitmap is null");
		}
		return bm;
	}

	public static boolean isEmpty(String string) {
		return string == null || string.trim().length() == 0;
	}

	/**
	 * Clean up cookie
	 * 
	 * @param context
	 */
	public static void cleanUpCookies(Context context) {

		CookieSyncManager.createInstance(context);
		CookieSyncManager.getInstance().startSync();
		CookieManager cookieManager = CookieManager.getInstance();
		// cookieManager.removeAllCookie();
		cookieManager.removeExpiredCookie();
		// cookieManager.removeSessionCookie();
		CookieSyncManager.getInstance().sync();
		CookieManager.getInstance().setAcceptCookie(true);
	}

	/**
	 * clean up facebook cookie
	 * 
	 * @param context
	 */
	public static void cleanUpFacebookCookies(Context context) {
		CookieSyncManager.createInstance(context);
		CookieSyncManager.getInstance().startSync();
		CookieManager cookieManager = CookieManager.getInstance();
		String facebookCookies = cookieManager.getCookie(".facebook.com");
		DebugLog.i("facebookCookies :: " + facebookCookies, Utils.class);
		if (facebookCookies != null) {

			String[] fb_cookie_arr = facebookCookies.split(" ");

			for (String str : fb_cookie_arr) {
				String[] cookieNameAndValue = str.split("=");
				cookieManager
				.setCookie(
						".facebook.com",
						cookieNameAndValue[0]
								+ "=deleted; expires=Thu, 01-Jan-1970 00:00:01 GMT; path=/; domain=.facebook.com");
			}
		}
		CookieSyncManager.getInstance().sync();
	}

	public static void cleanUpCookieByName(Context context, String rootURL,
			String cookieName) {
		CookieSyncManager.createInstance(context);
		CookieSyncManager.getInstance().startSync();
		CookieManager cookieManager = CookieManager.getInstance();
		String strCookies = cookieManager.getCookie(rootURL);

		if (strCookies != null) {

			String[] cookies = strCookies.split(";");

			for (String str : cookies) {
				String[] cookieNameAndValue = str.split("=");
				if (cookieNameAndValue[0].equals(cookieName)) {
					cookieManager
					.setCookie(
							rootURL,
							cookieName
							+ "=deleted; expires=Thu, 01-Jan-1970 00:00:01 GMT; path=/; "
							+ rootURL);
				}
			}
		}
		CookieSyncManager.getInstance().sync();
	}

	/**
	 * get cookie value
	 * 
	 * @param context
	 * @param url
	 * @param key
	 * @return
	 */
	public static String getCookie(Context context, String url, String key) {
		String result = "";
		try {
			CookieSyncManager.createInstance(context);
			CookieManager cookieManager = CookieManager.getInstance();
			String cookies = cookieManager.getCookie(url);
			if (cookies != null && cookies != "") {
				String[] arr = cookies.split(";");

				if (arr != null && arr.length > 0) {
					for (int i = 0; i < arr.length; i++) {
						String[] cookie = arr[i].trim().split("=");
						if (cookie.length >= 2) {
							if (cookie[0].equalsIgnoreCase(key)) {
								for (int j = 1; j < cookie.length; j++) {
									result += cookie[j] + "=";
								}
								result = result.substring(0,
										result.length() - 1);
								if (result.contains("%7E")) {
									result = result.substring(
											result.indexOf("%7E") + 3,
											result.length());
								} else {
									result = result.substring(
											result.indexOf("~") + 1,
											result.length());
								}

								break;
							}
						}

					}
				}
			}
		} catch (Exception e) {
			DebugLog.e("[getCookie] error : " + e, Utils.class);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static JSONArray getArrayCookie(Context context, String url,
			String key, String keyArr) {
		JSONArray array = null;
		try {
			String history = Utils.getCookie(context, url, key);
			JSONObject jsonObject = new JSONObject(history);
			Iterator keys = jsonObject.keys();
			while (keys.hasNext()) {
				String keyName = (String) keys.next();
				if (keyName.equalsIgnoreCase(keyArr)) {
					array = jsonObject.getJSONArray("arr");
					break;
				}
			}
		} catch (Exception e) {
			DebugLog.e("[getArrayCookie] error : " + e, Utils.class);
		}

		return array;
	}

	public static ArrayList<String[]> parseQueryString(String strQuery,
			String keySeparate, String keyValue) {
		ArrayList<String[]> result = new ArrayList<String[]>();
		try {
			String[] params = strQuery.split(keySeparate);
			for (String param : params) {
				String[] pair = param.split(keyValue); // $NON-NLS-1$
				result.add(pair);
			}
		} catch (Exception e) {
			DebugLog.e("[parseQueryString] error : " + e, Utils.class);
		}
		return result;
	}

	public static void onPageFinish(Context context, WebView webView,
			String rootURL, String url, boolean isTablet) {
		try {
//			webView.setVisibility(View.VISIBLE);
			WebViewUI.relativeLayout.setVisibility(View.INVISIBLE);
			System.gc();
		} catch (Exception e) {
			DebugLog.e("[onPageFinish - error] " + e, cl);
		}
	}

	public static void onPageStarted(Context context, WebView view,
			boolean isTablet, String root_url, String url,
			String deviceDetectionString) {
		DebugLog.e("[onPageStarted] begin : " + url, cl);

		if (url.indexOf("#_") != -1) {
			DebugLog.i("[onPageStarted] reload because url contain #", cl);
			url = url.substring(0, url.indexOf("#_"));
			view.stopLoading();
			view.loadUrl(url);
		} else if (url.equalsIgnoreCase("http://www.facebook.com/home.php")) {
			DebugLog.i("[onPageStarted] load facebook ", cl);
			view.stopLoading();
			view.loadUrl(root_url + deviceDetectionString);
		} else if (url.startsWith("exts")) {
			view.stopLoading();
			url = url.replace("exts", "https");

			Intent browserIntent = new Intent("android.intent.action.VIEW",
					Uri.parse(url));
			context.startActivity(browserIntent);
		} else if (url.startsWith("ext")) {
			DebugLog.i("[onPageStarted] load external page ", cl);
			view.stopLoading();
			url = url.replace("ext", "https");
			Intent browserIntent = new Intent("android.intent.action.VIEW",
					Uri.parse(url));
			context.startActivity(browserIntent);
		}
	}

	public static Boolean getStore(SharedPreferences storage, String key) {
		return storage.getBoolean(key, true);
	}

	public static Boolean getStore(Context mContext, String key,
			boolean default_value) {
		SharedPreferences storage = getSharedPreferences(mContext, "");
		return storage.getBoolean(key, default_value);
	}

	public static String getStore(Context mContext, String key,
			String default_value) {
		SharedPreferences storage = getSharedPreferences(mContext, "");
		return storage.getString(key, default_value);
	}

	public static Boolean getStore(SharedPreferences storage, String key,
			boolean default_value) {
		return storage.getBoolean(key, default_value);
	}

	public static void setStore(SharedPreferences storage, String key,
			boolean value) {
		storage.edit().putBoolean(key, value).commit();
	}

	public static void setStore(Context mContext, String key, boolean value) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		sharedPrefs.edit().putBoolean(key, value).commit();
	}

	public static SharedPreferences getSharedPreferences(Context mContext,
			String type) {
		if (type.equalsIgnoreCase(Constants.PREFS_FILE_NAME)) {
			return mContext.getSharedPreferences(Constants.PREFS_FILE_NAME,
					Context.MODE_APPEND);
		}
		return PreferenceManager.getDefaultSharedPreferences(mContext);
	}

	public static boolean isTablet(Context context) {
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
		boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		return (xlarge || large);
	}

	public static double getDeviceInch(Activity activity) {
		try {
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			double x = Math.pow((double) dm.widthPixels / dm.xdpi, 2);
			double y = Math.pow((double) dm.heightPixels / dm.ydpi, 2);
			return Math.round(Math.sqrt(x + y));
		} catch (Exception e) {
			DebugLog.e("[getDeviceInch] error : " + e, Utils.class);
		}
		return 0;
	}

	public static long totalStoreSize() {
		long total = -1;
		try {
			StatFs statFs = new StatFs(Environment.getRootDirectory()
					.getAbsolutePath());
			total = ((long) statFs.getBlockCount() * (long) statFs
					.getBlockSize()) / 1048576;
		} catch (Exception e) {
			DebugLog.e("[totalStoreSize] error : " + e, Utils.class);
		}
		return total;
	}

	public static long freeStoreSize() {
		long free = -1;
		try {
			StatFs statFs = new StatFs(Environment.getRootDirectory()
					.getAbsolutePath());
			free = (statFs.getAvailableBlocks() * (long) statFs.getBlockSize()) / 1048576;
		} catch (Exception e) {
			DebugLog.e("[freeStoreSize] error : " + e, Utils.class);
		}
		return free;
	}

	public static long usedStoreSize() {
		long busy = -1;
		try {
			StatFs statFs = new StatFs(Environment.getRootDirectory()
					.getAbsolutePath());
			long Total = ((long) statFs.getBlockCount() * (long) statFs
					.getBlockSize()) / 1048576;
			long Free = (statFs.getAvailableBlocks() * (long) statFs
					.getBlockSize()) / 1048576;
			busy = Total - Free;
		} catch (Exception e) {
			DebugLog.e("[usedStoreSize] error : " + e, Utils.class);
		}
		return busy;
	}

	public static int[] getMemoryRAM() {
		String str2;
		String[] arrayOfString;
		int[] result = new int[] { -1, -1 };
		try {
			FileReader localFileReader = new FileReader("/proc/meminfo");
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// read meminfo
			arrayOfString = str2.split("\\s+");
			// total Memory
			result[0] = Integer.valueOf(arrayOfString[1]).intValue();
			result[0] = Math.round(result[0] / 1024);

			str2 = localBufferedReader.readLine();// read meminfo
			arrayOfString = str2.split("\\s+");
			// free Memory
			result[1] = Integer.valueOf(arrayOfString[1]).intValue();
			result[1] = Math.round(result[1] / 1024);

			localBufferedReader.close();
		} catch (IOException e) {
			DebugLog.e("[getMemoryRAM] error " + e, Utils.class);
		}
		return result;
	}

	public static String getCarrierName(Context context) {
		String carrierName = "";

		try {
			TelephonyManager manager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			carrierName = manager.getNetworkOperatorName();
		} catch (Exception e) {
			DebugLog.e("[getCarrieName] error " + e, Utils.class);
		}

		DebugLog.d("[getCarrierName] " + carrierName, Utils.class);

		return carrierName;
	}

	public static void initConstant(Context context) {
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			Constants.APP_VERSION = pInfo.versionName;
			Constants.APP_PACKAGE_NAME = pInfo.packageName;
			ApplicationInfo aInfo = context.getApplicationInfo();
			Constants.UID = aInfo.uid;
		} catch (Exception e) {
			Log.e(TAG, "[initConstant] : " + e);
		}
	}

	public static String[] getDeviceDetectString(Context context,
			Boolean isTablet) {
		String[] result = new String[2];
		result[0] = "?devId=" + Uri.encode(android.os.Build.MODEL);
		result[1] = "";
		result[1] = context.getString(R.string.root_url);
		if (isTablet) {
			// tablet do
			result[0] += "&devTy=tablet";
		} else {
			// mobile do
			result[0] += "&devTy=mobile";
		}
//
//		// check Samsung package
//		if (Constants.APP_PACKAGE_NAME
//				.equalsIgnoreCase(Constants.PACKAGE_SAMSUNG)) {
//			result[0] += "&appstore=samsung";
//		} else if (Constants.APP_PACKAGE_NAME
//				.equalsIgnoreCase(Constants.PACKAGE_SAMSUNGPRE)) {
//			result[0] += "&appstore=samsungpre";
//		} else {
			result[0] += "&appstore=google";
//		}
		String ver = Constants.APP_VERSION;
		if (ver.contains(" ")) {
			ver = ver.substring(0, ver.indexOf(" "));
		}
		result[0] += "&ver=" + ver;
		return result;
	}

	public static void showPopupWifi(final Context context, int title,
			int message) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

		alertDialog.setTitle(context.getString(title));
		alertDialog.setMessage(context.getString(message));
		alertDialog.setCancelable(true);

		alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			public void onCancel(DialogInterface dialog) {
				return;
			}
		});
		DebugLog.d("[showPopupWifi] doneMedia", cl);
		alertDialog.setPositiveButton(context.getString(R.string.ok),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// MediaUtils.doneMedia(false, "showPopupWifi");
				return;
			}
		});
		alertDialog.setNegativeButton((context.getString(R.string.setting)),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// MediaUtils.doneMedia(false, "showPopupWifi");
				context.startActivity(new Intent(context,
						TogglePreference.class));
				return;
			}
		});

		alertDialog.show();

	}

	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		Display display = wm.getDefaultDisplay();
		display.getMetrics(metrics);
		return metrics.widthPixels;

	}

	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		Display display = wm.getDefaultDisplay();
		display.getMetrics(metrics);
		return metrics.heightPixels;
	}

	public static float convertDPIToPixel(int dpi, Context context) {
		float density = context.getResources().getDisplayMetrics().density;
		return dpi * density;
	}

	public static float convertPixelsToDp(float px, Context context) {
		float density = context.getResources().getDisplayMetrics().density;
		return px / density;
	}

	public static void showToast(Activity activity, final Context context,
			final int message, final int period) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, message, period).show();
			}
		});
	}

	public static void showToast(Activity activity, final Context context,
			final String message, final int period) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, message, period).show();
			}
		});
	}

	static public boolean checkFileExists(Context context, String uri) {
		File file = context.getFileStreamPath(uri);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static double calculateBandwidth(double second, double kbyte) {
		double lastBandwidth = 0;
		try {
			lastBandwidth = (kbyte * 8) / second;
			DebugLog.d("kbyte : " + kbyte + " - time : " + second
					+ " - lastBandwidth : " + lastBandwidth, cl); // DEBUG
		} catch (Exception e) {
		}
		return lastBandwidth;
	}

	public static String getLineEndWith(String strURL, String key, int order)
			throws Exception {
		String result = "";
		int loop = 0;
		int resultOrder = 0;
		DebugLog.d("[getLineEndWith] strURL : " + strURL, cl); // DEBUG
		while (result == "" && loop < 2) {
			try {
				loop++;
				URL url = new URL(strURL);
				Scanner s = new Scanner(url.openStream());
				// read playlist from your scanner
				while (s.hasNextLine()) {
					String txt = s.nextLine();
					if (txt.endsWith(key)) {
						if (resultOrder == order) {
							result = txt;
							break;
						}
						resultOrder++;
					}
				}
				s.close();
			} catch (Exception e) {
				throw e;
			}
		}
		return result;
	}
	public static void showMessage(Context mContext, String title,
			String message, final String jsFunction, int messageType,
			final WebView mWebView) {
		AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
		if (!title.isEmpty()) {
			adb.setTitle(title);
		}

		adb.setMessage(message);
		adb.setCancelable(true);

		adb.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
			}
		});

		adb.setPositiveButton(mContext.getString(R.string.ok),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (jsFunction != null && !jsFunction.isEmpty()) {
					WebViewUI.loadJSMethod(jsFunction + ";");
				}
			}
		});

		if (messageType == Constants.MESSAGE_TYPE.CONFIRM) {
			adb.setNegativeButton(mContext.getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

				}
			});
		}

		adb.show();
	}
	static AlertDialog dialog;

	public static void checkVersion() {
		/*
		 * WebUtils.mWebview only == null when app start (not restart) without
		 * connection
		 */
		if (WebViewUI.mWebview != null) {
			/* change to match with iOS and web */
			WebViewUI.mWebview.loadUrl("javascript:utility.checkVersion();");
		}
	}

	/**
	 * @param mContext
	 * @param via
	 * @param shareBody
	 */
	public static void share(Context mContext, String via, String shareBody) {
		try {
			boolean found = false;
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("text/plain");

			// gets the list of intents that can be loaded.
			List<ResolveInfo> resInfo = mContext.getPackageManager()
					.queryIntentActivities(share, 0);
			if (!resInfo.isEmpty()) {
				for (ResolveInfo info : resInfo) {
					if (info.activityInfo.packageName.toLowerCase().contains(
							via)
							|| info.activityInfo.name.toLowerCase().contains(
									via)) {
						share.putExtra(Intent.EXTRA_SUBJECT, "Likes this movie");
						share.putExtra(Intent.EXTRA_TEXT, shareBody);
						share.setPackage(info.activityInfo.packageName);
						found = true;
						break;
					}
				}
				if (!found)
					return;

				mContext.startActivity(Intent.createChooser(share, "Select"));
			}
		} catch (Exception e) {
		}
	}

	public static ArrayList<ShareItem> createShareList() {
		ArrayList<ShareItem> result = new ArrayList<ShareItem>();
		ShareItem item1 = new ShareItem("Facebook", "face", R.drawable.face);
		ShareItem item2 = new ShareItem("Twitter", "twi", R.drawable.twitter);
		ShareItem item3 = new ShareItem("Email", "mail", R.drawable.email);

		result.add(item1);
		result.add(item2);
		result.add(item3);

		return result;
	}

	public static String getCarrier(Context mContext) {
		String carrierName = "";
		try {
			TelephonyManager tm = (TelephonyManager) mContext
					.getSystemService(Context.TELEPHONY_SERVICE);
			carrierName = tm.getNetworkOperatorName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return carrierName;
	}

	public static Calendar toCalendar(final String iso8601string)
			throws ParseException {
		Calendar calendar = GregorianCalendar.getInstance();
		Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
		.parse(iso8601string);
		calendar.setTime(date);
		return calendar;
	}

	//reload webview when login wifi authen success or fail
	public static void wifiAuthenticationPage(Context context,final WebView view, final String rootURL,final String deviceDetectionString){
		DebugLog.e("wifiAuthenticationPage -- start -- view.getUrl(): "+view.getUrl(), cl);
//		view.loadUrl("javascript:AndroidFunction.getContentWebview(document.getElementsByTagName('head')[0].innerHTML);");
//		if (!JSInterface.inUUXSite && view.getUrl().indexOf(rootURL) == -1 && JSInterface.countReconnect<=3) {
//			view.loadUrl(rootURL+deviceDetectionString);
//
//			Timer t=new Timer();
//			t.scheduleAtFixedRate(new TimerTask() {
//				@Override
//				public void run() {
//					view.loadUrl(rootURL+deviceDetectionString);
//					this.cancel();
//				}
//			}, 5000, 1000000);
//		}

	}

	//reload webview when resume so content of webview has wifi authen page
	public static void reloadWifiAuthenticationPage(Activity activity, WebView view, String rootURL,String deviceDetectionString){
		DebugLog.e("reloadWifiAuthenticationPage -- start", cl);
//		view.loadUrl("javascript:AndroidFunction.getContentWebview(document.getElementsByTagName('head')[0].innerHTML);");
//		if (!JSInterface.inUUXSite) {
//			view.loadUrl(rootURL+deviceDetectionString);
//		}
	}



}
