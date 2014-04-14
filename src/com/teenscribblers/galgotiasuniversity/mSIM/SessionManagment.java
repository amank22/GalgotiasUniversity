package com.teenscribblers.galgotiasuniversity.mSIM;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManagment {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "TeenScribblersPref";

	// All Shared Preferences Keys

	// Cookie (make variable public to access from outside)
	public static final String KEY_COOKIE = "";

	// Constructor
	public SessionManagment(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(String cookie) {

		// Storing name in pref
		editor.putString(KEY_COOKIE, cookie);
		// commit changes
		editor.commit();
	}

	/**
	 * Check login method wil check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	public void checkLogin() {
		// Check login status
		if (pref.getString(KEY_COOKIE, "").equals("")) {
			logoutUser();
		}

	}

	public String getCookie() {
		return pref.getString(KEY_COOKIE, "");
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser() {
		// Clearing all data from Shared Preferences
		createLoginSession("");
		editor.clear();
		editor.commit();
		UrlConnectionParms u=new UrlConnectionParms(_context);
		u.disconnect();
		// After logout redirect user to Login Activity
		Intent i = new Intent(_context, LoginActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		_context.startActivity(i);
	}
}
