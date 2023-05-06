package com.fofa.fofa_full_search.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fofa.fofa_full_search.entity.Fofa;
import com.fofa.fofa_full_search.util.DealCity;
import com.github.kevinsawicki.http.HttpRequest;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FofaTaskService extends Task<Void> {
    private final String email;
    private final String key;
    private final String que;
    private final AtomicInteger id;
    private final TableView<Fofa> fofa_result;
    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public FofaTaskService(String email, String key, String que, AtomicInteger id, TableView<Fofa> fofa_result) {
        this.email = email;
        this.key = key;
        this.que = que;
        this.id = id;
        this.fofa_result = fofa_result;
    }

    @Override
    protected Void call() throws Exception {
        Map<String, Object> fofa_r1 = fofaSearch(que);
        int page_num = Integer.parseInt(fofa_r1.get("result_size").toString());
        if (page_num == 1) {
            processResults((JSONArray) fofa_r1.get("result_list"));
            return null;
        }
        String searchText = que;
        if (searchText.contains("city")) {
            searchText += getTimeFilter();
            processResults((JSONArray) fofaSearch(searchText).get("result_list"));
            return null;
        }
        if (searchText.contains("region")) {
            String region = getRegionFromQuery(searchText);
            List<String> cities = DealCity.DealCities(region);
            searchAndProcess(searchText, cities);
            return null;
        }
        List<String> regions = DealCity.DealProvince(que);
        for (String reg : regions) {
            searchText = que + "&& region=\"" + reg + "\"";
            Map<String, Object> regResult = fofaSearch(searchText);
            if ((int) regResult.get("result_size") < 2) {
                processResults((JSONArray) regResult.get("result_list"));
            } else {
                List<String> cities = DealCity.DealCities(reg);
                searchAndProcess(searchText, cities);
            }
        }
        return null;
    }

    private void searchAndProcess(String searchText, List<String> cities) throws UnsupportedEncodingException, InterruptedException {
        for (String city : cities) {
            String text = searchText + "&& city=\"" + city + "\"";
            Map<String, Object> result = fofaSearch(text);
            if ((int) result.get("result_size") < 2) {
                processResults((JSONArray) result.get("result_list"));
            } else {
                String t = text + getTimeFilter();
                processResults((JSONArray) fofaSearch(t).get("result_list"));
            }
        }
    }

    private String getTimeFilter() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = date.minusYears(1);
        return "&& after=" + date1.format(formatter);
    }

    private String getRegionFromQuery(String que) {
        String pattern = "region=\"([^\"]+)\"";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(que);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    private Map<String, Object> fofaSearch(String ques) throws InterruptedException {
        Map<String, Object> fofa_r = new HashMap<>();
        String que_base64 = Base64.getUrlEncoder().encodeToString(ques.getBytes(StandardCharsets.UTF_8));
        String url = "https://fofa.info/api/v1/search/all?email=" + email + "&key=" + key + "&qbase64=" + que_base64 + "&size=10000&page=1&fields=ip,port,host,title,region,city,server,icp";
        System.out.println(url);
        HttpRequest req = HttpRequest.get(url);
        JSONObject res_json0 = JSONObject.parseObject(req.body());
        String search_result = res_json0.get("error").toString();
        if (search_result.equals("true")) {
            Platform.runLater(() -> {
                alert.setTitle("提示:");
                alert.setHeaderText("错误提示");
                alert.setContentText("fofa搜索语法有误！");
                alert.showAndWait();
            });
            return null;
        }

        int size1 = Integer.parseInt(res_json0.get("size").toString());
        JSONArray target_lists = res_json0.getJSONArray("results");

        fofa_r.put("result_size", (size1 / 10000) + 1);
        fofa_r.put("result_list", target_lists);
        Thread.sleep(1500);
        return fofa_r;
    }

    private void processResults(JSONArray results) {

        for (int i = 0; i < results.size(); i++) {
            Object element = results.get(i);
            if (element instanceof JSONArray) {
                JSONArray jsonArray = results.getJSONArray(i);
                String ip = jsonArray.getString(0);
                String port = jsonArray.getString(1);
                String url = jsonArray.getString(2);
                String title = jsonArray.getString(3);
                String region = jsonArray.getString(4);
                String city = jsonArray.getString(5);
                String server = jsonArray.getString(6);
                String icp = jsonArray.getString(7);

                if (url.contains("https://")) {
                    Platform.runLater(() -> {
                        fofa_result.getItems().add(new Fofa(id.getAndIncrement(), ip, port, url, title, region, city, server, icp, "fofa"));

                    });
                } else {
                    Platform.runLater(() -> {
                        fofa_result.getItems().add(new Fofa(id.getAndIncrement(), ip, port, "http://" + url, title, region, city, server, icp, "fofa"));
                    });
                }
            }
        }
    }

    @Override
    protected void succeeded() {
        // 在任务完成时执行的代码，比如弹窗提示用户任务已经完成。
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示:");
        alert.setHeaderText("任务提示");
        alert.setContentText("Fofa任务执行完成");
        alert.showAndWait();
    }
}
