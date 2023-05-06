package com.fofa.fofa_full_search.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fofa.fofa_full_search.entity.Fofa;
import com.github.kevinsawicki.http.HttpRequest;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class HunterTaskService extends Task<Void> {
    private final String key;
    private final String que;
    private final String search_num;
    private final AtomicInteger id;
    private final TableView<Fofa> fofa_result;
    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public HunterTaskService(String key, String que, String search_num, AtomicInteger id, TableView<Fofa> fofa_result) {
        this.key = key;
        this.que = que;
        this.search_num = search_num;
        this.id = id;
        this.fofa_result = fofa_result;
    }

    @Override
    protected Void call() throws Exception {
        if (search_num.equals("10")) {
            Map<String, Object> hunter_result = hunterSearch(que, "10", "1");
            processResults((JSONArray) hunter_result.get("result_list"));
        } else if (search_num.equals("100")) {
            Map<String, Object> hunter_result = hunterSearch(que, "100", "1");
            System.out.println(hunter_result.get("result_list"));
            processResults((JSONArray) hunter_result.get("result_list"));
        } else if (search_num.equals("1000")) {
            //先看总数
            Map<String, Object> hunter_result = hunterSearch(que, "100", "1");
            int page_num = (int) hunter_result.get("result_size");
            if (page_num == 1) {
                processResults((JSONArray) hunter_result.get("result_list"));
                return null;
            }
            if (page_num > 10) {
                page_num = 10;
            }
            for (int i = 2; i <= page_num; i++) {
                Map<String, Object> hunter_result1 = hunterSearch(que, "100", String.valueOf(i));
                processResults((JSONArray) hunter_result1.get("result_list"));
            }
        } else if (search_num.equals("10000")) {
            Map<String, Object> hunter_result = hunterSearch(que, "100", "1");
            int page_num = (int) hunter_result.get("result_size");
            if (page_num == 1) {
                processResults((JSONArray) hunter_result.get("result_list"));
                return null;
            }
            if (page_num > 100) {
                page_num = 100;
            }
            for (int i = 1; i <= page_num; i++) {
                Map<String, Object> hunter_result1 = hunterSearch(que, "100", String.valueOf(i));
                processResults((JSONArray) hunter_result1.get("result_list"));
            }
        } else if (search_num.equals("all")) {
            Map<String, Object> hunter_result = hunterSearch(que, "100", "1");
            int page_num = (int) hunter_result.get("result_size");
            for (int i = 1; i <= page_num; i++) {
                Map<String, Object> hunter_result1 = hunterSearch(que, "100", String.valueOf(i));
                processResults((JSONArray) hunter_result1.get("result_list"));
            }
        }
        return null;
    }


    private Map<String, Object> hunterSearch(String ques, String page_size, String page_num) throws UnsupportedEncodingException, InterruptedException {
        Map<String, Object> hunter_r = new HashMap<>();
        String que_base64 = Base64.getUrlEncoder().encodeToString(ques.getBytes(StandardCharsets.UTF_8));
        System.out.println(que_base64);
        String url = "https://hunter.qianxin.com/openApi/search?api-key=" + key + "&search=" + que_base64 + "&page_size=" + page_size + "&page=" + page_num;
        System.out.println(url);
        HttpRequest req = HttpRequest.get(url);

        JSONObject res_json0 = JSONObject.parseObject(req.body());
        JSONObject res_json1 = (JSONObject) res_json0.get("data");
        int res_code = (int) res_json0.get("code");

        if (res_code == 400) {
            Platform.runLater(() -> {
                alert.setTitle("提示:");
                alert.setHeaderText("错误提示");
                alert.setContentText("hunter搜索语法有误！");
                alert.showAndWait();
            });
            return null;
        }

        if (res_code == 40204) {
            Platform.runLater(() -> {
                alert.setTitle("提示:");
                alert.setHeaderText("错误提示");
                alert.setContentText("hunter积分用尽！");
                alert.showAndWait();
            });
            return null;
        }

        int size1 = Integer.parseInt(res_json1.getString("total"));

        JSONArray res_json2 = (JSONArray) res_json1.get("arr");

        if (res_json2.size() == 0) {
            Platform.runLater(() -> {
                alert.setTitle("提示:");
                alert.setHeaderText("错误提示");
                alert.setContentText("hunter可能搜索语法有误！因为数据为空");
                alert.showAndWait();
            });
            return null;
        }

        hunter_r.put("result_size", (size1 / 100) + 1);
        hunter_r.put("result_list", res_json2);
        Thread.sleep(1500);

        return hunter_r;
    }

    private void processResults(JSONArray results) {
        for (int i = 0; i < results.size(); i++) {
            JSONObject res_jsons = (JSONObject) results.get(i);
            String ip = res_jsons.getString("ip");
            String port = res_jsons.getString("port");
            String url = res_jsons.getString("url");
            String title = res_jsons.getString("web_title");
            String region = res_jsons.getString("province");
            String city = res_jsons.getString("city");
            JSONArray server1 = (JSONArray) res_jsons.get("component");
            String server="";
            try {
                JSONObject servers = (JSONObject) server1.get(0);
                String name = servers.getString("name");
                String version = servers.getString("version");
                server = name + " " + version;
            } catch (Exception e) {

            }

            String icp = "";
            try {
                icp = res_jsons.getString("number");
            } catch (Exception e) {

            }
            String finalIcp = icp;
            String finalServer = server;
            Platform.runLater(() -> {
                fofa_result.getItems().add(new Fofa(id.getAndIncrement(), ip, port, url, title, region, city, finalServer, finalIcp, "hunter"));
            });
        }
    }

    @Override
    protected void succeeded() {
        // 在任务完成时执行的代码，比如弹窗提示用户任务已经完成。
        alert.setTitle("提示:");
        alert.setHeaderText("任务提示");
        alert.setContentText("Hunter任务执行完成");
        alert.showAndWait();
    }


}
