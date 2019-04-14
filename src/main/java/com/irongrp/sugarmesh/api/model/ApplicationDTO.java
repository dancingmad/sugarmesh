package com.irongrp.sugarmesh.api.model;

import java.util.List;

public class ApplicationDTO {

    private String domain; // e.g. org.springframework
    private String name; // e.g. boot

    private List<BeanDTO> beans;
    private List<PackageDTO> packages;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BeanDTO> getBeans() {
        return beans;
    }

    public void setBeans(List<BeanDTO> beans) {
        this.beans = beans;
    }

    public List<PackageDTO> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageDTO> packages) {
        this.packages = packages;
    }
}
