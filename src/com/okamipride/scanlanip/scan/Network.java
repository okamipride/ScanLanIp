package com.okamipride.scanlanip.scan;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


public class Network {
	
	private final static String TAG = "Network";
	
	public static DhcpInfo getNetworkInfo(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wifi.getDhcpInfo();
	}
	
	public static String getWifiMAC(Context context) {
		String mac = null;
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
		    WifiInfo info = wifiManager.getConnectionInfo();
		    mac = info.getMacAddress();
		} else {
		    wifiManager.setWifiEnabled(true);
		    WifiInfo info = wifiManager.getConnectionInfo();
		    mac = info.getMacAddress();
		    wifiManager.setWifiEnabled(false);
		}
		return mac;
	}

	public static boolean isConnectedToInternet(int timeout) {
		boolean flag = false;
		Socket localSocket = new Socket();
	    try {
			localSocket.bind(null);
			localSocket.connect(new InetSocketAddress("8.8.8.8", 53), timeout);
		    localSocket.close();
		    flag = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return flag;
	}
		
	/**
	 *  Get Network type
	 */
	public static int getNetworkType(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
		if (activeNetworkInfo == null) {
			Log.e(TAG, "Not have Network Info");
			return -1;
		}
		return activeNetworkInfo.getType();		
	}
	
	/**
	 * Return IP address of an active network
	 * @param context 
	 * @return return ip Address string in xxx.xxx.xxx.xxx format , 
	 * 		   return null if no active network
	 */
	public static String getIpAddr(Context context) {
		int ip = 0;
		String ipString = null;
		DhcpInfo info = getNetworkInfo(context);
		if (info != null) {
			ip = info.ipAddress;
			ipString = String.format(
				"%d.%d.%d.%d",
				(ip & 0xff),
				(ip >> 8 & 0xff),
				(ip >> 16 & 0xff),
				(ip >> 24 & 0xff)
			);
		}
		return ipString;
	}
	
	/**
	 * Return gateway ip address
	 * @param context
	 * @return return gateway ip Address string in xxx.xxx.xxx.xxx format , 
	 * 		   return null if no active network
	 */
	public static String getGatewayAddr(Context context) {
		int gateway = 0;
		String gatewayString = null;
		DhcpInfo info = getNetworkInfo(context);
		if (info != null) {
			gateway = info.gateway;
			gatewayString = String.format(
				"%d.%d.%d.%d",
				(gateway & 0xff),
				(gateway >> 8 & 0xff),
				(gateway >> 16 & 0xff),
				(gateway >> 24 & 0xff)
			);
		}
		return gatewayString;
	}
	
	public static String getDNS1Addr(Context context) {
		int dns = 0;
		String dnsString = null;
		DhcpInfo info = getNetworkInfo(context);
		if (info != null) {
			dns = info.dns1;
			dnsString = String.format(
				"%d.%d.%d.%d",
				(dns & 0xff),
				(dns >> 8 & 0xff),
				(dns >> 16 & 0xff), (dns >> 24 & 0xff)
			);
		}
		return dnsString;
	}
	
	public static String getMaskAddr(Context context) {
		int mask = 0;
		String maskString = null;
		DhcpInfo info = getNetworkInfo(context);
		if (info != null) {
			mask = info.netmask;
			maskString = String.format(
				"%d.%d.%d.%d",
				(mask & 0xff),
				(mask >> 8 & 0xff),
				(mask >> 16 & 0xff),
				(mask >> 24 & 0xff)
			);
		}
		return maskString;
	}
	
	/**
	 * Ping to host for 100 s 1 successful return
	 * @param str ip Address to ping
	 * @return true ping successfully , false ping failed 
	 */
	public static boolean pingHost(String str) { 
		boolean ret;
        try {  
            Process p = Runtime.getRuntime().exec("ping -c 1 -w 200 " + str);  
            int status = p.waitFor();  
            if (status == 0) {  
            	ret = true;
                Log.d(TAG, "success");
            } else {  
            	ret = false;
                Log.d(TAG, "failed");
            }  
        } catch (IOException e) {  
        	ret = false; 
        } catch (InterruptedException e) {  
        	e.printStackTrace();
        	ret = false; 
        }  
          
        return ret;  
    }
}
