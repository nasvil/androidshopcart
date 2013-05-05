package com.app.main;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class PayPalActivity extends Activity {
	
	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paypal_view);
		webView = (WebView) findViewById(R.id.webview1);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    String url = extras.getString("PAYPAL_URL");
		    webView.loadUrl(url);
		}
	}
}
