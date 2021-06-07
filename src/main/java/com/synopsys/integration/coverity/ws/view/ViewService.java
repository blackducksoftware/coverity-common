/*
 * coverity-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.ws.view;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.synopsys.integration.coverity.api.rest.View;
import com.synopsys.integration.coverity.api.rest.ViewContents;
import com.synopsys.integration.coverity.api.rest.ViewType;
import com.synopsys.integration.coverity.api.ws.configuration.ProjectDataObj;
import com.synopsys.integration.coverity.config.CoverityHttpClient;
import com.synopsys.integration.coverity.exception.CoverityIntegrationException;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.rest.HttpUrl;
import com.synopsys.integration.rest.request.Request;
import com.synopsys.integration.rest.response.Response;

/**
 * Service for interacting with the Coverity Connect Views Service JSON API
 */
public class ViewService {
    public static final String VIEWS_LINK = "/api/views/v1";
    public static final String VIEW_CONTENT_PREFIX = "/api/viewContents/";
    public static final String VIEW_REPORT_LINK = "/reports.htm";

    private final IntLogger logger;
    private final CoverityHttpClient coverityHttpClient;
    private final Gson gson;

    public ViewService(IntLogger logger, CoverityHttpClient coverityHttpClient, Gson gson) {
        this.logger = logger;
        this.coverityHttpClient = coverityHttpClient;
        this.gson = gson;
    }

    public List<View> getAllViews() throws IOException, IntegrationException {
        JsonObject json;

        Request.Builder builder = new Request.Builder(new HttpUrl(coverityHttpClient.getBaseUrl()).appendRelativeUrl(VIEWS_LINK));
        Request request = builder.build();

        try (Response response = coverityHttpClient.execute(request)) {
            String jsonString = response.getContentString();
            json = JsonParser.parseString(jsonString).getAsJsonObject();
        }

        return gson.fromJson(json.get("views"), new TypeToken<List<View>>() {}.getType());
    }

    public Optional<View> getViewByExactName(String viewName) throws IOException, IntegrationException {
        return getAllViews().stream()
                   .filter(view -> view.name != null)
                   .filter(view -> view.name.equals(viewName))
                   .findFirst();
    }

    public List<View> getAllViewsOfType(ViewType viewType) throws IOException, IntegrationException {
        return getAllViews().stream()
                   .filter(view -> view.type != null)
                   .filter(view -> view.type.equals(viewType.toString()))
                   .collect(Collectors.toList());
    }

    public ViewContents getViewContents(ProjectDataObj project, View view, int pageSize, int offset) throws IOException, IntegrationException {
        return getViewContents(project.getProjectKey(), view.type, view.id, pageSize, offset);
    }

    public ViewContents getViewContents(long projectKey, String viewType, long viewId, int pageSize, int offset) throws IOException, IntegrationException {
        String viewsContentsUri = coverityHttpClient.getBaseUrl() + VIEW_CONTENT_PREFIX + viewType + "/v1/" + viewId;

        Request.Builder builder = new Request.Builder(new HttpUrl(viewsContentsUri));
        builder.addQueryParameter("projectId", String.valueOf(projectKey));
        builder.addQueryParameter("rowCount", String.valueOf(pageSize));
        builder.addQueryParameter("offset", String.valueOf(offset));
        Request request = builder.build();

        logger.info("Retrieving View contents from " + viewsContentsUri);

        try (Response response = coverityHttpClient.execute(request)) {
            String jsonString = response.getContentString();

            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();

            if (json.has("viewContentsV1")) {
                return gson.fromJson(json.get("viewContentsV1"), ViewContents.class);
            } else {
                logger.error("The View response does not appear to be in the expected format. View response: " + jsonString);
                throw new CoverityIntegrationException("The View response does not appear to be in the expected format.");
            }
        }
    }

    public String getProjectViewReportUrl(ProjectDataObj project, View view) {
        return getProjectViewReportUrl(project.getProjectKey(), view.id);
    }

    public String getProjectViewReportUrl(long projectKey, long viewId) {
        return coverityHttpClient.getBaseUrl() + VIEW_REPORT_LINK + "#v" + viewId + "/p" + projectKey;
    }

}
