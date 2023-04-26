package com.fofa.fofa_full_search.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FofaUsed {

    private String example;
    private String used;
    private String anno;

    public FofaUsed(String example, String used, String anno) {
        this.example = example;
        this.used = used;
        this.anno = anno;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public StringProperty exampleProperty() {
        return new SimpleStringProperty(example);
    }

    public StringProperty usedProperty() {
        return new SimpleStringProperty(used);
    }

    public StringProperty annoProperty() {
        return new SimpleStringProperty(anno);
    }
}
