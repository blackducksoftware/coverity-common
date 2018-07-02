package com.synopsys.integration.coverity

import org.junit.Test

import com.blackducksoftware.integration.exception.IntegrationException
import com.blackducksoftware.integration.log.IntLogger
import com.blackducksoftware.integration.test.tool.TestLogger
import com.synopsys.integration.coverity.config.CoverityServerConfig
import com.synopsys.integration.coverity.config.CoverityServerConfigBuilder
import com.synopsys.integration.coverity.exception.CoverityIntegrationException
import com.synopsys.integration.coverity.ws.WebServiceFactory
import com.synopsys.integration.coverity.ws.v9.ConfigurationService
import com.synopsys.integration.coverity.ws.v9.CovRemoteServiceException_Exception
import com.synopsys.integration.coverity.ws.v9.ProjectDataObj
import com.synopsys.integration.coverity.ws.v9.ProjectFilterSpecDataObj
import com.synopsys.integration.coverity.ws.view.ViewContents
import com.synopsys.integration.coverity.ws.view.ViewService

class CoverityTest {
    @Test
    public void testCoverity() {
        TestLogger logger = new TestLogger();

        def projectName = 'Insecure'
        def viewName = 'ALL'

        CoverityServerConfigBuilder builder = new CoverityServerConfigBuilder();
        builder.url('http://igor.internal.synopsys.com:1411');
        builder.username('admin');
        builder.password('coverity');

        CoverityServerConfig coverityServerConfig = builder.build();
        WebServiceFactory webServiceFactory = new WebServiceFactory(coverityServerConfig, logger);
        webServiceFactory.connect();

        ConfigurationService configurationService = webServiceFactory.createConfigurationService();
        Optional<String> optionalProjectId = getProjectIdFromName(projectName, configurationService);

        ViewService viewService = webServiceFactory.createViewService();
        Optional<String> optionalViewId = getViewIdFromName(viewName, viewService);

        String projectId = optionalProjectId.orElse("");
        String viewId = optionalViewId.orElse("");

        int defectSize = getIssueCountVorView(projectId, viewId, viewService, logger);
        logger.info(String.format("[Coverity] Found %s issues for project \"%s\" and view \"%s\"", defectSize, projectName, viewName));
    }

    private Optional<String> getProjectIdFromName(String projectName, ConfigurationService configurationService) throws CovRemoteServiceException_Exception {
        ProjectFilterSpecDataObj projectFilterSpecDataObj = new ProjectFilterSpecDataObj();
        List<ProjectDataObj> projects = configurationService.getProjects(projectFilterSpecDataObj);
        for (ProjectDataObj projectDataObj : projects) {
            if (null != projectDataObj.getId() && null != projectDataObj.getId().getName() && projectDataObj.getId().getName().equals(projectName)) {
                if (null != projectDataObj.getProjectKey()) {
                    return Optional.of(String.valueOf(projectDataObj.getProjectKey()));
                }
            }
        }
        return Optional.empty();
    }

    private Optional<String> getViewIdFromName(String viewName, ViewService viewService) throws IntegrationException, IOException, URISyntaxException {
        Map<Long, String> views = viewService.getViews();
        for (Map.Entry<Long, String> view : views.entrySet()) {
            if (view.getValue().equals(viewName)) {
                return Optional.of(String.valueOf(view.getKey()));
            }
        }
        return Optional.empty();
    }

    private int getIssueCountVorView(String projectId, String viewId, ViewService viewService, IntLogger logger) throws IntegrationException {
        try {
            int pageSize = 1;
            int defectSize = 0;

            final ViewContents viewContents = viewService.getViewContents(projectId, viewId, pageSize, 0);

            defectSize = viewContents.totalRows.intValue();

            return defectSize;
        } catch (IOException | URISyntaxException e) {
            throw new CoverityIntegrationException(e.getMessage(), e);
        }
    }
}
