package com.teenscribblers.galgotiasuniversity.mSIM;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.teenscribblers.galgotiasuniversity.AlertDialogManager;
import com.teenscribblers.galgotiasuniversity.Connection_detect;
import com.teenscribblers.galgotiasuniversity.R;

public class ResultActivity extends SherlockActivity {
	ProgressDialog p;
	String typevalue;
	int count = 0;
	String[][] data;
	private CardsAdapter adapter;
	ListView list;
	SessionManagment session;
	private AdView mAdView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		session = new SessionManagment(ResultActivity.this);
		session.checkLogin();
		Connection_detect cd = new Connection_detect(getApplicationContext());
		Boolean isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			Intent i = new Intent(ResultActivity.this, LoginActivity.class);
			startActivity(i);

		}
		String content = getIntent().getStringExtra("type_result");
		typevalue = content;
		list = (ListView) findViewById(R.id.listView_result);
		if (savedInstanceState != null) {
			data = (String[][]) savedInstanceState
					.getSerializable("data_array");
			count = savedInstanceState.getInt("count");
			adapter = new CardsAdapter(getBaseContext(),
					android.R.layout.simple_list_item_1);
			list.setAdapter(adapter);
		} else
			new Result().execute();
		mAdView1 = (AdView) findViewById(R.id.adView_att_1);
		mAdView1.setAdListener(new ToastAdListener(this));
		mAdView1.loadAd(new AdRequest.Builder().build());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.user, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putSerializable("data_array", data);
		outState.putInt("count", count);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_logout:
			session.logoutUser();
			Intent i = new Intent(ResultActivity.this, LoginActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class Result extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			p = new ProgressDialog(ResultActivity.this);
			p.setMessage("Finding Your Result..");
			p.setCanceledOnTouchOutside(false);
			p.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String s = Result_Network();
			if (s.equals("Error")) {
				return "Error";
			}
			Document doc = Jsoup.parse(s);

			Element table = doc.getElementById("MCPH1_SCPH_gvExamResult");

			for (Element row : table.select("tr")) {
				count++;
			}

			data = new String[count][3];
			int i = 0;
			for (Element row : table.select("tr")) {
				int j = 0;
				for (Element column : row.select("td")) {
					if (!column.text().equals(""))
						data[i][j] = column.text();
					Log.d("result", column.text());
					j++;
				}
				i++;
			}
			return s;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			p.dismiss();
			if (result.equals("Error")) {
				AlertDialogManager.showAlertDialog(ResultActivity.this,
						"Error!", "Please Login Again!");
			} else {
				adapter = new CardsAdapter(getBaseContext(),
						android.R.layout.simple_list_item_1);
				list.setAdapter(adapter);
			}
		}
	}

	public String Result_Network() {
		UrlConnectionMethods u = new UrlConnectionMethods(
				getApplicationContext());
		String p = u.get(UrlConnectionParms.ResultString);
		if (p.equals("error")) {
			return "Error";
		}
		// Log.e("Atten", p);
		Document document = Jsoup.parse(p);
		UrlConnectionParms.viewstate = document.select("#__VIEWSTATE").attr(
				"value");
		UrlConnectionParms.eventvalidate = document
				.select("#__EVENTVALIDATION").attr("value");

		ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("__EVENTTARGET", ""));
		nameValuePair.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		nameValuePair.add(new BasicNameValuePair("__VIEWSTATE",
				UrlConnectionParms.viewstate));
		nameValuePair.add(new BasicNameValuePair("__LASTFOCUS", ""));
		nameValuePair.add(new BasicNameValuePair("ctl00$ctl00$txtCaseCSS",
				"textDefault"));
		nameValuePair.add(new BasicNameValuePair("__EVENTVALIDATION",
				UrlConnectionParms.eventvalidate));
		nameValuePair.add(new BasicNameValuePair(
				"ctl00$ctl00$MCPH1$SCPH$hdnStudentId", "1241"));
		nameValuePair.add(new BasicNameValuePair(
				"ctl00$ctl00$MCPH1$SCPH$ddldrp", typevalue));
		nameValuePair.add(new BasicNameValuePair(
				"ctl00$ctl00$MCPH1$SCPH$btnRun", "Show"));

		String s = u.Posturl(nameValuePair, UrlConnectionParms.ResultString);
		if (s.equals("error")) {
			return "Error";
		}
		return s;
	}

	// custom adapter
	public class CardsAdapter extends ArrayAdapter<String> {

		public TextView subject, grade;

		public CardsAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			// TODO Auto-generated constructor stub
		}

		private int lastPosition = -1;
		Typeface newsfont = Typeface.createFromAsset(getContext().getAssets(),
				"Adec.ttf");

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return count;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View row = convertView;

			if (row == null) {
				row = getLayoutInflater().inflate(R.layout.result_list_item,
						parent, false);
			}

			subject = (TextView) row.findViewById(R.id.textView_subject);
			grade = (TextView) row.findViewById(R.id.textView_grade);
			// setting font
			subject.setTypeface(newsfont);
			grade.setTypeface(newsfont);
			// setting text data
			if (position != 0) {
				subject.setText(data[position][1]);
				grade.setText(data[position][2]);

			} else {
				subject.setText("Subject");
				grade.setText("G");

			}

			Animation animation = AnimationUtils.loadAnimation(
					getBaseContext(),
					(position > lastPosition) ? R.anim.up_from_bottom
							: R.anim.down_from_top);
			row.startAnimation(animation);
			lastPosition = position;
			return row;
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mAdView1.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mAdView1.resume();

	}

	@Override
	protected void onDestroy() {
		mAdView1.destroy();

		super.onDestroy();
	}

}
