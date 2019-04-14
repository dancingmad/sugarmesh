package com.irongrp.sugarmesh.api.model;

import java.util.List;

public class PackageDTO {

    private String name;
    private List<String> packages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }
}
