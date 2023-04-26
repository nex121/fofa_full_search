package com.fofa.fofa_full_search.util;

import com.google.common.net.InternetDomainName;
import java.net.UnknownHostException;
import java.util.regex.Pattern;


public class DomainDealUtil {

    public static Boolean isIP(String url) {
        Pattern ipPattern = Pattern.compile("(http|https)://((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");
        // 匹配 IP 地址的正则表达式
        try {
            if (ipPattern.matcher(url).find()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 如http://114.114.114.114:8080 工具类处理后得到114.114.114.114
     */
    public static String get_http_root_ip(String ip) {
        String[] ips = ip.split("://");
        String ips0 = ips[1];
        if (ips0.contains(":")) {
            String[] ips1 = ips0.split(":");
            return ips1[0];
        } else {
            return ips0;
        }
    }

    public static String get_root_domain_apache(String domainName) throws UnknownHostException {
        if (domainName.split(":").length > 3) {
            return null;
        }
        if (domainName.contains("://")) {
            domainName = domainName.split("://")[1];
        }
        if (domainName.contains(":")) {
            domainName = domainName.split(":")[0];
        }
        if (!domainName.matches("^([a-z0-9]([a-z0-9-]{0,61}[a-z0-9])?.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]$")) {
            return null;   // 不标准域名,返回null
        }
        InternetDomainName host = InternetDomainName.from(domainName);
        String top = host.topPrivateDomain().toString();
        return top;
    }

}
