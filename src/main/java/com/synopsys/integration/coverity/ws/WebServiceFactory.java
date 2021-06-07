/*
 * coverity-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
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
import com.synopsys.integration.coverity.api.ws.configuration.ConfigurationService;
import com.synopsys.integration.coverity.api.ws.configuration.ConfigurationServiceService;
import com.synopsys.integration.coverity.api.ws.configuration.CovRemoteServiceException_Exception;
import com.synopsys.integration.coverity.api.ws.defect.DefectService;
import com.synopsys.integration.coverity.api.ws.defect.DefectServiceService;
import com.synopsys.integration.coverity.config.CoverityHttpClient;
import com.synopsys.integration.coverity.config.CoverityServerConfig;
import com.synopsys.integration.coverity.exception.CoverityIntegrationException;
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
        DefectServiceService defectServiceService = new DefectServiceService(
            new URL(coverityHttpClient.getBaseUrl() + DEFECT_SERVICE_V9_WSDL),
            new QName(COVERITY_V9_NAMESPACE, "DefectServiceService"));

        DefectService defectService = defectServiceService.getDefectServicePort();
        attachAuthenticationHandler((BindingProvider) defectService);

        return defectService;
    }

    public DefectServiceWrapper createDefectServiceWrapper() throws MalformedURLException {
        return new DefectServiceWrapper(logger, createDefectService());
    }

    public ConfigurationService createConfigurationService() throws MalformedURLException {
        ConfigurationServiceService configurationServiceService = new ConfigurationServiceService(
            new URL(coverityHttpClient.getBaseUrl() + CONFIGURATION_SERVICE_V9_WSDL),
            new QName(COVERITY_V9_NAMESPACE, "ConfigurationServiceService"));

        ConfigurationService configurationService = configurationServiceService.getConfigurationServicePort();
        attachAuthenticationHandler((BindingProvider) configurationService);

        return configurationService;
    }

    public ConfigurationServiceWrapper createConfigurationServiceWrapper() throws MalformedURLException {
        return new ConfigurationServiceWrapper(logger, createConfigurationService(), coverityServerConfig.getTimeoutInSeconds());
    }

    public ViewService createViewService() {
        return new ViewService(logger, coverityHttpClient, gson);
    }

    public void connect() throws MalformedURLException, CoverityIntegrationException {
        ConfigurationService configurationService = createConfigurationService();
        try {
            String username = coverityServerConfig.getCredentials().flatMap(Credentials::getUsername).orElse(null);
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

    private void attachAuthenticationHandler(BindingProvider service) {
        String username = coverityServerConfig.getCredentials().flatMap(Credentials::getUsername).orElse(null);
        String password = coverityServerConfig.getCredentials().flatMap(Credentials::getPassword).orElse(null);
        service.getBinding().setHandlerChain(Collections.singletonList(new ClientAuthenticationHandlerWSS(username, password)));
    }

}
