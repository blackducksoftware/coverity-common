/*
 * coverity-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity;

import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.log.LogLevel;
import com.synopsys.integration.util.IntEnvironmentVariables;

public abstract class CoverityIntLogger extends IntLogger {
    @Override
    public void setLogLevel(final IntEnvironmentVariables variables) {
        final String logLevel = variables.getValue("COVERITY_LOG_LEVEL", "INFO");
        try {
            setLogLevel(LogLevel.valueOf(logLevel.toUpperCase()));
        } catch (final IllegalArgumentException e) {
            setLogLevel(LogLevel.INFO);
        }
    }

}
