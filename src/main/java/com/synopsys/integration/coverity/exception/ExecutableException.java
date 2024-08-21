/*
 * coverity-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.exception;

public class ExecutableException extends CoverityIntegrationException {
    public ExecutableException() {
        super();
    }

    public ExecutableException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ExecutableException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExecutableException(final String message) {
        super(message);
    }

    public ExecutableException(final Throwable cause) {
        super(cause);
    }

}
