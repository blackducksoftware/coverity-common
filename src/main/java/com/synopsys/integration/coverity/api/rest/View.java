/*
 * coverity-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.api.rest;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class View {
    @SerializedName("id")
    public long id;
    @SerializedName("type")
    public String type;
    @SerializedName("name")
    public String name;
    @SerializedName("groupBy")
    public boolean groupBy;
    @SerializedName("columns")
    public List<ViewColumn> columns;
}
