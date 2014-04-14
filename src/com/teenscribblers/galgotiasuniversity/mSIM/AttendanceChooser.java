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

public class AttendanceChooser extends SherlockActivity {
	Button todays, monthly, subj, sem;
	String type, typevalue;
	SessionManagment session;
	private AdView mAdView1, mAdView2, mAdView3, mAdView4, mAdView5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendance_chooser);
		session = new SessionManagment(AttendanceChooser.this);
		session.checkLogin();
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		todays = (Button) findViewById(R.id.attendance_todays);
		monthly = (Button) findViewById(R.id.attendance_monthly);
		subj = (Button) findViewById(R.id.attendance_subjects);
		sem = (Button) findViewById(R.id.attendance_semester);
		// handling onclick listners
		todays.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				type = "ctl00$ctl00$MCPH1$SCPH$btntodayAtt";
				typevalue = "Today Attendance";
				gooon(type, typevalue);
			}
		});

		monthly.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				type = "ctl00$ctl00$MCPH1$SCPH$btnMonthlyAtt";
				typevalue = "Monthly Attendance";
				gooon(type, typevalue);
			}
		});

		subj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				type = "ctl00$ctl00$MCPH1$SCPH$btnSubjectWiseAtt";
				typevalue = "Subject Wise Attendance";
				gooon(type, typevalue);
			}
		});

		sem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				type = "ctl00$ctl00$MCPH1$SCPH$btnSemAtt";
				typevalue = "Semester Attendance";
				gooon(type, typevalue);
			}
		});

		mAdView1 = (AdView) findViewById(R.id.adView_1);
		mAdView1.setAdListener(new ToastAdListener(this));
		
		mAdView1.loadAd(new AdRequest.Builder().build());
		mAdView2 = (AdView) findViewById(R.id.adView_2);
		
		mAdView2.setAdListener(new ToastAdListener(this));
		mAdView2.loadAd(new AdRequest.Builder().build());
		mAdView3 = (AdView) findViewById(R.id.adView_3);
		
		mAdView3.setAdListener(new ToastAdListener(this));
		mAdView3.loadAd(new AdRequest.Builder().build());
		mAdView4 = (AdView) findViewById(R.id.adView_4);
		
		mAdView4.setAdListener(new ToastAdListener(this));
		mAdView4.loadAd(new AdRequest.Builder().build());
		mAdView5 = (AdView) findViewById(R.id.adView_5);
		
		mAdView5.setAdListener(new ToastAdListener(this));
		mAdView5.loadAd(new AdRequest.Builder().build());

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mAdView1.pause();
		mAdView2.pause();
		mAdView3.pause();
		mAdView4.pause();
		mAdView5.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mAdView1.resume();
		mAdView2.resume();
		mAdView3.resume();
		mAdView4.resume();
		mAdView5.resume();
	}

	@Override
	protected void onDestroy() {
		mAdView1.destroy();
		mAdView2.destroy();
		mAdView3.destroy();
		mAdView4.destroy();
		mAdView5.destroy();
		super.onDestroy();
	}

	protected void gooon(String type, String typevalue) {

		String[] content = { type, typevalue };
		Intent i = new Intent(getBaseContext(), AttendanceActivity.class);
		i.putExtra("type_key", content);
		startActivity(i);
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
			Intent i = new Intent(AttendanceChooser.this, LoginActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
