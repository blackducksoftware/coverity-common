/*
 * coverity-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
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
    public void verifyIsCoverityServer(URL coverityURL) throws IntegrationException {
        URL wsdlURL = null;
        try {
            wsdlURL = new URL(coverityURL.toString() + WebServiceFactory.CONFIGURATION_SERVICE_V9_WSDL);
            HttpURLConnection conn = (HttpURLConnection) wsdlURL.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            conn.getInputStream();
        } catch (MalformedURLException e) {
            throw new CoverityIntegrationException(e.getClass().getSimpleName() + ": " + e.getMessage(), e);
        } catch (FileNotFoundException e) {
            throw new CoverityIntegrationException("The Url does not appear to be a Coverity server:" + wsdlURL.toString(), e);
        } catch (IOException e) {
            throw new CoverityIntegrationException("The Url does not appear to be a Coverity server:" + wsdlURL.toString() + ", because: " + e.getMessage(), e);
        }
    }

}
