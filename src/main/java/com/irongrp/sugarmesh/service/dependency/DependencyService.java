package com.irongrp.sugarmesh.service.dependency;

import com.irongrp.sugarmesh.service.dependency.model.Application;
import com.irongrp.sugarmesh.service.dependency.model.ApplicationPackage;
import com.irongrp.sugarmesh.service.dependency.model.ApplicationRepository;
import com.irongrp.sugarmesh.service.dependency.model.Bean;
import com.irongrp.sugarmesh.service.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.beans.BeansEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DependencyService {

    private static Pattern TYPE_PATTERN = Pattern.compile("(\\w+\\.\\w+)\\.(\\w+)\\.?(.*)\\.([A-Z].*)");
    private static Logger LOGGER = LoggerFactory.getLogger(DependencyService.class);

    private ApplicationRepository applicationRepository;

    @Autowired
    public DependencyService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Map<String,Application> createDependencyGraph(User user,
                                                 BeansEndpoint.ApplicationBeans beans,
                                                 String domain) {

        Map<String,ApplicationPackage> packageMap = new HashMap<>();
        Map<String,Bean> beanMap = new HashMap<>();
        Map<String,Application> applicationMap = new HashMap<>();

        if (beans == null) {
            return null;
        }

        beans.getContexts().values()
                .forEach(ctx -> ctx.getBeans().forEach((key, value) -> addBean(key,
                value,
                applicationMap,
                beanMap,
                packageMap,
                domain,
                user)));

        beans.getContexts().values().forEach(
                ctx -> ctx.getBeans().forEach((key, value) -> linkBeanDependencies(key,
                value,
                beanMap)));
        applicationMap.values().forEach(
                app -> {
                    app.getRootPackage().setPackages(new ArrayList<>(app.getPackages()));
                    app.getPackages().add(app.getRootPackage());
                }
        );
        return applicationMap;
    }

    public Application updateApplication(Application app) {
        applicationRepository.deleteApplication(app.getCreatedBy().getUsername(), app.getName());
        return applicationRepository.save(app);
    }

    private void linkBeanDependencies(String beanName,
                          BeansEndpoint.BeanDescriptor beanData,
                          Map<String, Bean> beanMap) {
        Bean bean = beanMap.get(beanName);
        if (bean == null) {
            return;
        }
        bean.setDependsOn(
                Arrays.stream(beanData.getDependencies())
                        .map(beanMap::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
    }

    private void addBean(String beanName,
                         BeansEndpoint.BeanDescriptor beanData,
                         Map<String,Application> applicationMap,
                         Map<String,Bean> beanMap,
                         Map<String, ApplicationPackage> packageMap,
                         String targetDomain,
                         User user) {
        Matcher matcher = TYPE_PATTERN.matcher(beanData.getType().toString());
        if (!matcher.find() || matcher.groupCount() < 3) {
            LOGGER.warn("Weird package name for bean '{}': {}",
                    beanName,
                    beanData.getType());
            return;
        }
        String domain = matcher.group(1);
        if (!domain.equals(targetDomain)) {
          return;
        }
        String applicationName = matcher.group(2);
        String applicationKey = domain+"."+applicationName;
        Application app = applicationMap.get(applicationKey);
        if (app == null) {
            app = createApplication(domain,applicationName,user);
            applicationMap.put(applicationKey, app);
        }
        String beanPackage = matcher.group(3);
        createPackages(beanPackage, packageMap, app);
        Bean bean = new Bean();
        if (!StringUtils.isEmpty(beanPackage)) {
            bean.setBelongsToPackage(packageMap.get(beanPackage));
        } else {
            bean.setBelongsToPackage(app.getRootPackage());
        }
        bean.setName(beanName);
        bean.setFullName(beanData.getType().toString());
        beanMap.put(beanName,bean);
        app.getBeans().add(bean);
        LOGGER.info("Created Beaninfo for {}",beanName);
    }

    private Application createApplication(String domain, String applicationName,User user) {
        Application app = new Application();
        ApplicationPackage rootPackage = new ApplicationPackage();
        rootPackage.setName("root");
        app.setName(applicationName);
        app.setDomain(domain);
        app.setBeans(new ArrayList<>());
        app.setCreatedBy(user);
        app.setPackages(new ArrayList<>());
        app.setRootPackage(rootPackage);
        return app;
    }

    private void createPackages(String packageName,
                                Map<String, ApplicationPackage> packageMap,
                                Application app) {
        if (StringUtils.isEmpty(packageName)) {
            return;
        }
        ApplicationPackage childPackage = null;
        do {
            ApplicationPackage applicationPackage = packageMap.get(packageName);
            if (applicationPackage != null) {
                if (childPackage != null) {
                    applicationPackage.getPackages().add(childPackage);
                }
                return;
            }
            applicationPackage = new ApplicationPackage();
            applicationPackage.setName(packageName);
            applicationPackage.setPackages(new ArrayList<>());
            if (childPackage != null) {
                applicationPackage.getPackages().add(childPackage);
            }
            packageMap.put(packageName,applicationPackage);
            app.getPackages().add(applicationPackage);
            if (!packageName.contains(".")) {
               packageName = "";
            } else {
                packageName = packageName.substring(0, packageName.lastIndexOf("."));
            }
            childPackage = applicationPackage;
        } while(!StringUtils.isEmpty(packageName));
    }

}
