package com.sig.integration.coverity.exception;

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
