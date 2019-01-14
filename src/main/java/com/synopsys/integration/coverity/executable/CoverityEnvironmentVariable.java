package com.synopsys.integration.coverity.executable;

public enum CoverityEnvironmentVariable {
    HOST("COV_HOST"),
    PORT("COV_PORT"),
    USERNAME("COV_USER"),
    PASSWORD("COV_PASS"),
    STREAM("COV_STREAM");

    private final String name;

    CoverityEnvironmentVariable(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
