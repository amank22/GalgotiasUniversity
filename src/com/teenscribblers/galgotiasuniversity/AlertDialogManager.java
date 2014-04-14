package com.teenscribblers.galgotiasuniversity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.teenscribblers.galgotiasuniversity.mSIM.LoginActivity;
import com.teenscribblers.galgotiasuniversity.mSIM.SessionManagment;

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
	public void showAlertDialog(final Context context, String title,
			String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						SessionManagment s=new SessionManagment(context);
						s.logoutUser();
						Intent i = new Intent(context, LoginActivity.class);
						context.startActivity(i);
					}
				});
		// Showing Alert Message
		alertDialog.show();
	}
}
