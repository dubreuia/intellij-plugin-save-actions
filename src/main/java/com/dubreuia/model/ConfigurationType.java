package com.dubreuia.model;

public enum ConfigurationType {

    /**
     * TODO
     */
    GLOBAL,

    /**
     * TODO
     */
    PROJECT,

    /**
     * TODO
     */
    ALL;

    public boolean isIncluded(ConfigurationType configurationType) {
        return this == configurationType || this == ALL;
    }

}