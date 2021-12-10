/*
 * coverity-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
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
import com.synopsys.integration.rest.response.Response;
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
        return new CoverityHttpClient(logger, gson, timeoutInSeconds, trustCert, proxyInfo, getCoverityUrl().toString(), authenticationSupport, getCredentials().orElse(null));
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
