/*
 * coverity-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.executable;

public enum CoverityToolEnvironmentVariable implements SynopsysEnvironmentVariable {
    USER("COV_USER"),
    PASSPHRASE("COVERITY_PASSPHRASE"),
    PASSPHRASE_FILE("COVERITY_PASSPHRASE_FILE");

    private final String name;

    CoverityToolEnvironmentVariable(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
