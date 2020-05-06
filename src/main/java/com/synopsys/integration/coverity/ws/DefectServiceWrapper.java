/**
 * coverity-common
 *
 * Copyright (c) 2020 Synopsys, Inc.
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

import java.util.ArrayList;
import java.util.List;

import com.synopsys.integration.coverity.api.ws.defect.CovRemoteServiceException_Exception;
import com.synopsys.integration.coverity.api.ws.defect.DefectService;
import com.synopsys.integration.coverity.api.ws.defect.MergedDefectDataObj;
import com.synopsys.integration.coverity.api.ws.defect.MergedDefectFilterSpecDataObj;
import com.synopsys.integration.coverity.api.ws.defect.MergedDefectsPageDataObj;
import com.synopsys.integration.coverity.api.ws.defect.PageSpecDataObj;
import com.synopsys.integration.coverity.api.ws.defect.SnapshotScopeSpecDataObj;
import com.synopsys.integration.coverity.api.ws.defect.StreamIdDataObj;
import com.synopsys.integration.coverity.exception.CoverityIntegrationException;
import com.synopsys.integration.log.IntLogger;

public class DefectServiceWrapper {
    private final IntLogger logger;
    private final DefectService defectService;

    public DefectServiceWrapper(final IntLogger logger, final DefectService defectService) {
        this.logger = logger;
        this.defectService = defectService;
    }

    public List<MergedDefectDataObj> getDefectsForStream(final String streamName, final MergedDefectFilterSpecDataObj filter) throws CoverityIntegrationException {
        final List<MergedDefectDataObj> mergeList = new ArrayList<MergedDefectDataObj>();
        try {
            final StreamIdDataObj streamId = new StreamIdDataObj();
            streamId.setName(streamName);
            final List<StreamIdDataObj> streamIds = new ArrayList<StreamIdDataObj>();
            streamIds.add(streamId);

            final SnapshotScopeSpecDataObj snapshotScope = new SnapshotScopeSpecDataObj();
            snapshotScope.setShowSelector("last()");

            final PageSpecDataObj pageSpec = new PageSpecDataObj();
            // The loop will pull up to the maximum amount of defect, doing per page size
            final int pageSize = 1000; // Size of page to be pulled
            int defectSize = 3000; // Maximum amount of defect to pull
            for (int pageStart = 0; pageStart < defectSize; pageStart += pageSize) {
                if (pageStart >= pageSize)
                    logger.info(String.format("Fetching defects for stream \"%s\" (fetched %s of %s)", streamName, pageStart, defectSize));

                pageSpec.setPageSize(pageSize);
                pageSpec.setStartIndex(pageStart);
                pageSpec.setSortAscending(true);
                final MergedDefectsPageDataObj mergedDefectsForStreams = defectService.getMergedDefectsForStreams(streamIds, filter, pageSpec, snapshotScope);
                defectSize = mergedDefectsForStreams.getTotalNumberOfRecords();
                mergeList.addAll(mergedDefectsForStreams.getMergedDefects());
            }
        } catch (final CovRemoteServiceException_Exception e) {
            throw new CoverityIntegrationException(String.format("An unexpected error occurred fetching defects for stream %s. %s.", streamName, e.getMessage()), e);
        }
        return mergeList;
    }
}
