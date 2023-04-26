package com.fofa.fofa_full_search.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HunterUsed {

    private String syntax;
    private String search_syn_new;
    public HunterUsed(String syntax, String search_syn_new) {
        this.syntax = syntax;
        this.search_syn_new = search_syn_new;
    }
    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public String getSearch_syn_new() {
        return search_syn_new;
    }

    public void setSearch_syn_new(String search_syn_new) {
        this.search_syn_new = search_syn_new;
    }


    public StringProperty syntaxProperty() {
        return new SimpleStringProperty(syntax);
    }

    public StringProperty search_syn_newProperty() {
        return new SimpleStringProperty(search_syn_new);
    }

}
