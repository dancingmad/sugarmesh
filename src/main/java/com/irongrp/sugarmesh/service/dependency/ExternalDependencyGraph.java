package com.irongrp.sugarmesh.service.dependency;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
class ExternalDependencyGraph {
    private GraphContext contexts;

    public GraphContext getContexts() {
        return this.contexts;
    }



    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class GraphContext {
        private Application application;

        public Application getApplication() {
            return application;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Application {

        private Map<String, BeanData> beans;
        private String parentId;

        public Map<String, BeanData> getBeans() {
            return beans;
        }

        public String getParentId() {
            return parentId;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class BeanData {
       private String type;
       private List<String> dependencies;

        public String getType() {
            return type;
        }

        public List<String> getDependencies() {
            return dependencies;
        }
    }
}
