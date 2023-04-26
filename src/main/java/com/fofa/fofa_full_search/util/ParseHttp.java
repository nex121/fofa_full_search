package com.fofa.fofa_full_search.util;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ParseHttp {

    public static Map<String, Object> parseHttpRequest(String request) {
        // 分解请求行
        String[] tokens = request.split("\\s+");
        String method = tokens[0];     // 请求方式
        String uri = tokens[1];        // URI
        String httpVersion = tokens[2]; // HTTP版本

        String[] lines;
        String body = null;

        String[] reqIndex = request.split("(?<=\\n\\n)");
        if (reqIndex.length > 1) {
            body = String.join("", Arrays.copyOfRange(reqIndex, 1, reqIndex.length));
        }

        lines = reqIndex[0].split("\n");

        HashMap<String, String> headers = new HashMap<>();

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            int separatorIndex = line.indexOf(':');
            if (separatorIndex > 0) {
                String name = line.substring(0, separatorIndex).trim();
                String value = line.substring(separatorIndex + 1).trim();
                headers.put(name, value);
            }
        }

        // 组装结果为Map格式
        Map<String, Object> result = new HashMap<>();
        result.put("Method", method);
        result.put("URI", uri);
        result.put("HTTP Version", httpVersion);
        result.put("Headers", headers);
        result.put("Body", body);

        return result;
    }
}
