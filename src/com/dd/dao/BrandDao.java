package com.dd.dao;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dd.bean.Brand;
import com.dd.utils.DBHelper;

public class BrandDao {

	private DBHelper dbHelper;

	public BrandDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void addBrand(List<Brand> list) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		for (Brand brand : list) {
			if (findBrand(brand.getBrandID()) == null) {
				db.execSQL("insert into brand(brandID, name) values (?, ?)",
						new Object[] { brand.getBrandID(), brand.getName() });
			} else {
				update(brand);
			}
		}
		db.close();
	}

	public void update(Brand brand) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("update brand set name=? where brandID=?", new Object[] {
				brand.getName(), brand.getBrandID() });
	}

	public Brand findBrand(int brandID) {
		Brand brand = new Brand();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from brand where brandID=?",
				new String[] { String.valueOf(brandID) });
		if (cursor.moveToNext()) {
			brand.setBrandID(cursor.getInt(cursor.getColumnIndex("brandID")));
			brand.setName(cursor.getString(cursor.getColumnIndex("name")));
			return brand;
		} else
			return null;
	}
}
