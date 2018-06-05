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
package com.synopsys.integration.coverity.ws.view;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.blackducksoftware.integration.exception.IntegrationException;
import com.blackducksoftware.integration.log.IntLogger;
import com.blackducksoftware.integration.rest.connection.RestConnection;
import com.blackducksoftware.integration.rest.request.Request;
import com.blackducksoftware.integration.rest.request.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.synopsys.integration.coverity.exception.CoverityIntegrationException;

/**
 * Service for interacting with the Coverity Connect Views Service JSON API
 */
public class ViewService {
    private final IntLogger logger;
    private final RestConnection restConnection;

    public ViewService(IntLogger logger, RestConnection restConnection) {
        this.logger = logger;
        this.restConnection = restConnection;
    }

    /**
     * Returns a Map of available Coverity connect views, using the numeric identifier as the key and name as value
     */
    public Map<Long, String> getViews() throws IOException, URISyntaxException, IntegrationException {
        Map<Long, String> views = new HashMap<>();
        JsonObject json = null;

        String viewsUri = restConnection.baseUrl.toURI().toString() + "/api/views/v1";

        Request.Builder builder = new Request.Builder(viewsUri);
        Request request = builder.build();

        try (Response response = restConnection.executeRequest(request)) {
            String jsonString = response.getContentString();
            JsonParser jsonParser = new JsonParser();
            json = jsonParser.parse(jsonString).getAsJsonObject();
        }

        JsonArray jsonViews = (JsonArray) json.get("views");
        for (Object view : jsonViews) {
            JsonObject jsonView = (JsonObject) view;
            if (jsonView.has("type")) {
                JsonElement typeElement = jsonView.get("type");
                if (null != typeElement) {
                    String type = typeElement.getAsString();
                    if (type.equals("issues") && jsonView.has("id") && jsonView.has("name")) {
                        final Long viewId = Long.valueOf(jsonView.get("id").getAsString());
                        final String viewName = jsonView.get("name").getAsString();
                        if (viewId != null && viewName != null) {
                            views.put(viewId, viewName);
                        }
                    }
                }
            }
        }
        return views;
    }

    public ViewContents getViewContents(String projectId, String connectView, int pageSize, int offset) throws IOException, URISyntaxException, IntegrationException {
        String viewsContentsUri = restConnection.baseUrl.toURI().toString() + "api/viewContents/issues/v1/" + connectView;

        Request.Builder builder = new Request.Builder(viewsContentsUri);
        builder.addQueryParameter("projectId", projectId);
        builder.addQueryParameter("rowCount", String.valueOf(pageSize));
        builder.addQueryParameter("offset", String.valueOf(offset));
        Request request = builder.build();

        logger.info("Retrieving View contents from " + viewsContentsUri);

        ViewContents viewContents = null;

        try (Response response = restConnection.executeRequest(request)) {
            String jsonString = response.getContentString();

            JsonParser jsonParser = new JsonParser();
            JsonObject json = jsonParser.parse(jsonString).getAsJsonObject();

            if (json.has("viewContentsV1")) {
                return restConnection.gson.fromJson(json.get("viewContentsV1"), ViewContents.class);
            } else {
                logger.error("The View response does not appear to be in the expected format. View response: " + jsonString);
                throw new CoverityIntegrationException("The View response does not appear to be in the expected format.");
            }
        }
    }
}