package com.fofa.fofa_full_search.util;

import com.github.kevinsawicki.http.HttpRequest;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class DnslogUtil {
    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int LENGTH = 6;

    public static String generateRandomString() {

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(LENGTH);

        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static String extractValue(String input) {
        int startIndex = input.indexOf("{{");
        int endIndex = input.indexOf("}}");

        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return input.substring(startIndex + 2, endIndex);
        }

        return null;
    }

    public static String Ceye(String ceyeToken, String randomString) {
        String ceye_query = "http://api.ceye.io/v1/records?token=" + ceyeToken + "&type=dns&filter=" + randomString;
        HttpRequest request = HttpRequest.get(ceye_query)
                .connectTimeout(10000)
                .readTimeout(10000)
                .trustAllHosts().trustAllCerts();

        return request.body();
    }

    public static String Dig(String digToken) {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 Edg/114.0.1823.79");
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        HttpRequest request = HttpRequest.get("https://dig.pm/get_results?domain=ipv6.bypass.eu.org.&token=" + digToken.trim())
                .headers(headers)
                .connectTimeout(10000)
                .readTimeout(10000)
                .trustAllHosts().trustAllCerts();

        return request.body();
    }

//    public static void main(String[] args) {
////        System.out.println(Ceye("66eae08b193149e87210fd5124a37cf0", "tdRB2i"));
//        System.out.println(Dig("fljpqqrpnzzc"));
//    }
}

