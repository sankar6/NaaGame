package com.naagame.core.resources;

public class NgmSound implements IResource {
    private String fileName = "";
    private String name;

    public NgmSound(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}