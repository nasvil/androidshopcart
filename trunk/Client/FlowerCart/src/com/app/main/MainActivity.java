package com.app.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.app.debug.DebugLog;
import com.app.main.R;
import com.app.util.Constants;
import com.app.util.JSInterface;
import com.app.util.Utils;
import com.app.util.WebViewUI;

public class MainActivity extends Activity {

	@SuppressWarnings("rawtypes")
	private Class cl = MainActivity.class;

	private WebView mWebView;
	private String deviceDetectionString = "";
	String rootURL = "";
	public Context context = this;

	JSInterface jsInterface;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Utils.cleanUpCookies(context);

		Utils.initConstant(getApplicationContext());

		mWebView = (WebView) findViewById(R.id.webview);

		if (Utils.isTablet(getApplicationContext())) {
			Constants.isTablet = true;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}

		jsInterface = new JSInterface(context, this);

		String[] mString = Utils.getDeviceDetectString(context,
				Constants.isTablet);
		deviceDetectionString = mString[0];
		rootURL = mString[1];

		WebViewUI.setupWebview(context, mWebView, rootURL,
				deviceDetectionString, jsInterface,
				(RelativeLayout) findViewById(R.id.RelativeLayout03));

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		try {
			mWebView.saveState(outState);
		} catch (Exception e) {
			DebugLog.e("[onSaveInstanceState] error : " + e, cl);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		DebugLog.d("onRestart", cl);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		DebugLog.i("[onResume]", cl);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		DebugLog.i("[onActivityResult]", cl);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (JSInterface.inUUXSite) {
				mWebView.loadUrl("javascript:utility.showExitAppButton();");
			} else {
				Utils.reloadWifiAuthenticationPage(this, mWebView, rootURL,
						deviceDetectionString);
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
