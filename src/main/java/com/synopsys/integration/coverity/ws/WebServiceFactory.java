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
package com.synopsys.integration.coverity.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synopsys.integration.coverity.config.CoverityHttpClient;
import com.synopsys.integration.coverity.config.CoverityServerConfig;
import com.synopsys.integration.coverity.exception.CoverityIntegrationException;
import com.synopsys.integration.coverity.ws.v9.ConfigurationService;
import com.synopsys.integration.coverity.ws.v9.ConfigurationServiceService;
import com.synopsys.integration.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.synopsys.integration.coverity.ws.v9.DefectService;
import com.synopsys.integration.coverity.ws.v9.DefectServiceService;
import com.synopsys.integration.coverity.ws.view.ViewService;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.rest.RestConstants;
import com.synopsys.integration.rest.credentials.Credentials;
import com.synopsys.integration.util.IntEnvironmentVariables;

public class WebServiceFactory {
    public static final String COVERITY_V9_NAMESPACE = "http://ws.coverity.com/v9";
    public static final String DEFECT_SERVICE_V9_WSDL = "/ws/v9/defectservice?wsdl";
    public static final String CONFIGURATION_SERVICE_V9_WSDL = "/ws/v9/configurationservice?wsdl";

    private final CoverityServerConfig coverityServerConfig;
    private final CoverityHttpClient coverityHttpClient;
    private final IntLogger logger;
    private final Gson gson;
    private final IntEnvironmentVariables intEnvironmentVariables;

    public WebServiceFactory(IntEnvironmentVariables intEnvironmentVariables, Gson gson, CoverityServerConfig coverityServerConfig, CoverityHttpClient coverityHttpClient, IntLogger logger) {
        this.intEnvironmentVariables = intEnvironmentVariables;
        this.gson = gson;
        this.coverityHttpClient = coverityHttpClient;
        this.coverityServerConfig = coverityServerConfig;
        this.logger = logger;
    }

    public static Gson createDefaultGson() {
        return WebServiceFactory.createDefaultGsonBuilder().create();
    }

    public static GsonBuilder createDefaultGsonBuilder() {
        return new GsonBuilder().setDateFormat(RestConstants.JSON_DATE_FORMAT);
    }

    public DefectService createDefectService() throws MalformedURLException {
        final DefectServiceService defectServiceService = new DefectServiceService(
            new URL(coverityHttpClient.getBaseUrl() + DEFECT_SERVICE_V9_WSDL),
            new QName(COVERITY_V9_NAMESPACE, "DefectServiceService"));

        final DefectService defectService = defectServiceService.getDefectServicePort();
        attachAuthenticationHandler((BindingProvider) defectService);

        return defectService;
    }

    public DefectServiceWrapper createDefectServiceWrapper() throws MalformedURLException {
        return new DefectServiceWrapper(logger, createDefectService());
    }

    public ConfigurationService createConfigurationService() throws MalformedURLException {
        final ConfigurationServiceService configurationServiceService = new ConfigurationServiceService(
            new URL(coverityHttpClient.getBaseUrl() + CONFIGURATION_SERVICE_V9_WSDL),
            new QName(COVERITY_V9_NAMESPACE, "ConfigurationServiceService"));

        final ConfigurationService configurationService = configurationServiceService.getConfigurationServicePort();
        attachAuthenticationHandler((BindingProvider) configurationService);

        return configurationService;
    }

    public ViewService createViewService() {
        return new ViewService(logger, coverityHttpClient, gson);
    }

    public void connect() throws MalformedURLException, CoverityIntegrationException {
        final ConfigurationService configurationService = createConfigurationService();
        try {
            final String username = coverityServerConfig.getCredentials().flatMap(Credentials::getUsername).orElse(null);
            configurationService.getUser(username);
        } catch (SOAPFaultException | CovRemoteServiceException_Exception e) {
            if (StringUtils.isNotBlank(e.getMessage())) {
                throw new CoverityIntegrationException(e.getMessage(), e);
            }
            throw new CoverityIntegrationException("An unexpected error occurred.", e);
        }
    }

    public IntLogger getLogger() {
        return logger;
    }

    public CoverityHttpClient getCoverityHttpClient() {
        return coverityHttpClient;
    }

    public Gson getGson() {
        return gson;
    }

    public IntEnvironmentVariables getEnvironmentVariables() {
        return intEnvironmentVariables;
    }

    private void attachAuthenticationHandler(final BindingProvider service) {
        final String username = coverityServerConfig.getCredentials().flatMap(Credentials::getUsername).orElse(null);
        final String password = coverityServerConfig.getCredentials().flatMap(Credentials::getPassword).orElse(null);
        service.getBinding().setHandlerChain(Collections.singletonList(new ClientAuthenticationHandlerWSS(username, password)));
    }

}
