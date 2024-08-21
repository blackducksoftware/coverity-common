/*
 * coverity-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
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

    public DefectServiceWrapper(IntLogger logger, DefectService defectService) {
        this.logger = logger;
        this.defectService = defectService;
    }

    public List<MergedDefectDataObj> getDefectsForStream(String streamName, MergedDefectFilterSpecDataObj filter) throws CoverityIntegrationException {
        List<MergedDefectDataObj> mergeList = new ArrayList<>();
        try {
            StreamIdDataObj streamId = new StreamIdDataObj();
            streamId.setName(streamName);
            List<StreamIdDataObj> streamIds = new ArrayList<>();
            streamIds.add(streamId);

            SnapshotScopeSpecDataObj snapshotScope = new SnapshotScopeSpecDataObj();
            snapshotScope.setShowSelector("last()");

            PageSpecDataObj pageSpec = new PageSpecDataObj();
            // The loop will pull up to the maximum amount of defect, doing per page size
            final int pageSize = 1000; // Size of page to be pulled
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
