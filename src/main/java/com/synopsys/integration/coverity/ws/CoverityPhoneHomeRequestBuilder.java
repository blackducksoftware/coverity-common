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

import com.synopsys.integration.phonehome.PhoneHomeRequestBody;
import com.synopsys.integration.phonehome.enums.ProductIdEnum;

public class CoverityPhoneHomeRequestBuilder {
    private final PhoneHomeRequestBody.Builder builder;

    public CoverityPhoneHomeRequestBuilder() {
        this.builder = new PhoneHomeRequestBody.Builder();
    }

    public PhoneHomeRequestBody.Builder getBuilder() {
        return builder;
    }

    public void setProduct(final ProductIdEnum product) {
        builder.setProductId(product);
    }

    public void setProductVersion(final String versionName) {
        builder.setProductVersion(versionName);
    }

    public void setIntegrationRepoName(final String githubRepoName) {
        builder.setArtifactId(githubRepoName);
    }

    public void setIntegrationVersion(final String versionName) {
        builder.setArtifactVersion(versionName);
    }

    public void setCustomerDomainName(final String hostName) {
        builder.setHostName(hostName);
    }

}
