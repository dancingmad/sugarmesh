package com.irongrp.sugarmesh.api;

import com.irongrp.sugarmesh.service.dependency.model.Application;

import java.util.HashMap;
import java.util.Map;

public class DependencyGraph {
    private String domain;
    private String url;
    private Map<String, Application> applications;

    public String getDomain() {
        return domain;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, Application> getApplications() {
        return applications;
    }

    public static class Builder {
        private String domain;
        private String url;
        private Map<String,Application> applications;

        public Builder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder applications(Map<String, Application> applications) {
            if (applications == null) {
                return this;
            }
            this.applications = new HashMap<>(applications);
            return this;
        }

        public DependencyGraph build() {
            DependencyGraph dependencyGraph = new DependencyGraph();
            dependencyGraph.applications = this.applications;
            dependencyGraph.url = this.url;
            dependencyGraph.domain = this.domain;
            return dependencyGraph;
        }
    }
}
