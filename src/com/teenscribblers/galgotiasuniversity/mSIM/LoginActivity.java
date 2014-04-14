package com.teenscribblers.galgotiasuniversity.mSIM;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.teenscribblers.galgotiasuniversity.Connection_detect;
import com.teenscribblers.galgotiasuniversity.R;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends SherlockActivity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	ProgressDialog p;
	public static final String EXTRA_EMAIL = "teenscribblers@something.com";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mUsername;
	private String mPassword;
	// UI references.
	private EditText mUserView;
	private EditText mPasswordView;
	// private View mLoginFormView;
	private List<NameValuePair> nameValuePair;
	int statusCode;
	private SessionManagment session;

	public LoginActivity() {
		// TODO Auto-generated constructor stub

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		session = new SessionManagment(LoginActivity.this);
		if (!session.getCookie().equals("")) {
			Intent i = new Intent(LoginActivity.this, LogedInActivity.class);
			startActivity(i);
		}
		// Set up the login form.
		mUsername = getIntent().getStringExtra(EXTRA_EMAIL);
		mUserView = (EditText) findViewById(R.id.email);
		mUserView.setText(mUsername);
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		// mLoginFormView = findViewById(R.id.login_form);
		// mLoginStatusView = findViewById(R.id.login_status);
		// mLoginStatusMessageView = (TextView)
		// findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Connection_detect cd = new Connection_detect(
								getApplicationContext());
						Boolean isInternetPresent = cd.isConnectingToInternet();
						if (isInternetPresent)
							attemptLogin();
					}
				});
	}

	@Override
	public void onBackPressed() {
		NavUtils.navigateUpFromSameTask(this);
		super.onBackPressed();
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mUserView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mUsername = mUserView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid username.
		if (TextUtils.isEmpty(mUsername)) {
			mUserView.setError(getString(R.string.error_field_required));
			focusView = mUserView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			// mLoginStatusMessageView.setText(R.string.login_progress_signing_in);

			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			p = new ProgressDialog(LoginActivity.this,
					ProgressDialog.STYLE_HORIZONTAL);
			p.setTitle("Please Wait..");
			p.setMessage("Signing In");
			p.setCanceledOnTouchOutside(false);
			p.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			login();
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			// mAuthTask = null;
			// showProgress(false);
			Log.d("Tag_GU", "in post execute");
			p.dismiss();
			Log.d("Tag_GU", String.valueOf(UrlConnectionParms.loginstat));
			if (UrlConnectionParms.loginstat) {
				Intent i = new Intent(LoginActivity.this, LogedInActivity.class);
				startActivity(i);

			}

			else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
				mAuthTask = null;

			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
	}

	void login() {
		Log.d("Tag_GU", "in login");
		// Building post parameters, key and value pair
		nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("txtUserId", mUsername));
		nameValuePair.add(new BasicNameValuePair("txtPass", mPassword));
		nameValuePair
				.add(new BasicNameValuePair(
						"__VIEWSTATE",
						"/wEPDwUKMTUyOTcyNTk4Mw9kFgICAw8WAh4GYWN0aW9uBQsvaVNJTS9Mb2dpbhYCAgkPFgIeCWlubmVyaHRtbAUOMC9JbnZhbGlkIFVzZXJkZOasGEiK0+MGnGIBHqdMmhWHQh4t+oKIWWnQGUXY4/gL"));
		nameValuePair.add(new BasicNameValuePair("__LASTFOCUS", ""));
		nameValuePair.add(new BasicNameValuePair("__EVENTTARGET", ""));
		nameValuePair
				.add(new BasicNameValuePair(
						"__EVENTVALIDATION",
						"/wEWBQKc0s7ACwKz8dy8BQLKw6LdBQLa5//eAwKC3IeGDEedsNawA4at0Y+kPDQuYX73/YGipA59tjuWn0S9s/2K"));
		nameValuePair.add(new BasicNameValuePair("btnLogin", ""));

		// HttpHelperorsomething h = new HttpHelperorsomething();
		// h.loginrequest(nameValuePair, "http://122.160.168.158/iSIM/Login");
		UrlConnectionMethods conn = new UrlConnectionMethods(
				getApplicationContext());
		conn.LoginUrl(nameValuePair, UrlConnectionParms.LoginString);
	}

}
