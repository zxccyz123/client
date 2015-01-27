package com.dd.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dd.bean.Car;
import com.dd.utils.DBHelper;

public class CarDao {

	private DBHelper dbHelper;

	public CarDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void add(Car car) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(
				"insert into car (carID, brandID, number, type, clientID) values (?, ?, ?, ?, ?)",
				new Object[] { car.getCarID(), car.getBrandID(),
						car.getNumber(), car.getType(), car.getClientID() });
		db.close();
	}

	public Car find(Integer carID) {
		Car car = new Car();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from car where carID=?",
				new String[] { String.valueOf(carID) });
		if (cursor.moveToNext()) {
			car.setBrandID(cursor.getInt(cursor.getColumnIndex("brandID")));
			car.setCarID(cursor.getInt(cursor.getColumnIndex("carID")));
			car.setNumber(cursor.getString(cursor.getColumnIndex("number")));
			car.setType(cursor.getString(cursor.getColumnIndex("type")));
			car.setClientID(cursor.getString(cursor.getColumnIndex("clientID")));
		}
		return car;
	}
}
