/*
 * coverity-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.exception;

import com.synopsys.integration.exception.IntegrationException;

public class CoverityIntegrationException extends IntegrationException {
    public CoverityIntegrationException() {
        super();
    }

    public CoverityIntegrationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CoverityIntegrationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CoverityIntegrationException(final String message) {
        super(message);
    }

    public CoverityIntegrationException(final Throwable cause) {
        super(cause);
    }

}
