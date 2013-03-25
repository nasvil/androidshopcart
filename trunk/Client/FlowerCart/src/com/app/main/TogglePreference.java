package com.app.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;

import com.app.debug.DebugLog;
import com.app.main.R;
import com.app.util.Constants;
import com.app.util.Utils;

public class TogglePreference extends PreferenceActivity implements
OnSharedPreferenceChangeListener {

	String language = "";
	Context context;

	ListPreference planguage = null;
	Preference pWIFI = null;
	Preference p3GWARNING = null;
	Preference pOptimizeBandwidth = null;
	PreferenceGroup dataSetting = null;
	PreferenceGroup setting = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		context = this;
//		addPreferencesFromResource(R.xml.preference);
//		if (getIntent().hasExtra(Constants.START_LANG_KEY)) {
//			language = getIntent().getStringExtra(Constants.START_LANG_KEY);
//			DebugLog.e("[language dataSetting]  : " + language,
//					TogglePreference.class);
//		}
//
//		PreferenceManager.getDefaultSharedPreferences(getBaseContext())
//		.registerOnSharedPreferenceChangeListener(this);
//
//		try {
//			planguage = (ListPreference) findPreference(Constants.STOREKEY_LANGUAGE);
//			setting = ((PreferenceGroup) findPreference("setting"));
//			dataSetting = ((PreferenceGroup) findPreference("dataSettings"));
//			pWIFI = findPreference("WIFI");
//			p3GWARNING = findPreference("3GWARNING");
//			pOptimizeBandwidth = findPreference("LOWEST_DATA_USAGE");
//
//			initializeLanguageSelection(planguage, language);
//			initializeDataSettings(dataSetting);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private void initializeDataSettings(PreferenceGroup dataSettingsGroup) {
//		if (Utils.getCarrierName(context).isEmpty()) {
//			getPreferenceScreen().removePreference(dataSettingsGroup);
//		}
	}

	private void initializeLanguageSelection(ListPreference languageSelection, String language) {
//		languageSelection.setValue(language);
//		languageSelection.setSummary(languageSelection.getEntry());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//		Utils.setLanguage(context, language);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

//		if (key.equals(Constants.STOREKEY_LANGUAGE) && planguage != null && planguage.getEntry() != null) {
//			DebugLog.i("Set language : " + planguage.getEntry(),
//					TogglePreference.class);
//			planguage.setSummary(planguage.getEntry());
//			language = planguage.getValue();
//			Utils.setLanguage(context, language);
//			setLanguage();
//		}
	}

	private void setLanguage() {
//		setting.setTitle(R.string.setting);
//		planguage.setTitle(R.string.language);
//		dataSetting.setTitle(R.string.wifi_state_title);
//		pWIFI.setTitle(R.string.wifi_title);
//		p3GWARNING.setTitle(R.string.TG_warning);
//		pOptimizeBandwidth.setTitle(R.string.optimize_bandwidth);
	}

//	public static String getLanguage(Context context) {
//		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//		String language = settings.getString(Constants.STOREKEY_LANGUAGE, Constants.LANGUAGE_DEFAULT);
//		DebugLog.i("getLanguage() - " + language, TogglePreference.class);
//		return language;
//	}
//
//	public static void setLanguage(Context context, String language) {
//		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//		SharedPreferences.Editor prefEditor = settings.edit();
//		prefEditor.putString(Constants.STOREKEY_LANGUAGE, language); 
//		prefEditor.commit(); 
//
//	}
}
