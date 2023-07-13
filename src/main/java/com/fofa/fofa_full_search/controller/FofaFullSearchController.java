package com.fofa.fofa_full_search.controller;

import com.fofa.fofa_full_search.FofaFullSearchApplication;
import com.fofa.fofa_full_search.FofaSetApplication;
import com.fofa.fofa_full_search.entity.Fofa;
import com.fofa.fofa_full_search.entity.FofaUsed;
import com.fofa.fofa_full_search.entity.HunterUsed;
import com.fofa.fofa_full_search.service.*;
import com.fofa.fofa_full_search.util.DomainDealUtil;
import com.fofa.fofa_full_search.util.FileUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FofaFullSearchController {
    @FXML
    private TextField fofa_field, thread_num;
    @FXML
    private TextArea vul_data_area1, vul_data_area2, vul_list, target_list, features1, features2;
    @FXML
    private Button export_vul_list, fofa_search, updateButton;
    @FXML
    private ComboBox<String> features1com, features2com;
    @FXML
    protected TabPane tabPane;
    @FXML
    private TableView<FofaUsed> fofa_used;
    @FXML
    private TableView<HunterUsed> hunter_used;
    @FXML
    private TableColumn<FofaUsed, String> example_col;
    @FXML
    private TableColumn<FofaUsed, String> used_col;
    @FXML
    private TableColumn<FofaUsed, String> anno_col;
    @FXML
    private TableColumn<HunterUsed, String> syntax_col;
    @FXML
    private TableColumn<HunterUsed, String> search_syn_new_col;
    @FXML
    public TreeView<String> treeView;
    private static final String FILENAME = "vul.yaml";

    public void initialize() throws IOException {
        //初始化特征下拉框

        features1com.setValue("response_body1");
        features1com.getItems().addAll("response_header1", "response_body1", "ceye_token1", "dig_token1");

        features2com.setValue("response_body2");
        features2com.getItems().addAll("response_header2", "response_body2", "ceye_token2", "dig_token2");

        FofasUsed();
        HunterUsed();
        //如果配置文件为空，生成配置文件
        FileUtil.generateFile();
        FileUtil.generateFile1();
        //历史查询
        Popup popup = new Popup();
        popup.setAutoHide(true);

        ListView<String> historyList = new ListView<>();
        historyList.setPrefWidth(300);
        historyList.setPrefHeight(200);

        historyList.setPrefWidth(fofa_field.getMaxWidth());
        ObservableList<String> historyData = FXCollections.observableArrayList();
        historyList.setItems(historyData);

        popup.getContent().add(historyList);

        fofa_field.setOnMousePressed(e -> {
            ConfigManagerService cms = null;
            try {
                cms = new ConfigManagerService();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            historyData.clear();
            historyData.addAll(cms.getHistory());

            if (!popup.isShowing()) {
                double x = fofa_field.localToScreen(0, 0).getX();
                double y = fofa_field.localToScreen(0, 0).getY() + fofa_field.getHeight();
                popup.show(fofa_field.getScene().getWindow(), x, y);
            }
            historyList.setVisible(true);
        });

        historyList.setOnMouseClicked(event -> {
            String selectedItem = historyList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                fofa_field.setText(selectedItem);
                popup.hide();
            }
        });

        fofa_field.textProperty().addListener((observable, oldValue, newValue) -> {
            historyList.setVisible(false);
        });

        //初始加载
        VulConfigService vcs = new VulConfigService();
        vcs.load();

        //特征下拉框切换不同的值的实现
        features1com.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            vcs.load();
            TreeItem<String> node = treeView.getSelectionModel().getSelectedItem();
            // 根据选择的项更新TextArea的内容
            if (newValue != null && newValue.equals("response_header1")) {
                try {
                    features1.setText(vcs.getNodeValue(node.getValue(), newValue).toString());
                } catch (Exception e) {
                    features1.setText("");
                }
            }
            if (newValue != null && newValue.equals("response_body1")) {
                try {
                    features1.setText(vcs.getNodeValue(node.getValue(), newValue).toString());
                } catch (Exception e) {
                    features1.setText("");
                }
            }
            if (newValue != null && newValue.equals("ceye_token1")) {
                try {
                    features1.setText(vcs.getNodeValue(node.getValue(), newValue).toString());
                } catch (Exception e) {
                    features1.setText("");
                }
            }
            if (newValue != null && newValue.equals("dig_token1")) {
                try {
                    features1.setText(vcs.getNodeValue(node.getValue(), newValue).toString());
                } catch (Exception e) {
                    features1.setText("");
                }
            }
        });

        features2com.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            vcs.load();
            TreeItem<String> node = treeView.getSelectionModel().getSelectedItem();
            // 根据选择的项更新TextArea的内容
            if (newValue != null && newValue.equals("response_header2")) {
                try {
                    features2.setText(vcs.getNodeValue(node.getValue(), newValue).toString());
                } catch (Exception e) {
                    features2.setText("");
                }
            }
            if (newValue != null && newValue.equals("response_body2")) {
                try {
                    features2.setText(vcs.getNodeValue(node.getValue(), newValue).toString());
                } catch (Exception e) {
                    features2.setText("");
                }
            }
            if (newValue != null && newValue.equals("ceye_token2")) {
                try {
                    features2.setText(vcs.getNodeValue(node.getValue(), newValue).toString());
                } catch (Exception e) {
                    features2.setText("");
                }
            }
            if (newValue != null && newValue.equals("dig_token2")) {
                try {
                    features2.setText(vcs.getNodeValue(node.getValue(), newValue).toString());
                } catch (Exception e) {
                    features2.setText("");
                }
            }
        });


        //设置升级按钮
        updateButton.setOnAction(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                String text = vul_data_area1.getText();
                String features11 = features1.getText();
                //数据包2
                String text2 = vul_data_area2.getText();
                String features22 = features2.getText();

                // 更新绑定关系到配置文件
                vcs.getBindings().put(selectedItem.getValue(), text);
                if (features1com.getSelectionModel().getSelectedItem().equals("response_header1")) {
                    vcs.getResponseHeader1().put(selectedItem.getValue(), features11);
                }
                if (features1com.getSelectionModel().getSelectedItem().equals("response_body1")) {
                    vcs.getResponseBody1().put(selectedItem.getValue(), features11);
                }
                if (features1com.getSelectionModel().getSelectedItem().equals("ceye_token1")) {
                    vcs.getCeyeToken1().put(selectedItem.getValue(), features11);
                }
                if (features1com.getSelectionModel().getSelectedItem().equals("dig_token1")) {
                    vcs.getdigToken1().put(selectedItem.getValue(), features11);
                }
                vcs.getSecondTextAreaBindings().put(selectedItem.getValue(), text2);
                if (features2com.getSelectionModel().getSelectedItem().equals("response_header2")) {
                    vcs.getResponseHeader2().put(selectedItem.getValue(), features22);
                }
                if (features2com.getSelectionModel().getSelectedItem().equals("response_body2")) {
                    vcs.getResponseBody2().put(selectedItem.getValue(), features22);
                }
                if (features2com.getSelectionModel().getSelectedItem().equals("ceye_token2")) {
                    vcs.getCeyeToken2().put(selectedItem.getValue(), features22);
                }
                if (features2com.getSelectionModel().getSelectedItem().equals("dig_token2")) {
                    vcs.getdigToken2().put(selectedItem.getValue(), features22);
                }

                vcs.save();
            }
        });

        //设置treeview
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String feas1 = features1com.getSelectionModel().getSelectedItem();
                String feas2 = features2com.getSelectionModel().getSelectedItem();
                String selectedNodeValue = newValue.getValue();

                try {
                    features1.setText(vcs.getNodeValue(selectedNodeValue, feas1).toString());
                } catch (Exception e) {
                    features1.setText("");
                }

                try {
                    features2.setText(vcs.getNodeValue(selectedNodeValue, feas2).toString());
                } catch (Exception e) {
                    features2.setText("");
                }
                String boundText1 = (String) vcs.getBindings().get(selectedNodeValue);

                String boundText2 = (String) vcs.getSecondTextAreaBindings().get(selectedNodeValue);

                vul_data_area1.setText(boundText1 != null ? boundText1 : "");
                vul_data_area2.setText(boundText2 != null ? boundText2 : "");
            } else {
                vul_data_area1.setText("");
                features1.setText("");
                vul_data_area2.setText("");
                features2.setText("");
            }
        });


        //设置漏洞细节菜单
        loadTreeStructureFromFile(treeView);
        treeView.setEditable(true);

        //设置漏洞细节菜单右键
        treeView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    createContextMenu(selectedItem, treeView, vcs).show(treeView, event.getScreenX(), event.getScreenY());
                }
            }
        });

    }

    private ContextMenu createContextMenu(TreeItem<String> selectedItem, TreeView<String> treeView, VulConfigService vcs) {
        ContextMenu contextMenu = new ContextMenu();

        // Add node menu item
        MenuItem addMenuItem = new MenuItem("Add");
        addMenuItem.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Node");
            dialog.setHeaderText("请输入节点值:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                TreeItem<String> newItem = new TreeItem<>(value);
                selectedItem.getChildren().add(newItem);
                selectedItem.setExpanded(true);
                treeView.edit(newItem);
                saveTreeStructureToFile(treeView);
            });
        });

        // Delete node menu item
        MenuItem deleteMenuItem = new MenuItem("Delete");

        deleteMenuItem.setOnAction(event -> {
            TreeItem<String> parent = selectedItem.getParent();
            if (parent != null) {

                parent.getChildren().remove(selectedItem);

                vcs.removeBinding(selectedItem.getValue());
            }
            saveTreeStructureToFile(treeView);
        });

        contextMenu.getItems().addAll(addMenuItem, deleteMenuItem);
        return contextMenu;
    }

    private void saveTreeStructureToFile(TreeView<String> treeView) {
        TreeItem<String> root = treeView.getRoot();
        Map<String, Object> data = buildMap(root);

        try {
            Representer representer = new Representer();
            representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

            Yaml yaml = new Yaml(representer);
            FileOutputStream fileOutputStream = new FileOutputStream(FILENAME);
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            yaml.dump(data, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadTreeStructureFromFile(TreeView<String> treeView) {
        Map<String, Object> data = loadMapFromYaml(FILENAME);
        if (data != null) {
            TreeItem<String> rootItem = buildTreeView(data);
            treeView.setRoot(rootItem);
        }
    }

    private Map<String, Object> loadMapFromYaml(String filename) {
        try {
            Yaml yaml = new Yaml(new Constructor());
            FileInputStream fileInputStream = new FileInputStream(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            return yaml.load(inputStreamReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Map<String, Object> buildMap(TreeItem<String> treeItem) {
        Map<String, Object> map = new LinkedHashMap<>();
        List<Map<String, Object>> children = new ArrayList<>();
        map.put("name", treeItem.getValue());
        map.put("children", children);

        for (TreeItem<String> child : treeItem.getChildren()) {
            children.add(buildMap(child));
        }
        return map;
    }

    private TreeItem<String> buildTreeView(Map<String, Object> data) {
        String name = (String) data.get("name");
        List<Map<String, Object>> children = (List<Map<String, Object>>) data.get("children");

        TreeItem<String> treeItem = new TreeItem<>(name);
        for (Map<String, Object> childData : children) {
            TreeItem<String> childItem = buildTreeView(childData);
            treeItem.getChildren().add(childItem);
        }
        return treeItem;
    }


    private void FofasUsed() {
        example_col.setCellValueFactory(cellData -> cellData.getValue().exampleProperty());
        used_col.setCellValueFactory(cellData -> cellData.getValue().usedProperty());
        anno_col.setCellValueFactory(cellData -> cellData.getValue().annoProperty());

        ContextMenu contextMenu = new ContextMenu();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        MenuItem menuItem1 = new MenuItem("复制例句");
        contextMenu.getItems().addAll(menuItem1);
        fofa_used.setContextMenu(contextMenu);
        ObservableList<FofaUsed> data = FXCollections.observableArrayList(
                new FofaUsed("title=\"beijing\"", "从标题中搜索“北京”", "-"),
                new FofaUsed("header=\"elastic\"", "header=\"elastic\"", "-"),
                new FofaUsed("body=\"网络空间测绘\"", "从html正文中搜索“网络空间测绘”", "-"),
                new FofaUsed("fid=\"sSXXGNUO2FefBTcCLIT/2Q==\"", "查找相同的网站指纹", "搜索网站类型资产"),
                new FofaUsed("domain=\"qq.com\"", "搜索根域名带有qq.com的网站", "-"),
                new FofaUsed("icp=\"京ICP证030173号\"", "查找备案号为“京ICP证030173号”的网站", "搜索网站类型资产"),
                new FofaUsed("js_name=\"js/jquery.js\"", "查找网站正文中包含js/jquery.js的资产", "搜索网站类型资产"),
                new FofaUsed("js_md5=\"82ac3f14327a8b7ba49baa208d4eaa15\"", "查找js源码与之匹配的资产", "-"),
                new FofaUsed("cname=\"ap21.inst.siteforce.com\"", "查找cname为\"ap21.inst.siteforce.com\"的网站", "-"),
                new FofaUsed("cname_domain=\"siteforce.com\"", "查找cname包含“siteforce.com”的网站", "-"),
                new FofaUsed("cloud_name=\"Aliyundun\"\n", "通过云服务名称搜索资产", "-"),
                new FofaUsed("product=\"NGINX\"", "搜索此产品的资产", "个人版及以上可用"),
                new FofaUsed("category=\"服务\"\n", "搜索此产品分类的资产", "个人版及以上可用"),
                new FofaUsed("sdk_hash==\"Mkb4Ms4R96glv/T6TRzwPWh3UDatBqeF\"", "搜索使用此sdk的资产", "商业版及以上可用"),
                new FofaUsed("icon_hash=\"-247388890\"", "搜索使用此 icon 的资产", "个人版及以上可用"),
                new FofaUsed("host=\".gov.cn\"", "从url中搜索”.gov.cn”", "搜索要用host作为名称"),
                new FofaUsed("port=\"6379\"", "查找对应“6379”端口的资产", "-"),
                new FofaUsed("ip=\"1.1.1.1\"", "从ip中搜索包含“1.1.1.1”的网站", "搜索要用ip作为名称"),
                new FofaUsed("ip=\"220.181.111.1/24\"", "查询IP为“220.181.111.1”的C网段资产", "-"),
                new FofaUsed("status_code=\"402\"", "查询服务器状态为“402”的资产", "查询网站类型数据"),
                new FofaUsed("protocol=\"quic\"", "查询quic协议资产", "搜索指定协议类型(在开启端口扫描的情况下有效)"),
                new FofaUsed("country=\"CN\"", "搜索指定国家(编码)的资产", "-"),
                new FofaUsed("region=\"Xinjiang Uyghur Autonomous Region\"", "搜索指定行政区的资产", "-"),
                new FofaUsed("city=\"Ürümqi\"", "搜索指定城市的资产", "-"),
                new FofaUsed("cert=\"baidu\"", "搜索证书(https或者imaps等)中带有baidu的资产", "-"),
                new FofaUsed("cert.subject=\"Oracle Corporation\"", "搜索证书持有者是Oracle Corporation的资产", "-"),
                new FofaUsed("cert.issuer=\"DigiCert\"", "搜索证书颁发者为DigiCert Inc的资产", "-"),
                new FofaUsed("cert.is_valid=true", "验证证书是否有效，true有效，false无效", "个人版及以上可用"),
                new FofaUsed("cert.is_match=true\n", "证书和域名是否匹配；true匹配、false不匹配", "个人版及以上可用"),
                new FofaUsed("cert.is_expired=false\n", "证书是否过期；true过期、false未过期", "个人版及以上可用"),
                new FofaUsed("jarm=\"2ad...83e81\"", "搜索JARM指纹", "-"),
                new FofaUsed("banner=\"users\" && protocol=\"ftp\"", "搜索FTP协议中带有users文本的资产", "-"),
                new FofaUsed("type=\"service\"", "搜索所有协议资产，支持subdomain和service两种", "-"),
                new FofaUsed("os=\"centos\"", "搜索CentOS资产", "-"),
                new FofaUsed("server==\"Microsoft-IIS/10\"", "搜索IIS 10服务器", "-"),
                new FofaUsed("app=\"Microsoft-Exchange\"", "搜索Microsoft-Exchange设备", "-"),
                new FofaUsed("after=\"2017\" && before=\"2017-10-01\"", "时间范围段搜索", "-"),
                new FofaUsed("asn=\"19551\"", "搜索指定asn的资产", "-"),
                new FofaUsed("org=\"LLC Baxet\"", "搜索指定org(组织)的资产", "-"),
                new FofaUsed("base_protocol=\"udp\"", "搜索指定udp协议的资产", "-"),
                new FofaUsed("is_fraud=false", "排除仿冒/欺诈数据", "专业版及以上可用"),
                new FofaUsed("is_honeypot=false", "排除蜜罐数据", "专业版及以上可用"),
                new FofaUsed("is_ipv6=true", "搜索ipv6的资产", "搜索ipv6的资产,只接受true和false"),
                new FofaUsed("is_domain=true", "搜索域名的资产", "搜索域名的资产,只接受true和false"),
                new FofaUsed("is_cloud=true", "筛选使用了云服务的资产", "-"),
                new FofaUsed("port_size=\"6\"", "查询开放端口数量等于\"6\"的资产", "个人版及以上可用"),
                new FofaUsed("port_size_gt=\"6\"", "查询开放端口数量大于\"6\"的资产", "个人版及以上可用"),
                new FofaUsed("port_size_lt=\"12\"", "查询开放端口数量小于\"12\"的资产", "个人版及以上可用"),
                new FofaUsed("ip_ports=\"80,161\"", "搜索同时开放80和161端口的ip", "搜索同时开放80和161端口的ip资产(以ip为单位的资产数据)"),
                new FofaUsed("ip_country=\"CN\"", "搜索中国的ip资产(以ip为单位的资产数据)", "搜索中国的ip资产"),
                new FofaUsed("ip_region=\"Zhejiang\"", "搜索指定行政区的ip资产(以ip为单位的资产数据)", "搜索指定行政区的资产"),
                new FofaUsed("ip_city=\"Hangzhou\"", "搜索指定城市的ip资产(以ip为单位的资产数据)", "搜索指定城市的资产"),
                new FofaUsed("ip_after=\"2021-03-18\"", "搜索2021-03-18以后的ip资产(以ip为单位的资产数据)", "搜索2021-03-18以后的ip资产"),
                new FofaUsed("ip_before=\"2019-09-09\"", "搜索2019-09-09以前的ip资产(以ip为单位的资产数据)", "搜索2019-09-09以前的ip资产")
        );

        menuItem1.setOnAction(e -> {
            FofaUsed selectedItem = fofa_used.getSelectionModel().getSelectedItem();
            String value = selectedItem.getExample();
            content.putString(value);
            clipboard.setContent(content);
        });
        fofa_used.setItems(data);

    }

    private void HunterUsed() {
        syntax_col.setCellValueFactory(cellData -> cellData.getValue().syntaxProperty());
        search_syn_new_col.setCellValueFactory(cellData -> cellData.getValue().search_syn_newProperty());

        ContextMenu contextMenu = new ContextMenu();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        MenuItem menuItem1 = new MenuItem("复制检索语法");
        contextMenu.getItems().addAll(menuItem1);
        hunter_used.setContextMenu(contextMenu);
        ObservableList<HunterUsed> data = FXCollections.observableArrayList(
                new HunterUsed("查询包含IP标签\"CDN\"的资产", "ip.tag=\"CDN\""),
                new HunterUsed("搜索web资产", "is_web=true"),
                new HunterUsed("查询包含资产标签\"登录页面\"的资产", "web.tag=\"登录页面\""),
                new HunterUsed("查询存在历史漏洞的资产", "web.is_vul=true"),
                new HunterUsed("搜索ICP备案行业为“软件和信息技术服务业”的资产 (查看枚举值)", "icp.industry=\"软件和信息技术服务业\""),
                new HunterUsed("搜索IP为 ”1.1.1.1”的资产", "ip=\"1.1.1.1\""),
                new HunterUsed("ip=\"220.181.111.1/24\"", "查询IP为“220.181.111.1”的C网段资产"),
                new HunterUsed("搜索起始IP为”220.181.111.1“的C段资产", "ip=\"220.181.111.1/24\""),
                new HunterUsed("搜索开放端口为”80“的资产", "ip.port=\"80\""),
                new HunterUsed("搜索IP对应主机所在国为”中国“的资产", "ip.country=\"CN\""),
                new HunterUsed("搜索IP对应主机在江苏省的资产", "ip.province=\"江苏\""),
                new HunterUsed("搜索IP对应主机所在城市为”北京“市的资产", "ip.city=\"北京\""),
                new HunterUsed("搜索运营商为”中国电信”的资产", "ip.isp=\"电信\""),
                new HunterUsed("搜索操作系统标记为”Windows“的资产", "ip.os=\"Windows\""),
                new HunterUsed("检索使用了Hikvision且ip开放8000端口的资产", "app=\"Hikvision 海康威视 Firmware 5.0+\" && ip.ports=\"8000\""),
                new HunterUsed("搜索开放端口大于2的IP（支持等于、大于、小于）", "ip.port_count>\"2\""),
                new HunterUsed("查询开放了80和443端口号的资产", "ip.ports=\"80\" && ip.ports=\"443\""),
                new HunterUsed("查询包含IP标签\"CDN\"的资产", "ip.tag=\"CDN\""),
                new HunterUsed("搜索域名标记不为空的资产", "is_domain=true"),
                new HunterUsed("搜索域名包含\"qianxin.com\"的网站", "domain=\"qianxin.com\""),
                new HunterUsed("搜索主域为\"qianxin.com\"的网站", "domain.suffix=\"qianxin.com\""),
                new HunterUsed("搜索server全名为“Microsoft-IIS/10”的服务器", "header.server==\"Microsoft-IIS/10\""),
                new HunterUsed("搜索HTTP消息主体的大小为691的网站", "header.content_length=\"691\""),
                new HunterUsed("搜索HTTP请求返回状态码为”402”的资产", "header.status_code=\"402\""),
                new HunterUsed("搜索HTTP请求头中含有”elastic“的资产", "header=\"elastic\""),
                new HunterUsed("搜索web资产", "is_web=true"),
                new HunterUsed("从网站标题中搜索“北京”", "web.title=\"北京\""),
                new HunterUsed("搜索网站正文包含”网络空间测绘“的资产", "web.body=\"网络空间测绘\""),
                new HunterUsed("搜索2021年的资产", "after=\"2021-01-01\" && before=\"2021-12-31\""),
                new HunterUsed("查询与baidu.com:443网站的特征相似的资产", "web.similar=\"baidu.com:443\""),
                new HunterUsed("查询网站icon与该icon相似的资产", "web.similar_icon==\"17262739310191283300\""),
                new HunterUsed("查询网站icon与该icon相同的资产", "web.icon=\"22eeab765346f14faf564a4709f98548\""),
                new HunterUsed("查询与该网页相似的资产", "web.similar_id=\"3322dfb483ea6fd250b29de488969b35\""),
                new HunterUsed("查询包含资产标签\"登录页面\"的资产", "web.tag=\"登录页面\""),
                new HunterUsed("搜索通过域名关联的ICP备案号为”京ICP备16020626号-8”的网站资产", "icp.number=\"京ICP备16020626号-8\""),
                new HunterUsed("搜索ICP备案网站名中含有“奇安信”的资产", "icp.web_name=\"奇安信\""),
                new HunterUsed("搜索ICP备案单位名中含有“奇安信”的资产", "icp.name=\"奇安信\""),
                new HunterUsed("搜索ICP备案主体为“企业”的资产", "icp.type=\"企业\""),
                new HunterUsed("搜索ICP备案行业为“软件和信息技术服务业”的资产 (查看枚举值)", "icp.industry=\"软件和信息技术服务业\""),
                new HunterUsed("搜索协议为”http“的资产", "protocol=\"http\""),
                new HunterUsed("搜索传输层协议为”udp“的资产", "protocol.transport=\"udp\""),
                new HunterUsed("查询端口响应中包含\"nginx\"的资产", "protocol.banner=\"nginx\""),
                new HunterUsed("搜索标记为”小米 Router“的资产", "app.name=\"小米 Router\""),
                new HunterUsed("查询包含组件分类为\"开发与运维\"的资产", "app.type=\"开发与运维\""),
                new HunterUsed("查询包含组件厂商为\"PHP\"的资产", "app.vendor=\"PHP\""),
                new HunterUsed("查询包含组件版本为\"1.8.1\"的资产", "app.version=\"1.8.1\""),
                new HunterUsed("搜索证书中带有baidu的资产", "cert=\"baidu\""),
                new HunterUsed("搜索证书使用者是qianxin.com的资产", "cert.subject=\"qianxin.com\""),
                new HunterUsed("搜索证书使用者组织是奇安信科技集团股份有限公司的资产", "cert.subject_org=\"奇安信科技集团股份有限公司\""),
                new HunterUsed("搜索证书颁发者是Let's Encrypt Authority X3的资产", "cert.issuer=\"Let's Encrypt Authority X3\""),
                new HunterUsed("搜索证书颁发者组织是Let's Encrypt的资产", "cert.issuer_org=\"Let's Encrypt\""),
                new HunterUsed("搜索证书签名哈希算法sha1为be7605a3b72b60fcaa6c58b6896b9e2e7442ec50的资产", "cert.sha-1=\"be7605a3b72b60fcaa6c58b6896b9e2e7442ec50\""),
                new HunterUsed("搜索证书签名哈希算法sha256为4e529a65512029d77a28cbe694c7dad1e60f98b5cb89bf2aa329233acacc174e的资产", "cert.sha-256=\"4e529a65512029d77a28cbe694c7dad1e60f98b5cb89bf2aa329233acacc174e\""),
                new HunterUsed("搜索证书签名哈希算法shamd5为aeedfb3c1c26b90d08537523bbb16bf1的资产", "cert.sha-md5=\"aeedfb3c1c26b90d08537523bbb16bf1\""),
                new HunterUsed("搜索证书序列号是35351242533515273557482149369的资产", "cert.serial_number=\"35351242533515273557482149369\""),
                new HunterUsed("搜索证书已过期的资产", "cert.is_expired=true"),
                new HunterUsed("搜索证书可信的资产", "cert.is_trust=true"),
                new HunterUsed("搜索asn为\"136800\"的资产", "as.number=\"136800\""),
                new HunterUsed("搜索asn名称为\"CLOUDFLARENET\"的资产", "as.name=\"CLOUDFLARENET\""),
                new HunterUsed("搜索asn注册机构为\"PDR\"的资产", "as.org=\"PDR\""),
                new HunterUsed("搜索tls-jarm哈希为21d19d00021d21d21c21d19d21d21da1a818a999858855445ec8a8fdd38eb5的资产", "tls-jarm.hash=\"21d19d00021d21d21c21d19d21d21da1a818a999858855445ec8a8fdd38eb5\""),
                new HunterUsed("搜索tls-jarmANS为c013|0303|h2|ff01-0000-0001-000b-0023-0010-0017,00c0|0303|h2|ff01-0000-0001-0023-0010-0017,|||,c013|0303||ff01-0000-0001-000b-0023-0017,c013|0303||ff01-0000-0001-000b-0023-0017,c013|0302|h2|ff01-0000-0001-000b-0023-0010-0017,c013|0303|h2|ff01-0000-0001-000b-0023-0010-0017,00c0|0303|h2|ff01-0000-0001-0023-0010-0017,c013|0303|h2|ff01-0000-0001-000b-0023-0010-0017,c013|0303|h2|ff01-0000-0001-000b-0023-0010-0017的资产", "tls-jarm.ans=\"c013|0303|h2|ff01-0000-0001-000b-0023-0010-0017,00c0|0303|h2|ff01-0000-0001-0023-0010-0017,|||,c013|0303||ff01-0000-0001-000b-0023-0017,c013|0303||ff01-0000-0001-000b-0023-0017,c013|0302|h2|ff01-0000-0001-000b-0023-0010-0017,c013|0303|h2|ff01-0000-0001-000b-0023-0010-0017,00c0|0303|h2|ff01-0000-0001-0023-0010-0017,c013|0303|h2|ff01-0000-0001-000b-0023-0010-0017,c013|0303|h2|ff01-0000-0001-000b-0023-0010-0017\"")
        );

        menuItem1.setOnAction(e -> {
            HunterUsed selectedItem = hunter_used.getSelectionModel().getSelectedItem();
            String value = selectedItem.getSearch_syn_new();
            content.putString(value);
            clipboard.setContent(content);
        });
        hunter_used.setItems(data);

    }


    @FXML
    private void AboutTools(javafx.event.ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("by nex121");
        alert.setHeaderText("工具提示");
        alert.setContentText("本工具只供研究，禁止非法使用！");
        alert.showAndWait();
    }

    /**
     * 点击后打开设置窗口
     */
    @FXML
    private void FofaSet(javafx.event.ActionEvent actionEvent) throws IOException {
        FofaSetApplication fa = new FofaSetApplication();
        fa.start(new Stage());
    }


    @FXML
    private void VulVerify(ActionEvent actionEvent) throws UnknownHostException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        List<String> values = new ArrayList<>();


        //先看target_list的area中是否存在值
        if (!Objects.equals(target_list.getText(), "")) {
            values.addAll(Arrays.asList(target_list.getText().split("\n")));
        } else {
            //不存在值再看fofa_result
            try {
                //获取当前节点tab
                Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
                //获取tab中的anchorpane布局
                AnchorPane ap = (AnchorPane) currentTab.getContent();
                //布局中的tableview
                TableView<Fofa> tableView = (TableView<Fofa>) ap.getChildren().get(0);
                // 获取某一列的值
                TableColumn<Fofa, String> column = (TableColumn<Fofa, String>) tableView.getColumns().get(3);
                //获取fofa_line的行数
                int fofa_line = tableView.getItems().size();

                for (int i = 0; i < fofa_line; i++) {
                    values.add(column.getCellData(i));
                }
            } catch (Exception e) {
                alert.setTitle("提示:");
                alert.setHeaderText("任务提示");
                alert.setContentText("目标列表为空");
                alert.showAndWait();
                return;
            }
        }

        String flag1 = features1com.getValue();
        String flag2 = features2com.getValue();
        String feat1 = features1.getText();
        String feat2 = features2.getText();
        String vul_content1 = vul_data_area1.getText();
        String vul_content2 = vul_data_area2.getText();

        ArrayList<String> domain_list = new ArrayList<>();
        ArrayList<String> ip_list = new ArrayList<>();

        if (values.isEmpty()) {
            alert.setTitle("提示:");
            alert.setHeaderText("任务提示");
            alert.setContentText("目标列表为空");
            alert.showAndWait();
            return;
        }

        if (feat1.trim().equals("")) {
            alert.setTitle("提示:");
            alert.setHeaderText("任务提示");
            alert.setContentText("特征1为空");
            alert.showAndWait();
            return;
        }

        if (!vul_content2.trim().equals("") && feat2.trim().equals("")) {
            alert.setTitle("提示:");
            alert.setHeaderText("任务提示");
            alert.setContentText("特征2为空");
            alert.showAndWait();
            return;
        }

        int threadNum = 50;
        try {
            threadNum = Integer.parseInt(thread_num.getText());
        } catch (Exception e) {
        }
        //先看一下线程
//        if (values.size() < threadNum) {
//            alert.setTitle("提示:");
//            alert.setHeaderText("任务提示");
//            alert.setContentText("线程过多，请修改");
//            alert.showAndWait();
//            return;
//        }

        //这种弹窗方法还是存在问题，暂时不解决
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        CountDownLatch latch = new CountDownLatch(values.size());

        for (String host : values) {
            if (host.trim().equals("")) {
                continue;
            }

            String root_host = "";
            String root_ip = "";
            if (!DomainDealUtil.isIP(host)) {
                root_host = DomainDealUtil.get_root_domain_apache(host);
            }
            if (DomainDealUtil.isIP(host)) {
                root_ip = DomainDealUtil.get_http_root_ip(host);
            }
            if (!domain_list.contains(root_host) || !ip_list.contains(root_ip)) {
                domain_list.add(root_host);
                ip_list.add(root_ip);
                VerifyTaskService task = new VerifyTaskService(host, flag1, flag2, feat1, feat2, vul_content1, vul_content2, vul_list);
                task.setOnSucceeded(event -> {
                    latch.countDown();

                    if (latch.getCount() == 0) {
                        Platform.runLater(() -> {
                            alert.setTitle("提示:");
                            alert.setHeaderText("任务提示");
                            alert.setContentText("漏洞验证完成");
                            alert.showAndWait();
                        });
                    }
                });
                executor.execute(task);
            }
        }
//        executor.shutdown();
    }

    @FXML
    private void ExportVulList(javafx.event.ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) export_vul_list.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存文件");
        fileChooser.setInitialFileName("VulList.txt");
        File file = fileChooser.showSaveDialog(stage);

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(vul_list.getText());
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Tab createTab(String name) {
        Tab tab = new Tab(name);
        try {
            FXMLLoader loader = new FXMLLoader(FofaFullSearchApplication.class.getResource("http-send.fxml"));
            tab.setContent(loader.load());
            TabController tabController = loader.getController();
            tabController.initialize(name);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tab;
    }

    @FXML
    private void FofaSearch(javafx.event.ActionEvent actionEvent) throws IOException {
        ConfigManagerService cms = new ConfigManagerService();
        String que = fofa_field.getText();
        //把查询信息添加到history
        cms.addToHistory(que);

        Tab existingTab = tabPane.getSelectionModel().getSelectedItem();
        int index = tabPane.getTabs().indexOf(existingTab);
        Tab newTab = createTab(que);
        tabPane.getTabs().add(index + 1, newTab);
        tabPane.getSelectionModel().select(newTab);

    }
}