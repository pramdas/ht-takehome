package com.netflix.pojo;

public class StreamEvent {
	String device;
	String sev;
	String title;
	String country;
	Long time;
	
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getSev() {
		return sev;
	}
	public void setSev(String sev) {
		this.sev = sev;
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
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	
}
