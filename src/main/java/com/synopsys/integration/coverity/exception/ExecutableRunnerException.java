/*
 * coverity-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.exception;

public class ExecutableRunnerException extends CoverityIntegrationException {
    public ExecutableRunnerException() {
        super();
    }

    public ExecutableRunnerException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ExecutableRunnerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExecutableRunnerException(final String message) {
        super(message);
    }

    public ExecutableRunnerException(final Throwable cause) {
        super(cause);
    }
}
