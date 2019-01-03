package com.mobicule.documentscanner;

public class Document {
    private  String name;

    public Document(String name) {
        this.name = name;
    }

    public Document() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
