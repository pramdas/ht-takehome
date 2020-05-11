package com.netflix.pojo;

import java.util.Date;

public class Stat {
	String device;
	String title;
	String country;
	Long timesecond;
	int sps;
	//String date;
	
	public Stat(String device, String title, String country, Long timesecond, int sps) {
		this.device = device;
		this.title = title;
		this.country = country;
		this.timesecond = timesecond;
		//this.date = (new Date(timesecond))+"";
		this.sps = sps;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Long getTimesecond() {
		return timesecond;
	}
	public void setTimesecond(Long timesecond) {
		this.timesecond = timesecond;
	}
	public int getSps() {
		return sps;
	}
	public void setSps(int sps) {
		this.sps = sps;
	}
//	public String getDate() {
//		return date;
//	}
//	public void setDate(String date) {
//		this.date = date;
//	}
	
	
}
