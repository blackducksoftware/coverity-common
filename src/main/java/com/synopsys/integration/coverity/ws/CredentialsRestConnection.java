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

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;

import com.blackducksoftware.integration.exception.IntegrationException;
import com.blackducksoftware.integration.log.IntLogger;
import com.blackducksoftware.integration.rest.connection.RestConnection;
import com.blackducksoftware.integration.rest.exception.IntegrationRestException;
import com.blackducksoftware.integration.rest.proxy.ProxyInfo;

public class CredentialsRestConnection extends RestConnection {
    private final String username;
    private final String password;

    public CredentialsRestConnection(final IntLogger logger, final URL baseUrl, final String username, final String password, final int timeout, final ProxyInfo proxyInfo) {
        super(logger, baseUrl, timeout, proxyInfo);
        this.username = username;
        this.password = password;
    }

    @Override
    public void addBuilderAuthentication() throws IntegrationRestException {
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            final UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
            getCredentialsProvider().setCredentials(new AuthScope(baseUrl.getHost(), baseUrl.getPort()), creds);
        }
    }

    @Override
    public void clientAuthenticate() throws IntegrationException {
    }

}
