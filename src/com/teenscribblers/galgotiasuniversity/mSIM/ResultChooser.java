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

public class ResultChooser extends SherlockActivity {

	Button s1, s2, s3, s4, s5, s6;
	String typevalue;
	SessionManagment session;
	private AdView mAdView1, mAdView2, mAdView3, mAdView4, mAdView5, mAdView6,
			mAdView7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_chooser);
		session = new SessionManagment(ResultChooser.this);
		session.checkLogin();
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		s1 = (Button) findViewById(R.id.result_sem_1);
		s2 = (Button) findViewById(R.id.result_sem_2);
		s3 = (Button) findViewById(R.id.result_sem_3);
		s4 = (Button) findViewById(R.id.result_sem_4);
		s5 = (Button) findViewById(R.id.result_sem_5);
		s6 = (Button) findViewById(R.id.result_sem_6);
		s1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				typevalue = "1st Semester";
				intent();

			}

		});
		s2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				typevalue = "2nd Semester";
				intent();

			}
		});
		s3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				typevalue = "3rd Semester";
				intent();

			}
		});
		s4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				typevalue = "4th Semester";
				intent();

			}
		});
		s5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				typevalue = "5th Semester";
				intent();

			}
		});
		s6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				typevalue = "6th Semester";
				intent();

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
		mAdView6 = (AdView) findViewById(R.id.adView_6);

		mAdView6.setAdListener(new ToastAdListener(this));
		mAdView6.loadAd(new AdRequest.Builder().build());
		mAdView7 = (AdView) findViewById(R.id.adView_7);

		mAdView7.setAdListener(new ToastAdListener(this));
		mAdView7.loadAd(new AdRequest.Builder().build());

	}

	protected void intent() {
		// TODO Auto-generated method stub
		Intent i = new Intent(ResultChooser.this, ResultActivity.class);
		i.putExtra("type_result", typevalue);
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
			Intent i = new Intent(ResultChooser.this, LoginActivity.class);
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
		mAdView4.pause();
		mAdView5.pause();
		mAdView6.pause();
		mAdView7.pause();
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
		mAdView6.resume();
		mAdView7.resume();
	}

	@Override
	protected void onDestroy() {
		mAdView1.destroy();
		mAdView2.destroy();
		mAdView3.destroy();
		mAdView4.destroy();
		mAdView5.destroy();
		mAdView6.destroy();
		mAdView7.destroy();
		super.onDestroy();
	}

}
