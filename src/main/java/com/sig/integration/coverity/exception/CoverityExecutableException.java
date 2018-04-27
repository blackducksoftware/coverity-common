package com.sig.integration.coverity.exception;

public class CoverityExecutableException extends CoverityIntegrationException {

    public CoverityExecutableException() {
        super();
    }

    public CoverityExecutableException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CoverityExecutableException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CoverityExecutableException(final String message) {
        super(message);
    }

    public CoverityExecutableException(final Throwable cause) {
        super(cause);
    }

}
