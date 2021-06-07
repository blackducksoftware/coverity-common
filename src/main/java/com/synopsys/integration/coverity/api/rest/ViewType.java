/*
 * coverity-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.api.rest;

public enum ViewType {
    ISSUES("issues"),
    ISSUES_BY_PROJECT("issuesByProject"),
    FUNCTIONS("functions"),
    FILES("files"),
    COMPONENTS("components"),
    CHECKERS("checkers"),
    OWNERS("owners"),
    TESTS("tests"),
    TRENDS("trends"),
    SNAPSHOTS("snapshots");

    private final String camelCaseName;

    ViewType(String camelCaseName) {
        this.camelCaseName = camelCaseName;
    }

    @Override
    public String toString() {
        return camelCaseName;
    }
}
