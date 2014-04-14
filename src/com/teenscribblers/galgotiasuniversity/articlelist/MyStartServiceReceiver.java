package com.teenscribblers.galgotiasuniversity.articlelist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyStartServiceReceiver extends BroadcastReceiver {

	  @Override
	  public void onReceive(Context context, Intent intent) {
		  Log.i("ts", "in start service class");
	    Intent service = new Intent(context, ArticlesService.class);
	    context.startService(service);
	  }
	} 
