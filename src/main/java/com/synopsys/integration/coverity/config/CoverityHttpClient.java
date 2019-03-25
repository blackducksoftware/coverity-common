/**
 * coverity-common
 *
 * Copyright (C) 2019 Black Duck Software, Inc.
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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;

import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.rest.RestConstants;
import com.synopsys.integration.rest.client.AuthenticatingIntHttpClient;
import com.synopsys.integration.rest.credentials.Credentials;
import com.synopsys.integration.rest.proxy.ProxyInfo;
import com.synopsys.integration.rest.request.Response;
import com.synopsys.integration.rest.support.AuthenticationSupport;

public class CoverityHttpClient extends AuthenticatingIntHttpClient {
    private final String baseUrl;
    private final Credentials credentials;
    private final AuthenticationSupport authenticationSupport;

    public CoverityHttpClient(IntLogger logger, int timeout, boolean alwaysTrustServerCertificate, ProxyInfo proxyInfo, String baseUrl, AuthenticationSupport authenticationSupport, Credentials credentials) {
        super(logger, timeout, alwaysTrustServerCertificate, proxyInfo);
        this.baseUrl = baseUrl;
        this.credentials = credentials;
        this.authenticationSupport = authenticationSupport;

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
    }

    @Override
    public boolean isAlreadyAuthenticated(HttpUriRequest request) {
        return request.containsHeader(RestConstants.X_CSRF_TOKEN);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public Response attemptAuthentication() throws IntegrationException {
        List<NameValuePair> bodyValues = new ArrayList<>();
        bodyValues.add(new BasicNameValuePair("username", credentials.getUsername().orElse(null)));
        bodyValues.add(new BasicNameValuePair("password", credentials.getPassword().orElse(null)));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(bodyValues, Charsets.UTF_8);

        return authenticationSupport.attemptAuthentication(this, baseUrl, "login", entity);
    }

    @Override
    protected void completeAuthenticationRequest(final HttpUriRequest request, final Response response) throws IntegrationException {
        if (response.isStatusCodeOkay()) {
            CloseableHttpResponse actualResponse = response.getActualResponse();
            Header csrfHeader = actualResponse.getFirstHeader(RestConstants.X_CSRF_TOKEN);
            String csrfHeaderValue = csrfHeader.getValue();
            if (null != csrfHeaderValue) {
                authenticationSupport.addAuthenticationHeader(this, request, RestConstants.X_CSRF_TOKEN, csrfHeaderValue);
            } else {
                logger.error("No CSRF token found when authenticating.");
            }
        }
    }
}
