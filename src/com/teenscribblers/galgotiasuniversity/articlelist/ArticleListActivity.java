package com.teenscribblers.galgotiasuniversity.articlelist;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.teenscribblers.galgotiasuniversity.MainActivity;
import com.teenscribblers.galgotiasuniversity.R;

public class ArticleListActivity extends SherlockListFragment {
	View rootview;
	private CardsAdapter adapter;
	ArticleListActivity context = this;
	private String BROADCAST = "com.teenscribblers.GU.Broadcast";
	MyScheduleReceiver myreceiver;
	private DbHelper helper;
	List<String> datatitle;
	List<String> dataDate;
	List<String> dataContent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootview = inflater.inflate(R.layout.listmain, container, false);
		// progress = (ProgressBar) rootview.findViewById(R.id.progressBarlist);
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setTitle("GU NEWS");
		actionBar.setDisplayHomeAsUpEnabled(true);
		helper = new DbHelper(getActivity());
		datatitle = helper.getAllArticlesTitle();
		dataDate = helper.getAllArticlesDate();
		dataContent = helper.getAllArticlesContent();
		if (datatitle.size() != 0 && datatitle != null)
			populate_listview();
		myreceiver = new MyScheduleReceiver();
		return rootview;

	}

	// method to populate the listview with the RSS titles
	public void populate_listview() {

		adapter = new CardsAdapter(getActivity().getApplicationContext(),
				android.R.layout.simple_list_item_1);
		setListAdapter(adapter);

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// start newsfeed activity passing position in bundle
		Bundle mybundle = new Bundle();
		String[] a = { datatitle.get(position), dataContent.get(position) };
		mybundle.putStringArray("number", a);
		MainActivity activity = (MainActivity) getActivity();
		activity.saveData(mybundle);

		getSherlockActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, new Podcast())
				.addToBackStack(null).commit();
	}

	private class CardsAdapter extends ArrayAdapter<String> {
		String[] colorlist = { "#f2a400", "#e00707", "#4ac925", "#00d5f2",
				"#f269ae", "#05001f", "#efa560" };
		ImageView img;
		TextView text, pdtext;
		private int lastPosition = -1;
		Typeface newsfont = Typeface.createFromAsset(getContext().getAssets(),
				"Adec.ttf");

		public CardsAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datatitle.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View row = convertView;

			if (row == null) {

				row = getActivity().getLayoutInflater().inflate(
						R.layout.recent_activity_news, parent, false);
			}
				pdtext = (TextView) row.findViewById(R.id.pdate);
				pdtext.setHighlightColor(Color.LTGRAY);
				pdtext.setTypeface(newsfont);
				pdtext.setText(dataDate.get(position).substring(0, 19));
				img = (ImageView) row.findViewById(R.id.imageView1);

				img.setBackgroundColor(Color
						.parseColor(colorlist[position % 7]));
				text = (TextView) row.findViewById(R.id.description);
				text.setTypeface(newsfont);
				text.setText(datatitle.get(position));
				// image processing
				// Imageview to show
				ImageView image = (ImageView) row.findViewById(R.id.thumbnews);
				int loader = R.drawable.news_icon;

				boolean booleanimage = dataContent.get(position).contains(
						"imageanchor");
				if (booleanimage) {
					String[] s = dataContent.get(position).split("imageanchor",
							2);
					String[] hrefstring = s[0].split("href");
					int i = hrefstring.length;
					String image_url = hrefstring[i - 1].replace("&quot;", "")
							.replace("=", "").replace("\"", "");
					//Log.e("link_image", image_url);
					// ImageLoader class instance
					ImageLoader imgLoader = new ImageLoader(getActivity());
					// whenever you want to load an image from url
					// call DisplayImage function
					// url - image url to load
					// loader - loader image, will be displayed before getting
					// image
					// image - ImageView
					imgLoader.DisplayImage(image_url, loader, image);

				} else {
					image.setBackgroundResource(loader);
				}
				Animation animation = AnimationUtils.loadAnimation(
						getActivity(),
						(position > lastPosition) ? R.anim.up_from_bottom
								: R.anim.down_from_top);
				row.startAnimation(animation);
				lastPosition = position;
			

			return row;

		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		getSherlockActivity().getSupportMenuInflater().inflate(R.menu.listmenu,
				menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.refresh) {
			if (helper.getArticlesCount()!=0) {
				Toast.makeText(getActivity(), "Retriving new data!",
						Toast.LENGTH_SHORT).show();
				// getActivity().unregisterReceiver(myreceiver);
				Intent i = new Intent("com.teenscribblers.GU.SERVICE");
				getActivity().stopService(i);
			}
			IntentFilter inf = new IntentFilter(BROADCAST);
			getActivity().registerReceiver(myreceiver, inf);
			Intent intent = new Intent(BROADCAST);
			getActivity().sendBroadcast(intent);
			
		}
		return super.onOptionsItemSelected(item);
	}

}
