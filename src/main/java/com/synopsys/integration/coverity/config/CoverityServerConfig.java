/**
 * coverity-common
 *
 * Copyright (c) 2019 Synopsys, Inc.
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

import java.net.URL;
import java.util.Optional;

import com.google.gson.Gson;
import com.synopsys.integration.builder.Buildable;
import com.synopsys.integration.coverity.ws.WebServiceFactory;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.log.SilentIntLogger;
import com.synopsys.integration.rest.client.ConnectionResult;
import com.synopsys.integration.rest.credentials.Credentials;
import com.synopsys.integration.rest.proxy.ProxyInfo;
import com.synopsys.integration.rest.request.Response;
import com.synopsys.integration.rest.support.AuthenticationSupport;
import com.synopsys.integration.util.IntEnvironmentVariables;
import com.synopsys.integration.util.Stringable;

public class CoverityServerConfig extends Stringable implements Buildable {
    private final URL coverityUrl;
    private final Credentials credentials;
    private final IntEnvironmentVariables intEnvironmentVariables;
    private final Gson gson;
    private final AuthenticationSupport authenticationSupport;
    private final boolean trustCert;
    private final ProxyInfo proxyInfo;
    private final int timeoutInSeconds;

    CoverityServerConfig(URL url, int timeoutInSeconds, Credentials credentials, ProxyInfo proxyInfo, boolean trustCert, IntEnvironmentVariables intEnvironmentVariables, Gson gson, AuthenticationSupport authenticationSupport) {
        this.credentials = credentials;
        this.coverityUrl = url;
        this.timeoutInSeconds = timeoutInSeconds;
        this.proxyInfo = proxyInfo;
        this.trustCert = trustCert;
        this.intEnvironmentVariables = intEnvironmentVariables;
        this.gson = gson;
        this.authenticationSupport = authenticationSupport;
    }

    public static CoverityServerConfigBuilder newBuilder() {
        return new CoverityServerConfigBuilder();
    }

    public boolean canConnect() {
        return canConnect(new SilentIntLogger());
    }

    public boolean canConnect(IntLogger logger) {
        ConnectionResult connectionResult = attemptConnection(logger);
        return connectionResult.isSuccess();
    }

    public ConnectionResult attemptConnection(IntLogger logger) {
        String errorMessage = null;
        int httpStatusCode = 0;
        Exception e = null;

        try {
            CoverityHttpClient coverityHttpClient = createCoverityHttpClient(logger);
            try (Response response = coverityHttpClient.attemptAuthentication()) {
                // if you get an error response, you know that a connection could not be made
                httpStatusCode = response.getStatusCode();
                if (response.isStatusCodeError()) {
                    errorMessage = response.getContentString();
                }
            }
        } catch (Exception ex) {
            e = ex;
            errorMessage = ex.getMessage();
        }

        if (null != errorMessage) {
            logger.error(errorMessage);
            return ConnectionResult.FAILURE(httpStatusCode, errorMessage, e);
        }

        logger.info("A successful connection was made.");
        return ConnectionResult.SUCCESS(httpStatusCode);
    }

    public WebServiceFactory createWebServiceFactory(IntLogger logger) {
        CoverityHttpClient httpClient = createCoverityHttpClient(logger);
        return new WebServiceFactory(intEnvironmentVariables, gson, this, httpClient, logger);
    }

    public CoverityHttpClient createCoverityHttpClient(IntLogger logger) {
        return new CoverityHttpClient(logger, timeoutInSeconds, trustCert, proxyInfo, getCoverityUrl().toString(), authenticationSupport, getCredentials().orElse(null));
    }

    public URL getCoverityUrl() {
        return coverityUrl;
    }

    public Optional<Credentials> getCredentials() {
        return Optional.ofNullable(credentials);
    }

    public int getTimeoutInSeconds() {
        return timeoutInSeconds;
    }

}
