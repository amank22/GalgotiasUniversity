package com.teenscribblers.galgotiasuniversity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ViewFlipper;

import com.actionbarsherlock.app.SherlockFragment;

public class MainView extends SherlockFragment {
	private ViewFlipper mFlippertop;
	View rootview;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater
				.inflate(R.layout.flipper_fragment, container, false);
		mFlippertop = (ViewFlipper) rootview
				.findViewById(R.id.view_flipper_top);

		mFlippertop.startFlipping();
		mFlippertop.getInAnimation().setAnimationListener(
				new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						int dchild = mFlippertop.getDisplayedChild();
						int tchild = mFlippertop.getChildCount();
						if (dchild == tchild - 1) {
							mFlippertop.stopFlipping();
						}
					}
				});

		return rootview;
	}

}