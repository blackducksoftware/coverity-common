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

package com.synopsys.integration.coverity.ws;

import java.net.URL;
import java.util.concurrent.Callable;

import com.blackducksoftware.integration.log.IntLogger;
import com.blackducksoftware.integration.phonehome.PhoneHomeClient;
import com.blackducksoftware.integration.phonehome.PhoneHomeRequestBody;
import com.blackducksoftware.integration.phonehome.enums.ProductIdEnum;
import com.blackducksoftware.integration.util.IntEnvironmentVariables;
import com.synopsys.integration.coverity.ws.v9.ConfigurationService;
import com.synopsys.integration.coverity.ws.v9.VersionDataObj;

public class PhoneHomeCallable implements Callable<Boolean> {
    private final IntLogger logger;
    private final PhoneHomeClient client;
    private final ConfigurationService configurationService;
    private final URL coverityURL;
    private final String artifactId;
    private final String artifactVersion;
    private final IntEnvironmentVariables intEnvironmentVariables;

    public PhoneHomeCallable(final IntLogger logger, final PhoneHomeClient client, final ConfigurationService configurationService, final URL coverityURL, final String artifactId, final String artifactVersion,
            final IntEnvironmentVariables intEnvironmentVariables) {
        this.logger = logger;
        this.client = client;
        this.configurationService = configurationService;
        this.coverityURL = coverityURL;
        this.artifactId = artifactId;
        this.artifactVersion = artifactVersion;
        this.intEnvironmentVariables = intEnvironmentVariables;
    }

    public PhoneHomeRequestBody createPhoneHomeRequestBody() {
        final PhoneHomeRequestBody.Builder phoneHomeRequestBodyBuilder = new PhoneHomeRequestBody.Builder();
        phoneHomeRequestBodyBuilder.setArtifactId(artifactId);
        phoneHomeRequestBodyBuilder.setArtifactVersion(artifactVersion);
        phoneHomeRequestBodyBuilder.setHostName(coverityURL.toString());
        phoneHomeRequestBodyBuilder.setProductId(ProductIdEnum.COVERITY);
        try {
            final VersionDataObj versionDataObj = configurationService.getVersion();
            phoneHomeRequestBodyBuilder.setProductVersion(versionDataObj.getExternalVersion());
        } catch (final Exception e) {
            logger.debug("Couldn't get the Coverity version: " + e.getMessage());
        }
        return phoneHomeRequestBodyBuilder.build();
    }

    @Override
    public Boolean call() throws Exception {
        Boolean result = Boolean.FALSE;
        try {
            logger.debug("starting phone home");
            final PhoneHomeRequestBody phoneHomeRequestBody = createPhoneHomeRequestBody();
            client.postPhoneHomeRequest(phoneHomeRequestBody, intEnvironmentVariables.getVariables());
            result = Boolean.TRUE;
            logger.debug("completed phone home");
        } catch (final Exception ex) {
            logger.debug("Phone home error.", ex);
        }

        return result;
    }
}
