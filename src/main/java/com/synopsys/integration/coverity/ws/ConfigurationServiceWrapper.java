/*
 * coverity-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.ws;

import java.util.Optional;

import com.synopsys.integration.coverity.api.ws.configuration.ConfigurationService;
import com.synopsys.integration.coverity.api.ws.configuration.CovRemoteServiceException_Exception;
import com.synopsys.integration.coverity.api.ws.configuration.ProjectDataObj;
import com.synopsys.integration.coverity.api.ws.configuration.ProjectFilterSpecDataObj;
import com.synopsys.integration.coverity.api.ws.configuration.ProjectIdDataObj;
import com.synopsys.integration.coverity.api.ws.configuration.ProjectSpecDataObj;
import com.synopsys.integration.coverity.api.ws.configuration.StreamDataObj;
import com.synopsys.integration.coverity.api.ws.configuration.StreamFilterSpecDataObj;
import com.synopsys.integration.coverity.api.ws.configuration.StreamSpecDataObj;
import com.synopsys.integration.coverity.api.ws.configuration.TriageStoreDataObj;
import com.synopsys.integration.coverity.api.ws.configuration.TriageStoreFilterSpecDataObj;
import com.synopsys.integration.log.IntLogger;

public class ConfigurationServiceWrapper {
    public static final String DEFAULT_TIRAGE_STORE_NAME = "Default Triage Store";
    public static final String DEFAULT_PROGRAMMING_LANGUAGE = "MIXED";

    private final IntLogger logger;
    private final ConfigurationService configurationService;
    private final int timeoutInSeconds;

    public ConfigurationServiceWrapper(final IntLogger logger, final ConfigurationService configurationService, final int timeoutInSeconds) {
        this.logger = logger;
        this.configurationService = configurationService;
        this.timeoutInSeconds = timeoutInSeconds;
    }

    public Optional<ProjectDataObj> getAndWaitForProjectWithExactName(final String exactProjectName) throws CovRemoteServiceException_Exception, InterruptedException {
        long startTime = System.currentTimeMillis();
        Optional<ProjectDataObj> matchingProject;

        int attemptCount = 1;
        do {
            // We use a do/while here to make sure we always check at least once, even if the timeout is 0o
            matchingProject = getProjectByExactName(exactProjectName);

            if (matchingProject.isPresent()) {
                logger.info(String.format("Successfully found a project named '%s", exactProjectName));
            } else {
                attemptCount++;
                logger.info(String.format("No project named '%s' found yet, retrying in 1 second (try #%d)...", exactProjectName, attemptCount));
                Thread.sleep(1000);
            }
        } while (!matchingProject.isPresent() && System.currentTimeMillis() - startTime <= timeoutInSeconds * 1000);

        if (!matchingProject.isPresent()) {
            logger.info(String.format("It was not possible to find a project named '%s' within the timeout (%ds) provided.", exactProjectName, timeoutInSeconds));
        }

        return matchingProject;
    }

    public Optional<StreamDataObj> getAndWaitForStreamWithExactName(final String exactStreamName) throws CovRemoteServiceException_Exception, InterruptedException {
        long startTime = System.currentTimeMillis();
        Optional<StreamDataObj> matchingStream;

        int attemptCount = 1;
        do {
            // We use a do/while here to make sure we always check at least once, even if the timeout is 0
            matchingStream = getStreamByExactName(exactStreamName);

            if (matchingStream.isPresent()) {
                logger.info(String.format("Successfully found a stream named '%s'", exactStreamName));
            } else {
                attemptCount++;
                logger.info(String.format("No stream named '%s' found yet, retrying in 1 second (try #%d)...", exactStreamName, attemptCount));
                Thread.sleep(1000);
            }
        } while (!matchingStream.isPresent() && System.currentTimeMillis() - startTime <= timeoutInSeconds * 1000);

        if (!matchingStream.isPresent()) {
            logger.info(String.format("It was not possible to find a stream named '%s' within the timeout (%ds) provided.", exactStreamName, timeoutInSeconds));
        }

        return matchingStream;
    }

    public void createSimpleProject(final String projectName) throws CovRemoteServiceException_Exception {
        final ProjectSpecDataObj projectSpec = new ProjectSpecDataObj();
        projectSpec.setName(projectName);

        configurationService.createProject(projectSpec);
    }

    public void createSimpleStreamInProject(final ProjectIdDataObj projectId, final String streamName) throws CovRemoteServiceException_Exception {
        final Optional<TriageStoreDataObj> matchingTriageStore = getTriageStoreByExactName(DEFAULT_TIRAGE_STORE_NAME);

        if (matchingTriageStore.isPresent()) {
            final StreamSpecDataObj streamSpec = new StreamSpecDataObj();
            streamSpec.setName(streamName);
            streamSpec.setLanguage(DEFAULT_PROGRAMMING_LANGUAGE);
            streamSpec.setTriageStoreId(matchingTriageStore.get().getId());

            configurationService.createStreamInProject(projectId, streamSpec);
        } else {
            logger.error("Stream creation failed: Could not find Default Triage Store");
        }
    }

    public Optional<ProjectDataObj> getProjectByExactName(final String exactProjectName) throws CovRemoteServiceException_Exception {
        logger.debug(String.format("Searching for a project named exactly '%s'", exactProjectName));
        final ProjectFilterSpecDataObj projectFilter = new ProjectFilterSpecDataObj();
        projectFilter.setNamePattern(exactProjectName);

        return configurationService.getProjects(projectFilter).stream()
                   .filter(projectDataObj -> projectDataObj.getId() != null)
                   .filter(projectDataObj -> exactProjectName.equals(projectDataObj.getId().getName()))
                   .findFirst();
    }

    public Optional<StreamDataObj> getStreamByExactName(final String exactStreamName) throws CovRemoteServiceException_Exception {
        logger.debug(String.format("Searching for a stream named exactly '%s'", exactStreamName));
        final StreamFilterSpecDataObj streamFilter = new StreamFilterSpecDataObj();
        streamFilter.setNamePattern(exactStreamName);

        return configurationService.getStreams(streamFilter).stream()
                   .filter(streamDataObj -> streamDataObj.getId() != null)
                   .filter(streamDataObj -> exactStreamName.equals(streamDataObj.getId().getName()))
                   .findFirst();
    }

    public Optional<TriageStoreDataObj> getTriageStoreByExactName(final String exactTriageStoreName) throws CovRemoteServiceException_Exception {
        logger.debug(String.format("Searching for a triage store named exactly '%s'", exactTriageStoreName));
        final TriageStoreFilterSpecDataObj triageStoreFilter = new TriageStoreFilterSpecDataObj();
        triageStoreFilter.setNamePattern(exactTriageStoreName);

        return configurationService.getTriageStores(triageStoreFilter).stream()
                   .filter(triageStoreDataObj -> triageStoreDataObj.getId() != null)
                   .filter(triageStoreDataObj -> exactTriageStoreName.equals(triageStoreDataObj.getId().getName()))
                   .findFirst();
    }

}
