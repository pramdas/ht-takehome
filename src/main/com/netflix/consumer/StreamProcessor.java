package com.netflix.consumer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.netflix.outstream.SinkExecutor;
import com.netflix.pojo.StreamEvent;
import com.netflix.pojo.Stat;
import com.netflix.util.MyStringUtil;

public class StreamProcessor extends Thread {
	private final BlockingQueue<String> eventQueue;
	
	private Map<Long,Map<String,Integer>> stats = new HashMap<>();
	
	
	public StreamProcessor(BlockingQueue<String> queue) {
        this.eventQueue = queue;
    }
	
	@Override
	public void run() {
		try {
            while (!Thread.currentThread().isInterrupted()) {
                String event = eventQueue.take();
                process(event);
            }
            printStats();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
		
	}
	
	private void process(String event) throws InterruptedException {
		Gson g = new Gson();
		StreamEvent json = g.fromJson(event.replace("data: ", ""),StreamEvent.class);
		processEvent(json);
    }
	
	private void processEvent(StreamEvent event) {
		if(!event.getSev().equalsIgnoreCase("error")) {
			Long time = event.getTime();
			Long timeSec = TimeUnit.MILLISECONDS.toSeconds(time);
			if(!stats.containsKey(timeSec)) stats.put(timeSec, new HashMap<>());
			String device = event.getDevice();
			String title = MyStringUtil.cleanStr(event.getTitle());
			String country = event.getCountry();
			String key = device+"/"+title+"/"+country;
			Map<String,Integer> layer2map = stats.get(timeSec);
			int count = 0;
			if(layer2map.containsKey(key)) 
				count=layer2map.get(key);
			count++;
			layer2map.put(key,count);
			stats.put(timeSec,layer2map);
		}
	}

	private void printStats() {
		Gson gson = new Gson();
		for(Long ts:stats.keySet()) {
			Map<String,Integer> layer2map = stats.get(ts);
			for(String key:layer2map.keySet()) {
				
				Stat outStat = getStatObject(key, ts*1000, layer2map.get(key));
				SinkExecutor.write(gson.toJson(outStat));
			}
		}
	}
	
	private Stat getStatObject(String hashkey, Long timesec, int sps) {
		String[] tokens = hashkey.split("/");
		String device="",title="",country="";
		if(tokens.length>=3) {
			device = tokens[0];
			title = tokens[1];
			country = tokens[2];
		}
		Stat stat = new Stat(device, title, country, timesec, sps);

		return stat;
	}
	
	public void cancel() {
		interrupt();
	}
}
