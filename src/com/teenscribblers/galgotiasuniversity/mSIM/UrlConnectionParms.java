package com.teenscribblers.galgotiasuniversity.mSIM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import com.teenscribblers.galgotiasuniversity.AlertDialogManager;

import android.content.Context;
import android.util.Log;

public class UrlConnectionParms {
	public static HttpURLConnection httpurl;
	public static final String LoginString = "http://122.160.168.158/iSIM/Login";
	public static final String HomeString = "http://122.160.168.158/iSIM/Home";
	public static final String AttendanceString = "http://122.160.168.158/iSIM/Student/TodayAttendence";
	public static final String AlertString = "http://122.160.168.158/iSIM/Student/Alerts";
	public static final String ResultString = "http://122.160.168.158/iSIM/Student/ExamResult";
	public static final String PersonalInfoString = "http://122.160.168.158/iSIM/Student/Course";
	public static final String StudentImagebase = "http://122.160.168.158/iSIM/studentimages/";
	public static String viewstate = "";
	public static String eventvalidate = "";
	public static boolean loginstat = false;
	public static ArrayList<NameValuePair> cookie = new ArrayList<NameValuePair>();
	public static URL currenturl;
	//public static String cookieString = "";
	public static List<String> ids=null;
	public static List<String> idvalues=null;
	public static List<String> titletext=null;
	SessionManagment session;
	private static AlertDialogManager alertcreate;
	private Context context;

	public UrlConnectionParms(Context context) {
		// TODO Auto-generated constructor stub
		Log.d("Tag_GU", "in urlconparms constructor");
		session=new SessionManagment(context);
		this.context=context;

	}

	public void InitConnection(String type, String url) {
		Log.d("Tag_GU", "in init connection");
		try {
			httpurl.setRequestMethod(type);
		} catch (ProtocolException e) {
			alertcreate.showAlertDialog(context.getApplicationContext(),"Error!","Connection Error");
			//e.printStackTrace();
		}

		//httpurl.setReadTimeout(timeout);
		httpurl.setUseCaches(true);
		httpurl.setDoInput(true);
		httpurl.setDoOutput(true);
		httpurl.setInstanceFollowRedirects(false);
		httpurl.setChunkedStreamingMode(0);
		if(!session.getCookie().equals(""))
		httpurl.setRequestProperty("Cookie", session.getCookie());
	}

	public String Reader() {
		Log.d("Tag_GU", "String Reader");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					httpurl.getInputStream(), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			alertcreate.showAlertDialog(context.getApplicationContext(),"Error!","Connection Error");
		//	e1.printStackTrace();
		} catch (IOException e1) {
			alertcreate.showAlertDialog(context.getApplicationContext().getApplicationContext(),"Error!","Connection Error");
			//e1.printStackTrace();
		}
		String webpage = "", data = "";
		try {
			while ((data = reader.readLine()) != null) {
				webpage += data + "\n";
			}
		} catch (IOException e) {
			alertcreate.showAlertDialog(context.getApplicationContext(),"Error!","Connection Error");
		//	e.printStackTrace();
		}
		return webpage;
	}

	public void disconnect() {
		httpurl.disconnect();
	}

	public void openconnect(String url) {
		URL u = null;
		try {
			u = new URL(url);
		} catch (MalformedURLException e) {
			alertcreate.showAlertDialog(context.getApplicationContext(),"Error!","Connection Error");
			//e.printStackTrace();
		}
		try {
			httpurl = (HttpURLConnection) u.openConnection();
		} catch (IOException e) {
			alertcreate.showAlertDialog(context.getApplicationContext(),"Error!","Connection Error");
			//e.printStackTrace();
		}
		currenturl = u;
	}

	public void connect() {
		try {
			httpurl.connect();
		} catch (IOException e) {
			alertcreate.showAlertDialog(context.getApplicationContext(),"Error!","Connection Error");
			//e.printStackTrace();
		}
	}

	public int requestcode() {
		try {

			return httpurl.getResponseCode();
		} catch (IOException e) {
			return 0;
		}
		
	}

	public boolean checkredirect() {
		int status = requestcode();
		Log.d("Tag_GU_Request", "Request Code=" + String.valueOf(status));
		if (status != HttpURLConnection.HTTP_OK) {
			if (status == HttpURLConnection.HTTP_MOVED_TEMP)
				return true;
		}
		
		return false;
	}

	public String getWriterQuery(List<NameValuePair> params) {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (NameValuePair pair : params) {
			if (first) {
				first = false;
			} else {
				result.append("&");
			}
			try {
				result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				alertcreate.showAlertDialog(context.getApplicationContext().getApplicationContext(),"Error!","Connection Error");
			//	e.printStackTrace();
			}
			result.append("=");
			try {
				result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				alertcreate.showAlertDialog(context.getApplicationContext().getApplicationContext(),"Error!","Connection Error");
				//e.printStackTrace();
			}

		}
		return result.toString();
	}

}
