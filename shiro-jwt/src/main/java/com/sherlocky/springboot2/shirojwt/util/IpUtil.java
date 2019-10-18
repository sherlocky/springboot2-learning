package com.sherlocky.springboot2.shirojwt.util;

import org.apache.commons.text.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * IP地址工具类
 */
public class IpUtil {
    private static final String N255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    private static final Pattern PATTERN = Pattern.compile("^(?:" + N255 + "\\.){3}" + N255 + "$");
    private static final String X_FORWARDED_FOR = "x-forwarded-for";
    /** 多ip分隔符 */
    private static final String IP_SEPARATOR = ",";


    private IpUtil() {

    }

    private static String longToIpV4(long longIp) {
        int octet3 = (int) ((longIp >> 24) % 256);
        int octet2 = (int) ((longIp >> 16) % 256);
        int octet1 = (int) ((longIp >> 8) % 256);
        int octet0 = (int) ((longIp) % 256);
        return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
    }

    private static long ipV4ToLong(String ip) {
        String[] octets = ip.split("\\.");
        return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16)
                + (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
    }

    private static boolean isIPv4Private(String ip) {
        long longIp = ipV4ToLong(ip);
        return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255"))
                || (longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255"))
                || longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255");
    }

    private static boolean isIPv4Valid(String ip) {
        return PATTERN.matcher(ip).matches();
    }

    public static String getIpFromRequest(HttpServletRequest request) {
        String ip;
        boolean found = false;
        if ((ip = request.getHeader(X_FORWARDED_FOR)) != null) {
            StringTokenizer tokenizer = new StringTokenizer(ip, IP_SEPARATOR);
            while (tokenizer.hasNext()) {
                ip = tokenizer.nextToken().trim();
                if (isIPv4Valid(ip) && !isIPv4Private(ip)) {
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

