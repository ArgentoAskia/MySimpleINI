package tech.argentoaskia.models.impl;

import tech.argentoaskia.models.interfaces.DocumentItem;

import java.io.File;


public class Document extends DocumentItem<String> {


    public Document(String nameFlag) {
        super(nameFlag);
    }
    public Document(File fileFlag){
        super(fileFlag);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
