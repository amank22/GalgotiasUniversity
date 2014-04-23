package com.teenscribblers.galgotiasuniversity.mSIM;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;

import com.teenscribblers.galgotiasuniversity.AlertDialogManager;

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
	public static final String KEY_COOKIE = "Cookie_Key";

	// private boolean status = false;

	// Constructor
	public SessionManagment(Context context) {
		_context = context;
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
			new Handler().post(new Runnable() {

				@Override
				public void run() {
					AlertDialogManager.showAlertDialog(_context, "error",
							"Please Login Again to continue..");
				}
			});
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
		// createLoginSession("");
		editor.clear();
		editor.commit();
		File dir = _context.getFilesDir();
		File file = new File(dir, "user.txt");
		file.delete();
	}
}
