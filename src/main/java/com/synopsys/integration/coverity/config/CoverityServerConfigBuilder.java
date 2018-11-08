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
import java.net.URISyntaxException;
import java.net.URL;

import com.synopsys.integration.coverity.CoverityServerVerifier;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.rest.credentials.Credentials;
import com.synopsys.integration.rest.proxy.ProxyInfo;
import com.synopsys.integration.util.BuilderStatus;
import com.synopsys.integration.util.IntegrationBuilder;

public class CoverityServerConfigBuilder extends IntegrationBuilder<CoverityServerConfig> {
    private final CoverityServerVerifier coverityServerVerifier;
    private String url;
    private Credentials credentials;
    private ProxyInfo proxyInfo;

    public CoverityServerConfigBuilder(final CoverityServerVerifier coverityServerVerifier) {
        this.coverityServerVerifier = coverityServerVerifier;
    }

    public CoverityServerConfigBuilder() {
        this.coverityServerVerifier = new CoverityServerVerifier();
    }

    @Override
    protected CoverityServerConfig buildWithoutValidation() {
        URL url = null;
        try {
            url = new URL(this.url);
        } catch (final MalformedURLException ignore) {
            // Skipping validation
        }

        return new CoverityServerConfig(url, credentials, proxyInfo);
    }

    @Override
    protected void validate(final BuilderStatus builderStatus) {
        if (credentials == null) {
            builderStatus.addErrorMessage("No Coverity credentials set.");
            return;
        }

        if (proxyInfo == null) {
            builderStatus.addErrorMessage("No Coverity ProxyInfo set.");
            return;
        }

        if (url == null) {
            builderStatus.addErrorMessage("No Coverity URL set.");
            return;
        }

        final URL validUrl;
        try {
            validUrl = new URL(url);
            validUrl.toURI();
        } catch (final MalformedURLException | URISyntaxException e) {
            builderStatus.addErrorMessage("The Coverity URL is not a valid URL. " + e.getMessage());
            return;
        }

        try {
            coverityServerVerifier.verifyIsCoverityServer(validUrl);
        } catch (final IntegrationException e) {
            builderStatus.addErrorMessage(e.getMessage());
        }
    }

    public CoverityServerConfigBuilder url(final String url) {
        this.url = url;
        return this;
    }

    public CoverityServerConfigBuilder credentails(final Credentials credentials) {
        this.credentials = credentials;
        return this;
    }

    public CoverityServerConfigBuilder proxyInfo(final ProxyInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
        return this;
    }
}
