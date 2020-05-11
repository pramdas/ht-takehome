package com.netflix.outstream;

import java.io.FileOutputStream;
import java.io.IOException;

public class SinkExecutor {
	public static FileOutputStream fos;
	public static String outFile = "stats.txt";
	
	static {
		try {
			fos = new FileOutputStream(outFile, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void write(String stat) {
		try {
			fos.write("data: ".getBytes());
			fos.write(stat.getBytes());
			fos.write("\n".getBytes());
			System.out.println("data: "+stat);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
