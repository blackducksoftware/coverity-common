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
package com.synopsys.integration.coverity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.synopsys.integration.coverity.exception.CoverityIntegrationException;
import com.synopsys.integration.coverity.ws.WebServiceFactory;
import com.synopsys.integration.exception.IntegrationException;

public class CoverityServerVerifier {
    public static void verifyIsCoverityServer(final URL coverityURL) throws IntegrationException {
        URL wsdlURL = null;
        try {
            wsdlURL = new URL(coverityURL.toString() + WebServiceFactory.CONFIGURATION_SERVICE_V9_WSDL);
            final HttpURLConnection conn = (HttpURLConnection) wsdlURL.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            conn.getInputStream();
        } catch (final MalformedURLException e) {
            throw new CoverityIntegrationException(e.getClass().getSimpleName() + ": " + e.getMessage(), e);
        } catch (final FileNotFoundException e) {
            throw new CoverityIntegrationException("The Url does not appear to be a Coverity server:" + wsdlURL.toString(), e);
        } catch (final IOException e) {
            throw new CoverityIntegrationException("The Url does not appear to be a Coverity server:" + wsdlURL.toString() + ", because: " + e.getMessage(), e);
        }
    }

}
