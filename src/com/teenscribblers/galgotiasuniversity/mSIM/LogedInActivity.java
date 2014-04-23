package com.teenscribblers.galgotiasuniversity.mSIM;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.teenscribblers.galgotiasuniversity.AlertDialogManager;
import com.teenscribblers.galgotiasuniversity.R;
import com.teenscribblers.galgotiasuniversity.articlelist.ImageLoader;

public class LogedInActivity extends SherlockActivity {

	ListView list;
	private CardsAdapter adapter;
	ProgressBar pb;
	ImageView dp;
	Button options;
	SessionManagment session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loged_in);
		session = new SessionManagment(LogedInActivity.this);
		session.checkLogin();
		// Log.d("cookie_logeddin", session.getCookie());
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		UrlConnectionParms.ids = new ArrayList<String>();
		UrlConnectionParms.idvalues = new ArrayList<String>();
		UrlConnectionParms.titletext = new ArrayList<String>();
		dp = (ImageView) findViewById(R.id.student_pic);
		pb = (ProgressBar) findViewById(R.id.progressBar_personal);
		options = (Button) findViewById(R.id.button_options);
		idlist();
		titles();
		list = (ListView) findViewById(R.id.listView_personal);
		// dvsv
		try {
			FileInputStream input = openFileInput("user.txt"); // Open input
																// stream
			DataInputStream din = new DataInputStream(input);
			int sz = din.readInt(); // Read line count
			for (int i = 0; i < sz; i++) { // Read lines
				String line = din.readUTF();
				UrlConnectionParms.idvalues.add(line);
			}
			din.close();
			pb.setVisibility(View.GONE);
			adapter = new CardsAdapter(getBaseContext(),
					android.R.layout.simple_list_item_1);
			list.setAdapter(adapter);
			ImageLoader imgLoader = new ImageLoader(getBaseContext());
			imgLoader.DisplayImage(UrlConnectionParms.StudentImagebase
					+ UrlConnectionParms.idvalues.get(0) + ".jpg",
					R.drawable.abs__toast_frame, dp);
		} catch (FileNotFoundException e) {
			new Info().execute();
		} catch (IOException e) {
			Toast.makeText(this, "Error Reading file!", Toast.LENGTH_SHORT)
					.show();
		}
		// vdsvs

		options.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(), OptionsActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.push_down_in,
						R.anim.push_down_out);
			}
		});
	}

	void addtolist(String s) {
		UrlConnectionParms.ids.add(s);
	}

	void addtotitlelist(String s) {
		UrlConnectionParms.titletext.add(s);
	}

	public class Info extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			UrlConnectionMethods u = new UrlConnectionMethods(
					getApplicationContext());
			String s = u.get(UrlConnectionParms.PersonalInfoString);
			if (s.equals("error")) {
				return "Error";
			}
			Document document = Jsoup.parse(s);
			int i = 0;
			while (i != UrlConnectionParms.ids.size()) {
				Element doc = document.getElementById(UrlConnectionParms.ids
						.get(i));
				if (doc.text().equals("")) {
					UrlConnectionParms.idvalues.add("-");

				} else
					UrlConnectionParms.idvalues.add(doc.text());
				Log.d("values", doc.text());
				i++;
			}
			try {
				FileOutputStream output = openFileOutput("user.txt",
						MODE_PRIVATE);
				DataOutputStream dout = new DataOutputStream(output);
				dout.writeInt(UrlConnectionParms.idvalues.size()); // Save line
																	// count
				for (String line : UrlConnectionParms.idvalues)
					// Save lines
					dout.writeUTF(line);
				dout.flush(); // Flush stream ...
				dout.close(); // ... and close.
			} catch (FileNotFoundException e) {
				return "Error";
			} catch (IOException e) {
				return "Error";
			}
			return s;

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pb.setVisibility(View.GONE);
			if (result.equals("Error")) {
				AlertDialogManager.showAlertDialog(LogedInActivity.this,
						"Error!", "Error retriving data!");

			} else {
				adapter = new CardsAdapter(getBaseContext(),
						android.R.layout.simple_list_item_1);
				list.setAdapter(adapter);

				// image processing
				// ImageLoader class instance
				ImageLoader imgLoader = new ImageLoader(getBaseContext());
				// whenever you want to load an image from url
				// call DisplayImage function
				// url - image url to load
				// loader - loader image, will be displayed before getting
				// image - ImageView

				imgLoader.DisplayImage(UrlConnectionParms.StudentImagebase
						+ UrlConnectionParms.idvalues.get(0) + ".jpg",
						R.drawable.abs__toast_frame, dp);

				// Element tableheader = document.select("tr").get(1);
				// for (Element element : tableheader.children()) {
				// Log.d("table", element.text());
				// }
			}
		}
	}

	public class CardsAdapter extends ArrayAdapter<String> {

		public TextView text, textvalue;

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
			return UrlConnectionParms.ids.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View row = convertView;

			if (row == null) {

				row = getLayoutInflater().inflate(R.layout.list_msim, parent,
						false);
			}
			text = (TextView) row.findViewById(R.id.title);
			text.setTypeface(newsfont);
			text.setText(UrlConnectionParms.titletext.get(position));

			textvalue = (TextView) row.findViewById(R.id.value);
			textvalue.setTypeface(newsfont);
			textvalue.setText(UrlConnectionParms.idvalues.get(position));

			Animation animation = AnimationUtils.loadAnimation(
					getBaseContext(),
					(position > lastPosition) ? R.anim.up_from_bottom
							: R.anim.down_from_top);
			row.startAnimation(animation);
			lastPosition = position;
			return row;
		}

	}

	private void titles() {
		// TODO Auto-generated method stub
		addtotitlelist("Admission No");
		addtotitlelist("Reg. Form No");
		addtotitlelist("Name");
		addtotitlelist("Gender");
		addtotitlelist("Blood Group");
		addtotitlelist("DOB");
		addtotitlelist("Present Address");
		addtotitlelist("City");
		addtotitlelist("State");
		addtotitlelist("Pin code");
		addtotitlelist("Phone");
		addtotitlelist("Email ID");
		addtotitlelist("Remark");
		addtotitlelist("Local Guardian Name");
		addtotitlelist("Local Guardian Address");
		addtotitlelist("Local Guardian Phone No");
		addtotitlelist("Father Name");
		addtotitlelist("Father's Mobile");
		addtotitlelist("Father's E-Mail");
		addtotitlelist("Mother Name");
		addtotitlelist("Mother's Mobile");
		addtotitlelist("Mother's E-Mail");
		addtotitlelist("Occupation");
		addtotitlelist("Designation");
		addtotitlelist("Monthly Income");
		addtotitlelist("Permanent Address");
		addtotitlelist("City");
		addtotitlelist("State");
		addtotitlelist("Pin code");
		addtotitlelist("Phone");
		addtotitlelist("Mobile No");
	}

	private void idlist() {
		// TODO Auto-generated method stub
		addtolist("MCPH1_SCPH_lblAdmNo");
		addtolist("MCPH1_SCPH_lblRegNo");
		addtolist("MCPH1_SCPH_lblName");
		addtolist("MCPH1_SCPH_lblGender");
		addtolist("MCPH1_SCPH_lblBG");
		addtolist("MCPH1_SCPH_lblDOB");
		addtolist("MCPH1_SCPH_lblPresentAdd");
		addtolist("MCPH1_SCPH_lblCity1");
		addtolist("MCPH1_SCPH_lblstate1");
		addtolist("MCPH1_SCPH_lblPin1");
		addtolist("MCPH1_SCPH_lblPhone1");
		addtolist("MCPH1_SCPH_lblEmail1");
		addtolist("MCPH1_SCPH_lblRemark");
		addtolist("MCPH1_SCPH_lblLGuard");
		addtolist("MCPH1_SCPH_lblAddress1");
		addtolist("MCPH1_SCPH_lblPhone");
		addtolist("MCPH1_SCPH_lblfather");
		addtolist("MCPH1_SCPH_lblfmob");
		addtolist("MCPH1_SCPH_lblFEmail");
		addtolist("MCPH1_SCPH_lblmother");
		addtolist("MCPH1_SCPH_lblMobile");
		addtolist("MCPH1_SCPH_lblMEmail");
		addtolist("MCPH1_SCPH_lblOccupation");
		addtolist("MCPH1_SCPH_lblDesi");
		addtolist("MCPH1_SCPH_lblMon");
		addtolist("MCPH1_SCPH_lblParmanantAdd");
		addtolist("MCPH1_SCPH_lblCity2");
		addtolist("MCPH1_SCPH_lblState2");
		addtolist("MCPH1_SCPH_lblpin2");
		addtolist("MCPH1_SCPH_lblPhone2");
		addtolist("MCPH1_SCPH_lblmob");

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
			Intent i = new Intent(LogedInActivity.this, LoginActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		NavUtils.navigateUpFromSameTask(this);
		return true;
	}
}
