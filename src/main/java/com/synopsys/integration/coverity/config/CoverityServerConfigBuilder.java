/**
 * coverity-common
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.integration.coverity.config;

import java.net.MalformedURLException;
import java.net.URL;

import com.synopsys.integration.builder.AbstractBuilder;
import com.synopsys.integration.rest.credentials.CredentialsBuilder;

public class CoverityServerConfigBuilder extends AbstractBuilder<CoverityServerConfig> {
    private final CoverityServerConfigValidator coverityServerConfigValidator;
    private String url;
    private String username;
    private String password;

    public CoverityServerConfigBuilder(final CoverityServerConfigValidator coverityServerConfigValidator) {
        this.coverityServerConfigValidator = coverityServerConfigValidator;
    }

    public CoverityServerConfigBuilder() {
        this.coverityServerConfigValidator = new CoverityServerConfigValidator();
    }

    @Override
    public CoverityServerConfigValidator createValidator() {
        coverityServerConfigValidator.setUrl(url);
        coverityServerConfigValidator.setUsername(username);
        coverityServerConfigValidator.setPassword(password);
        return coverityServerConfigValidator;
    }

    @Override
    public CoverityServerConfig buildObject() {
        URL uRL = null;
        try {
            uRL = new URL(url);
        } catch (final MalformedURLException e) {
        }
        final CredentialsBuilder credentialsBuilder = new CredentialsBuilder();
        credentialsBuilder.setUsername(username);
        credentialsBuilder.setPassword(password);
        return new CoverityServerConfig(uRL, credentialsBuilder.buildObject());
    }

    public CoverityServerConfigBuilder url(final String url) {
        this.url = url;
        return this;
    }

    public CoverityServerConfigBuilder username(final String username) {
        this.username = username;
        return this;
    }

    public CoverityServerConfigBuilder password(final String password) {
        this.password = password;
        return this;
    }

}
