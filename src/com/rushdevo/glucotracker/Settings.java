package com.rushdevo.glucotracker;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * @author jasonrush
 * Settings for Glucotracker
 * Range of target glucose level represented by High and Low
 */
public class Settings extends PreferenceActivity {
	// Option names and default values
	private static final String OPT_HIGH = "high";
	private static final Integer OPT_HIGH_DEF = 160;
	private static final String OPT_LOW = "low";
	private static final Integer OPT_LOW_DEF = 70;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
	
	// Get the current value of the high end of the range
	public static Integer getHigh(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(OPT_HIGH, OPT_HIGH_DEF);
	}
	
	public static Integer getLow(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(OPT_LOW, OPT_LOW_DEF);
	}
}
