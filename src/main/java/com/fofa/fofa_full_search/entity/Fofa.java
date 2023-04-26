package com.fofa.fofa_full_search.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Fofa {

    private String ip;
    private String port;
    private String url;
    private String title;
    private String region;
    private String city;
    private String server;
    private String icp;
    private final String from;
    private Integer id;

    public Fofa(int id, String ip, String port, String url, String title, String region, String city, String server, String icp,String from) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.url = url;
        this.title = title;
        this.region = region;
        this.city = city;
        this.server = server;
        this.icp = icp;
        this.from = from;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getIcp() {
        return icp;
    }

    public void setIcp(String icp) {
        this.icp = icp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public StringProperty ipProperty() {
        return new SimpleStringProperty(ip);
    }

    public StringProperty portProperty() {
        return new SimpleStringProperty(port);
    }

    public StringProperty urlProperty() {
        return new SimpleStringProperty(url);
    }

    public StringProperty titleProperty() {
        return new SimpleStringProperty(title);
    }

    public StringProperty regionProperty() {
        return new SimpleStringProperty(region);
    }

    public StringProperty cityProperty() {
        return new SimpleStringProperty(city);
    }

    public StringProperty serverProperty() {
        return new SimpleStringProperty(server);
    }

    public StringProperty icpProperty() {
        return new SimpleStringProperty(icp);
    }

    public StringProperty fromProperty() {
        return new SimpleStringProperty(from);
    }

    public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

}

