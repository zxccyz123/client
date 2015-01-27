package com.dd.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dd.bean.Apply;
import com.dd.utils.DBHelper;

public class ApplyDao {

	private DBHelper dbHelper;

	public ApplyDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void save(Apply apply) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(
				"insert into apply (clientID, location, illustrate, time, isAccept) values (?, ?, ?, ?, ?)",
				new Object[] { apply.getClientID(), apply.getLocation(),
						apply.getIllustrate(), apply.getTime(),
						apply.getIsAccept() });
		db.close();
	}

	public List<Apply> find() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		List<Apply> list = new ArrayList<Apply>();
		Cursor cursor = db.rawQuery("select * from apply", null);
		while (cursor.moveToNext()) {
			Apply apply = new Apply();
			apply.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			apply.setIsAccept(cursor.getInt(cursor.getColumnIndex("isAccept")));
			apply.setClientID(cursor.getString(cursor
					.getColumnIndex("clientID")));
			apply.setIllustrate(cursor.getString(cursor
					.getColumnIndex("illustrate")));
			apply.setLocation(cursor.getString(cursor
					.getColumnIndex("location")));
			apply.setTime(cursor.getString(cursor.getColumnIndex("time")));
			list.add(apply);
		}

		return list;
	}
}
