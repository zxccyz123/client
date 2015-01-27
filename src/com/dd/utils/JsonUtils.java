package com.dd.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dd.bean.Brand;
import com.dd.bean.Car;
import com.dd.bean.Client;

/**
 * json工具类
 * 
 * @author daidong
 * 
 */
public class JsonUtils {

	private static int code;

	public static int getCode(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			code = jsonObject.getInt("code");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return code;
	}

	public static Client getClient(String json) {
		Client client = new Client();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject jo = jsonObject.getJSONObject("client");
			client.setAccount(jo.getString("account"));
			client.setAddress(jo.getString("address"));
			client.setCreditID(jo.getString("creditID"));
			client.setGender(jo.getInt("gender"));
			client.setName(jo.getString("name"));
			client.setPassword(jo.getString("password"));
			client.setLogo(jo.getString("logo"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return client;
	}

	public static Car getCar(String json) {
		Car car = new Car();

		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject jo = jsonObject.getJSONObject("car");
			car.setBrandID(jo.getInt("brandID"));
			car.setCarID(jo.getInt("carID"));
			car.setClientID(jo.getString("clientID"));
			car.setNumber(jo.getString("number"));
			car.setType(jo.getString("type"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return car;
	}

	public static List<Brand> getBrands(String json) {
		List<Brand> list = new ArrayList<Brand>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("brands");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jo = jsonArray.getJSONObject(i);
				Brand brand = new Brand();
				brand.setBrandID(jo.getInt("brandID"));
				brand.setName(jo.getString("name"));
				list.add(brand);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
