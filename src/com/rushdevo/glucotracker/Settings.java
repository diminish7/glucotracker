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
	private static final String OPT_HIGH_DEF = "160";
	private static final Integer OPT_HIGH_DEF_INT = Integer.parseInt(OPT_HIGH_DEF);
	private static final String OPT_LOW = "low";
	private static final String OPT_LOW_DEF = "70";
	private static final Integer OPT_LOW_DEF_INT = Integer.parseInt(OPT_LOW_DEF);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
	
	// Get the current value of the high end of the range
	public static Integer getHigh(Context context) {
		String highStr = PreferenceManager.getDefaultSharedPreferences(context).getString(OPT_HIGH, OPT_HIGH_DEF);
		try {
			return Integer.parseInt(highStr);
		} catch(NumberFormatException e) {
			return OPT_HIGH_DEF_INT;
		}
	}
	
	public static Integer getLow(Context context) {
		String lowStr = PreferenceManager.getDefaultSharedPreferences(context).getString(OPT_LOW, OPT_LOW_DEF);
		try {
			return Integer.parseInt(lowStr);
		} catch(NumberFormatException e) {
			return OPT_LOW_DEF_INT;
		}
	}
}
