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

package com.sig.integration.coverity.ws.v9;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.0
 */
@WebService(name = "DefectService", targetNamespace = "http://ws.coverity.com/v9")
public interface DefectService {

    /**
     * @param properties
     * @param defectInstanceId
     * @throws CovRemoteServiceException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "updateDefectInstanceProperties", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.UpdateDefectInstanceProperties")
    @ResponseWrapper(localName = "updateDefectInstancePropertiesResponse", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.UpdateDefectInstancePropertiesResponse")
    public void updateDefectInstanceProperties(
            @WebParam(name = "defectInstanceId", targetNamespace = "")
                    DefectInstanceIdDataObj defectInstanceId,
            @WebParam(name = "properties", targetNamespace = "")
                    List<PropertySpecDataObj> properties)
            throws CovRemoteServiceException_Exception
    ;

    /**
     * @param streamDefectIds
     * @param defectStateSpec
     * @throws CovRemoteServiceException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "updateStreamDefects", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.UpdateStreamDefects")
    @ResponseWrapper(localName = "updateStreamDefectsResponse", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.UpdateStreamDefectsResponse")
    public void updateStreamDefects(
            @WebParam(name = "streamDefectIds", targetNamespace = "")
                    List<StreamDefectIdDataObj> streamDefectIds,
            @WebParam(name = "defectStateSpec", targetNamespace = "")
                    DefectStateSpecDataObj defectStateSpec)
            throws CovRemoteServiceException_Exception
    ;

    /**
     * @param triageStoreIds
     * @param mergedDefectIdDataObj
     * @return returns java.util.List<com.coverity.ws.v9.TriageHistoryDataObj>
     * @throws CovRemoteServiceException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getTriageHistory", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetTriageHistory")
    @ResponseWrapper(localName = "getTriageHistoryResponse", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetTriageHistoryResponse")
    public List<TriageHistoryDataObj> getTriageHistory(
            @WebParam(name = "mergedDefectIdDataObj", targetNamespace = "")
                    MergedDefectIdDataObj mergedDefectIdDataObj,
            @WebParam(name = "triageStoreIds", targetNamespace = "")
                    List<TriageStoreIdDataObj> triageStoreIds)
            throws CovRemoteServiceException_Exception
    ;

    /**
     * @param mergedDefectIdDataObjs
     * @param filterSpec
     * @return returns java.util.List<com.coverity.ws.v9.StreamDefectDataObj>
     * @throws CovRemoteServiceException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getStreamDefects", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetStreamDefects")
    @ResponseWrapper(localName = "getStreamDefectsResponse", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetStreamDefectsResponse")
    public List<StreamDefectDataObj> getStreamDefects(
            @WebParam(name = "mergedDefectIdDataObjs", targetNamespace = "")
                    List<MergedDefectIdDataObj> mergedDefectIdDataObjs,
            @WebParam(name = "filterSpec", targetNamespace = "")
                    StreamDefectFilterSpecDataObj filterSpec)
            throws CovRemoteServiceException_Exception
    ;

    /**
     * @param streamIds
     * @param snapshotScope
     * @param filterSpec
     * @param pageSpec
     * @return returns com.coverity.ws.v9.MergedDefectsPageDataObj
     * @throws CovRemoteServiceException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getMergedDefectsForStreams", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetMergedDefectsForStreams")
    @ResponseWrapper(localName = "getMergedDefectsForStreamsResponse", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetMergedDefectsForStreamsResponse")
    public MergedDefectsPageDataObj getMergedDefectsForStreams(
            @WebParam(name = "streamIds", targetNamespace = "")
                    List<StreamIdDataObj> streamIds,
            @WebParam(name = "filterSpec", targetNamespace = "")
                    MergedDefectFilterSpecDataObj filterSpec,
            @WebParam(name = "pageSpec", targetNamespace = "")
                    PageSpecDataObj pageSpec,
            @WebParam(name = "snapshotScope", targetNamespace = "")
                    SnapshotScopeSpecDataObj snapshotScope)
            throws CovRemoteServiceException_Exception
    ;

    /**
     * @param streamIds
     * @param mergedDefectIdDataObj
     * @return returns java.util.List<com.coverity.ws.v9.DefectChangeDataObj>
     * @throws CovRemoteServiceException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getMergedDefectHistory", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetMergedDefectHistory")
    @ResponseWrapper(localName = "getMergedDefectHistoryResponse", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetMergedDefectHistoryResponse")
    public List<DefectChangeDataObj> getMergedDefectHistory(
            @WebParam(name = "mergedDefectIdDataObj", targetNamespace = "")
                    MergedDefectIdDataObj mergedDefectIdDataObj,
            @WebParam(name = "streamIds", targetNamespace = "")
                    List<StreamIdDataObj> streamIds)
            throws CovRemoteServiceException_Exception
    ;

    /**
     * @param defectState
     * @param triageStore
     * @param mergedDefectIdDataObjs
     * @throws CovRemoteServiceException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "updateTriageForCIDsInTriageStore", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.UpdateTriageForCIDsInTriageStore")
    @ResponseWrapper(localName = "updateTriageForCIDsInTriageStoreResponse", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.UpdateTriageForCIDsInTriageStoreResponse")
    public void updateTriageForCIDsInTriageStore(
            @WebParam(name = "triageStore", targetNamespace = "")
                    TriageStoreIdDataObj triageStore,
            @WebParam(name = "mergedDefectIdDataObjs", targetNamespace = "")
                    List<MergedDefectIdDataObj> mergedDefectIdDataObjs,
            @WebParam(name = "defectState", targetNamespace = "")
                    DefectStateSpecDataObj defectState)
            throws CovRemoteServiceException_Exception
    ;

    /**
     * @param filterSpec
     * @param projectId
     * @return returns java.util.List<com.coverity.ws.v9.ProjectMetricsDataObj>
     * @throws CovRemoteServiceException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getTrendRecordsForProject", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetTrendRecordsForProject")
    @ResponseWrapper(localName = "getTrendRecordsForProjectResponse", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetTrendRecordsForProjectResponse")
    public List<ProjectMetricsDataObj> getTrendRecordsForProject(
            @WebParam(name = "projectId", targetNamespace = "")
                    ProjectIdDataObj projectId,
            @WebParam(name = "filterSpec", targetNamespace = "")
                    ProjectTrendRecordFilterSpecDataObj filterSpec)
            throws CovRemoteServiceException_Exception
    ;

    /**
     * @param componentIds
     * @param projectId
     * @return returns java.util.List<com.coverity.ws.v9.ComponentMetricsDataObj>
     * @throws CovRemoteServiceException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getComponentMetricsForProject", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetComponentMetricsForProject")
    @ResponseWrapper(localName = "getComponentMetricsForProjectResponse", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetComponentMetricsForProjectResponse")
    public List<ComponentMetricsDataObj> getComponentMetricsForProject(
            @WebParam(name = "projectId", targetNamespace = "")
                    ProjectIdDataObj projectId,
            @WebParam(name = "componentIds", targetNamespace = "")
                    List<ComponentIdDataObj> componentIds)
            throws CovRemoteServiceException_Exception
    ;

    /**
     * @param externalPreventVersion
     * @param dateOriginated
     * @param internalPreventVersion
     * @param checkerName
     * @param domainName
     * @param mergeKey
     * @throws CovRemoteServiceException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "createMergedDefect", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.CreateMergedDefect")
    @ResponseWrapper(localName = "createMergedDefectResponse", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.CreateMergedDefectResponse")
    public void createMergedDefect(
            @WebParam(name = "mergeKey", targetNamespace = "")
                    String mergeKey,
            @WebParam(name = "dateOriginated", targetNamespace = "")
                    XMLGregorianCalendar dateOriginated,
            @WebParam(name = "externalPreventVersion", targetNamespace = "")
                    String externalPreventVersion,
            @WebParam(name = "internalPreventVersion", targetNamespace = "")
                    String internalPreventVersion,
            @WebParam(name = "checkerName", targetNamespace = "")
                    String checkerName,
            @WebParam(name = "domainName", targetNamespace = "")
                    String domainName)
            throws CovRemoteServiceException_Exception
    ;

    /**
     * @param fileId
     * @param streamId
     * @return returns com.coverity.ws.v9.FileContentsDataObj
     * @throws CovRemoteServiceException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getFileContents", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetFileContents")
    @ResponseWrapper(localName = "getFileContentsResponse", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetFileContentsResponse")
    public FileContentsDataObj getFileContents(
            @WebParam(name = "streamId", targetNamespace = "")
                    StreamIdDataObj streamId,
            @WebParam(name = "fileId", targetNamespace = "")
                    FileIdDataObj fileId)
            throws CovRemoteServiceException_Exception
    ;

    /**
     * @param snapshotScope
     * @param filterSpec
     * @param projectId
     * @param pageSpec
     * @return returns com.coverity.ws.v9.MergedDefectsPageDataObj
     * @throws CovRemoteServiceException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getMergedDefectsForSnapshotScope", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetMergedDefectsForSnapshotScope")
    @ResponseWrapper(localName = "getMergedDefectsForSnapshotScopeResponse", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetMergedDefectsForSnapshotScopeResponse")
    public MergedDefectsPageDataObj getMergedDefectsForSnapshotScope(
            @WebParam(name = "projectId", targetNamespace = "")
                    ProjectIdDataObj projectId,
            @WebParam(name = "filterSpec", targetNamespace = "")
                    SnapshotScopeDefectFilterSpecDataObj filterSpec,
            @WebParam(name = "pageSpec", targetNamespace = "")
                    PageSpecDataObj pageSpec,
            @WebParam(name = "snapshotScope", targetNamespace = "")
                    SnapshotScopeSpecDataObj snapshotScope)
            throws CovRemoteServiceException_Exception
    ;

    /**
     * @param streamIds
     * @param mergedDefectIdDataObj
     * @return returns java.util.List<com.coverity.ws.v9.DefectDetectionHistoryDataObj>
     * @throws CovRemoteServiceException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getMergedDefectDetectionHistory", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetMergedDefectDetectionHistory")
    @ResponseWrapper(localName = "getMergedDefectDetectionHistoryResponse", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetMergedDefectDetectionHistoryResponse")
    public List<DefectDetectionHistoryDataObj> getMergedDefectDetectionHistory(
            @WebParam(name = "mergedDefectIdDataObj", targetNamespace = "")
                    MergedDefectIdDataObj mergedDefectIdDataObj,
            @WebParam(name = "streamIds", targetNamespace = "")
                    List<StreamIdDataObj> streamIds)
            throws CovRemoteServiceException_Exception
    ;

    /**
     * @param filterSpec
     * @param projectId
     * @param pageSpec
     * @return returns com.coverity.ws.v9.MergedDefectsPageDataObj
     * @throws CovRemoteServiceException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getMergedDefectsForProjectScope", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetMergedDefectsForProjectScope")
    @ResponseWrapper(localName = "getMergedDefectsForProjectScopeResponse", targetNamespace = "http://ws.coverity.com/v9", className = "com.coverity.ws.v9.GetMergedDefectsForProjectScopeResponse")
    public MergedDefectsPageDataObj getMergedDefectsForProjectScope(
            @WebParam(name = "projectId", targetNamespace = "")
                    ProjectIdDataObj projectId,
            @WebParam(name = "filterSpec", targetNamespace = "")
                    ProjectScopeDefectFilterSpecDataObj filterSpec,
            @WebParam(name = "pageSpec", targetNamespace = "")
                    PageSpecDataObj pageSpec)
            throws CovRemoteServiceException_Exception
    ;

}
