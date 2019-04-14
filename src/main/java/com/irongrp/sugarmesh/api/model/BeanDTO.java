package com.irongrp.sugarmesh.api.model;

import java.util.List;

public class BeanDTO {

    private String name;

    private String fullName;

    private String belongsToPackage;

    private List<String> dependsOn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBelongsToPackage() {
        return belongsToPackage;
    }

    public void setBelongsToPackage(String belongsToPackage) {
        this.belongsToPackage = belongsToPackage;
    }

    public List<String> getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(List<String> dependsOn) {
        this.dependsOn = dependsOn;
    }
}
