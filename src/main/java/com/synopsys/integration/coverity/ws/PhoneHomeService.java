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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import com.blackducksoftware.integration.log.IntLogger;
import com.blackducksoftware.integration.phonehome.PhoneHomeClient;
import com.blackducksoftware.integration.util.IntEnvironmentVariables;
import com.synopsys.integration.coverity.ws.v9.ConfigurationService;

public class PhoneHomeService {
    private final IntLogger logger;
    private final ConfigurationService configurationService;
    private final IntEnvironmentVariables intEnvironmentVariables;
    private final PhoneHomeClient phoneHomeClient;
    private final ExecutorService executorService;

    public PhoneHomeService(final IntLogger logger, final ConfigurationService configurationService, final IntEnvironmentVariables intEnvironmentVariables, final PhoneHomeClient phoneHomeClient, final ExecutorService executorService) {
        this.logger = logger;
        this.configurationService = configurationService;
        this.intEnvironmentVariables = intEnvironmentVariables;
        this.phoneHomeClient = phoneHomeClient;
        this.executorService = executorService;
    }

    public PhoneHomeService(final IntLogger logger, final ConfigurationService configurationService, final IntEnvironmentVariables intEnvironmentVariables, final PhoneHomeClient phoneHomeClient) {
        this.logger = logger;
        this.configurationService = configurationService;
        this.intEnvironmentVariables = intEnvironmentVariables;
        this.phoneHomeClient = phoneHomeClient;
        final ThreadFactory threadFactory = Executors.defaultThreadFactory();
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), threadFactory);
    }

    /**
     * @param coverityURL     The URL of the Coverity server.
     * @param artifactId      The name of the jar without the version. For example: <i>coverity-common</i>.
     * @param artifactVersion The version of the jar.
     * @return
     */
    public PhoneHomeResponse startPhoneHome(final URL coverityURL, final String artifactId, final String artifactVersion) {
        try {
            final PhoneHomeCallable task = new PhoneHomeCallable(logger, phoneHomeClient, configurationService, coverityURL, artifactId, artifactVersion, intEnvironmentVariables);
            final Future<Boolean> resultTask = executorService.submit(task);
            return new PhoneHomeResponse(resultTask);
        } catch (final Exception e) {
            logger.debug("Could not build phone home body" + e.getMessage(), e);
        }
        return null;
    }
}
