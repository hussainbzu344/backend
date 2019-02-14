package com.edu.autoclass.bean;

import java.util.List;

public class Framework {
    private int id;
    private String name;
    private String coreLibrary;
    private int typeId;
    List<EntryPoint> entryPointList;

    public List<EntryPoint> getEntryPointList() {
        return entryPointList;
    }

    public void setEntryPointList(List<EntryPoint> entryPointList) {
        this.entryPointList = entryPointList;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoreLibrary() {
        return coreLibrary;
    }

    public void setCoreLibrary(String coreLibrary) {
        this.coreLibrary = coreLibrary;
    }
}
