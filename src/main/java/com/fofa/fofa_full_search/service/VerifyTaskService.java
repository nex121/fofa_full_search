package com.fofa.fofa_full_search.service;

import com.fofa.fofa_full_search.util.ParseHttp;
import com.github.kevinsawicki.http.HttpRequest;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

import java.util.Map;

public class VerifyTaskService extends Task<Void> {
    private final String host;
    private final String feature1;
    private final String feature2;
    private final String sendPack1;
    private final String sendPack2;
    private final TextArea vul_list;

    public VerifyTaskService(String host, String feature1, String feature2, String sendPack1, String sendPack2, TextArea vul_list) {
        this.host = host;
        this.feature1 = feature1;
        this.feature2 = feature2;

        this.sendPack1 = sendPack1;
        this.sendPack2 = sendPack2;
        this.vul_list = vul_list;
    }

    @Override
    public Void call() {
        if (sendPack2.equals("")) {
            Map<String, Object> httpPack = ParseHttp.parseHttpRequest(sendPack1);
            Map<String, String> headers = (Map<String, String>) httpPack.get("Headers");
            String uri = (String) httpPack.get("URI");
            String body = (String) httpPack.get("Body");
            HttpRequest request;
            try {
                if (httpPack.get("Method").equals("POST")) {
                    request = HttpRequest.post(host + uri)
                            .headers(headers)
                            .connectTimeout(10000)
                            .readTimeout(10000)
                            .trustAllHosts().trustAllCerts();
                    if (body != null && !body.equals("")) {
                        request.send(body);
                    }
                } else if (httpPack.get("Method").equals("GET")) {
                    request = HttpRequest.get(host + uri)
                            .headers(headers)
                            .connectTimeout(10000)
                            .readTimeout(10000)
                            .trustAllCerts()
                            .trustAllHosts();
                } else {
                    // 没有该种方法，目前只支持POST与GET
                    Platform.runLater(() -> vul_list.appendText("没有该种方法，目前只支持POST与GET"));
                    return null;
                }
                if (request.body().contains(feature1)) {
                    Platform.runLater(() -> vul_list.appendText(host + "\n"));
                }
            } catch (Exception e) {
            }
        } else {
            Map<String, Object> httpPack1 = ParseHttp.parseHttpRequest(sendPack1);
            Map<String, Object> httpPack2 = ParseHttp.parseHttpRequest(sendPack2);
            Map<String, String> headers1 = (Map<String, String>) httpPack1.get("Headers");
            Map<String, String> headers2 = (Map<String, String>) httpPack2.get("Headers");
            String uri1 = (String) httpPack1.get("URI");
            String uri2 = (String) httpPack2.get("URI");
            String body1 = (String) httpPack1.get("Body");
            String body2 = (String) httpPack2.get("Body");

            HttpRequest request1;
            HttpRequest request2;
            try {
                if (httpPack1.get("Method").equals("POST")) {
                    request1 = HttpRequest.post(host + uri1)
                            .headers(headers1)
                            .connectTimeout(10000)
                            .readTimeout(10000)
                            .trustAllHosts().trustAllCerts();

                    if (body1 != null && !body1.equals("")) {
                        request1.send(body1);
                    }
                } else if (httpPack1.get("Method").equals("GET")) {
                    request1 = HttpRequest.get(host + uri1)
                            .headers(headers1)
                            .connectTimeout(10000)
                            .readTimeout(10000)
                            .trustAllCerts()
                            .trustAllHosts();
                } else {
                    return null;
                }

                if (httpPack2.get("Method").equals("POST")) {
                    request2 = HttpRequest.post(host + uri2)
                            .headers(headers2)
                            .connectTimeout(10000)
                            .readTimeout(10000)
                            .trustAllHosts().trustAllCerts();

                    request2.send(body2);
                } else if (httpPack2.get("Method").equals("GET")) {
                    request2 = HttpRequest.get(host + uri2)
                            .headers(headers2)
                            .connectTimeout(10000)
                            .readTimeout(10000)
                            .trustAllCerts()
                            .trustAllHosts();
                } else {
                    return null;
                }

                if (request1.body().contains(feature1) && request2.body().contains(feature2)) {
                    Platform.runLater(() -> vul_list.appendText(host + "\n"));
                }
            } catch (Exception e) {
            }
        }

        return null;
    }
}

