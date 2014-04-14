package com.teenscribblers.galgotiasuniversity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter extends ArrayAdapter<String> {
	

	String[] desc;
	int[] icons_int = { R.drawable.ts_slideshow, R.drawable.ts_knowgu,
			R.drawable.ts_news, R.drawable.ts_websim};
	ImageView icons;
	TextView text;
	Context c;
	Activity act;
	public DrawerAdapter(Context context, int resource, String[] list,Activity a) {
		super(context, resource, list);
		// TODO Auto-generated constructor stub
		c=context;
		desc=list;
		act=a;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return desc.length;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
String color[]={"#f25353","#82dd00","#00c0dd","#db0061"};

		if (row == null) {
			row = act.getLayoutInflater().inflate(
					R.layout.drawer_items, parent, false);
			Typeface newsfont = Typeface.createFromAsset(getContext().getAssets(),
					"Adec.ttf");
			icons = (ImageView) row.findViewById(R.id.sidemenuicons);
			text = (TextView) row.findViewById(R.id.description);
			text.setText(desc[position]);
			icons.setImageDrawable(c.getResources().getDrawable(
					icons_int[position]));
			text.setTextColor(Color.parseColor(color[position]));
			text.setTypeface(newsfont);
		}
		return row;
	}

}
