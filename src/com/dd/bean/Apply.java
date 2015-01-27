package com.dd.bean;

public class Apply {

	private Integer id;

	private String clientID;

	private String location;

	private String illustrate;

	private String time;

	private int isAccept = 0;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getIllustrate() {
		return illustrate;
	}

	public void setIllustrate(String illustrate) {
		this.illustrate = illustrate;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getIsAccept() {
		return isAccept;
	}

	public void setIsAccept(int isAccept) {
		this.isAccept = isAccept;
	}

}
