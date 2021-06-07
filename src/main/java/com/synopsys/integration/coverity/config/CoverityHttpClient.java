/*
 * coverity-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.config;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpUriRequest;

import com.synopsys.integration.coverity.ws.view.ViewService;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.rest.HttpMethod;
import com.synopsys.integration.rest.HttpUrl;
import com.synopsys.integration.rest.client.AuthenticatingIntHttpClient;
import com.synopsys.integration.rest.credentials.Credentials;
import com.synopsys.integration.rest.proxy.ProxyInfo;
import com.synopsys.integration.rest.response.Response;
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
    public boolean isAlreadyAuthenticated(HttpUriRequest request) {
        return validCredentials;
    }

    @Override
    public Response attemptAuthentication() throws IntegrationException {
        // Since the REST API for CIM is very narrow (consisting of only the ViewService) we attempt authentication simply by hitting an arbitrary endpoint that requires authentication. --rotte MAY 2020
        return authenticationSupport.attemptAuthentication(this, new HttpUrl(getBaseUrl()).appendRelativeUrl(ViewService.VIEWS_LINK), this.createRequestBuilder(HttpMethod.GET));
    }

    @Override
    protected void completeAuthenticationRequest(HttpUriRequest request, Response response) {
        validCredentials = response.isStatusCodeSuccess();
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
