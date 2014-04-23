package com.teenscribblers.galgotiasuniversity;

import com.teenscribblers.galgotiasuniversity.R.style;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

public class AlertDialogManager {
	/**
	 * Function to display simple Alert Dialog
	 * 
	 * @param context
	 *            - application context
	 * @param title
	 *            - alert dialog title
	 * @param message
	 *            - alert message
	 * */
	public static void showAlertDialog(Context context, String title,
			String message) {
		Dialog d = new Dialog(context,style.Theme_D1NoTitleDim);
		d.setContentView(R.layout.dialog);
		TextView t = (TextView) d.findViewById(R.id.textViewdialog);
		t.setText(message);
		d.setCancelable(true);
		d.setCanceledOnTouchOutside(true);
		d.show();
	}
}
