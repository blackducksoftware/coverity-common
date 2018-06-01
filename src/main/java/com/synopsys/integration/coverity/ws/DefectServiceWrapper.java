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

import java.util.ArrayList;
import java.util.List;

import com.blackducksoftware.integration.log.IntLogger;
import com.synopsys.integration.coverity.exception.CoverityIntegrationException;
import com.synopsys.integration.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.synopsys.integration.coverity.ws.v9.DefectService;
import com.synopsys.integration.coverity.ws.v9.MergedDefectDataObj;
import com.synopsys.integration.coverity.ws.v9.MergedDefectFilterSpecDataObj;
import com.synopsys.integration.coverity.ws.v9.MergedDefectsPageDataObj;
import com.synopsys.integration.coverity.ws.v9.PageSpecDataObj;
import com.synopsys.integration.coverity.ws.v9.SnapshotScopeSpecDataObj;
import com.synopsys.integration.coverity.ws.v9.StreamIdDataObj;

public class DefectServiceWrapper {
    private final IntLogger logger;
    private final DefectService defectService;

    public DefectServiceWrapper(IntLogger logger, DefectService defectService) {
        this.logger = logger;
        this.defectService = defectService;
    }

    public List<MergedDefectDataObj> getDefectsForStream(String streamName, MergedDefectFilterSpecDataObj filter) throws CoverityIntegrationException {
        List<MergedDefectDataObj> mergeList = new ArrayList<MergedDefectDataObj>();
        try {
            StreamIdDataObj streamId = new StreamIdDataObj();
            streamId.setName(streamName);
            List<StreamIdDataObj> streamIds = new ArrayList<StreamIdDataObj>();
            streamIds.add(streamId);

            SnapshotScopeSpecDataObj snapshotScope = new SnapshotScopeSpecDataObj();
            snapshotScope.setShowSelector("last()");

            PageSpecDataObj pageSpec = new PageSpecDataObj();
            // The loop will pull up to the maximum amount of defect, doing per page size
            int pageSize = 1000; // Size of page to be pulled
            int defectSize = 3000; // Maximum amount of defect to pull
            for (int pageStart = 0; pageStart < defectSize; pageStart += pageSize) {
                if (pageStart >= pageSize)
                    logger.info(String.format("Fetching defects for stream \"%s\" (fetched %s of %s)", streamName, pageStart, defectSize));

                pageSpec.setPageSize(pageSize);
                pageSpec.setStartIndex(pageStart);
                pageSpec.setSortAscending(true);
                MergedDefectsPageDataObj mergedDefectsForStreams = defectService.getMergedDefectsForStreams(streamIds, filter, pageSpec, snapshotScope);
                defectSize = mergedDefectsForStreams.getTotalNumberOfRecords();
                mergeList.addAll(mergedDefectsForStreams.getMergedDefects());
            }
        } catch (CovRemoteServiceException_Exception e) {
            throw new CoverityIntegrationException(String.format("An unexpected error occurred fetching defects for stream %s. %s.", streamName, e.getMessage()), e);
        }
        return mergeList;
    }
}
