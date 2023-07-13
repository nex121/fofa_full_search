package com.fofa.fofa_full_search.service;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class VulConfigService {
    private static final String FILENAME = "vuld.yaml";
    private final Map<String, Object> bindings;
    private final Map<String, Object> secondTextAreaBindings;
    private final Map<String, Object> responseHeader1;
    private final Map<String, Object> responseBody1;
    private final Map<String, Object> ceyeToken1;
    private final Map<String, Object> digToken1;
    private final Map<String, Object> responseHeader2;
    private final Map<String, Object> responseBody2;
    private final Map<String, Object> ceyeToken2;
    private final Map<String, Object> digToken2;


    public VulConfigService() {
        this.bindings = new HashMap<>();
        this.secondTextAreaBindings = new HashMap<>();
        this.responseHeader1 = new HashMap<>();
        this.responseBody1 = new HashMap<>();
        this.ceyeToken1 = new HashMap<>();
        this.digToken1 = new HashMap<>();

        this.responseHeader2 = new HashMap<>();
        this.responseBody2 = new HashMap<>();
        this.ceyeToken2 = new HashMap<>();
        this.digToken2 = new HashMap<>();
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public Map<String, Object> getSecondTextAreaBindings() {
        return secondTextAreaBindings;
    }

    public Map<String, Object> getResponseHeader1() {
        return responseHeader1;
    }

    public Map<String, Object> getResponseBody1() {
        return responseBody1;
    }

    public Map<String, Object> getCeyeToken1() {
        return ceyeToken1;
    }

    public Map<String, Object> getdigToken1() {
        return digToken1;
    }

    public Map<String, Object> getResponseHeader2() {
        return responseHeader2;
    }

    public Map<String, Object> getResponseBody2() {
        return responseBody2;
    }

    public Map<String, Object> getCeyeToken2() {
        return ceyeToken2;
    }

    public Map<String, Object> getdigToken2() {
        return digToken2;
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

                //特征一
                Object responseH1 = responseHeader1.get(node);
                Object responseB1 = responseBody1.get(node);
                Object ceye1 = ceyeToken1.get(node);
                Object dig1 = digToken1.get(node);

                //特征二
                Object responseH2 = responseHeader2.get(node);
                Object responseB2 = responseBody2.get(node);
                Object ceye2 = ceyeToken2.get(node);
                Object dig2 = digToken2.get(node);

                Map<String, Object> nodeData = new HashMap<>();

                if (data1 != null) {
                    nodeData.put("data1", data1);
                    nodeData.put("response_header1", responseH1);
                    nodeData.put("response_body1", responseB1);
                    nodeData.put("ceye_token1", ceye1);
                    nodeData.put("dig_token1", dig1);
                }

                if (data2 != null) {
                    nodeData.put("data2", data2);
                    nodeData.put("response_header2", responseH2);
                    nodeData.put("response_body2", responseB2);
                    nodeData.put("ceye_token2", ceye2);
                    nodeData.put("dig_token2", dig2);
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
        secondTextAreaBindings.remove(node);

        responseHeader1.remove(node);
        responseBody1.remove(node);
        ceyeToken1.remove(node);
        digToken1.remove(node);

        responseHeader2.remove(node);
        responseBody2.remove(node);
        ceyeToken2.remove(node);
        digToken2.remove(node);
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
//                    features1.put(node, nodeData.get("features1"));
                    responseHeader1.put(node, nodeData.get("response_header1"));
                    responseBody1.put(node, nodeData.get("response_body1"));
                    ceyeToken1.put(node, nodeData.get("ceye_token1"));
                    digToken1.put(node, nodeData.get("dig_token1"));

                    Object data2 = nodeData.get("data2");
                    if (data2 != null) {
                        secondTextAreaBindings.put(node, data2);
                        responseHeader2.put(node, nodeData.get("response_header2"));
                        responseBody2.put(node, nodeData.get("response_body2"));
                        ceyeToken2.put(node, nodeData.get("ceye_token2"));
                        digToken2.put(node, nodeData.get("dig_token2"));
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getNodeValue(String node, String valueName) {
        if (valueName.equals("data1")) {
            return bindings.get(node);
        } else if (valueName.equals("data2")) {
            return secondTextAreaBindings.get(node);
        } else if (valueName.equals("response_header1")) {
            return responseHeader1.get(node);
        } else if (valueName.equals("response_body1")) {
            return responseBody1.get(node);
        } else if (valueName.equals("ceye_token1")) {
            return ceyeToken1.get(node);
        } else if (valueName.equals("dig_token1")) {
            return digToken1.get(node);
        } else if (valueName.equals("response_header2")) {
            return responseHeader2.get(node);
        } else if (valueName.equals("response_body2")) {
            return responseBody2.get(node);
        } else if (valueName.equals("ceye_token2")) {
            return ceyeToken2.get(node);
        } else if (valueName.equals("dig_token2")) {
            return digToken2.get(node);
        }
        return null;
    }
}
