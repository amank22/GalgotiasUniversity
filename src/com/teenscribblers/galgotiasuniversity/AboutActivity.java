package com.teenscribblers.galgotiasuniversity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;

import com.actionbarsherlock.app.SherlockActivity;

public class AboutActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar));
	}
	@Override
	public void onBackPressed() {
		NavUtils.navigateUpFromSameTask(this);
		super.onBackPressed();
	}

}
