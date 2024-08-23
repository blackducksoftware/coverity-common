/*
 * coverity-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.ws.view;

import com.synopsys.integration.coverity.api.rest.ViewContents;

// TODO: When paging is implemented correctly for the ViewService, this should be the return type of a helper method to bundle the ui url with the programmatic ViewContents
// --rotte MAY 2020
public class ViewReportWrapper {
    private final ViewContents viewContents;
    private final String viewReportUrl;

    public ViewReportWrapper(final ViewContents viewContents, final String viewReportUrl) {
        this.viewContents = viewContents;
        this.viewReportUrl = viewReportUrl;
    }

    public ViewContents getViewContents() {
        return viewContents;
    }

    public String getViewReportUrl() {
        return viewReportUrl;
    }
}
