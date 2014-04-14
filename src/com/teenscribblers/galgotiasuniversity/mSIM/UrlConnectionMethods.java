package com.teenscribblers.galgotiasuniversity.mSIM;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.teenscribblers.galgotiasuniversity.AlertDialogManager;

import android.content.Context;
import android.util.Log;

public class UrlConnectionMethods {

	private static UrlConnectionParms u;
	private static boolean redirect;
	private static SessionManagment session;
	private Context context;
	private static AlertDialogManager alertcreate;

	public UrlConnectionMethods(Context context) {
		// TODO Auto-generated constructor stub
		u = new UrlConnectionParms(context);
		session = new SessionManagment(context);
		this.context = context;
	}

	public String LoginUrl(List<NameValuePair> nameValuePair, String url) {
		Log.d("Tag_GU", "in post");
		u.openconnect(url);
		u.openconnect(url);
		u.InitConnection("POST", url);
		Log.d("Tag_GU", "after open");
		OutputStream os = null;
		BufferedWriter writer = null;
		try {
			os = UrlConnectionParms.httpurl.getOutputStream();
		} catch (IOException e) {
			alertcreate.showAlertDialog(context, "Error!", "Connection Error");
			// e.printStackTrace();
		}
		try {
			writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			alertcreate.showAlertDialog(context, "Error!", "Connection Error");
			// e.printStackTrace();
		}
		try {
			writer.write(u.getWriterQuery(nameValuePair));
			writer.flush();
			writer.close();
			os.close();
		} catch (IOException e) {
			alertcreate.showAlertDialog(context, "Error!", "Connection Error");
			// e.printStackTrace();
		}
		u.connect();

		redirect = u.checkredirect();
		Log.d("Tag_GU", String.valueOf(redirect));
		if (redirect) {
			// get redirect url from location header field
			String newUrl = "http://122.160.168.158"
					+ UrlConnectionParms.httpurl.getHeaderField("Location");
			Log.d("Tag_GU", newUrl);
			// get the cookie
			if (UrlConnectionParms.cookie.isEmpty())
				cookieextract();
			// open the new connection again
			u.openconnect(newUrl);
			UrlConnectionParms.httpurl.setRequestProperty("Cookie",
					returncookie());
			Log.d("Tag_GU", "cookiestring=" + returncookie());
			if (newUrl.equals(UrlConnectionParms.HomeString)) {
				UrlConnectionParms.loginstat = true;
				return null;
			} else
				UrlConnectionParms.loginstat = false;
			u.connect();
		}

		String s = u.Reader();
		return s;
	}

	public String Posturl(List<NameValuePair> nameValuePair, String url) {
		Log.d("Tag_GU", "in post");
		u.openconnect(url);
		u.openconnect(url);
		u.InitConnection("POST", url);
		Log.d("Tag_GU", "after open");
		OutputStream os = null;
		BufferedWriter writer = null;
		try {
			os = UrlConnectionParms.httpurl.getOutputStream();
		} catch (IOException e) {
			alertcreate.showAlertDialog(context, "Error!", "Connection Error");
			// e.printStackTrace();
		}
		try {
			writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			alertcreate.showAlertDialog(context, "Error!", "Connection Error");
			// e.printStackTrace();
		}
		try {
			writer.write(u.getWriterQuery(nameValuePair));
			writer.flush();
			writer.close();
			os.close();
		} catch (IOException e) {
			alertcreate.showAlertDialog(context, "Error!", "Connection Error");
			// e.printStackTrace();
		}
		u.connect();

		redirect = u.checkredirect();
		Log.d("Tag_GU", String.valueOf(redirect));
		if (redirect) {
			// get redirect url from location header field
			String newUrl = "http://122.160.168.158"
					+ UrlConnectionParms.httpurl.getHeaderField("Location");
			Log.d("Tag_GU", newUrl);
			if (UrlConnectionParms.httpurl.getHeaderField("Location").equals(
					"/iSIM/Login?Session=timeout")) {
				UrlConnectionParms.loginstat = false;
			}
			// get the cookie
			if (UrlConnectionParms.cookie.isEmpty())
				cookieextract();

			// open the new connection again
			u.openconnect(newUrl);
			UrlConnectionParms.httpurl.setRequestProperty("Cookie",
					returncookie());
			Log.d("Tag_GU", "cookiestring=" + returncookie());
			u.connect();
		}
		String s = u.Reader();
		return s;
	}

	public String get(String url) {
		Log.d("Tag_GU", "in get");
		u.openconnect(url);
		if (session.getCookie().equals(""))
			session.createLoginSession(returncookie());
		UrlConnectionParms.httpurl.setRequestProperty("Cookie",
				session.getCookie());
		Log.d("Tag_GU", "cookiestring=" + returncookie());
		u.connect();
		if (u.requestcode() != 200
				|| UrlConnectionParms.httpurl
						.getURL()
						.toString()
						.equals("http://122.160.168.158/iSIM/Login?Session=timeout")) {
			return "Error";
		}
		Log.d("Request Code att", String.valueOf(u.requestcode()));
		Log.d("URL", UrlConnectionParms.httpurl.getURL().toString());
		String s = u.Reader();
		setvsev(s);
		return s;
	}

	String returncookie() {
		String cookiestring = "";
		for (int i = 0; i < UrlConnectionParms.cookie.size(); i++) {
			cookiestring += UrlConnectionParms.cookie.get(i) + ";";
		}
		return cookiestring;
	}

	void setvsev(String s) {
		Document document = Jsoup.parse(s);
		UrlConnectionParms.viewstate = document.select("#__VIEWSTATE").attr(
				"value");
		UrlConnectionParms.eventvalidate = document
				.select("#__EVENTVALIDATION").attr("value");
		Log.d("Tag_GU", UrlConnectionParms.viewstate + "-"
				+ UrlConnectionParms.eventvalidate);
	}

	void cookieextract() {
		// TODO Auto-generated method stub
		String headername = null;
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		for (int i = 1; (headername = UrlConnectionParms.httpurl
				.getHeaderFieldKey(i)) != null; i++) {
			if (headername.equalsIgnoreCase("Set-Cookie")) {
				String allcookies = "";
				allcookies = UrlConnectionParms.httpurl.getHeaderField(i);
				allcookies = allcookies.substring(0, allcookies.indexOf(";"));
				String cookiename = allcookies.substring(0,
						allcookies.indexOf("="));
				String cookievalue = allcookies.substring(
						allcookies.indexOf("=") + 1, allcookies.length());
				Log.d("Tag_GU", "cookie=" + allcookies);
				nvp.add(new BasicNameValuePair(cookiename, cookievalue));
			}
		}
		UrlConnectionParms.cookie = nvp;
	}
}
