package com.irongrp.sugarmesh.service.dependency;

import com.irongrp.sugarmesh.api.DependencyGraph;
import com.irongrp.sugarmesh.service.dependency.model.*;
import com.irongrp.sugarmesh.service.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DependencyService {

    private static Pattern TYPE_PATTERN = Pattern.compile("(\\w+\\.\\w+)\\.(\\w+)\\.?(.*)\\.([A-Z].*)");
    private static Logger LOGGER = LoggerFactory.getLogger(DependencyService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationRepository applicationRepository;


    public DependencyGraph createDependencyGraph(User user, DependencyGraph dependencyGraph) {
        ExternalDependencyGraph graph = restTemplate.getForObject(dependencyGraph.getUrl(),ExternalDependencyGraph.class);
        Map<String,ApplicationPackage> packageMap = new HashMap<>();
        Map<String,Bean> beanMap = new HashMap<>();
        Map<String,Application> applicationMap = new HashMap<>();

        if (graph == null) {
            return null;
        }
        graph.getContexts().getApplication().getBeans().forEach((key, value) -> addBean(key,
                value,
                applicationMap,
                beanMap,
                packageMap,
                dependencyGraph.getDomain(),
                user));
        graph.getContexts().getApplication().getBeans().forEach((key, value) -> linkBeanDependencies(key,
                value,
                beanMap));
        applicationMap
                .values()
                .forEach(app -> applicationRepository.deleteApplication(user, app.getName()));
        applicationMap.values()
                .forEach(app -> applicationRepository.save(app));
        return new DependencyGraph.Builder()
                .domain(dependencyGraph.getDomain())
                .url(dependencyGraph.getUrl())
                .applications(applicationMap)
                .build();
    }

    private void linkBeanDependencies(String beanName,
                          ExternalDependencyGraph.BeanData beanData,
                          Map<String, Bean> beanMap) {
        Bean bean = beanMap.get(beanName);
        if (bean == null) {
            return;
        }
        bean.setDependsOn(
                beanData.getDependencies()
                        .stream()
                        .map(beanMap::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
    }

    private void addBean(String beanName,
                         ExternalDependencyGraph.BeanData beanData,
                         Map<String,Application> applicationMap,
                         Map<String,Bean> beanMap,
                         Map<String, ApplicationPackage> packageMap,
                         String targetDomain,
                         User user) {
        Matcher matcher = TYPE_PATTERN.matcher(beanData.getType());
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
        }
        bean.setName(beanName);
        bean.setFullName(beanData.getType());
        beanMap.put(beanName,bean);
        app.getBeans().add(bean);
        LOGGER.info("Created Beaninfo for {}",beanName);
    }

    private Application createApplication(String domain, String applicationName,User user) {
        Application app = new Application();
        app.setName(applicationName);
        app.setDomain(domain);
        app.setBeans(new ArrayList<>());
        app.setCreatedBy(user);
        app.setPackages(new ArrayList<>());
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
            if (!packageName.contains(".")) {
               packageName = "";
               app.getPackages().add(applicationPackage);
            } else {
                packageName = packageName.substring(0, packageName.lastIndexOf("."));
            }
            childPackage = applicationPackage;
        } while(!StringUtils.isEmpty(packageName));
    }

}
