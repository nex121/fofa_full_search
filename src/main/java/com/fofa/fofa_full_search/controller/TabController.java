package com.fofa.fofa_full_search.controller;

import com.fofa.fofa_full_search.entity.Fofa;
import com.fofa.fofa_full_search.service.ConfigManagerService;
import com.fofa.fofa_full_search.service.FofaTaskService;
import com.fofa.fofa_full_search.service.HunterTaskService;
import com.fofa.fofa_full_search.service.XlsxExporter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

import static com.fofa.fofa_full_search.util.queDeal.hunterQueDeal;

public class TabController {
    @FXML
    private TableView<Fofa> fofa_result;
    @FXML
    private TableColumn<Fofa, Integer> id_col;
    @FXML
    private TableColumn<Fofa, String> ip_col;
    @FXML
    private TableColumn<Fofa, String> port_col;
    @FXML
    private TableColumn<Fofa, String> url_col;
    @FXML
    private TableColumn<Fofa, String> title_col;
    @FXML
    private TableColumn<Fofa, String> region_col;
    @FXML
    private TableColumn<Fofa, String> city_col;
    @FXML
    private TableColumn<Fofa, String> server_col;
    @FXML
    private TableColumn<Fofa, String> icp_col;
    @FXML
    private TableColumn<Fofa, String> from_col;

    public void initialize(String que) throws IOException {

        ContextMenu contextMenu = new ContextMenu();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        MenuItem menuItem1 = new MenuItem("复制URL");
        MenuItem menuItem2 = new MenuItem("复制IP");
        MenuItem menuItem3 = new MenuItem("复制省份");
        MenuItem menuItem4 = new MenuItem("复制市区");
        MenuItem menuItem5 = new MenuItem("复制ICP");
        MenuItem menuItem6 = new MenuItem("导出全部");


        contextMenu.getItems().addAll(menuItem1, menuItem2, menuItem3, menuItem4, menuItem5,menuItem6);

        fofa_result.setContextMenu(contextMenu);

        menuItem1.setOnAction(e -> {
            Fofa selectedItem = fofa_result.getSelectionModel().getSelectedItem();
            String value = selectedItem.getUrl();
            content.putString(value);
            clipboard.setContent(content);
        });

        menuItem2.setOnAction(e -> {
            Fofa selectedItem = fofa_result.getSelectionModel().getSelectedItem();
            String value = selectedItem.getIp();
            content.putString(value);
            clipboard.setContent(content);
        });
        menuItem3.setOnAction(e -> {
            Fofa selectedItem = fofa_result.getSelectionModel().getSelectedItem();
            String value = selectedItem.getRegion();
            content.putString("region=\"" + value + "\"");
            clipboard.setContent(content);
        });
        menuItem4.setOnAction(e -> {
            Fofa selectedItem = fofa_result.getSelectionModel().getSelectedItem();
            String value = selectedItem.getCity();
            content.putString("city=\"" + value + "\"");
            clipboard.setContent(content);
        });
        menuItem5.setOnAction(e -> {
            Fofa selectedItem = fofa_result.getSelectionModel().getSelectedItem();
            String value = selectedItem.getIcp();
            content.putString(value);
            clipboard.setContent(content);
        });

        menuItem6.setOnAction(e -> {
            Stage stage = (Stage) fofa_result.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as XLSX");
            try {
                String fileName = DatatypeConverter.printBase64Binary(que.getBytes("utf-8")) + ".xlsx";
                fileChooser.setInitialFileName(fileName);
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx"));
                File file = fileChooser.showSaveDialog(stage);
                if (file != null) {
                    XlsxExporter.exportTableToXlsx(file, fofa_result);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示:");
                alert.setHeaderText("任务提示");
                alert.setContentText("导出完成");
                alert.showAndWait();
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


        ConfigManagerService cms = new ConfigManagerService();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        AtomicInteger taskId = new AtomicInteger(1);
        //把查询信息添加到history
        cms.addToHistory(que);

        id_col.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        ip_col.setCellValueFactory(cellData -> cellData.getValue().ipProperty());
        port_col.setCellValueFactory(cellData -> cellData.getValue().portProperty());
        url_col.setCellValueFactory(cellData -> cellData.getValue().urlProperty());
        title_col.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        region_col.setCellValueFactory(cellData -> cellData.getValue().regionProperty());
        city_col.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        server_col.setCellValueFactory(cellData -> cellData.getValue().serverProperty());
        icp_col.setCellValueFactory(cellData -> cellData.getValue().icpProperty());
        from_col.setCellValueFactory(cellData -> cellData.getValue().fromProperty());

        String email = cms.getFofa_email().trim();
        String key = cms.getFofa_key().trim();
        String hunter_key = cms.getHunter_key().trim();
        String search_num = cms.getSearch_num();

        if (que.equals("")) {
            alert.setTitle("提示:");
            alert.setHeaderText("任务提示");
            alert.setContentText("fofa 检索语句为空");
            alert.showAndWait();
            return;
        }
        if (email.equals("")) {
            alert.setTitle("提示:");
            alert.setHeaderText("任务提示");
            alert.setContentText("fofa email为空");
            alert.showAndWait();
            return;
        }
        if (key.equals("")) {
            alert.setTitle("提示:");
            alert.setHeaderText("任务提示");
            alert.setContentText("fofa key为空");
            alert.showAndWait();
            return;
        }
        System.out.println(cms.getSearch_tag());
        if (cms.getSearch_tag().equals("0")) {
            FofaTaskService task = new FofaTaskService(email, key, que, taskId, fofa_result);
            new Thread(task).start();
        } else if (cms.getSearch_tag().equals("1")) {
            HunterTaskService task1 = new HunterTaskService(hunter_key, que, search_num, taskId, fofa_result);
            new Thread(task1).start();
        } else if (cms.getSearch_tag().equals("2")) {
            FofaTaskService task = new FofaTaskService(email, key, que, taskId, fofa_result);
            new Thread(task).start();
            String hunter_que = hunterQueDeal(que);
            HunterTaskService task1 = new HunterTaskService(hunter_key, hunter_que, search_num, taskId, fofa_result);
            new Thread(task1).start();

        } else {
            alert.setTitle("提示:");
            alert.setHeaderText("任务提示");
            alert.setContentText("请取人是否已勾选测绘平台");
            alert.showAndWait();
        }
    }

}
