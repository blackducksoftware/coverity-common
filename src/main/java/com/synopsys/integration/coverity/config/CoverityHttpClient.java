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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpUriRequest;

import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.rest.HttpMethod;
import com.synopsys.integration.rest.client.AuthenticatingIntHttpClient;
import com.synopsys.integration.rest.credentials.Credentials;
import com.synopsys.integration.rest.proxy.ProxyInfo;
import com.synopsys.integration.rest.request.Response;
import com.synopsys.integration.rest.support.AuthenticationSupport;

public class CoverityHttpClient extends AuthenticatingIntHttpClient {
    private final String baseUrl;
    private final Credentials credentials;
    private final AuthenticationSupport authenticationSupport;
    private Boolean validCredentials;

    public CoverityHttpClient(IntLogger logger, int timeout, boolean alwaysTrustServerCertificate, ProxyInfo proxyInfo, String baseUrl, AuthenticationSupport authenticationSupport, Credentials credentials) {
        super(logger, timeout, alwaysTrustServerCertificate, proxyInfo);
        this.baseUrl = baseUrl;
        this.credentials = credentials;
        this.authenticationSupport = authenticationSupport;
        this.validCredentials = false;

        if (StringUtils.isBlank(baseUrl)) {
            throw new IllegalArgumentException("No base url was provided.");
        } else {
            try {
                URL url = new URL(baseUrl);
                url.toURI();
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("The provided base url is not a valid java.net.URL.", e);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("The provided base url is not a valid java.net.URI.", e);
            }
        }

        if (credentials == null) {
            throw new IllegalArgumentException("Credentials cannot be null.");
        }

        String authHeader = constructBasicAuthorizationHeader();
        if (StringUtils.isNotBlank(authHeader)) {
            addCommonRequestHeader(AuthenticationSupport.AUTHORIZATION_HEADER, authHeader);
        }
    }

    @Override
    public boolean isAlreadyAuthenticated(final HttpUriRequest request) {
        return validCredentials;
    }

    @Override
    public Response attemptAuthentication() throws IntegrationException {
        return authenticationSupport.attemptAuthentication(this, getBaseUrl() + "/", "login", this.createRequestBuilder(HttpMethod.GET));
    }

    @Override
    protected void completeAuthenticationRequest(final HttpUriRequest request, final Response response) {
        validCredentials = response.isStatusCodeOkay();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    private String constructBasicAuthorizationHeader() {
        String username = credentials.getUsername().orElse(null);
        String password = credentials.getPassword().orElse(null);
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
            return "Basic " + new String(encodedAuth);
        }

        return StringUtils.EMPTY;
    }
}
