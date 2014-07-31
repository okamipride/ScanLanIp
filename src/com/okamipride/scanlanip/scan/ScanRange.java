package com.okamipride.scanlanip.scan;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.util.Log;


public class ScanRange implements Callable<ArrayList<InetAddress>> {
	
	private String TAG = "ScanRange";
	private long start = 0;
	private long end = 0;
	//private final int REACH_TIME_OUT = 100;
	public static int reachOutTime = 100;
	
	public ScanRange(long start, long end) {
		this.start = start;
		this.end = end;
	}
	
	@Override
	public ArrayList<InetAddress> call() throws Exception {
		ArrayList<InetAddress> devicesAddr = new ArrayList<InetAddress>();
		InetAddress address = null;
		Log.d(TAG, "IP Scan from " + IpUtil.getIpFromLongUnsigned(start) + " to " + IpUtil.getIpFromLongUnsigned(end) + " start!");
		for (long i = start; i <= end; i++) {
			if (Thread.currentThread().isInterrupted()) {
				Log.e(TAG, "thread is interupted");
				return null;
			}
			String addr = IpUtil.getIpFromLongUnsigned(i);
			try {
				address = InetAddress.getByName(addr);
				if (address != null) {
					if (address.isReachable(reachOutTime)) {  
						devicesAddr.add(address);
					    Log.e(TAG, "IPADDR: " + addr +  " Device: " + address.getHostName());
					}
				}
			} catch (UnknownHostException e) {	
				Log.e(TAG, "UnknownHostException");
				i = end + 1; // exit loop
			} catch (IOException e) {
				Log.e(TAG, "IOException");
				i = end + 1; // exit loop
			}
		}
		Log.d(TAG, "IP Scan from " + IpUtil.getIpFromLongUnsigned(start) + " to " + IpUtil.getIpFromLongUnsigned(end)+ "end!");		
		return devicesAddr;
	}
	
	public void setReachOutTime(int ms) {
		if (ms > 0)
			reachOutTime = ms;  
	}
}
