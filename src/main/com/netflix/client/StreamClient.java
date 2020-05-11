package com.netflix.client;

import java.util.concurrent.BlockingQueue;

import com.netflix.connection.ConnectionManager;

public class StreamClient implements Runnable {
	private final BlockingQueue<String> eventQueue;
	
	public StreamClient(BlockingQueue<String> queue) {
        this.eventQueue = queue;
    }
	
	@Override
	public void run() {
		try {
			process();
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	private void process() throws InterruptedException{
		ConnectionManager.getInstance();
    	
		String inputLine;
		try {
			while((inputLine = ConnectionManager.getInstance().getStream().readLine()) != null) {
				if(inputLine.startsWith("data")) {
					eventQueue.add(inputLine);
			    }
			}
		} catch (Exception e) {
			System.out.println("Error while listening URL... "+e.getMessage());
			ConnectionManager.getInstance().close();
			process();
		}
	}
	
}
