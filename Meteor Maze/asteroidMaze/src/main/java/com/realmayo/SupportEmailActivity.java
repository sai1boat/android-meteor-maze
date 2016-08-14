package com.realmayo;

import android.R;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings.Secure;

import com.joao024.mystery3d.mm.game.MyGameActivity;

public class SupportEmailActivity {

	private MyGameActivity mg;

	public SupportEmailActivity(MyGameActivity mg) {
		this.mg = mg;
	}

	// CALL THIS METHOD UPON THE USER CLICKING A HELP BUTTON ON THE SCREEN
	public void showHelpPopup() {
		mg.runOnUiThread(new Runnable() {
			public void run() {
				// DEFINE A POPUP MODAL THAT NEEDS TO BE DISPLAYED
				AlertDialog.Builder mBuildDialog = new AlertDialog.Builder(mg);
				mBuildDialog
						.setTitle("Help!")
						.setMessage(
								"Send us an email if you are experiencing any problems, or if you have any suggestions to improve the game. \n"
										+ "\n")
						// ALLOW THE USE OF THE HARD BACK BUTTON TO CLOSE THE
						// POPUP
						.setCancelable(true)
						// DEFINE THE OK BUTTON
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// DEFINE HOW THE SUPPORT EMAIL WILL
										// LOOK INCLUDING A PREPOPULATED SUBJECT
										// LINE CONTAINING USEFUL DEVICE INFO
										Intent myIntent1 = new Intent(
												android.content.Intent.ACTION_SEND);
										myIntent1
												.putExtra(
														android.content.Intent.EXTRA_EMAIL,
														new String[] { "ibowkdher@gmail.com" });
										final String my1 = Secure.getString(mg
												.getBaseContext()
												.getContentResolver(),
												Secure.ANDROID_ID);
										final String my2 = android.os.Build.DEVICE;
										final String my3 = android.os.Build.MANUFACTURER;
										final String my4 = android.os.Build.MODEL;
										final String my5 = android.os.Build.VERSION.RELEASE;
										final int my6 = android.os.Build.VERSION.SDK_INT;
										final String my7 = android.os.Build.BRAND;
										final String my8 = android.os.Build.VERSION.INCREMENTAL;
										final String my9 = android.os.Build.PRODUCT;
										myIntent1
												.putExtra(
														android.content.Intent.EXTRA_SUBJECT,
														"Support Request: "
																+ my1 + "x"
																+ my2 + "x"
																+ my3 + "x"
																+ my4 + "x"
																+ my5 + "x"
																+ my6 + "x"
																+ my7 + "x"
																+ my8 + "x"
																+ my9);
										myIntent1.setType("text/plain");
										// IN CASE EMAIL APP FAILS, THEN DEFINE
										// THE OPTION TO LAUNCH SUPPORT WEBSITE
										String url2 = "http://asteroidmaze-jwow.rhcloud.com/";
										Intent myIntent2 = new Intent(
												Intent.ACTION_VIEW);
										myIntent2.setData(Uri.parse(url2));
										// IF USER CLICKS THE OK BUTTON, THEN DO
										// THIS
										try {
											// TRY TO LAUNCH TO EMAIL APP
											mg.startActivity(Intent
													.createChooser(myIntent1,
															"Send email to JBowker Games"));
											// startActivity(myIntent1);
										} catch (ActivityNotFoundException ex) {
											// ELSE LAUNCH TO WEB BROWSER
											mg.startActivity(myIntent2);
										}
									}
								})
						// DEFINE THE CANCEL BUTTON
						.setNegativeButton("CANCEL",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				// NOW WE ACTUALLY DISPLAY THE POPUP MODAL
				AlertDialog myDialog = mBuildDialog.create();
				myDialog.setIcon(R.drawable.ic_dialog_email);
				myDialog.show();
			}
		});
	}
}

