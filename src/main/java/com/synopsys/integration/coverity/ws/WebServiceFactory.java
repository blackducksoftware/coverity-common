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
package com.synopsys.integration.coverity.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.HttpClientBuilder;

import com.blackducksoftware.integration.exception.EncryptionException;
import com.blackducksoftware.integration.log.IntLogger;
import com.blackducksoftware.integration.phonehome.PhoneHomeCallable;
import com.blackducksoftware.integration.phonehome.PhoneHomeClient;
import com.blackducksoftware.integration.phonehome.PhoneHomeRequestBody;
import com.blackducksoftware.integration.phonehome.PhoneHomeService;
import com.blackducksoftware.integration.phonehome.google.analytics.GoogleAnalyticsConstants;
import com.blackducksoftware.integration.rest.proxy.ProxyInfo;
import com.blackducksoftware.integration.util.IntEnvironmentVariables;
import com.google.gson.Gson;
import com.synopsys.integration.coverity.config.CoverityServerConfig;
import com.synopsys.integration.coverity.exception.CoverityIntegrationException;
import com.synopsys.integration.coverity.ws.v9.ConfigurationService;
import com.synopsys.integration.coverity.ws.v9.ConfigurationServiceService;
import com.synopsys.integration.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.synopsys.integration.coverity.ws.v9.DefectService;
import com.synopsys.integration.coverity.ws.v9.DefectServiceService;
import com.synopsys.integration.coverity.ws.view.ViewService;

public class WebServiceFactory {
    public static final String COVERITY_V9_NAMESPACE = "http://ws.coverity.com/v9";
    public static final String DEFECT_SERVICE_V9_WSDL = "/ws/v9/defectservice?wsdl";
    public static final String CONFIGURATION_SERVICE_V9_WSDL = "/ws/v9/configurationservice?wsdl";

    private final CoverityServerConfig coverityServerConfig;
    private final IntLogger logger;
    private final Gson gson;
    private final IntEnvironmentVariables intEnvironmentVariables;

    public WebServiceFactory(final CoverityServerConfig coverityServerConfig, final IntLogger logger) {
        this(coverityServerConfig, logger, new Gson(), new IntEnvironmentVariables());
    }

    public WebServiceFactory(final CoverityServerConfig coverityServerConfig, final IntLogger logger, final Gson gson) {
        this(coverityServerConfig, logger, gson, new IntEnvironmentVariables());
    }

    public WebServiceFactory(final CoverityServerConfig coverityServerConfig, final IntLogger logger, final IntEnvironmentVariables intEnvironmentVariables) {
        this(coverityServerConfig, logger, new Gson(), intEnvironmentVariables);
    }

    public WebServiceFactory(final CoverityServerConfig coverityServerConfig, final IntLogger logger, final Gson gson, final IntEnvironmentVariables intEnvironmentVariables) {
        this.coverityServerConfig = coverityServerConfig;
        this.logger = logger;
        this.gson = gson;
        this.intEnvironmentVariables = intEnvironmentVariables;
    }

    public CoverityServerConfig getCoverityServerConfig() {
        return coverityServerConfig;
    }

    public IntLogger getLogger() {
        return logger;
    }

    public DefectService createDefectService() throws MalformedURLException, EncryptionException {
        final DefectServiceService defectServiceService = new DefectServiceService(
                new URL(coverityServerConfig.getUrl(), DEFECT_SERVICE_V9_WSDL),
                new QName(COVERITY_V9_NAMESPACE, "DefectServiceService"));

        final DefectService defectService = defectServiceService.getDefectServicePort();
        attachAuthenticationHandler((BindingProvider) defectService, coverityServerConfig.getUsername(), coverityServerConfig.getPassword());

        return defectService;
    }

    public DefectServiceWrapper createDefectServiceWrapper() throws MalformedURLException, EncryptionException {
        final DefectServiceWrapper defectServiceWrapper = new DefectServiceWrapper(logger, createDefectService());
        return defectServiceWrapper;
    }

    public ConfigurationService createConfigurationService() throws MalformedURLException, EncryptionException {
        final ConfigurationServiceService configurationServiceService = new ConfigurationServiceService(
                new URL(coverityServerConfig.getUrl(), CONFIGURATION_SERVICE_V9_WSDL),
                new QName(COVERITY_V9_NAMESPACE, "ConfigurationServiceService"));

        final ConfigurationService configurationService = configurationServiceService.getConfigurationServicePort();
        attachAuthenticationHandler((BindingProvider) configurationService, coverityServerConfig.getUsername(), coverityServerConfig.getPassword());

        return configurationService;
    }

    public ViewService createViewService() throws EncryptionException {
        final CredentialsRestConnection credentialsRestConnection = createCredentialsRestConnection();
        final ViewService viewService = new ViewService(logger, credentialsRestConnection, gson);
        return viewService;
    }

    public CredentialsRestConnection createCredentialsRestConnection() throws EncryptionException {
        return new CredentialsRestConnection(logger, coverityServerConfig.getUrl(), coverityServerConfig.getUsername(), coverityServerConfig.getPassword(), 300, ProxyInfo.NO_PROXY_INFO);
    }

    public void connect() throws MalformedURLException, CoverityIntegrationException, EncryptionException {
        final ConfigurationService configurationService = createConfigurationService();
        try {
            configurationService.getUser(coverityServerConfig.getUsername());
        } catch (SOAPFaultException | CovRemoteServiceException_Exception e) {
            if (StringUtils.isNotBlank(e.getMessage())) {
                throw new CoverityIntegrationException(e.getMessage(), e);
            }
            throw new CoverityIntegrationException("An unexpected error occurred.", e);
        }
    }

    public PhoneHomeService createPhoneHomeService() {
        final PhoneHomeService phoneHomeService = new PhoneHomeService(logger);
        return phoneHomeService;
    }

    public PhoneHomeService createPhoneHomeService(final ExecutorService executorService) {
        final PhoneHomeService phoneHomeService = new PhoneHomeService(logger, executorService);
        return phoneHomeService;
    }

    public PhoneHomeCallable createCoverityPhoneHomeCallable(final URL productURL, final String artifactId, final String artifactVersion) throws MalformedURLException, EncryptionException {
        final PhoneHomeCallable phoneHomeCallable = new CoverityPhoneHomeCallable(logger, createPhoneHomeClient(), createConfigurationService(), productURL, artifactId, artifactVersion, intEnvironmentVariables);
        return phoneHomeCallable;
    }

    public PhoneHomeCallable createCoverityPhoneHomeCallable(final URL productURL, final String artifactId, final String artifactVersion, final PhoneHomeRequestBody.Builder phoneHomeRequestBodyBuilder)
            throws MalformedURLException, EncryptionException {
        final PhoneHomeCallable phoneHomeCallable = new CoverityPhoneHomeCallable(logger, createPhoneHomeClient(), createConfigurationService(), productURL, artifactId, artifactVersion,
                intEnvironmentVariables, phoneHomeRequestBodyBuilder);
        return phoneHomeCallable;
    }

    public PhoneHomeClient createPhoneHomeClient() {
        final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        final String googleAnalyticsTrackingId = GoogleAnalyticsConstants.PRODUCTION_INTEGRATIONS_TRACKING_ID;
        return new PhoneHomeClient(googleAnalyticsTrackingId, logger, httpClientBuilder, gson);
    }

    private void attachAuthenticationHandler(final BindingProvider service, final String username, final String password) {
        service.getBinding().setHandlerChain(Arrays.<Handler>asList(new ClientAuthenticationHandlerWSS(
                username, password)));
    }

}
