package com.teenscribblers.galgotiasuniversity.mSIM;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.teenscribblers.galgotiasuniversity.R;

public class OptionsActivity extends SherlockActivity {
	Button attendance, result;
	SessionManagment session;
	private AdView mAdView1, mAdView2, mAdView3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		session = new SessionManagment(OptionsActivity.this);
		session.checkLogin();
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		attendance = (Button) findViewById(R.id.button_attendance);
		result = (Button) findViewById(R.id.button_Results);

		attendance.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(), AttendanceChooser.class);
				startActivity(i);
			}
		});
		result.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(), ResultChooser.class);
				startActivity(i);
			}
		});
		mAdView1 = (AdView) findViewById(R.id.adView_option_1);

		mAdView1.setAdListener(new ToastAdListener(this));
		mAdView1.loadAd(new AdRequest.Builder().build());
		mAdView2 = (AdView) findViewById(R.id.adView_option_2);

		mAdView2.setAdListener(new ToastAdListener(this));
		mAdView2.loadAd(new AdRequest.Builder().build());
		mAdView3 = (AdView) findViewById(R.id.adView_option_3);

		mAdView3.setAdListener(new ToastAdListener(this));
		mAdView3.loadAd(new AdRequest.Builder().build());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_logout:
			session.logoutUser();
			Intent i = new Intent(OptionsActivity.this, LoginActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mAdView1.pause();
		mAdView2.pause();
		mAdView3.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mAdView1.resume();
		mAdView2.resume();
		mAdView3.resume();
	}

	@Override
	protected void onDestroy() {
		mAdView1.destroy();
		mAdView2.destroy();
		mAdView3.destroy();
		super.onDestroy();
	}

}
