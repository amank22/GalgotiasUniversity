package com.teenscribblers.galgotiasuniversity.articlelist;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	static final String dbName = "TeenScribblers.db";
	static final String TableName = "Articles";
	static final String colTitle = "Title";
	static final String colpubdate = "Pub_Date";
	static final String colContent = "Content";
	static final String colID = "ArticlesID";
	
	static final String articlestablesql = "CREATE TABLE " + TableName + " ("
			+ colID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colTitle
			+ " TEXT, " + colpubdate + " TEXT, " + colContent + " TEXT)";

	public DbHelper(Context context) {
		super(context, dbName, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(articlestablesql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TableName);
		onCreate(db);
	}

	void addnewarticles(String title, String date, String content) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(colTitle, title);
		cv.put(colpubdate, date);
		cv.put(colContent, content);
		db.insert(TableName, colTitle, cv);
		db.close();
	}
	int getArticlesCount() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("Select * from " + TableName, null);
		int x = cur.getCount();
		cur.close();
		return x;
	}
	
	public List<String> getAllArticlesTitle() {
		List<String> list = new ArrayList<String>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("Select " + colID + ", " + colTitle + " from "
				+ TableName+" ORDER BY "+colID+" DESC", null);
		// looping through all rows and adding to list
		if (cur.moveToFirst()) {
			do {
				list.add(cur.getString(1));
			} while (cur.moveToNext());
		}
		// closing connection
		cur.close();
		db.close();

		return list;
	}
	public List<String> getAllArticlesDate() {
		List<String> list = new ArrayList<String>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("Select " + colID + ", " + colpubdate + " from "
				+ TableName+" ORDER BY "+colID+" DESC", null);
		// looping through all rows and adding to list
		if (cur.moveToFirst()) {
			do {
				list.add(cur.getString(1));
			} while (cur.moveToNext());
		}
		// closing connection
		cur.close();
		db.close();

		return list;
	}
	public List<String> getAllArticlesContent() {
		List<String> list = new ArrayList<String>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("Select " + colID + ", " + colContent + " from "
				+ TableName+" ORDER BY "+colID+" DESC", null);
		// looping through all rows and adding to list
		if (cur.moveToFirst()) {
			do {
				list.add(cur.getString(1));
			} while (cur.moveToNext());
		}
		// closing connection
		cur.close();
		db.close();

		return list;
	}
	
	void deletearticles(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TableName, null, null);
	}
}
