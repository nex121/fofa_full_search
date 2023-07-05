package com.fofa.fofa_full_search.service;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class VulConfigService {
    private static final String FILENAME = "vuld.yaml";
    private final Map<String, Object> bindings;
    private final Map<String, Object> features1;
    private final Map<String, Object> secondTextAreaBindings;
    private final Map<String, Object> features2;

    public VulConfigService() {
        this.bindings = new HashMap<>();
        this.features1 = new HashMap<>();
        this.secondTextAreaBindings = new HashMap<>();
        this.features2 = new HashMap<>();
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public Map<String, Object> getFeatures1() {
        return features1;
    }

    public Map<String, Object> getSecondTextAreaBindings() {
        return secondTextAreaBindings;
    }

    public Map<String, Object> getFeatures2() {
        return features2;
    }

    public void save() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(FILENAME);
             OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8)) {
            Yaml yaml = new Yaml();
            Map<String, Object> config = new HashMap<>();

            for (Map.Entry<String, Object> entry : bindings.entrySet()) {
                String node = entry.getKey();
                Object data1 = entry.getValue();
                Object data2 = secondTextAreaBindings.get(node);
                Object feature1 = features1.get(node);

                Map<String, Object> nodeData = new HashMap<>();
                nodeData.put("data1", data1);
                if (data2 != null) {
                    nodeData.put("data2", data2);
                    nodeData.put("features2", features2.get(node));
                }
                if (feature1 != null) {
                    nodeData.put("features1", feature1);
                }
                config.put(node, nodeData);
            }

            yaml.dump(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void removeBinding(String node) {
        bindings.remove(node);
        features1.remove(node);
        secondTextAreaBindings.remove(node);
        features2.remove(node);
        save();
    }

    public void load() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(FILENAME), StandardCharsets.UTF_8))) {
            Yaml yaml = new Yaml();
            Map<String, Object> config = yaml.load(reader);
            if (config != null) {
                for (Map.Entry<String, Object> entry : config.entrySet()) {
                    String node = entry.getKey();
                    Map<String, Object> nodeData = (Map<String, Object>) entry.getValue();
                    bindings.put(node, nodeData.get("data1"));
                    features1.put(node, nodeData.get("features1"));
                    Object data2 = nodeData.get("data2");
                    if (data2 != null) {
                        secondTextAreaBindings.put(node, data2);
                        features2.put(node, nodeData.get("features2"));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
