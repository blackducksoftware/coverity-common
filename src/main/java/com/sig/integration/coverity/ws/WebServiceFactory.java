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
package com.sig.integration.coverity.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import com.sig.integration.coverity.config.CoverityServerConfig;
import com.sig.integration.coverity.ws.v9.ConfigurationService;
import com.sig.integration.coverity.ws.v9.ConfigurationServiceService;
import com.sig.integration.coverity.ws.v9.DefectService;
import com.sig.integration.coverity.ws.v9.DefectServiceService;

public class WebServiceFactory {
    public static final String COVERITY_V9_NAMESPACE = "http://ws.coverity.com/v9";
    public static final String DEFECT_SERVICE_V9_WSDL = "/ws/v9/defectservice?wsdl";
    public static final String CONFIGURATION_SERVICE_V9_WSDL = "/ws/v9/configurationservice?wsdl";

    private final CoverityServerConfig coverityServerConfig;

    public WebServiceFactory(CoverityServerConfig coverityServerConfig) {
        this.coverityServerConfig = coverityServerConfig;
    }

    public CoverityServerConfig getCoverityServerConfig() {
        return coverityServerConfig;
    }

    public DefectService createDefectService() throws MalformedURLException {
        DefectServiceService defectServiceService = new DefectServiceService(
                new URL(coverityServerConfig.getUrl(), DEFECT_SERVICE_V9_WSDL),
                new QName(COVERITY_V9_NAMESPACE, "DefectServiceService"));

        DefectService defectService = defectServiceService.getDefectServicePort();
        attachAuthenticationHandler((BindingProvider) defectService, coverityServerConfig.getUsername(), coverityServerConfig.getPassword());

        return defectService;
    }

    public ConfigurationService createConfigurationService() throws MalformedURLException {
        ConfigurationServiceService configurationServiceService = new ConfigurationServiceService(
                new URL(coverityServerConfig.getUrl(), CONFIGURATION_SERVICE_V9_WSDL),
                new QName(COVERITY_V9_NAMESPACE, "ConfigurationServiceService"));

        ConfigurationService configurationService = configurationServiceService.getConfigurationServicePort();
        attachAuthenticationHandler((BindingProvider) configurationService, coverityServerConfig.getUsername(), coverityServerConfig.getPassword());

        return configurationService;
    }

    private void attachAuthenticationHandler(BindingProvider service, String username, String password) {
        service.getBinding().setHandlerChain(Arrays.<Handler>asList(new ClientAuthenticationHandlerWSS(
                username, password)));
    }

}
