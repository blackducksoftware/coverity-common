/**
 * coverity-common
 *
 * Copyright (C) 2019 Black Duck Software, Inc.
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
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.synopsys.integration.coverity.config.CoverityHttpClient;
import com.synopsys.integration.coverity.exception.CoverityIntegrationException;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.rest.request.Request;
import com.synopsys.integration.rest.request.Response;

/**
 * Service for interacting with the Coverity Connect Views Service JSON API
 */
public class ViewService {
    private final IntLogger logger;
    private final CoverityHttpClient coverityHttpClient;
    private final Gson gson;

    public ViewService(final IntLogger logger, final CoverityHttpClient coverityHttpClient, final Gson gson) {
        this.logger = logger;
        this.coverityHttpClient = coverityHttpClient;
        this.gson = gson;
    }

    /**
     * Returns a Map of available Coverity connect views, using the numeric identifier as the key and name as value
     */
    public Map<Long, String> getViews() throws IOException, IntegrationException {
        final Map<Long, String> views = new HashMap<>();
        final JsonObject json;

        final String viewsUri = coverityHttpClient.getBaseUrl() + "/api/views/v1";

        final Request.Builder builder = new Request.Builder(viewsUri);
        final Request request = builder.build();

        try (Response response = coverityHttpClient.execute(request)) {
            final String jsonString = response.getContentString();
            final JsonParser jsonParser = new JsonParser();
            json = jsonParser.parse(jsonString).getAsJsonObject();
        }

        final JsonArray jsonViews = (JsonArray) json.get("views");
        for (final Object view : jsonViews) {
            final JsonObject jsonView = (JsonObject) view;
            if (jsonView.has("type")) {
                final JsonElement typeElement = jsonView.get("type");
                if (null != typeElement) {
                    final String type = typeElement.getAsString();
                    if (type.equals("issues") && jsonView.has("id") && jsonView.has("name")) {
                        final Long viewId = Long.valueOf(jsonView.get("id").getAsString());
                        final String viewName = jsonView.get("name").getAsString();
                        if (viewName != null) {
                            views.put(viewId, viewName);
                        }
                    }
                }
            }
        }
        return views;
    }

    public ViewContents getViewContents(final String projectId, final String connectView, final int pageSize, final int offset) throws IOException, IntegrationException {
        final String viewsContentsUri = coverityHttpClient.getBaseUrl() + "/api/viewContents/issues/v1/" + URLEncoder.encode(connectView, "UTF-8");

        final Request.Builder builder = new Request.Builder(viewsContentsUri);
        builder.addQueryParameter("projectId", projectId);
        builder.addQueryParameter("rowCount", String.valueOf(pageSize));
        builder.addQueryParameter("offset", String.valueOf(offset));
        final Request request = builder.build();

        logger.info("Retrieving View contents from " + viewsContentsUri);

        try (Response response = coverityHttpClient.execute(request)) {
            final String jsonString = response.getContentString();

            final JsonParser jsonParser = new JsonParser();
            final JsonObject json = jsonParser.parse(jsonString).getAsJsonObject();

            if (json.has("viewContentsV1")) {
                return gson.fromJson(json.get("viewContentsV1"), ViewContents.class);
            } else {
                logger.error("The View response does not appear to be in the expected format. View response: " + jsonString);
                throw new CoverityIntegrationException("The View response does not appear to be in the expected format.");
            }
        }
    }
    
}
