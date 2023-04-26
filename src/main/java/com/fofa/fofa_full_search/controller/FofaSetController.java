package com.fofa.fofa_full_search.controller;

import com.fofa.fofa_full_search.service.ConfigManagerService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;


public class FofaSetController {
    @FXML
    private TextField fofa_email, fofa_key, hunter_key;
    @FXML
    private ChoiceBox<String> search_num;
    @FXML
    private CheckBox fofa_select, hunter_select;
    private String search_tag;

    public void initialize() throws IOException {
        //设置hunter搜索初始值
        search_num.getItems().addAll("10", "100", "1000", "10000", "all");

        ConfigManagerService cms = new ConfigManagerService();
        fofa_email.setText(cms.getFofa_email());
        fofa_key.setText(cms.getFofa_key());
        hunter_key.setText(cms.getHunter_key());
        search_num.setValue(cms.getSearch_num());
        search_num.setValue(cms.getSearch_num());
        search_tag = cms.getSearch_tag();
        if (search_tag.equals("0")) {
            fofa_select.setSelected(true);
            hunter_select.setSelected(false);
        } else if (search_tag.equals("1")) {
            fofa_select.setSelected(false);
            hunter_select.setSelected(true);
        }else if (search_tag.equals("2")){
            fofa_select.setSelected(true);
            hunter_select.setSelected(true);
        }else {
            fofa_select.setSelected(false);
            hunter_select.setSelected(false);
        }
    }

    @FXML
    private void SaveConifg(javafx.event.ActionEvent actionEvent) throws IOException {

        ConfigManagerService cms = new ConfigManagerService();
        String fofa_emails = fofa_email.getText();
        String fofa_keys = fofa_key.getText();
        String hunter_keys = hunter_key.getText();
        String search_nums = search_num.getValue();
        if (fofa_select.isSelected() && !hunter_select.isSelected()) {
            search_tag = "0";
        } else if (!fofa_select.isSelected() && hunter_select.isSelected()) {
            search_tag = "1";
        } else if (fofa_select.isSelected() && hunter_select.isSelected()) {
            search_tag = "2";
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示:");
            alert.setHeaderText("设置提示");
            alert.setContentText("没有选择资产测绘平台");
            alert.showAndWait();
        }
        String search_tags = search_tag;

        cms.setFofa_email(fofa_emails);
        cms.setFofa_key(fofa_keys);
        cms.setHunter_key(hunter_keys);
        cms.setSearch_num(search_nums);
        cms.setSearch_tag(search_tags);

        cms.saveConfig();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示:");
        alert.setHeaderText("保存成功");
        alert.showAndWait();
    }
}
