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

import android.content.Context;
import android.util.Log;

public class UrlConnectionMethods {

	private static UrlConnectionParms u;
	private static boolean redirect;
	private static SessionManagment session;

	public UrlConnectionMethods(Context context) {
		// TODO Auto-generated constructor stub
		u = new UrlConnectionParms(context);
		session = new SessionManagment(context);
	}

	public String LoginUrl(List<NameValuePair> nameValuePair, String url) {
		Log.d("Tag_GU", "in post");
		String status = "ok";
		status = u.openconnect(url);
		Log.d("STATUS", status);
		if (!status.equals("ok"))
			return "error";
		status = u.InitConnection("POST", url);
		Log.d("STATUS", status);
		if (!status.equals("ok"))
			return "error";
		Log.d("Tag_GU", "after open");
		OutputStream os = null;
		BufferedWriter writer = null;
		try {
			os = UrlConnectionParms.httpurl.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return "error";

		}
		try {
			writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "error";
		}
		try {
			writer.write(u.getWriterQuery(nameValuePair));
			writer.flush();
			writer.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
		status = u.connect();
		if (!status.equals("ok"))
			return "error";
		// get the cookie
		cookieextract();
		redirect = u.checkredirect();
		Log.d("Tag_GU", String.valueOf(redirect));
		if (redirect) {
			// get redirect url from location header field
			String newUrl = "http://122.160.168.158"
					+ UrlConnectionParms.httpurl.getHeaderField("Location");
			Log.d("Tag_GU", newUrl);
			// open the new connection again
			status = u.openconnect(newUrl);
			if (!status.equals("ok"))
				return "error";
			session.createLoginSession(returncookie());
			UrlConnectionParms.httpurl.setRequestProperty("Cookie",
					returncookie());
			Log.d("Tag_GU", "cookiestring=" + returncookie());
			if (newUrl.equals(UrlConnectionParms.HomeString)) {
				UrlConnectionParms.loginstat = true;
				return "ok";
			} else {
				UrlConnectionParms.loginstat = false;
				return "error";
			}

		}

		return "error";
	}

	public String Posturl(List<NameValuePair> nameValuePair, String url) {
		Log.d("Tag_GU", "in post");
		String status;
		status = u.openconnect(url);
		if (!status.equals("ok"))
			return "error";
		status = u.InitConnection("POST", url);
		if (!status.equals("ok"))
			return "error";
		Log.d("Tag_GU", "after open");
		OutputStream os = null;
		BufferedWriter writer = null;
		try {
			os = UrlConnectionParms.httpurl.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
		try {
			writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "error";
		}
		try {
			writer.write(u.getWriterQuery(nameValuePair));
			writer.flush();
			writer.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
			return "error";

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
			status = u.openconnect(newUrl);
			if (!status.equals("ok"))
				return "error";
			UrlConnectionParms.httpurl.setRequestProperty("Cookie",
					returncookie());
			Log.d("Tag_GU", "cookiestring=" + returncookie());
			status = u.connect();
			if (!status.equals("ok"))
				return "error";
		}
		String s = u.Reader();
		if (s.equals("error"))
			return "error";
		return s;
	}

	public String get(String url) {
		Log.d("Tag_GU", "in get");
		String status;
		status = u.openconnect(url);
		if (!status.equals("ok"))
			return "error";
		UrlConnectionParms.httpurl.setRequestProperty("Cookie",
				session.getCookie());
		Log.d("Tag_GU", "cookiestring=" + returncookie());
		u.connect();
		if (u.requestcode() != 200
				|| UrlConnectionParms.httpurl
						.getURL()
						.toString()
						.equals("http://122.160.168.158/iSIM/Login?Session=timeout")) {
			return "error";
		}
		Log.d("Request Code att", String.valueOf(u.requestcode()));
		Log.d("URL", UrlConnectionParms.httpurl.getURL().toString());
		String s = u.Reader();
		if (s.equals("error"))
			return "error";
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
