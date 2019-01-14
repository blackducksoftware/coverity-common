/**
 * coverity-common
 *
 * Copyright (C) 2019 Black Duck Software, Inc.
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

import java.net.URL;

import com.synopsys.integration.coverity.ws.v9.ConfigurationService;
import com.synopsys.integration.coverity.ws.v9.VersionDataObj;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.phonehome.PhoneHomeCallable;
import com.synopsys.integration.phonehome.PhoneHomeClient;
import com.synopsys.integration.phonehome.PhoneHomeRequestBody;
import com.synopsys.integration.phonehome.enums.ProductIdEnum;
import com.synopsys.integration.util.IntEnvironmentVariables;

public class CoverityPhoneHomeCallable extends PhoneHomeCallable {
    private final IntLogger logger;
    private final ConfigurationService configurationService;
    private final PhoneHomeRequestBody.Builder phoneHomeRequestBodyBuilder;

    public CoverityPhoneHomeCallable(final IntLogger logger, final PhoneHomeClient client, final ConfigurationService configurationService, final URL coverityURL, final String artifactId, final String artifactVersion,
            final IntEnvironmentVariables intEnvironmentVariables) {
        super(logger, client, coverityURL, artifactId, artifactVersion, intEnvironmentVariables);
        this.logger = logger;
        this.configurationService = configurationService;
        this.phoneHomeRequestBodyBuilder = new PhoneHomeRequestBody.Builder();
    }

    public CoverityPhoneHomeCallable(final IntLogger logger, final PhoneHomeClient client, final ConfigurationService configurationService, final URL coverityURL, final String artifactId, final String artifactVersion,
            final IntEnvironmentVariables intEnvironmentVariables, final PhoneHomeRequestBody.Builder phoneHomeRequestBodyBuilder) {
        super(logger, client, coverityURL, artifactId, artifactVersion, intEnvironmentVariables);
        this.logger = logger;
        this.configurationService = configurationService;
        this.phoneHomeRequestBodyBuilder = phoneHomeRequestBodyBuilder;
    }

    @Override
    public PhoneHomeRequestBody.Builder createPhoneHomeRequestBodyBuilder() {
        phoneHomeRequestBodyBuilder.setCustomerId(PhoneHomeRequestBody.Builder.UNKNOWN_ID);
        phoneHomeRequestBodyBuilder.setProductId(ProductIdEnum.COVERITY);
        try {
            final VersionDataObj versionDataObj = configurationService.getVersion();
            phoneHomeRequestBodyBuilder.setProductVersion(versionDataObj.getExternalVersion());
        } catch (final Exception e) {
            logger.debug("Couldn't get the Coverity version: " + e.getMessage());
        }
        return phoneHomeRequestBodyBuilder;
    }

}
