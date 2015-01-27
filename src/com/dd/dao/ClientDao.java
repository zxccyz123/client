package com.dd.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dd.bean.Client;
import com.dd.utils.DBHelper;

public class ClientDao {
	private DBHelper dbHelper;

	public ClientDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 添加客户信息
	 */
	public void addClient(Client client) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (find(client.getAccount()) == null)
			db.execSQL(
					"insert into client(account, password, name, gender, address, creditID, logo) values ( ?, ?, ?, ?, ?, ?, ?)",
					new Object[] { client.getAccount(), client.getPassword(),
							client.getName(), client.getGender(),
							client.getAddress(), client.getCreditID(),
							client.getLogo() });
		else
			update(client);
		db.close();
	}

	public void update(Client client) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(
				"update client set password=?, name=?, gender=?, address=?, creditID=?, logo=? where account=?",
				new Object[] { client.getPassword(), client.getName(),
						client.getGender(), client.getAddress(),
						client.getCreditID(), client.getLogo(),
						client.getAccount() });
	}

	public Client find(String account) {
		Client client = new Client();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from client where account=?",
				new String[] { account });
		if (cursor.moveToNext()) {
			client.setAccount(account);
			client.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			client.setCreditID(cursor.getString(cursor
					.getColumnIndex("creditID")));
			client.setGender(cursor.getInt(cursor.getColumnIndex("gender")));
			client.setLogo(cursor.getString(cursor.getColumnIndex("logo")));
			client.setName(cursor.getString(cursor.getColumnIndex("name")));
			client.setPassword(cursor.getString(cursor
					.getColumnIndex("password")));
			return client;
		} else
			return null;
	}

}
