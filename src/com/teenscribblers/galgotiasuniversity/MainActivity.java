package com.teenscribblers.galgotiasuniversity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.teenscribblers.galgotiasuniversity.articlelist.ArticleListActivity;
import com.teenscribblers.galgotiasuniversity.articlelist.DbHelper;
import com.teenscribblers.galgotiasuniversity.articlelist.MyScheduleReceiver;
import com.teenscribblers.galgotiasuniversity.mSIM.LoginActivity;

public class MainActivity extends SherlockFragmentActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private String[] desc = { "SlideShow", "Know GU", "NewsFeeds", "mSIM" };
	private DrawerAdapter adapter;
	private CharSequence mTitle, mDrawerTitle;
	private ActionBarDrawerToggle mDrawerToggle;
	private Fragment newContent = null;
	private Bundle b;
	private String BROADCAST = "com.teenscribblers.GU.Broadcast";
	public MyScheduleReceiver myreceiver;
	DbHelper dbhelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null)
			newContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (newContent == null)
			newContent = new MainView();
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_main);
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar));
		if (getIntent().getAction().equals("news_feed")) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, new ArticleListActivity())
					.commit();
		} else
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, newContent).commit();
		setTitle("SlideShow");
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		adapter = new DrawerAdapter(getApplicationContext(), android.R.id.list,
				desc, MainActivity.this);
		mTitle = mDrawerTitle = getTitle();
		mDrawerList.setAdapter(adapter);
		mDrawerLayout.setScrimColor(Color.parseColor("#778a8a8a"));
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.END);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu(); // creates call to
												// onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(mDrawerTitle);
				supportInvalidateOptionsMenu(); // creates call to
												// onPrepareOptionsMenu()
			}

		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		Connection_detect cd = new Connection_detect(getApplicationContext());

		Boolean isInternetPresent = cd.isConnectingToInternet();
		myreceiver = new MyScheduleReceiver();
		IntentFilter inf = new IntentFilter(BROADCAST);
		registerReceiver(myreceiver, inf);
		// call the DoInBackground AsyncTask class
		if (isInternetPresent) {

			Intent intent = new Intent(BROADCAST);
			sendBroadcast(intent);

		}
		new DbcreaterAsync().execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			selectItem(position);
		}

		/** Swaps fragments in the main content view */
		private void selectItem(int position) {
			switch (position) {
			case 0:
				newContent = new MainView();

				break;
			case 1:
				newContent = new Details();

				break;
			case 2:
				newContent = new ArticleListActivity();

				break;
			case 3:
				Intent i = new Intent(getBaseContext(), LoginActivity.class);
				startActivity(i);

			}
			if (newContent != null) {
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame, newContent).commit();
				// Highlight the selected item, update the title, and close the
				// drawer
				mDrawerList.setItemChecked(position, true);
				setTitle(desc[position]);
				mDrawerLayout.closeDrawer(mDrawerList);
			}

		}

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (item.getItemId() == android.R.id.home) {

			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}
		if (item.getItemId() == R.id.action_about) {
			Intent i = new Intent(this, AboutActivity.class);
			startActivity(i);
		}
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}

	public void saveData(Bundle data) {
		// based on the id you'll know which fragment is trying to save data(see
		// below)
		// the Bundle will hold the data
		b = data;
	}

	public Bundle getSavedData() {
		return b;
		// here you'll save the data previously retrieved from the fragments and
		// return it in a Bundle
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(myreceiver);
		Intent service = new Intent("com.teenscribblers.GUService.SERVICE");
		stopService(service);
		Log.i("ts", "service destroyed");
	}

	private class DbcreaterAsync extends AsyncTask<Void, Void, Void> {

		public DbcreaterAsync() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				dbhelper = new DbHelper(getBaseContext());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}
}
