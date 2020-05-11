package com.netflix.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;

public class ConnectionManager {
	private static ConnectionManager streamReader = null;
	
	private HttpURLConnection connection;
	private BufferedReader stream;
	
	private ConnectionManager() {
		openConnection();
	}
	
	private void openConnection() {
		try {
			URL sps = new URL("https://tweet-service.herokuapp.com/sps");
			connection = (HttpURLConnection) sps.openConnection();
			connection.setRequestMethod("GET");
			connection.setReadTimeout(15*1000);
			if(connection.getErrorStream()==null && connection.getResponseCode() == 200) {
				stream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			}
		} catch(SocketException e) {
			System.out.println("SocketException:	"+e.getMessage());
			//e.printStackTrace();
			try{Thread.sleep(15*1000);}catch(InterruptedException e1) {}
			openConnection();
		} catch(Exception e) {
			System.out.println("A connection error happened. Reconnecting... "+e.getMessage());
			//e.printStackTrace();
			try{Thread.sleep(15*1000);}catch(InterruptedException e1) {}
			openConnection();
		}
	}
	
	public BufferedReader getStream() {
		return stream;
	}
	
	public static ConnectionManager getInstance() {
		if(streamReader==null) {
			streamReader = new ConnectionManager();
		}
		return streamReader;
	}
	
	public void close() {
		try {
			streamReader = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
