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
package com.synopsys.integration.coverity.config;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.synopsys.integration.coverity.CoverityServerVerifier;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.rest.credentials.CredentialsValidator;
import com.synopsys.integration.validator.AbstractValidator;
import com.synopsys.integration.validator.ValidationResult;
import com.synopsys.integration.validator.ValidationResultEnum;
import com.synopsys.integration.validator.ValidationResults;

public class CoverityServerConfigValidator extends AbstractValidator {

    private final CoverityServerVerifier coverityServerVerifier;
    private String url;
    private String username;
    private String password;

    public CoverityServerConfigValidator(final CoverityServerVerifier coverityServerVerifier) {
        this.coverityServerVerifier = coverityServerVerifier;
    }

    public CoverityServerConfigValidator() {
        this.coverityServerVerifier = new CoverityServerVerifier();
    }

    @Override
    public ValidationResults assertValid() {
        final ValidationResults validationResults = new ValidationResults();

        assertCredentialsValid(validationResults, username, password);
        assertURLValid(validationResults, url);

        return validationResults;
    }

    public void assertURLValid(final ValidationResults results, final String url) {
        if (url == null) {
            results.addResult(CoverityServerConfigFieldEnum.URL, new ValidationResult(ValidationResultEnum.ERROR, "No Coverity URL set."));
            return;
        }
        URL uRL = null;
        try {
            uRL = new URL(url);
            uRL.toURI();
        } catch (final MalformedURLException | URISyntaxException e) {
            results.addResult(CoverityServerConfigFieldEnum.URL, new ValidationResult(ValidationResultEnum.ERROR, "The Coverity URL is not a valid URL. " + e.getMessage()));
            return;
        }
        try {
            coverityServerVerifier.verifyIsCoverityServer(uRL);
        } catch (final IntegrationException e) {
            results.addResult(CoverityServerConfigFieldEnum.URL, new ValidationResult(ValidationResultEnum.ERROR, e.getMessage(), e));
        }
    }

    public void assertCredentialsValid(final ValidationResults results, final String username, final String password) {
        final CredentialsValidator credentialsValidator = new CredentialsValidator();
        credentialsValidator.setUsername(username);
        credentialsValidator.setPassword(password);
        final ValidationResults credentialResults = credentialsValidator.assertValid();
        results.addAllResults(credentialResults.getResultMap());
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
