package com.fofa.fofa_full_search.service;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigManagerService {
    private String fofa_email;
    private String fofa_key;
    private String hunter_key;
    private String search_num;
    private String search_tag;
    private List<String> history;

    public ConfigManagerService() throws IOException {
        // 读取配置文件
        File configFile = new File("config.yaml");
        FileInputStream inputStream = new FileInputStream(configFile);
        Yaml yaml = new Yaml();
        Map<String, Object> configMap = yaml.load(inputStream);

        // 解析配置信息
        this.fofa_email = (String) configMap.get("fofa_email");
        this.fofa_key = (String) configMap.get("fofa_key");

        this.hunter_key = (String) configMap.get("hunter_key");
        this.search_num = (String) configMap.get("search_num");
        this.search_tag = (String) configMap.get("search_tag");

        this.history = (List<String>) configMap.get("history");
        if (this.history == null) {
            this.history = new ArrayList<>();
        }
    }

    public void saveConfig() throws IOException {
        // 将配置信息写入配置文件
        File configFile = new File("config.yaml");
        FileWriter writer = new FileWriter(configFile);
        Yaml yaml = new Yaml();
        yaml.dump(getConfigMap(), writer);
        writer.close();
    }

    public void addToHistory(String query) throws IOException {
        if (!history.contains(query)) { // 如果历史记录中不包含该记录，则进行添加
            history.add(query);
            saveConfig();
        }
    }

    public List<String> getHistory() {
        return history;
    }

    public String getFofa_email() {
        return fofa_email;
    }

    public void setFofa_email(String fofa_email) {
        this.fofa_email = fofa_email;
    }

    public String getFofa_key() {
        return fofa_key;
    }

    public void setFofa_key(String fofa_key) {
        this.fofa_key = fofa_key;
    }

    public String getHunter_key() {
        return hunter_key;
    }

    public void setHunter_key(String hunter_key) {
        this.hunter_key = hunter_key;
    }

    public String getSearch_num() {
        return search_num;
    }

    public void setSearch_num(String search_num) {
        this.search_num = search_num;
    }

    public String getSearch_tag() {
        return search_tag;
    }

    public void setSearch_tag(String search_tag) {
        this.search_tag = search_tag;
    }


    private Map<String, Object> getConfigMap() {
        // 将配置信息转换为 Map 对象
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("fofa_email", fofa_email);
        map.put("fofa_key", fofa_key);
        map.put("hunter_key", hunter_key);
        map.put("search_num", search_num);
        map.put("search_tag", search_tag);
        map.put("history", history);
        return map;
    }
}
