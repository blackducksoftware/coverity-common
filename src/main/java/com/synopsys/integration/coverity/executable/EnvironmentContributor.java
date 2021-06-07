/*
 * coverity-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.executable;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public abstract class EnvironmentContributor {
    protected void populateEnvironmentMap(final Map<String, String> environment, final CoverityToolEnvironmentVariable key, final String value) {
        populateEnvironmentMap(environment, key.toString(), value);
    }

    protected void populateEnvironmentMap(final Map<String, String> environment, final String key, final String value) {
        // ProcessBuilder's environment's keys and values must be non-null java.lang.String's
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
            environment.put(key, value);
        }
    }
}
