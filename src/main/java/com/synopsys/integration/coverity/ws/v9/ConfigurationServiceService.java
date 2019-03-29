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
package com.synopsys.integration.coverity.ws.v9;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;

/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.0
 */
@WebServiceClient(name = "ConfigurationServiceService", targetNamespace = "http://ws.coverity.com/v9", wsdlLocation = "http://frossi-wrkst:8081/ws/v9/configurationservice?wsdl")
public class ConfigurationServiceService
        extends Service {

    private final static URL CONFIGURATIONSERVICESERVICE_WSDL_LOCATION;
    private final static WebServiceException CONFIGURATIONSERVICESERVICE_EXCEPTION;
    private final static QName CONFIGURATIONSERVICESERVICE_QNAME = new QName("http://ws.coverity.com/v9", "ConfigurationServiceService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://frossi-wrkst:8081/ws/v9/configurationservice?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        CONFIGURATIONSERVICESERVICE_WSDL_LOCATION = url;
        CONFIGURATIONSERVICESERVICE_EXCEPTION = e;
    }

    public ConfigurationServiceService() {
        super(__getWsdlLocation(), CONFIGURATIONSERVICESERVICE_QNAME);
    }

    public ConfigurationServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    private static URL __getWsdlLocation() {
        if (CONFIGURATIONSERVICESERVICE_EXCEPTION != null) {
            throw CONFIGURATIONSERVICESERVICE_EXCEPTION;
        }
        return CONFIGURATIONSERVICESERVICE_WSDL_LOCATION;
    }

    /**
     * @return returns ConfigurationService
     */
    @WebEndpoint(name = "ConfigurationServicePort")
    public ConfigurationService getConfigurationServicePort() {
        return super.getPort(new QName("http://ws.coverity.com/v9", "ConfigurationServicePort"), ConfigurationService.class);
    }

}
