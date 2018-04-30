package com.sig.integration.coverity.executable;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public abstract class EnvironmentContributor {

    protected void populateEnvironmentMap(final Map<String, String> environment, final String key, final String value) {
        // ProcessBuilder's environment's keys and values must be non-null java.lang.String's
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
            environment.put(key, value);
        }
    }
}
