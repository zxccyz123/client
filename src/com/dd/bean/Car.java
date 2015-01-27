package com.dd.bean;

public class Car {

	private Integer carID;

	private int brandID;

	private String clientID;

	private String number;

	private String type;

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public Integer getCarID() {
		return carID;
	}

	public void setCarID(Integer carID) {
		this.carID = carID;
	}

	public int getBrandID() {
		return brandID;
	}

	public void setBrandID(int brandID) {
		this.brandID = brandID;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
