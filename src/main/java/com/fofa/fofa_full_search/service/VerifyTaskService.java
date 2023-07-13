package com.fofa.fofa_full_search.service;

import com.fofa.fofa_full_search.util.DnslogUtil;
import com.fofa.fofa_full_search.util.ParseHttp;
import com.github.kevinsawicki.http.HttpRequest;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class VerifyTaskService extends Task<Void> {
    private final String host;
    private final String flag1;
    private final String flag2;
    private final String feature1;
    private final String feature2;
    private String sendPack1;
    private String sendPack2;
    private final TextArea vul_list;
    CompletableFuture<Void> completableFuture = new CompletableFuture<>();

    public VerifyTaskService(String host, String flag1, String flag2, String feature1, String feature2, String sendPack1, String sendPack2, TextArea vul_list) {
        this.host = host;
        this.flag1 = flag1;
        this.flag2 = flag2;
        this.feature1 = feature1;
        this.feature2 = feature2;
        this.sendPack1 = sendPack1;
        this.sendPack2 = sendPack2;
        this.vul_list = vul_list;
    }

    @Override
    public Void call() {
        String ranstr1;
        String ranstr2;

        if (sendPack1.contains("{{")) {
            String digAddr = DnslogUtil.extractValue(sendPack1);
            ranstr1 = DnslogUtil.generateRandomString();
            String digNewAddr = ranstr1 + "." + digAddr;
            sendPack1 = sendPack1.replace("{{" + digAddr + "}}", digNewAddr);
        } else {
            ranstr1 = "";
        }

        if (sendPack2.contains("{{")) {
            String digAddr = DnslogUtil.extractValue(sendPack2);
            ranstr2 = DnslogUtil.generateRandomString();
            String digNewAddr = ranstr2 + "." + digAddr;
            sendPack2 = sendPack2.replace("{{" + digAddr + "}}", digNewAddr);
        } else {
            ranstr2 = "";
        }

        try {
            if (sendPack2.equals("")) {
                Map<String, Object> httpPack = ParseHttp.parseHttpRequest(sendPack1);
                Map<String, String> headers = (Map<String, String>) httpPack.get("Headers");
                String uri = (String) httpPack.get("URI");
                String body = (String) httpPack.get("Body");
                HttpRequest request;

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
                    Platform.runLater(() -> vul_list.appendText("没有该种方法，目前只支持POST与GET"));
                    return null;
                }

                System.out.println(ranstr1);
                String result = "";

                try {
                    result = request.body();
                } catch (Exception e) {
                }

                if (flag1.equals("response_body1") && result.contains(feature1)) {
                    System.out.println(host);
                    Platform.runLater(() -> vul_list.appendText(host + "\n"));
                }

                if (flag1.equals("response_header1") && request.headers().toString().contains(feature1)) {
                    Platform.runLater(() -> vul_list.appendText(host + "\n"));
                }

                if (flag1.equals("ceye_token1")) {
                    executeDelayedTask(() -> {
                        String ceye_result = DnslogUtil.Ceye(feature1, ranstr1);

                        if (ceye_result.toLowerCase().contains(ranstr1.toLowerCase())) {
                            vul_list.appendText(host + "\n");
                        }

                        completableFuture.complete(null);
                    });
                }

                if (flag1.equals("dig_token1")) {
                    executeDelayedTask(() -> {
                        String dig_result = DnslogUtil.Dig(feature1);

                        if (dig_result.toLowerCase().contains(ranstr1.toLowerCase())) {
                            vul_list.appendText(host + "\n");
                        }

                        completableFuture.complete(null);
                    });
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

                System.out.println(ranstr1);
                System.out.println(ranstr2);
                String result1 = "";
                String result2 = "";
                try {
                    result1 = request1.body();
                } catch (Exception e) {
                }

                try {
                    result2 = request2.body();
                } catch (Exception e) {
                }

                if (flag1.equals("response_body1") && result1.contains(feature1)) {
                    if (flag2.equals("response_body2") && result2.contains(feature2)) {
                        Platform.runLater(() -> vul_list.appendText(host + "\n"));
                    }
                    if (flag2.equals("response_header2") && request2.headers().toString().contains(feature2)) {
                        Platform.runLater(() -> vul_list.appendText(host + "\n"));
                    }
                    if (flag2.equals("ceye_token2")) {
                        executeDelayedTask(() -> {
                            String ceye_result = DnslogUtil.Ceye(feature2, ranstr2);

                            if (ceye_result.toLowerCase().contains(ranstr2.toLowerCase())) {
                                vul_list.appendText(host + "\n");
                            }

                            completableFuture.complete(null);
                        });
                    }
                    if (flag2.equals("dig_token2")) {
                        executeDelayedTask(() -> {
                            String dig_result = DnslogUtil.Dig(feature2);

                            if (dig_result.toLowerCase().contains(ranstr2.toLowerCase())) {
                                vul_list.appendText(host + "\n");
                            }

                            completableFuture.complete(null);
                        });
                    }
                }

                if (flag1.equals("response_header1") && request1.headers().toString().contains(feature1)) {
                    if (flag2.equals("response_body2") && result2.contains(feature2)) {
                        Platform.runLater(() -> vul_list.appendText(host + "\n"));
                    }
                    if (flag2.equals("response_header2") && request2.headers().toString().contains(feature2)) {
                        Platform.runLater(() -> vul_list.appendText(host + "\n"));
                    }
                    if (flag2.equals("ceye_token2")) {
                        executeDelayedTask(() -> {
                            String ceye_result = DnslogUtil.Ceye(feature2, ranstr2);

                            if (ceye_result.toLowerCase().contains(ranstr2.toLowerCase())) {
                                vul_list.appendText(host + "\n");
                            }

                            completableFuture.complete(null);
                        });
                    }
                    if (flag2.equals("dig_token2")) {
                        executeDelayedTask(() -> {
                            String dig_result = DnslogUtil.Dig(feature2);

                            if (dig_result.toLowerCase().contains(ranstr2.toLowerCase())) {
                                vul_list.appendText(host + "\n");
                            }

                            completableFuture.complete(null);
                        });
                    }
                }

                if (flag1.equals("ceye_token1")) {
                    String finalResult = result2;
                    executeDelayedTask(() -> {
                        String ceye_result = DnslogUtil.Ceye(feature1, ranstr1);

                        if (ceye_result.toLowerCase().contains(ranstr1.toLowerCase()) && flag2.equals("response_body2") && finalResult.contains(feature2)) {
                            vul_list.appendText(host + "\n");
                        }

                        if (ceye_result.toLowerCase().contains(ranstr1.toLowerCase()) && flag2.equals("response_header2") && request2.headers().toString().contains(feature2)) {
                            vul_list.appendText(host + "\n");
                        }

                        if (ceye_result.toLowerCase().contains(ranstr1.toLowerCase()) && flag2.equals("ceye_token2")) {
                            String ceye_result2 = DnslogUtil.Ceye(feature2, ranstr2);
                            if (ceye_result2.toLowerCase().contains(ranstr2.toLowerCase())) {
                                vul_list.appendText(host + "\n");
                            }
                        }

                        if (ceye_result.toLowerCase().contains(ranstr1.toLowerCase()) && flag2.equals("dig_token2")) {
                            String dig_result = DnslogUtil.Dig(feature2);
                            if (dig_result.toLowerCase().contains(ranstr2.toLowerCase())) {
                                vul_list.appendText(host + "\n");
                            }
                        }

                        completableFuture.complete(null);
                    });
                }

                if (flag1.equals("dig_token1")) {
                    String finalResult1 = result2;
                    executeDelayedTask(() -> {
                        String dig_result = DnslogUtil.Dig(feature1);

                        if (dig_result.toLowerCase().contains(ranstr1.toLowerCase()) && flag2.equals("response_body2") && finalResult1.contains(feature2)) {
                            vul_list.appendText(host + "\n");
                        }

                        if (dig_result.toLowerCase().contains(ranstr1.toLowerCase()) && flag2.equals("response_header2") && request2.headers().toString().contains(feature2)) {
                            vul_list.appendText(host + "\n");
                        }

                        if (dig_result.toLowerCase().contains(ranstr1.toLowerCase()) && flag2.equals("ceye_token2")) {
                            String ceye_result = DnslogUtil.Ceye(feature2, ranstr2);
                            if (ceye_result.toLowerCase().contains(ranstr2.toLowerCase())) {
                                vul_list.appendText(host + "\n");
                            }
                        }

                        if (dig_result.toLowerCase().contains(ranstr1.toLowerCase()) && flag2.equals("dig_token2")) {
                            String dig_result2 = DnslogUtil.Dig(feature2);
                            if (dig_result2.toLowerCase().contains(ranstr2.toLowerCase())) {
                                vul_list.appendText(host + "\n");
                            }
                        }

                        completableFuture.complete(null);
                    });
                }
            }
        } catch (Exception e) {
        }

        return null;
    }

    private void executeDelayedTask(Runnable task) {
        PauseTransition delay = new PauseTransition(Duration.seconds(10));
        delay.setOnFinished(event -> {
            task.run();
            completableFuture.complete(null);
        });
        delay.play();
        completableFuture.join();
    }
}