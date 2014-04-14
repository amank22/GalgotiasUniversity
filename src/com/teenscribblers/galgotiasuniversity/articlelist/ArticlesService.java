package com.teenscribblers.galgotiasuniversity.articlelist;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.teenscribblers.galgotiasuniversity.Connection_detect;
import com.teenscribblers.galgotiasuniversity.MainActivity;
import com.teenscribblers.galgotiasuniversity.R;

public class ArticlesService extends Service {

	public static final String ARTICLE_SERVICE = "com.teenscribblers.GU.SERVICE";
	// private static final String DEBUG_TAG = "Teenscribblers";
	private static final int Notify_TAG = 2821994;
	private NotificationManager notifier = null;
	private DbHelper dbhelper;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		dbhelper = new DbHelper(getBaseContext());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Connection_detect cd = new Connection_detect(getApplicationContext());

		Boolean isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent)
			doServiceStart(intent, startId);
		else
			stopSelf();
		return Service.START_NOT_STICKY;
	}

	private void doServiceStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		new DoInBackground().execute();
	}

	private class DoInBackground extends AsyncTask<String, Void, Boolean> {

		protected Boolean doInBackground(String... unused) {
			try {
				rssparser.parse();
			} catch (DOMException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				return false;
			} catch (SAXException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		protected void onPostExecute(Boolean result) {
			if (result == false) {
				alertbuilder();
				onCancelled();
				this.cancel(true);
			} else {
				if (arrays.Title != null) {
					int l = arrays.Title.length;
					int p = dbhelper.getArticlesCount();
					if (l > p) {
						int diff = l - p;
						for (int i = 0; i < diff; i++) {
							dbhelper.addnewarticles(arrays.Title[i],
									arrays.Pubdate[i], arrays.Content[i]);
						}
						Intent toLaunch = new Intent(getApplicationContext(),
								MainActivity.class);
						toLaunch.setAction("news_feed");
						PendingIntent intentBack = PendingIntent.getActivity(
								getApplicationContext(), 0, toLaunch, 0);
						Notification notify = new NotificationCompat.Builder(
								getApplicationContext())
								.setContentTitle("GU News")
								.setContentText(
										"You have got " + diff + " news update")
								.setSmallIcon(R.drawable.notification)
								.addAction(android.R.drawable.stat_notify_more,
										"GU News", intentBack)
								.setAutoCancel(true).build();
						notify.flags |= Notification.FLAG_AUTO_CANCEL;
						notifier.notify(Notify_TAG, notify);
					} else if (l < p) {
						dbhelper.deletearticles();
						for (int i = 0; i < l; i++) {
							dbhelper.addnewarticles(arrays.Title[i],
									arrays.Pubdate[i], arrays.Content[i]);
						}

					}
				}
			}
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			stopSelf();
		}
	}

	public void alertbuilder() {
		Toast.makeText(getBaseContext(), "Data not retrieved.Trying Again.",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
}
