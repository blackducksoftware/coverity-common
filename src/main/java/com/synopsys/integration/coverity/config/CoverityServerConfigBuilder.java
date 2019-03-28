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
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.synopsys.integration.builder.BuilderProperties;
import com.synopsys.integration.builder.BuilderPropertyKey;
import com.synopsys.integration.builder.BuilderStatus;
import com.synopsys.integration.builder.IntegrationBuilder;
import com.synopsys.integration.coverity.CoverityServerVerifier;
import com.synopsys.integration.coverity.ws.WebServiceFactory;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.log.LogLevel;
import com.synopsys.integration.log.PrintStreamIntLogger;
import com.synopsys.integration.rest.credentials.Credentials;
import com.synopsys.integration.rest.credentials.CredentialsBuilder;
import com.synopsys.integration.rest.exception.IntegrationCertificateException;
import com.synopsys.integration.rest.proxy.ProxyInfo;
import com.synopsys.integration.rest.support.AuthenticationSupport;
import com.synopsys.integration.util.IntEnvironmentVariables;
import com.synopsys.integration.util.NoThreadExecutorService;

public class CoverityServerConfigBuilder extends IntegrationBuilder<CoverityServerConfig> {
    public static final BuilderPropertyKey URL_KEY = new BuilderPropertyKey("COVERITY_URL");
    public static final BuilderPropertyKey USERNAME_KEY = new BuilderPropertyKey("COVERITY_USERNAME");
    public static final BuilderPropertyKey PASSWORD_KEY = new BuilderPropertyKey("COVERITY_PASSWORD");

    private final BuilderProperties builderProperties;
    private IntLogger logger = new PrintStreamIntLogger(System.out, LogLevel.INFO);
    private IntEnvironmentVariables intEnvironmentVariables = new IntEnvironmentVariables();
    private Gson gson = WebServiceFactory.createDefaultGson();
    private AuthenticationSupport authenticationSupport = new AuthenticationSupport();
    private ExecutorService executorService = new NoThreadExecutorService();

    public CoverityServerConfigBuilder() {
        Set<BuilderPropertyKey> propertyKeys = new HashSet<>();
        propertyKeys.add(URL_KEY);
        propertyKeys.add(USERNAME_KEY);
        propertyKeys.add(PASSWORD_KEY);
        builderProperties = new BuilderProperties(propertyKeys);
    }

    @Override
    public CoverityServerConfig build() {
        try {
            return super.build();
        } catch (Exception e) {
            if (!e.getMessage().contains("SunCertPathBuilderException")) {
                throw e;
            }
            throw new IntegrationCertificateException(String.format("Please import the certificate for %s into your Java keystore.", getUrl()), e);
        }
    }

    @Override
    protected CoverityServerConfig buildWithoutValidation() {
        URL coverityUrl = null;
        try {
            coverityUrl = new URL(getUrl());
        } catch (MalformedURLException e) {
        }

        String username = getUsername();
        String password = getPassword();
        CredentialsBuilder credentialsBuilder = Credentials.newBuilder();
        credentialsBuilder.setUsernameAndPassword(username, password);
        Credentials credentials = credentialsBuilder.build();

        return new CoverityServerConfig(coverityUrl, 120, credentials, ProxyInfo.NO_PROXY_INFO, false, intEnvironmentVariables, gson, authenticationSupport, executorService);
    }

    @Override
    protected void validate(final BuilderStatus builderStatus) {
        CredentialsBuilder credentialsBuilder = Credentials.newBuilder();
        credentialsBuilder.setUsernameAndPassword(getUsername(), getPassword());
        BuilderStatus credentialsBuilderStatus = credentialsBuilder.validateAndGetBuilderStatus();
        if (!credentialsBuilderStatus.isValid()) {
            builderStatus.addAllErrorMessages(credentialsBuilderStatus.getErrorMessages());
        } else {
            Credentials credentials = credentialsBuilder.build();
            if (credentials.isBlank()) {
                builderStatus.addErrorMessage("Username and password must be specified.");
            }
        }

        if (StringUtils.isBlank(getUrl())) {
            builderStatus.addErrorMessage("The Coverity url must be specified.");
        } else {
            try {
                URL coverityUrl = new URL(getUrl());
                coverityUrl.toURI();
                CoverityServerVerifier.verifyIsCoverityServer(coverityUrl);
            } catch (MalformedURLException | URISyntaxException e) {
                builderStatus.addErrorMessage(String.format("The provided Coverity url (%s) is not a valid URL.", getUrl()));
            } catch (final IntegrationException e) {
                builderStatus.addErrorMessage(e.getMessage());
            }
        }
    }

    public Set<BuilderPropertyKey> getKeys() {
        return builderProperties.getKeys();
    }

    public Set<String> getPropertyKeys() {
        return builderProperties.getPropertyKeys();
    }

    public Set<String> getEnvironmentVariableKeys() {
        return builderProperties.getEnvironmentVariableKeys();
    }

    public Map<BuilderPropertyKey, String> getProperties() {
        return builderProperties.getProperties();
    }

    public void setProperties(final Set<? extends Map.Entry<String, String>> propertyEntries) {
        builderProperties.setProperties(propertyEntries);
    }

    public void setProperty(final String key, final String value) {
        builderProperties.setProperty(key, value);
    }

    public String getUrl() {
        return builderProperties.get(URL_KEY);
    }

    public CoverityServerConfigBuilder setUrl(String url) {
        String sanitizedUrl = url;
        if (url.endsWith("/")) {
            sanitizedUrl = url.substring(0, url.length() - 1);
        }

        builderProperties.set(URL_KEY, sanitizedUrl);
        return this;
    }

    public CoverityServerConfigBuilder setCredentials(Credentials credentials) {
        builderProperties.set(USERNAME_KEY, credentials.getUsername().orElse(null));
        builderProperties.set(PASSWORD_KEY, credentials.getPassword().orElse(null));
        return this;
    }

    public String getUsername() {
        return builderProperties.get(USERNAME_KEY);
    }

    public CoverityServerConfigBuilder setUsername(String username) {
        builderProperties.set(USERNAME_KEY, username);
        return this;
    }

    public String getPassword() {
        return builderProperties.get(PASSWORD_KEY);
    }

    public CoverityServerConfigBuilder setPassword(String password) {
        builderProperties.set(PASSWORD_KEY, password);
        return this;
    }

    public IntLogger getLogger() {
        return logger;
    }

    public CoverityServerConfigBuilder setLogger(IntLogger logger) {
        if (null != logger) {
            this.logger = logger;
        }
        return this;
    }

    public Optional<ExecutorService> getExecutorService() {
        return Optional.ofNullable(executorService);
    }

    public CoverityServerConfigBuilder setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    public AuthenticationSupport getAuthenticationSupport() {
        return authenticationSupport;
    }

    public CoverityServerConfigBuilder setAuthenticationSupport(AuthenticationSupport authenticationSupport) {
        if (null != authenticationSupport) {
            this.authenticationSupport = authenticationSupport;
        }
        return this;
    }

    public IntEnvironmentVariables getIntEnvironmentVariables() {
        return intEnvironmentVariables;
    }

    public CoverityServerConfigBuilder setIntEnvironmentVariables(IntEnvironmentVariables intEnvironmentVariables) {
        if (null != intEnvironmentVariables) {
            this.intEnvironmentVariables = intEnvironmentVariables;
        }
        return this;
    }

}
