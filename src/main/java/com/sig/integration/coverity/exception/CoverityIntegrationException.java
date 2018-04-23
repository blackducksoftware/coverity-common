package com.sig.integration.coverity.exception;

import com.blackducksoftware.integration.exception.IntegrationException;

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
