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

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.synopsys.integration.coverity.config.CoverityHttpClient;
import com.synopsys.integration.coverity.ws.v9.ConfigurationService;
import com.synopsys.integration.coverity.ws.v9.LicenseDataObj;
import com.synopsys.integration.coverity.ws.v9.VersionDataObj;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.phonehome.PhoneHomeClient;
import com.synopsys.integration.phonehome.PhoneHomeRequestBody;
import com.synopsys.integration.phonehome.PhoneHomeResponse;
import com.synopsys.integration.phonehome.PhoneHomeService;
import com.synopsys.integration.phonehome.enums.ProductIdEnum;
import com.synopsys.integration.rest.client.IntHttpClient;
import com.synopsys.integration.util.IntEnvironmentVariables;

public class CoverityPhoneHomeHelper {
    private final IntLogger logger;
    private final PhoneHomeService phoneHomeService;
    private final IntEnvironmentVariables intEnvironmentVariables;
    private final ConfigurationService configurationService;
    private final CoverityHttpClient coverityHttpClient;

    public CoverityPhoneHomeHelper(IntLogger logger, PhoneHomeService phoneHomeService, CoverityHttpClient coverityHttpClient, ConfigurationService configurationService, IntEnvironmentVariables intEnvironmentVariables) {
        this.logger = logger;
        this.phoneHomeService = phoneHomeService;
        this.intEnvironmentVariables = intEnvironmentVariables;
        this.coverityHttpClient = coverityHttpClient;
        this.configurationService = configurationService;
    }

    public static CoverityPhoneHomeHelper createAsynchronousPhoneHomeHelper(WebServiceFactory webServiceFactory, ConfigurationService configurationService, ExecutorService executorService) {
        IntLogger intLogger = webServiceFactory.getLogger();
        IntEnvironmentVariables intEnvironmentVariables = webServiceFactory.getEnvironmentVariables();
        CoverityHttpClient coverityHttpClient = webServiceFactory.getCoverityHttpClient();
        Gson gson = webServiceFactory.getGson();
        PhoneHomeClient phoneHomeClient = CoverityPhoneHomeHelper.createPhoneHomeClient(intLogger, coverityHttpClient, gson);

        PhoneHomeService phoneHomeService = PhoneHomeService.createAsynchronousPhoneHomeService(intLogger, phoneHomeClient, executorService);

        return new CoverityPhoneHomeHelper(intLogger, phoneHomeService, coverityHttpClient, configurationService, intEnvironmentVariables);
    }

    public static PhoneHomeClient createPhoneHomeClient(IntLogger intLogger, IntHttpClient intHttpClient, Gson gson) {
        HttpClientBuilder httpClientBuilder = intHttpClient.getClientBuilder();
        return new PhoneHomeClient(intLogger, httpClientBuilder, gson);
    }

    public PhoneHomeResponse handlePhoneHome(String integrationRepoName, String integrationVersion) {
        return handlePhoneHome(integrationRepoName, integrationVersion, Collections.emptyMap());
    }

    public PhoneHomeResponse handlePhoneHome(String integrationRepoName, String integrationVersion, Map<String, String> metaData) {
        try {
            PhoneHomeRequestBody phoneHomeRequestBody = createPhoneHomeRequestBody(integrationRepoName, integrationVersion, metaData);
            return phoneHomeService.phoneHome(phoneHomeRequestBody, getEnvironmentVariables());
        } catch (Exception e) {
            logger.debug("Problem phoning home: " + e.getMessage(), e);
        }
        return PhoneHomeResponse.createResponse(Boolean.FALSE);
    }

    private PhoneHomeRequestBody createPhoneHomeRequestBody(String integrationRepoName, String integrationVersion, Map<String, String> metaData) {
        CoverityPhoneHomeRequestBuilder coverityPhoneHomeRequestBuilder = new CoverityPhoneHomeRequestBuilder();
        coverityPhoneHomeRequestBuilder.setIntegrationRepoName(integrationRepoName);
        coverityPhoneHomeRequestBuilder.setIntegrationVersion(integrationVersion);

        coverityPhoneHomeRequestBuilder.setProduct(ProductIdEnum.COVERITY);
        coverityPhoneHomeRequestBuilder.setProductVersion(getProductVersion());

        coverityPhoneHomeRequestBuilder.setCustomerName(getCustomerName());
        coverityPhoneHomeRequestBuilder.setCustomerDomainName(coverityHttpClient.getBaseUrl());

        PhoneHomeRequestBody.Builder actualBuilder = coverityPhoneHomeRequestBuilder.getBuilder();
        boolean metaDataSuccess = actualBuilder.addAllToMetaData(metaData);
        if (!metaDataSuccess) {
            logger.debug("The metadata provided to phone-home exceeded its size limit. At least some metadata will be missing.");
        }

        return actualBuilder.build();
    }

    private Map<String, String> getEnvironmentVariables() {
        if (intEnvironmentVariables != null) {
            return intEnvironmentVariables.getVariables();
        }
        return Collections.emptyMap();
    }

    private String getCustomerName() {
        try {
            final LicenseDataObj licenseDataObj = configurationService.getLicenseConfiguration();
            return licenseDataObj.getCustomer();
        } catch (final Exception e) {
            logger.debug("Couldn't get the Coverity customer id: " + e.getMessage());
        }
        return PhoneHomeRequestBody.Builder.UNKNOWN_ID;
    }

    private String getProductVersion() {
        try {
            final VersionDataObj versionDataObj = configurationService.getVersion();
            return versionDataObj.getExternalVersion();
        } catch (final Exception e) {
            logger.debug("Couldn't get the Coverity version: " + e.getMessage());
        }
        return PhoneHomeRequestBody.Builder.UNKNOWN_ID;
    }

}
