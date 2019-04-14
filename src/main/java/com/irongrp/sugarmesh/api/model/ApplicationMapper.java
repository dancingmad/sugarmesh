package com.irongrp.sugarmesh.api.model;

import com.irongrp.sugarmesh.service.dependency.model.Application;
import com.irongrp.sugarmesh.service.dependency.model.ApplicationPackage;
import com.irongrp.sugarmesh.service.dependency.model.Bean;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationMapper {

    public static ApplicationDTO map(Application app) {
        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setName(app.getName());
        applicationDTO.setDomain(app.getDomain());
        applicationDTO.setBeans(app.getBeans().stream()
                .map(ApplicationMapper::map)
                .collect(Collectors.toList()));
        applicationDTO.setPackages(app.getPackages().stream()
                .map(ApplicationMapper::map)
                .collect(Collectors.toList()));
        return applicationDTO;
    }

    private static BeanDTO map(Bean bean) {
        BeanDTO beanDTO = new BeanDTO();
        beanDTO.setBelongsToPackage(bean.getBelongsToPackage().getName());
        beanDTO.setDependsOn(bean.getDependsOn().stream()
                .map(Bean::getFullName)
                .collect(Collectors.toList()));
        beanDTO.setFullName(bean.getFullName());
        beanDTO.setName(bean.getName());
        return beanDTO;
    }

    private static PackageDTO map(ApplicationPackage pack) {
        PackageDTO packageDTO = new PackageDTO();
        packageDTO.setName(pack.getName());
        packageDTO.setPackages(pack.getPackages().stream()
                .map(ApplicationPackage::getName)
                .collect(Collectors.toList()));
        return packageDTO;
    }

    public static Application map(ApplicationDTO applicationDTO) {
        Application app = new Application();
        app.setName(applicationDTO.getName());
        app.setDomain(applicationDTO.getDomain());
        Map<String, Bean> beanMap = new HashMap<>();
        Map<String, ApplicationPackage> packageMap = new HashMap<>();
        applicationDTO.getBeans().forEach(bean -> {
            beanMap.put(bean.getFullName(), createFrom(bean));
        });
        applicationDTO.getPackages().forEach(pack -> {
            packageMap.put(pack.getName(), createFrom(pack));
        });
        app.setPackages(applicationDTO.getPackages().stream()
                .map(p -> mapLinked(p,packageMap))
                .collect(Collectors.toList()));
        app.setBeans(applicationDTO.getBeans().stream()
                .map(b -> mapLinked(b, beanMap, packageMap))
                .collect(Collectors.toList()));
        return app;
    }

    private static Bean createFrom(BeanDTO beanDTO) {
        Bean bean = new Bean();
        bean.setName(beanDTO.getName());
        bean.setFullName(beanDTO.getFullName());
        return bean;
    }

    private static ApplicationPackage createFrom(PackageDTO packageDTO) {
        ApplicationPackage pack = new ApplicationPackage();
        pack.setName(packageDTO.getName());
        return pack;
    }

    private static Bean mapLinked(BeanDTO beanDTO, Map<String,Bean> beanMap, Map<String,ApplicationPackage> packMap) {
        Bean bean = beanMap.get(beanDTO.getFullName());
        bean.setFullName(beanDTO.getFullName());
        bean.setName(beanDTO.getName());
        bean.setDependsOn(beanDTO.getDependsOn().stream().map(beanMap::get).collect(Collectors.toList()));
        bean.setBelongsToPackage(packMap.get(beanDTO.getBelongsToPackage()));
        return bean;
    }

    private static ApplicationPackage mapLinked(PackageDTO packageDTO, Map<String,ApplicationPackage> packMap) {
        ApplicationPackage pack = packMap.get(packageDTO.getName());
        pack.setPackages(packageDTO.getPackages().stream().map(packMap::get).collect(Collectors.toList()));
        return pack;
    }


}


