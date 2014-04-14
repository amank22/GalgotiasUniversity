package com.teenscribblers.galgotiasuniversity.articlelist;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragment;
import com.teenscribblers.galgotiasuniversity.MainActivity;
import com.teenscribblers.galgotiasuniversity.R;

public class Podcast extends SherlockFragment {
	View rootview;
	WebView Content;
	ProgressBar progressBar;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview=inflater.inflate(R.layout.podcast,container,false);
		progressBar = (ProgressBar) rootview.findViewById(R.id.progressBar1);
		Content = (WebView) rootview.findViewById(R.id.webView1);
		MainActivity activity = (MainActivity) getActivity();
		Bundle n=activity.getSavedData();
		String[] i=n.getStringArray("number");
		final String CurrentTitle = i[0];
		String CurrentContent = i[1];
		getSherlockActivity().getSupportActionBar().setTitle(CurrentTitle);
		Content.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		Content.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		Content.setWebViewClient(new myWebClient());
		Content.loadDataWithBaseURL(null, CurrentContent, "text/html", "utf-8",
				"about:blank");
		return rootview;
	}
	public class myWebClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return true;

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);

			progressBar.setVisibility(View.GONE);
		}
	}

	}
