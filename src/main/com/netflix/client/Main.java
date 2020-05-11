package com.netflix.client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.netflix.connection.ConnectionManager;
import com.netflix.consumer.StreamProcessor;

public class Main {
	
	public static void main(String[] args) {

        BlockingQueue<String> queue = new LinkedBlockingQueue<>();

        new Thread(new StreamClient(queue)).start();
        
        try {
	    	while(true) {	
	    		StreamProcessor oneSecThread = new StreamProcessor(queue);
	    		oneSecThread.start();
		    	try {
		    		TimeUnit.SECONDS.sleep(1);
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
				} finally{
					oneSecThread.cancel();
		    	}
	    	}
    	} finally {
    		ConnectionManager.getInstance().close();
    	}

    }
}
