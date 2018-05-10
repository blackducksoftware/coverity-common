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
package com.sig.integration.coverity;

import java.io.IOException;
import java.net.URL;

import com.blackducksoftware.integration.exception.IntegrationException;
import com.blackducksoftware.integration.hub.request.Request;
import com.blackducksoftware.integration.hub.request.Response;
import com.blackducksoftware.integration.hub.rest.UnauthenticatedRestConnection;
import com.blackducksoftware.integration.hub.rest.UnauthenticatedRestConnectionBuilder;
import com.blackducksoftware.integration.hub.rest.UriCombiner;
import com.blackducksoftware.integration.hub.rest.exception.IntegrationRestException;
import com.blackducksoftware.integration.log.LogLevel;
import com.blackducksoftware.integration.log.PrintStreamIntLogger;
import com.sig.integration.coverity.exception.CoverityIntegrationException;
import com.sig.integration.coverity.ws.WebServiceFactory;

public class CoverityServerVerifier {

    private final UriCombiner uriCombiner;

    public CoverityServerVerifier(final UriCombiner uriCombiner) {
        this.uriCombiner = uriCombiner;
    }

    public void verifyIsCoverityServer(final URL coverityURL,
            final int timeoutSeconds) throws IntegrationException {

        final UnauthenticatedRestConnectionBuilder connectionBuilder = new UnauthenticatedRestConnectionBuilder();
        connectionBuilder.setLogger(new PrintStreamIntLogger(System.out, LogLevel.INFO));
        connectionBuilder.setBaseUrl(coverityURL.toString());
        connectionBuilder.setTimeout(timeoutSeconds);
        connectionBuilder.setAlwaysTrustServerCertificate(true);
        final UnauthenticatedRestConnection restConnection = connectionBuilder.build();

        String wsdlURI = uriCombiner.pieceTogetherUri(coverityURL, WebServiceFactory.CONFIGURATION_SERVICE_V9_WSDL);
        Request request = new Request.Builder(wsdlURI).build();
        try (Response response = restConnection.executeRequest(request)) {

        } catch (final IntegrationRestException e) {
            throw new CoverityIntegrationException("The Url does not appear to be a Coverity server :" + wsdlURI + ", because: " + e.getHttpStatusCode() + " : " + e.getHttpStatusMessage(), e);
        } catch (final IntegrationException e) {
            throw new CoverityIntegrationException("The Url does not appear to be a Coverity server :" + wsdlURI + ", because: " + e.getMessage(), e);
        } catch (final IOException e) {
            throw new CoverityIntegrationException(e.getMessage(), e);
        }
    }
}
