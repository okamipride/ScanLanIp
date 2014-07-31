package com.okamipride.scanlanip.scan;

public class IpUtil {
	 public static int IpToCidr(String ip) {
	        double sum = -2;
	        String[] part = ip.split("\\.");
	        for (String p : part) {
	            sum += 256D - Double.parseDouble(p);
	        }
	        return 32 - (int) (Math.log(sum) / Math.log(2d));
	 }
	 
	 public static long getUnsignedLongFromIp(String ip_addr) {
	        String[] a = ip_addr.split("\\.");
	        return (Integer.parseInt(a[0]) * 16777216 + Integer.parseInt(a[1]) * 65536
	                + Integer.parseInt(a[2]) * 256 + Integer.parseInt(a[3]));
	 }
	 
	 public static String getIpFromLongUnsigned(long ip_long) {
	        String ip = "";
	        for (int k = 3; k > -1; k--) {
	            ip = ip + ((ip_long >> k * 8) & 0xFF) + ".";
	        }
	        return ip.substring(0, ip.length() - 1);
	 }
}
