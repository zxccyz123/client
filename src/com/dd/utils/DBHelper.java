package com.dd.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "washcar.db";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE table IF NOT EXISTS client"
				+ " (account TEXT PRIMARY KEY, password TEXT, name TEXT, gender int, address TEXT, creditID TEXT, logo TEXT)");

		db.execSQL("CREATE table IF NOT EXISTS apply"
				+ "(_id Integer PRIMARY KEY AUTOINCREMENT, clientID TEXT, location TEXT, illustrate TEXT, time TEXT, isAccept int)");

		db.execSQL("CREATE table IF NOT EXISTS brand"
				+ "(brandID Integer PRIMARY KEY, name TEXT)");

		db.execSQL("CREATE table IF NOT EXISTS car"
				+ "(carID Integer PRIMARY KEY, brandID Integer, number TEXT, type TEXT, clientID TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("ALTER TABLE user ADD COLUMN other TEXT");
	}

}
