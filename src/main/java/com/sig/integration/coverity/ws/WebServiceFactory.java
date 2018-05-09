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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import com.sig.integration.coverity.ws.v9.ConfigurationService;
import com.sig.integration.coverity.ws.v9.ConfigurationServiceService;
import com.sig.integration.coverity.ws.v9.DefectService;
import com.sig.integration.coverity.ws.v9.DefectServiceService;

public class WebServiceFactory {
    public static final String COVERITY_V9_NAMESPACE = "http://ws.coverity.com/v9";
    public static final String DEFECT_SERVICE_V9_WSDL = "/ws/v9/defectservice?wsdl";
    public static final String CONFIGURATION_SERVICE_V9_WSDL = "/ws/v9/configurationservice?wsdl";

    protected DefectService createDefectService(URL url, String username, String password) throws MalformedURLException {
        DefectServiceService defectServiceService = new DefectServiceService(
                new URL(url, DEFECT_SERVICE_V9_WSDL),
                new QName(COVERITY_V9_NAMESPACE, "DefectServiceService"));

        DefectService defectService = defectServiceService.getDefectServicePort();
        attachAuthenticationHandler((BindingProvider) defectService, username, password);

        return defectService;
    }

    protected ConfigurationService createConfigurationService(URL url, String username, String password) throws MalformedURLException {
        ConfigurationServiceService configurationServiceService = new ConfigurationServiceService(
                new URL(url, CONFIGURATION_SERVICE_V9_WSDL),
                new QName(COVERITY_V9_NAMESPACE, "ConfigurationServiceService"));

        ConfigurationService configurationService = configurationServiceService.getConfigurationServicePort();
        attachAuthenticationHandler((BindingProvider) configurationService, username, password);

        return configurationService;
    }

    public CheckWsResponse getCheckWsdlURLResponse(URL baseUrl) {
        URL url = null;
        try {
            url = new URL(baseUrl, WebServiceFactory.CONFIGURATION_SERVICE_V9_WSDL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            conn.getInputStream();
            return new CheckWsResponse(conn.getResponseCode(), conn.getResponseMessage());
        } catch (MalformedURLException e) {
            return new CheckWsResponse(-1, e.getClass().getSimpleName() + ": " + e.getMessage());
        } catch (FileNotFoundException e) {
            return new CheckWsResponse(404, "URL '" + url + "' not found");
        } catch (IOException e) {
            return new CheckWsResponse(-1, e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void attachAuthenticationHandler(BindingProvider service, String username, String password) {
        service.getBinding().setHandlerChain(Arrays.<Handler>asList(new ClientAuthenticationHandlerWSS(
                username, password)));
    }

    public static class CheckWsResponse {
        private final int responseCode;
        private final String responseMessage;

        public CheckWsResponse(int responseCode, String responseMessage) {
            this.responseCode = responseCode;
            this.responseMessage = responseMessage;
        }

        public int getResponseCode() {
            return responseCode;
        }

        @Override
        public String toString() {
            return "Check Coverity Web Service Response: {" +
                           " Code=" + responseCode +
                           ", Message=\"" + responseMessage + "\" " +
                           '}';
        }
    }
}
