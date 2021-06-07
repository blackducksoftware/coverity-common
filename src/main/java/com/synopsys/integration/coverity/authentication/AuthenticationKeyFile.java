/*
 * coverity-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.authentication;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class AuthenticationKeyFile {
    @SerializedName("key")
    public String key;
    @SerializedName("id")
    public Long id;
    @SerializedName("type")
    public String type;
    @SerializedName("username")
    public String username;
    @SerializedName("comments")
    public Map<String, String> comments;
    @SerializedName("domain")
    public String domain;
    @SerializedName("version")
    public Integer version;
}
