package com.teenscribblers.galgotiasuniversity;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class Details extends SherlockFragment {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	View rootview;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater
				.inflate(R.layout.activity_details, container, false);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.

		mViewPager = (ViewPager) rootview.findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		return rootview;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new Detailsfragment();
			Bundle args = new Bundle();
			args.putInt(Detailsfragment.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 5 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_welcome).toUpperCase(l);
			case 1:
				return getString(R.string.title_approval).toUpperCase(l);
			case 2:
				return getString(R.string.title_campus).toUpperCase(l);

			case 3:
				return getString(R.string.title_achievements).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class Detailsfragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public Detailsfragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView;
			switch (getArguments() != null ? getArguments().getInt(
					ARG_SECTION_NUMBER) : 1) {
			case 0:
				rootView = inflater.inflate(R.layout.detailswelcome, container,
						false);
				return rootView;
			case 1:
				rootView = inflater.inflate(R.layout.detailsapproval,
						container, false);
				return rootView;
			case 2:
				rootView = inflater.inflate(R.layout.detailscampuslife,
						container, false);
				return rootView;
			case 3:
				rootView = inflater.inflate(R.layout.detailsachievements,
						container, false);
				return rootView;
			}
			return null;

		}

	}
}
