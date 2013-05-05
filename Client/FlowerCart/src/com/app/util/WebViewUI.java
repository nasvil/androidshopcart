/**
 * 
 */
package com.app.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.app.debug.DebugLog;
import com.app.main.PayPalActivity;
import com.app.main.R;

public class WebViewUI {

	@SuppressWarnings("rawtypes")
	static Class cl = WebViewUI.class;
	static WebView mWebview;
	static Context mContext;
	public static String rootURL;
	static String deviceDetectionString;
	public static RelativeLayout relativeLayout;

	// static Activity activity;

	public static void setupWebview(Context mContext, WebView mWebview,
			String rootURL, String deviceDetectionString,
			JSInterface jsInterface, RelativeLayout r) {
		WebViewUI.mContext = mContext;
		WebViewUI.mWebview = mWebview;
		WebViewUI.rootURL = rootURL;
		WebViewUI.deviceDetectionString = deviceDetectionString;
		relativeLayout = r;

		setupMain(jsInterface);
	}

	private static void setupMain(JSInterface jsInterface) {
		String url = rootURL;
		setupWebview(mWebview, new CustomWebViewClientForContent(), url);
		mWebview.addJavascriptInterface(jsInterface, "AndroidFunction");
	}

	private static class CustomWebViewClientForContent extends WebViewClient {
		@SuppressWarnings("rawtypes")
		Class cl1 = CustomWebViewClientForContent.class;

		@Override
		public void onTooManyRedirects(WebView view, Message cancelMsg,
				Message continueMsg) {
			// super.onTooManyRedirects(view, cancelMsg, continueMsg);
			DebugLog.e("[onTooManyRedirects] error :", cl1);
		}

		@Override
		public void onReceivedHttpAuthRequest(WebView view,
				HttpAuthHandler handler, String host, String realm) {
			super.onReceivedHttpAuthRequest(view, handler, host, realm);
			DebugLog.d("[onReceivedHttpAuthRequest] host : " + host, cl1);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// super.onReceivedSslError(view, handler, error);
			// popSSLErrorDialog("Error Code: "+error.getPrimaryError());
			DebugLog.e("[onReceivedSslError] : ", cl1);
			handler.proceed();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			DebugLog.e("onPageStarted -- url: " + url, cl);
			super.onPageStarted(view, url, favicon);
			Utils.onPageStarted(mContext, view, Constants.isTablet, rootURL,
					url, deviceDetectionString);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Utils.onPageFinish(mContext, mWebview, rootURL, url,
					Constants.isTablet);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			DebugLog.e("[shouldOverrideUrlLoading] begin : " + url, cl1);
			if(url.indexOf("https://www.paypal.com") != -1 && url.indexOf("SESSION") != -1){
				DebugLog.e("return true for track paypal link: "+url, cl1);
				Intent intent = new Intent(mContext, PayPalActivity.class);
				intent.putExtra("PAYPAL_URL", url);
				mContext.startActivity(intent);
//				WebViewUI.loadJSMethod("utility.showExternalLink('"+url+"')");
				return true;
			}
			return false;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			if (errorCode == -8) {
				view.reload();
			}
		}
		
		@Override
		public void onLoadResource(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onLoadResource(view, url);
			
		}
		
		

	}

	@SuppressLint("SetJavaScriptEnabled")
	private static void setupWebview(WebView webView, WebViewClient client,
			String strURL) {
		webView.setBackgroundColor(mContext.getResources().getColor(
				R.color.white));
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setDatabasePath(
				"/data/data/" + webView.getContext().getPackageName()
						+ "/databases/");
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setDefaultTextEncodingName("utf-8");
		webView.setWebViewClient(client);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setSavePassword(false);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
				// TODO Auto-generated method stub
				return super.onConsoleMessage(consoleMessage);
			}
		});

		webView.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_UP:
					if (!v.hasFocus()) {
						v.requestFocus();
					}
					break;
				}
				return false;
			}
		});

		webView.setLongClickable(true);
		webView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
		webView.clearCache(true);
		webView.loadUrl(strURL);
	}

	public static void loadJSMethod(String methodName) {
		if (WebViewUI.mWebview != null) {
			WebViewUI.mWebview.loadUrl("javascript:" + methodName);
		}
	}

	public static void reload() {
		if (WebViewUI.mWebview != null) {
			mWebview.reload();
		}
	}

}
