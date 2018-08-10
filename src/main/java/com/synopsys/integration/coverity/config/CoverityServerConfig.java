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
package com.synopsys.integration.coverity.config;

import java.io.Serializable;
import java.net.URL;

import com.synopsys.integration.exception.EncryptionException;
import com.synopsys.integration.rest.credentials.Credentials;
import com.synopsys.integration.util.Stringable;

public class CoverityServerConfig extends Stringable implements Serializable {
    private static final long serialVersionUID = 8314444738247849945L;

    private final URL url;
    private final Credentials coverityCredentials;

    public CoverityServerConfig(final URL url, final Credentials coverityCredentials) {
        this.url = url;
        this.coverityCredentials = coverityCredentials;
    }

    public URL getUrl() {
        return url;
    }

    public Credentials getCoverityCredentials() {
        return coverityCredentials;
    }

    public String getUsername() {
        if (null != coverityCredentials) {
            return coverityCredentials.getUsername();
        }
        return null;
    }

    public String getPassword() throws EncryptionException {
        if (null != coverityCredentials) {
            return coverityCredentials.getDecryptedPassword();
        }
        return null;
    }

}
